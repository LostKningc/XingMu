package top.ashher.xingmu.redisson.servicelock.util;

/**
 * 没有返回值的任务执行接口
 * */
@FunctionalInterface
public interface TaskRun {

    /**
     * 执行任务
     * */
    void run();
}