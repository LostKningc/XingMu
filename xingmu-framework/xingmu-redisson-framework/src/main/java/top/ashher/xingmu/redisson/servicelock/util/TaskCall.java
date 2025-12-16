package top.ashher.xingmu.redisson.servicelock.util;

/**
 * 有返回值的任务执行接口
 * */
@FunctionalInterface
public interface TaskCall<V> {

    /**
     * 执行任务
     * @return 结果
     * */
    V call();
}