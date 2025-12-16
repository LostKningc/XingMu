package top.ashher.xingmu.threadpool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "business.thread-pool")
public class ThreadPoolProperties {
    /** 核心线程数 */
    private Integer corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
    /** 最大线程数 */
    private Integer maxPoolSize = Runtime.getRuntime().availableProcessors() * 2;
    /** 队列容量 */
    private Integer queueCapacity = 600;
    /** 线程空闲保活时间(秒) */
    private Integer keepAliveSeconds = 60;
    /** 线程名前缀 */
    private String threadNamePrefix = "biz-pool-";
    /** 等待所有任务结束后关闭线程池的超时时间(秒) */
    private Integer awaitTerminationSeconds = 60;
}
