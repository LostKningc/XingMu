package top.ashher.xingmu.redisson.servicelock.info;

/**
 * 锁超时处理器
 * */
public interface LockTimeOutHandler {

    /**
     * 处理
     * @param lockName 锁名
     * */
    void handler(String lockName);
}
