package top.ashher.xingmu.redisson.servicelock.core;

import org.redisson.api.RedissonClient;
import top.ashher.xingmu.redisson.lockinfo.LockType;
import top.ashher.xingmu.redisson.servicelock.ServiceLocker;
import top.ashher.xingmu.redisson.servicelock.impl.*;

import java.util.HashMap;
import java.util.Map;

import static top.ashher.xingmu.redisson.lockinfo.LockType.*;

public class ManageLocker {

    private final Map<LockType, ServiceLocker> cacheLocker = new HashMap<>();

    public ManageLocker(RedissonClient redissonClient){
        cacheLocker.put(Reentrant,new RedissonReentrantLocker(redissonClient));
        cacheLocker.put(Fair,new RedissonFairLocker(redissonClient));
        cacheLocker.put(Write,new RedissonWriteLocker(redissonClient));
        cacheLocker.put(Read,new RedissonReadLocker(redissonClient));
    }

    public ServiceLocker getReentrantLocker(){
        return cacheLocker.get(Reentrant);
    }

    public ServiceLocker getFairLocker(){
        return cacheLocker.get(Fair);
    }

    public ServiceLocker getWriteLocker(){
        return cacheLocker.get(Write);
    }

    public ServiceLocker getReadLocker(){
        return cacheLocker.get(Read);
    }
}
