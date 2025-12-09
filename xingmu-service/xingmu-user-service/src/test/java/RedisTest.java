import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Redis 和 Redisson 配置测试
 */
@SpringBootTest(classes= top.ashher.xingmu.UserServiceApplication.class)
class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    void testRedisAndLock() {
        // 1. 测试普通读写 (验证 RedisConfiguration)
        redisTemplate.opsForValue().set("test:key", "Hello Damai");
        Object val = redisTemplate.opsForValue().get("test:key");
        System.out.println("Redis取值结果: " + val);
        // 预期输出: Hello Damai (且去 RDM 看一眼，key 应该是 test:key 而不是乱码)

        // 2. 测试分布式锁 (验证 RedissonConfig)
        var lock = redissonClient.getLock("test:lock");
        if (lock.tryLock()) {
            try {
                System.out.println("成功拿到锁！Redisson 配置成功！");
            } finally {
                lock.unlock();
            }
        }
    }
}