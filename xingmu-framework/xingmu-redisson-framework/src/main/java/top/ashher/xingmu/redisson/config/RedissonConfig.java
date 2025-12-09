package top.ashher.xingmu.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RedissonConfig {

    // 如果你的 Redis 有密码，这里必须配，否则连不上
    @Bean
    @Primary
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 假设是单机模式
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")// 你的密码
                .setDatabase(0);
        return Redisson.create(config);
    }
}
