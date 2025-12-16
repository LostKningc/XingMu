package top.ashher.xingmu.threadpool;

import jakarta.annotation.Resource; // 如果是 SpringBoot 2.x 用 javax.annotation
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // 开启 @Async 注解支持
public class ThreadPoolConfig {

    @Resource
    private ThreadPoolProperties properties;

    @Bean("businessExecutor") // Bean 名称，使用时通过 @Async("businessExecutor") 引用
    public Executor businessExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 1. 设置核心参数
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());

        // 2. 设置拒绝策略
        executor.setRejectedExecutionHandler(new GlobalRejectedPolicy());

        // 3. 设置任务装饰器 (解决上下文透传)
        executor.setTaskDecorator(new ContextDecorator());

        // 4. 优雅关闭配置 (关键)
        // 设置为 true，表示在容器关闭时，等待队列中的任务执行完
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待的时长，超过这个时间强制销毁
        executor.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());

        // 5. 初始化
        executor.initialize();
        return executor;
    }
}
