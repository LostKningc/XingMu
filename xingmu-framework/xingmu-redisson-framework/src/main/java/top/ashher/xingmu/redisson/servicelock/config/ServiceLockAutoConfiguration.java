package top.ashher.xingmu.redisson.servicelock.config;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.ashher.xingmu.redisson.lockinfo.LockInfoHandle;
import top.ashher.xingmu.redisson.lockinfo.factory.LockInfoHandleFactory;
import top.ashher.xingmu.redisson.servicelock.aspect.ServiceLockAspect;
import top.ashher.xingmu.redisson.servicelock.core.ManageLocker;
import top.ashher.xingmu.redisson.servicelock.factory.ServiceLockFactory;
import top.ashher.xingmu.redisson.servicelock.lockinfoImpl.ServiceLockInfoHandle;

@Configuration
public class ServiceLockAutoConfiguration {

    @Bean("service_lock")
    public LockInfoHandle serviceLockInfoHandle(){
        return new ServiceLockInfoHandle();
    }

    @Bean
    public ManageLocker manageLocker(RedissonClient redissonClient){
        return new ManageLocker(redissonClient);
    }

    @Bean
    public ServiceLockFactory serviceLockFactory(ManageLocker manageLocker){
        return new ServiceLockFactory(manageLocker);
    }

    @Bean
    public ServiceLockAspect serviceLockAspect(LockInfoHandleFactory lockInfoHandleFactory, ServiceLockFactory serviceLockFactory){
        return new ServiceLockAspect(lockInfoHandleFactory,serviceLockFactory);
    }

//    @Bean
//    public ServiceLockTool serviceLockUtil(LockInfoHandleFactory lockInfoHandleFactory,ServiceLockFactory serviceLockFactory){
//        return new ServiceLockTool(lockInfoHandleFactory,serviceLockFactory);
//    }
}
