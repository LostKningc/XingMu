package top.ashher.xingmu.config;

import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.ashher.xingmu.context.DelayQueueBasePart;
import top.ashher.xingmu.context.DelayQueueContext;
import top.ashher.xingmu.init.DelayQueueInitHandler;


@EnableConfigurationProperties(DelayQueueProperties.class)
public class DelayQueueAutoConfig {

    @Bean
    public DelayQueueInitHandler delayQueueInitHandler(DelayQueueBasePart delayQueueBasePart){
        return new DelayQueueInitHandler(delayQueueBasePart);
    }

    @Bean
    public DelayQueueBasePart delayQueueBasePart(RedissonClient redissonClient,DelayQueueProperties delayQueueProperties){
        return new DelayQueueBasePart(redissonClient,delayQueueProperties);
    }

    @Bean
    public DelayQueueContext delayQueueContext(DelayQueueBasePart delayQueueBasePart){
        return new DelayQueueContext(delayQueueBasePart);
    }
}
