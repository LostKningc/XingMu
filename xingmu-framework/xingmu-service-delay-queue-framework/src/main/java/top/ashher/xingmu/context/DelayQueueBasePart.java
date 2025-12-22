package top.ashher.xingmu.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.redisson.api.RedissonClient;
import top.ashher.xingmu.config.DelayQueueProperties;

@Data
@AllArgsConstructor
public class DelayQueueBasePart {

    private final RedissonClient redissonClient;

    private final DelayQueueProperties delayQueueProperties;
}
