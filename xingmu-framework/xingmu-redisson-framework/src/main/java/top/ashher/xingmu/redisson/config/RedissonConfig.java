package top.ashher.xingmu.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ReflectionUtils;
import top.ashher.xingmu.redisson.locallock.LocalLockCache;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableConfigurationProperties(RedissonBaseProperties.class)
public class RedissonConfig {
    private final AtomicInteger executeTaskThreadCount = new AtomicInteger(1);

    @Bean
    @Primary
    public RedissonClient redissonClient(RedisProperties redisProperties, RedissonBaseProperties redissonBaseProperties){
        Config config = new Config();
        String prefix = "redis://";
        Method method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
        if (method != null && (Boolean)ReflectionUtils.invokeMethod(method, redisProperties)) {
            prefix = "rediss://";
        }
        config.useSingleServer()
                .setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setConnectTimeout(1000)
                .setDatabase(redisProperties.getDatabase())
                .setPassword(redisProperties.getPassword());
        config.setThreads(redissonBaseProperties.getThreads());
        config.setNettyThreads(redissonBaseProperties.getNettyThreads());
        if (Objects.nonNull(redissonBaseProperties.getCorePoolSize()) &&
                Objects.nonNull(redissonBaseProperties.getMaximumPoolSize())) {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                    redissonBaseProperties.getCorePoolSize(),
                    redissonBaseProperties.getMaximumPoolSize(),
                    redissonBaseProperties.getKeepAliveTime(),
                    redissonBaseProperties.getUnit(),
                    new LinkedBlockingQueue<>(redissonBaseProperties.getWorkQueueSize()),
                    r -> new Thread(Thread.currentThread().getThreadGroup(), r,
                            "redisson-thread-" + executeTaskThreadCount.getAndIncrement()));
            config.setExecutor(threadPoolExecutor);
        }
        return Redisson.create(config);
    }

    @Bean
    public LocalLockCache localLockCache(){
        return new LocalLockCache();
    }
}
