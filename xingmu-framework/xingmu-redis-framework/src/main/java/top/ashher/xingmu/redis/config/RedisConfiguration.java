package top.ashher.xingmu.redis.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.ashher.xingmu.redis.cache.RedisCache;

@Configuration
public class RedisConfiguration {

//    @Bean("redisToolRedisTemplate")
//    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
//        StringRedisSerializer stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
//        redisTemplate.setValueSerializer(jsonSerializer);
//        redisTemplate.setHashValueSerializer(jsonSerializer);
//
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        return redisTemplate;
//    }

    @Primary
    @Bean("redisToolStringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate myStringRedisTemplate = new StringRedisTemplate();
        myStringRedisTemplate.setDefaultSerializer(new StringRedisSerializer());
        myStringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return myStringRedisTemplate;
    }

    @Bean
    public RedisCache redisCache(@Qualifier("redisToolStringRedisTemplate") StringRedisTemplate stringRedisTemplate) {
        return new RedisCache(stringRedisTemplate);
    }
}
