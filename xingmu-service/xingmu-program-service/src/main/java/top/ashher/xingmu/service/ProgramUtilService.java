package top.ashher.xingmu.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ashher.xingmu.client.BaseDataClient;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.dto.AreaGetDto;
import top.ashher.xingmu.entity.Program;
import top.ashher.xingmu.entity.ProgramGroup;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.mapper.ProgramGroupMapper;
import top.ashher.xingmu.mapper.ProgramMapper;
import top.ashher.xingmu.redis.cache.RedisCache;
import top.ashher.xingmu.redis.key.RedisKeyBuild;
import top.ashher.xingmu.redis.key.RedisKeyManage;
import top.ashher.xingmu.redisson.lockinfo.LockType;
import top.ashher.xingmu.redisson.servicelock.annotion.ServiceLock;
import top.ashher.xingmu.redisson.servicelock.util.ServiceLockTool;
import top.ashher.xingmu.util.DateUtils;
import top.ashher.xingmu.vo.AreaVo;
import top.ashher.xingmu.vo.ProgramGroupVo;
import top.ashher.xingmu.vo.ProgramSimpleInfoVo;
import top.ashher.xingmu.vo.ProgramVo;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static top.ashher.xingmu.redisson.servicelock.core.DistributedLockConstants.*;


@Slf4j
@Service
public class ProgramUtilService {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ServiceLockTool serviceLockTool;
    @Autowired
    private ProgramGroupMapper programGroupMapper;
    @Autowired
    private ProgramMapper programMapper;
    @Autowired
    private BaseDataClient baseDataClient;


    @ServiceLock(lockType= LockType.Read,name = PROGRAM_GROUP_LOCK,keys = {"#programGroupId"})
    public ProgramGroupVo getProgramGroup(Long programGroupId) {
        ProgramGroupVo programGroupVo =
                redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_GROUP, programGroupId), ProgramGroupVo.class);
        if (Objects.nonNull(programGroupVo)) {
            return programGroupVo;
        }
        RLock lock = serviceLockTool.getLock(LockType.Reentrant, GET_PROGRAM_LOCK, new String[]{String.valueOf(programGroupId)});
        lock.lock();
        try {
            programGroupVo = redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_GROUP, programGroupId),
                    ProgramGroupVo.class);
            if (Objects.isNull(programGroupVo)) {
                programGroupVo = createProgramGroupVo(programGroupId);
                redisCache.set(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_GROUP, programGroupId),programGroupVo,
                        DateUtils.countBetweenSecond(DateUtils.now(),programGroupVo.getRecentShowTime()), TimeUnit.SECONDS);
            }
            return programGroupVo;
        }finally {
            lock.unlock();
        }
    }

    @ServiceLock(lockType= LockType.Read,name = PROGRAM_LOCK,keys = {"#programId"})
    public ProgramVo getById(Long programId, Long expireTime, TimeUnit timeUnit) {
        ProgramVo programVo =
                redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM, programId), ProgramVo.class);
        if (Objects.nonNull(programVo)) {
            return programVo;
        }
        log.info("查询节目详情 从Redis缓存没有查询到 节目id : {}",programId);
        RLock lock = serviceLockTool.getLock(LockType.Reentrant, GET_PROGRAM_LOCK, new String[]{String.valueOf(programId)});
        lock.lock();
        try {
            return redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM,programId)
                    ,ProgramVo.class,
                    () -> createProgramVo(programId)
                    ,expireTime,
                    timeUnit);
        }finally {
            lock.unlock();
        }
    }


    private ProgramGroupVo createProgramGroupVo(Long programGroupId){
        ProgramGroupVo programGroupVo = new ProgramGroupVo();
        ProgramGroup programGroup =
                Optional.ofNullable(programGroupMapper.selectById(programGroupId))
                        .orElseThrow(() -> new XingMuFrameException(BaseCode.PROGRAM_GROUP_NOT_EXIST));
        programGroupVo.setId(programGroup.getId());
        programGroupVo.setProgramSimpleInfoVoList(JSON.parseArray(programGroup.getProgramJson(), ProgramSimpleInfoVo.class));
        programGroupVo.setRecentShowTime(programGroup.getRecentShowTime());
        return programGroupVo;
    }

    public ProgramVo createProgramVo(Long programId){
        ProgramVo programVo = new ProgramVo();
        Program program =
                Optional.ofNullable(programMapper.selectById(programId))
                        .orElseThrow(() -> new XingMuFrameException(BaseCode.PROGRAM_NOT_EXIST));
        BeanUtil.copyProperties(program,programVo);
        AreaGetDto areaGetDto = new AreaGetDto();
        areaGetDto.setId(program.getAreaId());
        ApiResponse<AreaVo> areaResponse = baseDataClient.getById(areaGetDto);
        if (Objects.equals(areaResponse.getCode(), ApiResponse.ok().getCode())) {
            if (Objects.nonNull(areaResponse.getData())) {
                programVo.setAreaName(areaResponse.getData().getName());
            }
        }else {
            log.error("base-data rpc getById error areaResponse:{}", JSON.toJSONString(areaResponse));
        }
        return programVo;
    }
}
