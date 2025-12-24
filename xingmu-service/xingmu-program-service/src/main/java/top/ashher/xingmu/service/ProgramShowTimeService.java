package top.ashher.xingmu.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ashher.xingmu.dto.ProgramShowTimeAddDto;
import top.ashher.xingmu.entity.Program;
import top.ashher.xingmu.entity.ProgramGroup;
import top.ashher.xingmu.entity.ProgramShowTime;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.mapper.ProgramGroupMapper;
import top.ashher.xingmu.mapper.ProgramMapper;
import top.ashher.xingmu.mapper.ProgramShowTimeMapper;
import top.ashher.xingmu.redis.cache.RedisCache;
import top.ashher.xingmu.redis.key.RedisKeyBuild;
import top.ashher.xingmu.redis.key.RedisKeyManage;
import top.ashher.xingmu.redisson.lockinfo.LockType;
import top.ashher.xingmu.redisson.servicelock.annotion.ServiceLock;
import top.ashher.xingmu.redisson.servicelock.util.ServiceLockTool;
import top.ashher.xingmu.service.localcache.LocalCacheProgramShowTime;
import top.ashher.xingmu.util.DateUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static top.ashher.xingmu.redisson.servicelock.core.DistributedLockConstants.GET_PROGRAM_SHOW_TIME_LOCK;
import static top.ashher.xingmu.redisson.servicelock.core.DistributedLockConstants.PROGRAM_SHOW_TIME_LOCK;

@Service
public class ProgramShowTimeService extends ServiceImpl<ProgramShowTimeMapper, ProgramShowTime> {

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ProgramMapper programMapper;

    @Autowired
    private ProgramShowTimeMapper programShowTimeMapper;

    @Autowired
    private ProgramGroupMapper programGroupMapper;

    @Autowired
    private ServiceLockTool serviceLockTool;

    @Autowired
    private LocalCacheProgramShowTime localCacheProgramShowTime;


    @Transactional(rollbackFor = Exception.class)
    public Long add(ProgramShowTimeAddDto programShowTimeAddDto) {
        ProgramShowTime programShowTime = new ProgramShowTime();
        BeanUtil.copyProperties(programShowTimeAddDto,programShowTime);
        programShowTime.setId(uidGenerator.getUID());
        programShowTimeMapper.insert(programShowTime);
        return programShowTime.getId();
    }

    public ProgramShowTime selectProgramShowTimeByProgramIdMultipleCache(Long programId){
        return localCacheProgramShowTime.getCache(RedisKeyBuild.createRedisKey
                        (RedisKeyManage.PROGRAM_SHOW_TIME, programId).getRelKey(),
                key -> selectProgramShowTimeByProgramId(programId));
    }

    public ProgramShowTime simpleSelectProgramShowTimeByProgramIdMultipleCache(Long programId){
        ProgramShowTime programShowTimeCache = localCacheProgramShowTime.getCache(RedisKeyBuild.createRedisKey(
                RedisKeyManage.PROGRAM_SHOW_TIME, programId).getRelKey());
        if (Objects.nonNull(programShowTimeCache)) {
            return programShowTimeCache;
        }
        return redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SHOW_TIME,
                programId), ProgramShowTime.class);
    }


    @ServiceLock(lockType= LockType.Read,name = PROGRAM_SHOW_TIME_LOCK,keys = {"#programId"})
    public ProgramShowTime selectProgramShowTimeByProgramId(Long programId){
        ProgramShowTime programShowTime = redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SHOW_TIME,
                programId), ProgramShowTime.class);
        if (Objects.nonNull(programShowTime)) {
            return programShowTime;
        }
        RLock lock = serviceLockTool.getLock(LockType.Reentrant, GET_PROGRAM_SHOW_TIME_LOCK,
                new String[]{String.valueOf(programId)});
        lock.lock();
        try {
            programShowTime = redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SHOW_TIME,
                    programId), ProgramShowTime.class);
            if (Objects.isNull(programShowTime)) {
                LambdaQueryWrapper<ProgramShowTime> programShowTimeLambdaQueryWrapper =
                        Wrappers.lambdaQuery(ProgramShowTime.class).eq(ProgramShowTime::getProgramId, programId);
                programShowTime = Optional.ofNullable(programShowTimeMapper.selectOne(programShowTimeLambdaQueryWrapper))
                        .orElseThrow(() -> new XingMuFrameException(BaseCode.PROGRAM_SHOW_TIME_NOT_EXIST));
                redisCache.set(RedisKeyBuild.createRedisKey(RedisKeyManage.PROGRAM_SHOW_TIME, programId),programShowTime
                        , DateUtils.countBetweenSecond(DateUtils.now(),programShowTime.getShowTime()), TimeUnit.SECONDS);
            }
            return programShowTime;
        }finally {
            lock.unlock();
        }
    }
}
