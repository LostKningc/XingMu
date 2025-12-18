package top.ashher.xingmu.redisson.bloom.config;

import jakarta.annotation.PostConstruct;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.redisson.bloom.handler.BloomFilterHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@EnableConfigurationProperties(BloomFilterProperties.class)
public class BloomFilterManager {

    @Autowired
    private BloomFilterProperties properties;

    @Autowired
    private RedissonClient redissonClient;

    private final Map<String, BloomFilterHandler> filterContainer = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        for (Map.Entry<String, BloomFilterProperties.FilterConfig> entry : properties.getConfigs().entrySet()) {
            String filterName = entry.getKey();
            BloomFilterProperties.FilterConfig config = entry.getValue();
            BloomFilterHandler handler = new BloomFilterHandler(redissonClient, filterName, config);
            filterContainer.put(filterName, handler);
        }
    }

    public BloomFilterHandler getFilter(String name) {
        return filterContainer.get(name);
    }

}
