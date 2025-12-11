package top.ashher.xingmu.redisson.servicelock.factory;

import lombok.AllArgsConstructor;
import top.ashher.xingmu.redisson.lockinfo.LockType;
import top.ashher.xingmu.redisson.servicelock.ServiceLocker;
import top.ashher.xingmu.redisson.servicelock.core.ManageLocker;

@AllArgsConstructor
public class ServiceLockFactory {

    private final ManageLocker manageLocker;


    public ServiceLocker getLock(LockType lockType){
        ServiceLocker lock;
        switch (lockType) {
            case Fair:
                lock = manageLocker.getFairLocker();
                break;
            case Write:
                lock = manageLocker.getWriteLocker();
                break;
            case Read:
                lock = manageLocker.getReadLocker();
                break;
            default:
                lock = manageLocker.getReentrantLocker();
                break;
        }
        return lock;
    }
}
