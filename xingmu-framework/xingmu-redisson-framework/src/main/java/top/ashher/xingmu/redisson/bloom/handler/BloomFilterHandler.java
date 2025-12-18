package top.ashher.xingmu.redisson.bloom.handler;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import top.ashher.xingmu.redisson.bloom.config.BloomFilterProperties;

/**
 * 布隆过滤器处理器
 */
public class BloomFilterHandler {

    private final RBloomFilter<String> cachePenetrationBloomFilter;

    public BloomFilterHandler(RedissonClient redissonClient, String name, BloomFilterProperties.FilterConfig bloomFilterProperties) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter(name);
        cachePenetrationBloomFilter.tryInit(bloomFilterProperties.getExpectedInsertions(),
                bloomFilterProperties.getFalseProbability());
        this.cachePenetrationBloomFilter = cachePenetrationBloomFilter;
    }

    public boolean add(String data) {
        return cachePenetrationBloomFilter.add(data);
    }

    public boolean contains(String data) {
        return cachePenetrationBloomFilter.contains(data);
    }

    public long getExpectedInsertions() {
        return cachePenetrationBloomFilter.getExpectedInsertions();
    }

    public double getFalseProbability() {
        return cachePenetrationBloomFilter.getFalseProbability();
    }

    public long getSize() {
        return cachePenetrationBloomFilter.getSize();
    }

    public int getHashIterations() {
        return cachePenetrationBloomFilter.getHashIterations();
    }

    public long count() {
        return cachePenetrationBloomFilter.count();
    }
}
