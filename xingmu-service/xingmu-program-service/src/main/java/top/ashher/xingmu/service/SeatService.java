package top.ashher.xingmu.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ashher.xingmu.dto.*;
import top.ashher.xingmu.entity.ProgramShowTime;
import top.ashher.xingmu.entity.Seat;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.enums.BusinessStatus;
import top.ashher.xingmu.enums.SeatType;
import top.ashher.xingmu.enums.SellStatus;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.mapper.SeatMapper;
import top.ashher.xingmu.redis.cache.RedisCache;
import top.ashher.xingmu.redis.key.RedisKeyBuild;
import top.ashher.xingmu.redis.key.RedisKeyManage;
import top.ashher.xingmu.redisson.lockinfo.LockType;
import top.ashher.xingmu.redisson.servicelock.annotion.ServiceLock;
import top.ashher.xingmu.redisson.servicelock.util.ServiceLockTool;
import top.ashher.xingmu.service.lua.ProgramSeatCacheData;
import top.ashher.xingmu.util.DateUtils;
import top.ashher.xingmu.vo.ProgramVo;
import top.ashher.xingmu.vo.SeatRelateInfoVo;
import top.ashher.xingmu.vo.SeatVo;
import top.ashher.xingmu.vo.TicketCategoryVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static top.ashher.xingmu.redisson.servicelock.core.DistributedLockConstants.GET_SEAT_LOCK;
import static top.ashher.xingmu.redisson.servicelock.core.DistributedLockConstants.SEAT_LOCK;


@Service
public class SeatService extends ServiceImpl<SeatMapper, Seat> {

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SeatMapper seatMapper;

    @Autowired
    private ProgramService programService;

    @Autowired
    private ProgramShowTimeService programShowTimeService;

    @Autowired
    private ServiceLockTool serviceLockTool;

    @Autowired
    private TicketCategoryService ticketCategoryService;

    @Autowired
    private ProgramSeatCacheData programSeatCacheData;

    /**
     * 添加座位
     * */
    public Long add(SeatAddDto seatAddDto) {
        LambdaQueryWrapper<Seat> seatLambdaQueryWrapper = Wrappers.lambdaQuery(Seat.class)
                .eq(Seat::getProgramId, seatAddDto.getProgramId())
                .eq(Seat::getRowCode, seatAddDto.getRowCode())
                .eq(Seat::getColCode, seatAddDto.getColCode());
        Seat seat = seatMapper.selectOne(seatLambdaQueryWrapper);
        if (Objects.nonNull(seat)) {
            throw new XingMuFrameException(BaseCode.SEAT_IS_EXIST);
        }
        seat = new Seat();
        BeanUtil.copyProperties(seatAddDto,seat);
        seat.setId(uidGenerator.getUID());
        seatMapper.insert(seat);
        return seat.getId();
    }

    /**
     * 查询节目座位信息(缓存预热)
     * */
    @ServiceLock(lockType= LockType.Read,name = SEAT_LOCK,keys = {"#programId","#ticketCategoryId"})
    public List<SeatVo> selectSeatResolution(Long programId,Long ticketCategoryId,Long expireTime,TimeUnit timeUnit) {
        List<SeatVo> seatVoList = getSeatVoListByCacheResolution(programId,ticketCategoryId);
        if (CollectionUtil.isNotEmpty(seatVoList)) {
            return seatVoList;
        }
        RLock lock = serviceLockTool.getLock(LockType.Reentrant, GET_SEAT_LOCK, new String[]{String.valueOf(programId),
                String.valueOf(ticketCategoryId)});
        lock.lock();
        try {
            seatVoList = getSeatVoListByCacheResolution(programId,ticketCategoryId);
            if (CollectionUtil.isNotEmpty(seatVoList)) {
                return seatVoList;
            }
            LambdaQueryWrapper<Seat> seatLambdaQueryWrapper =
                    Wrappers.lambdaQuery(Seat.class).eq(Seat::getProgramId, programId)
                            .eq(Seat::getTicketCategoryId,ticketCategoryId);
            List<Seat> seats = seatMapper.selectList(seatLambdaQueryWrapper);
            for (Seat seat : seats) {
                SeatVo seatVo = new SeatVo();
                BeanUtil.copyProperties(seat, seatVo);
                seatVo.setSeatTypeName(SeatType.getMsg(seat.getSeatType()));
                seatVoList.add(seatVo);
            }
            Map<Integer, List<SeatVo>> seatMap = seatVoList.stream().collect(Collectors.groupingBy(SeatVo::getSellStatus));
            List<SeatVo> noSoldSeatVoList = seatMap.get(SellStatus.NO_SOLD.getCode());
            List<SeatVo> lockSeatVoList = seatMap.get(SellStatus.LOCK.getCode());
            List<SeatVo> soldSeatVoList = seatMap.get(SellStatus.SOLD.getCode());
            if (CollectionUtil.isNotEmpty(noSoldSeatVoList)) {
                redisCache.putHash(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_NO_SOLD_RESOLUTION_HASH,
                                programId,ticketCategoryId),noSoldSeatVoList.stream()
                                .collect(Collectors.toMap(s -> String.valueOf(s.getId()),s -> s,(v1,v2) -> v2))
                        ,expireTime, timeUnit);
            }
            if (CollectionUtil.isNotEmpty(lockSeatVoList)) {
                redisCache.putHash(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_LOCK_RESOLUTION_HASH,
                                programId,ticketCategoryId),lockSeatVoList.stream()
                                .collect(Collectors.toMap(s -> String.valueOf(s.getId()),s -> s,(v1,v2) -> v2))
                        ,expireTime, timeUnit);
            }
            if (CollectionUtil.isNotEmpty(soldSeatVoList)) {
                redisCache.putHash(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_SOLD_RESOLUTION_HASH,
                                programId,ticketCategoryId)
                        ,soldSeatVoList.stream()
                                .collect(Collectors.toMap(s -> String.valueOf(s.getId()),s -> s,(v1,v2) -> v2))
                        ,expireTime, timeUnit);
            }
            seatVoList = seatVoList.stream().sorted(Comparator.comparingInt(SeatVo::getRowCode)
                    .thenComparingInt(SeatVo::getColCode)).collect(Collectors.toList());
            return seatVoList;
        }finally {
            lock.unlock();
        }
    }

    public List<SeatVo> getSeatVoListByCacheResolution(Long programId, Long ticketCategoryId){
        List<String> keys = new ArrayList<>(4);
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_NO_SOLD_RESOLUTION_HASH,
                programId, ticketCategoryId).getRelKey());
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_LOCK_RESOLUTION_HASH,
                programId, ticketCategoryId).getRelKey());
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SEAT_SOLD_RESOLUTION_HASH,
                programId, ticketCategoryId).getRelKey());
        return programSeatCacheData.getData(keys, new String[]{});
    }

    /**
     * 查询节目关联座位信息
     * */
    public SeatRelateInfoVo relateInfo(SeatListDto seatListDto) {
        SeatRelateInfoVo seatRelateInfoVo = new SeatRelateInfoVo();
        ProgramVo programVo =
                redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM,seatListDto.getProgramId()),ProgramVo.class);
        if (Objects.isNull(programVo)){
            ProgramGetDto programGetDto = new ProgramGetDto();
            programGetDto.setId(seatListDto.getProgramId());
            programVo = programService.detail(programGetDto);
        }
        ProgramShowTime programShowTime = programShowTimeService.selectProgramShowTimeByProgramId(seatListDto.getProgramId());
        List<TicketCategoryVo> ticketCategoryVoList = ticketCategoryService
                .selectTicketCategoryListByProgramIdMultipleCache(programVo.getId(),programShowTime.getShowTime());

        List<SeatVo> seatVos = new ArrayList<>();
        for (TicketCategoryVo ticketCategoryVo : ticketCategoryVoList) {
            seatVos.addAll(selectSeatResolution(seatListDto.getProgramId(),ticketCategoryVo.getId(),
                    DateUtils.countBetweenSecond(DateUtils.now(), programShowTime.getShowTime()), TimeUnit.SECONDS));
        }

        if (programVo.getPermitChooseSeat().equals(BusinessStatus.NO.getCode())) {
            throw new XingMuFrameException(BaseCode.PROGRAM_NOT_ALLOW_CHOOSE_SEAT);
        }

        Map<String, List<SeatVo>> seatVoMap =
                seatVos.stream().collect(Collectors.groupingBy(seatVo -> seatVo.getPrice().toString()));
        seatRelateInfoVo.setProgramId(programVo.getId());
        seatRelateInfoVo.setPlace(programVo.getPlace());
        seatRelateInfoVo.setShowTime(programShowTime.getShowTime());
        seatRelateInfoVo.setShowWeekTime(programShowTime.getShowWeekTime());
        seatRelateInfoVo.setPriceList(seatVoMap.keySet().stream().sorted().collect(Collectors.toList()));
        seatRelateInfoVo.setSeatVoMap(seatVoMap);
        return seatRelateInfoVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean batchAdd(SeatBatchAddDto seatBatchAddDto) {
        Long programId = seatBatchAddDto.getProgramId();
        List<SeatBatchRelateInfoAddDto> seatBatchRelateInfoAddDtoList = seatBatchAddDto.getSeatBatchRelateInfoAddDtoList();


        int rowIndex = 0;
        for (SeatBatchRelateInfoAddDto seatBatchRelateInfoAddDto : seatBatchRelateInfoAddDtoList) {
            Long ticketCategoryId = seatBatchRelateInfoAddDto.getTicketCategoryId();
            BigDecimal price = seatBatchRelateInfoAddDto.getPrice();
            Integer count = seatBatchRelateInfoAddDto.getCount();

            int colCount = 10;
            int rowCount = count / colCount;

            for (int i = 1;i<= rowCount;i++) {
                rowIndex++;
                for (int j = 1;j<=colCount;j++) {
                    Seat seat = new Seat();
                    seat.setProgramId(programId);
                    seat.setTicketCategoryId(ticketCategoryId);
                    seat.setRowCode(rowIndex);
                    seat.setColCode(j);
                    seat.setSeatType(1);
                    seat.setPrice(price);
                    seat.setSellStatus(SellStatus.NO_SOLD.getCode());
                    seatMapper.insert(seat);
                }
            }
        }

        return true;
    }
}