package top.ashher.xingmu.core;

import lombok.extern.slf4j.Slf4j;
import top.ashher.xingmu.context.DelayQueuePart;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 延时队列消费者
 */
@Slf4j
public class DelayConsumerQueue extends DelayBaseQueue{
    // 监听启动线程计数器
    private final AtomicInteger listenStartThreadCount = new AtomicInteger(1);

    private final AtomicInteger executeTaskThreadCount = new AtomicInteger(1);

    private final ThreadPoolExecutor listenStartThreadPool;

    private final ThreadPoolExecutor executeTaskThreadPool;

    private final AtomicBoolean runFlag = new AtomicBoolean(false);

    private final ConsumerTask consumerTask;

    public DelayConsumerQueue(DelayQueuePart delayQueuePart, String relTopic){
        super(delayQueuePart.getDelayQueueBasePart().getRedissonClient(),relTopic);
        this.listenStartThreadPool = new ThreadPoolExecutor(1,1,60,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(),r -> new Thread(Thread.currentThread().getThreadGroup(), r,
                "listen-start-thread-" + listenStartThreadCount.getAndIncrement()));
        this.executeTaskThreadPool = new ThreadPoolExecutor(
                delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties().getCorePoolSize(),
                delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties().getMaximumPoolSize(),
                delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties().getKeepAliveTime(),
                delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties().getUnit(),
                new LinkedBlockingQueue<>(delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties().getWorkQueueSize()),
                r -> new Thread(Thread.currentThread().getThreadGroup(), r,
                        "delay-queue-consume-thread-" + executeTaskThreadCount.getAndIncrement()));
        this.consumerTask = delayQueuePart.getConsumerTask();
    }

    public synchronized void listenStart(){
        if (!runFlag.get()) {
            runFlag.set(true);
            listenStartThreadPool.execute(() -> {
                while (!Thread.interrupted()) {
                    try {
                        assert blockingQueue != null;
                        String content = blockingQueue.take();
                        try {
                            executeTaskThreadPool.execute(() -> {
                                try {
                                    consumerTask.execute(content);
                                }
                                catch (Exception e) {
                                    log.error("consumer execute error",e);
                                }
                            });
                        }
                        catch (RejectedExecutionException re) {
                            log.warn("executeTaskThreadPool is full,rejected execution",re);
                            try {
                                if (!blockingQueue.offer(content)) {
                                    log.error("blockingQueue offer failed,content={}", content);
                                }
                                TimeUnit.MILLISECONDS.sleep(100);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                destroy(executeTaskThreadPool);
                            } catch (Exception ex) {
                                log.error("Re-queue operations failed", ex);
                            }
                        }
                    }
                    catch (InterruptedException e) {
                        destroy(executeTaskThreadPool);
                    } catch (Throwable e) {
                        log.error("blockingQueue take error",e);
                        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException ignored) {}
                    }
                }
            });
        }
    }

    public void destroy(ExecutorService executorService) {
        try {
            if (Objects.nonNull(executorService)) {
                executorService.shutdown();
            }
        } catch (Exception e) {
            log.error("destroy error",e);
        }
    }
}
