package top.ashher.xingmu.redisson.repeatexecute.aspect;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.redisson.handle.RedissonDataHandle;
import top.ashher.xingmu.redisson.locallock.LocalLockCache;
import top.ashher.xingmu.redisson.lockinfo.LockInfoHandle;
import top.ashher.xingmu.redisson.lockinfo.LockType;
import top.ashher.xingmu.redisson.lockinfo.factory.LockInfoHandleFactory;
import top.ashher.xingmu.redisson.repeatexecute.annotion.RepeatExecuteLimit;
import top.ashher.xingmu.redisson.servicelock.ServiceLocker;
import top.ashher.xingmu.redisson.servicelock.factory.ServiceLockFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static top.ashher.xingmu.redisson.repeatexecute.constant.RepeatExecuteLimitConstant.PREFIX_NAME;
import static top.ashher.xingmu.redisson.repeatexecute.constant.RepeatExecuteLimitConstant.SUCCESS_FLAG;

@Slf4j
@Aspect
@Order(-11)
@AllArgsConstructor
public class RepeatExecuteLimitAspect {

    private final LocalLockCache localLockCache;

    private final LockInfoHandleFactory lockInfoHandleFactory;

    private final ServiceLockFactory serviceLockFactory;

    private final RedissonDataHandle redissonDataHandle;


    @Around("@annotation(repeatLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RepeatExecuteLimit repeatLimit) throws Throwable {
        long durationTime = repeatLimit.durationTime();
        String message = repeatLimit.message();
        Object obj;
        LockInfoHandle lockInfoHandle = lockInfoHandleFactory.getLockInfoHandle("repeat_execute_limit");
        String lockName = lockInfoHandle.getLockName(joinPoint,repeatLimit.name(), repeatLimit.keys());
        String repeatFlagName = PREFIX_NAME + lockName;
        String flagObject = redissonDataHandle.get(repeatFlagName);
        if (SUCCESS_FLAG.equals(flagObject)) {
            throw new XingMuFrameException(message);
        }
        ReentrantLock localLock = localLockCache.getLock(lockName,true);
        boolean localLockResult = localLock.tryLock();
        if (!localLockResult) {
            throw new XingMuFrameException(message);
        }
        try {
            ServiceLocker lock = serviceLockFactory.getLock(LockType.Fair);
            boolean result = lock.tryLock(lockName, TimeUnit.SECONDS, 0);
            if (result) {
                try{
                    flagObject = redissonDataHandle.get(repeatFlagName);
                    if (SUCCESS_FLAG.equals(flagObject)) {
                        throw new XingMuFrameException(message);
                    }
                    obj = joinPoint.proceed();
                    if (durationTime > 0) {
                        try {
                            redissonDataHandle.set(repeatFlagName,SUCCESS_FLAG,durationTime,TimeUnit.SECONDS);
                        }catch (Exception e) {
                            log.error("getBucket error",e);
                        }
                    }
                    return obj;
                } finally {
                    lock.unlock(lockName);
                }
            }else{
                throw new XingMuFrameException(message);
            }
        }finally {
            localLock.unlock();
        }
    }
}
