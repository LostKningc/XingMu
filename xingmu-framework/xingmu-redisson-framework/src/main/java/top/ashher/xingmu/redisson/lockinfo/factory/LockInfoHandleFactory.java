package top.ashher.xingmu.redisson.lockinfo.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import top.ashher.xingmu.redisson.lockinfo.LockInfoHandle;

public class LockInfoHandleFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public LockInfoHandle getLockInfoHandle(String lockInfoType){
        return applicationContext.getBean(lockInfoType,LockInfoHandle.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}