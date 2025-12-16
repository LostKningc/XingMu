package top.ashher.xingmu.threadpool;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class GlobalRejectedPolicy implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 打印详细的报警日志
        log.error("警告：线程池任务被拒绝！Task: [{}], Pool Size: [{}], Active: [{}], Queue: [{}]",
                r.toString(),
                executor.getPoolSize(),
                executor.getActiveCount(),
                executor.getQueue().size());

        // 这里可以选择抛出异常，或者根据业务降级处理
        // 抛出异常是 AbortPolicy 的默认行为
        throw new java.util.concurrent.RejectedExecutionException("Task " + r.toString() + " rejected from " + executor.toString());
    }
}