package top.ashher.xingmu.redis.cache;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.redis.key.RedisKeyBuild;
import top.ashher.xingmu.util.StringUtil;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Redis 缓存工具类重构版
 * 优化点：统一序列化逻辑、减少重复代码、增强可读性
 */
@Slf4j
@Component
@AllArgsConstructor
public class RedisCache {

    private static final String CACHE_NULL_VALUE = "##_NULL_VALUE_##";
    private final StringRedisTemplate redisTemplate;
    private final RedisTemplate<String, Object> redisTemplate2;

    // ============================ 1. 基础 Key 操作 ============================

    public Boolean hasKey(RedisKeyBuild redisKeyBuild) {
        return redisTemplate.hasKey(getKey(redisKeyBuild));
    }

    public void del(RedisKeyBuild redisKeyBuild) {
        redisTemplate.delete(getKey(redisKeyBuild));
    }

    public void del(Collection<RedisKeyBuild> keys) {
        if (CacheUtil.isEmpty(keys)) return;
        redisTemplate.delete(CacheUtil.getBatchKey(keys));
    }

    public Boolean expire(RedisKeyBuild redisKeyBuild, long ttl, TimeUnit timeUnit) {
        return redisTemplate.expire(getKey(redisKeyBuild), ttl, timeUnit);
    }

    public Long getExpire(RedisKeyBuild redisKeyBuild) {
        return redisTemplate.getExpire(getKey(redisKeyBuild));
    }

    public Long getExpire(RedisKeyBuild redisKeyBuild, TimeUnit timeUnit) {
        return redisTemplate.getExpire(getKey(redisKeyBuild), timeUnit);
    }

    public Set<String> keys(String pattern) {
        // ⚠️ 生产环境慎用 keys 指令，建议改用 scan
        return redisTemplate.keys(pattern);
    }

    public DataType type(RedisKeyBuild redisKeyBuild) {
        return redisTemplate.type(getKey(redisKeyBuild));
    }

    // ============================ 2. String (Value) 操作 ============================

    public void set(RedisKeyBuild redisKeyBuild, Object object) {
        redisTemplate.opsForValue().set(getKey(redisKeyBuild), toJson(object));
    }

    public void set(RedisKeyBuild redisKeyBuild, Object object, long ttl, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(getKey(redisKeyBuild), toJson(object), ttl, timeUnit);
    }

    public boolean setIfAbsent(RedisKeyBuild redisKeyBuild, Object object) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(getKey(redisKeyBuild), toJson(object)));
    }

    /**
     * 普通获取
     */
    public <T> T get(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(getKey(redisKeyBuild));
        if (CACHE_NULL_VALUE.equals(value)) {
            return null;
        }
        return toBean(value, clazz);
    }

    /**
     * 核心方法：防缓存穿透的获取 (Cache Aside 模式)
     */
    public <T> T get(RedisKeyBuild redisKeyBuild, Class<T> clazz, Supplier<T> supplier, long ttl, TimeUnit timeUnit) {
        String key = getKey(redisKeyBuild);
        String value = redisTemplate.opsForValue().get(key);

        // 1. 缓存命中且为占位符 -> 返回 null
        if (CACHE_NULL_VALUE.equals(value)) {
            return null;
        }

        // 2. 缓存命中且有值 -> 反序列化返回
        if (StringUtil.isNotEmpty(value)) {
            return toBean(value, clazz);
        }

        // 3. 缓存未命中 -> 查询数据库 (Supplier)
        T t = supplier.get();

        // 4. 数据库也没值 -> 写入空占位符 (防止穿透)，过期时间较短 (3分钟)
        if (CacheUtil.isEmpty(t)) {
            redisTemplate.opsForValue().set(key, CACHE_NULL_VALUE, 3, TimeUnit.MINUTES);
            return null;
        }

        // 5. 数据库有值 -> 写入缓存并返回
        set(redisKeyBuild, t, ttl, timeUnit);
        return t;
    }

    public Long incrBy(RedisKeyBuild redisKeyBuild, long increment) {
        return redisTemplate.opsForValue().increment(getKey(redisKeyBuild), increment);
    }

    public Double incrByDouble(RedisKeyBuild redisKeyBuild, double increment) {
        return redisTemplate.opsForValue().increment(getKey(redisKeyBuild), increment);
    }

    // ============================ 3. List 操作 ============================

    public <T> List<T> getValueIsList(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(getKey(redisKeyBuild));
        return toList(value, clazz);
    }

    public <T> List<T> getValueIsList(RedisKeyBuild redisKeyBuild, Class<T> clazz, Supplier<List<T>> supplier, long ttl, TimeUnit timeUnit) {
        String key = getKey(redisKeyBuild);
        String value = redisTemplate.opsForValue().get(key);

        // 1. 缓存命中且为占位符 -> 返回空列表
        if (CACHE_NULL_VALUE.equals(value)) {
            return new ArrayList<>();
        }

        // 2. 缓存命中且有值 -> 反序列化返回
        if (StringUtil.isNotEmpty(value)) {
            return toList(value, clazz);
        }

        // 3. 缓存未命中 -> 查询数据库 (Supplier)
        List<T> list = supplier.get();

        // 4. 数据库也没值 -> 写入空占位符 (防止穿透)，过期时间较短 (3分钟)
        if (CacheUtil.isEmpty(list)) {
            redisTemplate.opsForValue().set(key, CACHE_NULL_VALUE, 3, TimeUnit.MINUTES);
            return new ArrayList<>();
        }

        // 5. 数据库有值 -> 写入缓存并返回
        set(redisKeyBuild, list, ttl, timeUnit);
        return list;
    }

    public <T> T indexForList(RedisKeyBuild redisKeyBuild, long index, Class<T> clazz) {
        return toBean(redisTemplate.opsForList().index(getKey(redisKeyBuild), index), clazz);
    }

    public Long leftPushForList(RedisKeyBuild redisKeyBuild, Object value) {
        return redisTemplate.opsForList().leftPush(getKey(redisKeyBuild), toJson(value));
    }

    public Long leftPushAllForList(RedisKeyBuild redisKeyBuild, List<?> values) {
        if (CacheUtil.isEmpty(values)) return 0L;
        return redisTemplate.opsForList().leftPushAll(getKey(redisKeyBuild), toJsonList(values));
    }

    public Long rightPushForList(RedisKeyBuild redisKeyBuild, Object value) {
        return redisTemplate.opsForList().rightPush(getKey(redisKeyBuild), toJson(value));
    }

    public Long rightPushAllForList(RedisKeyBuild redisKeyBuild, List<?> values) {
        if (CacheUtil.isEmpty(values)) return 0L;
        return redisTemplate.opsForList().rightPushAll(getKey(redisKeyBuild), toJsonList(values));
    }

    public <T> T leftPopForList(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        return toBean(redisTemplate.opsForList().leftPop(getKey(redisKeyBuild)), clazz);
    }

    public <T> T rightPopForList(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        return toBean(redisTemplate.opsForList().rightPop(getKey(redisKeyBuild)), clazz);
    }

    public <T> List<T> rangeForList(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz) {
        List<String> range = redisTemplate.opsForList().range(getKey(redisKeyBuild), start, end);
        return parseObjectList(range, clazz);
    }

    public Long lenForList(RedisKeyBuild redisKeyBuild) {
        return redisTemplate.opsForList().size(getKey(redisKeyBuild));
    }

    // ============================ 4. Hash 操作 ============================

    public void putHash(RedisKeyBuild redisKeyBuild, String hashKey, Object value) {
        redisTemplate.opsForHash().put(getKey(redisKeyBuild), hashKey, toJson(value));
    }

    public void putHash(RedisKeyBuild redisKeyBuild, Map<String, ?> map, long ttl, TimeUnit timeUnit) {
        if (CacheUtil.isEmpty(map)) return;
        Map<String, String> stringMap = new HashMap<>();
        map.forEach((k, v) -> stringMap.put(k, toJson(v)));

        String key = getKey(redisKeyBuild);
        redisTemplate.opsForHash().putAll(key, stringMap);
        if (ttl > 0) {
            redisTemplate.expire(key, ttl, timeUnit);
        }
    }

    public <T> T getForHash(RedisKeyBuild redisKeyBuild, String hashKey, Class<T> clazz) {
        Object value = redisTemplate.opsForHash().get(getKey(redisKeyBuild), hashKey);
        return toBean(value, clazz);
    }

    public Long delForHash(RedisKeyBuild redisKeyBuild, String... hashKeys) {
        return redisTemplate.opsForHash().delete(getKey(redisKeyBuild), (Object[]) hashKeys);
    }

    // 获取整个 Hash 结构并转换为 Map
    public <T> Map<String, T> getAllMapForHash(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(getKey(redisKeyBuild));
        Map<String, T> result = new HashMap<>();
        entries.forEach((k, v) -> result.put(String.valueOf(k), toBean(v, clazz)));
        return result;
    }

    // ============================ 5. Set 操作 ============================

    public Long addForSet(RedisKeyBuild redisKeyBuild, Object... values) {
        if (values == null || values.length == 0) return 0L;
        String[] jsonValues = Arrays.stream(values).map(this::toJson).toArray(String[]::new);
        return redisTemplate.opsForSet().add(getKey(redisKeyBuild), jsonValues);
    }

    public Long addForSet(RedisKeyBuild redisKeyBuild, List<?> values) {
        return addForSet(redisKeyBuild, values.toArray());
    }

    public Long removeForSet(RedisKeyBuild redisKeyBuild, Object value) {
        return redisTemplate.opsForSet().remove(getKey(redisKeyBuild), toJson(value));
    }

    public <T> Set<T> membersForSet(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        Set<String> members = redisTemplate.opsForSet().members(getKey(redisKeyBuild));
        return parseObjectSet(members, clazz);
    }

    public Boolean isMemberForSet(RedisKeyBuild redisKeyBuild, Object value) {
        return redisTemplate.opsForSet().isMember(getKey(redisKeyBuild), toJson(value));
    }

    // ============================ 6. Sorted Set (ZSet) 操作 ============================

    public Boolean addForSortedSet(RedisKeyBuild redisKeyBuild, Object value, double score) {
        return redisTemplate.opsForZSet().add(getKey(redisKeyBuild), toJson(value), score);
    }

    public Long addForSortedSet(RedisKeyBuild redisKeyBuild, Map<?, Double> map, long ttl, TimeUnit timeUnit) {
        String key = getKey(redisKeyBuild);
        Set<ZSetOperations.TypedTuple<String>> tuples = map.entrySet().stream()
                .map(e -> new DefaultTypedTuple<>(toJson(e.getKey()), e.getValue()))
                .collect(Collectors.toSet());

        Long count = redisTemplate.opsForZSet().add(key, tuples);
        if (ttl > 0) {
            redisTemplate.expire(key, ttl, timeUnit);
        }
        return count;
    }

    public <T> Set<T> rangeForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz) {
        Set<String> range = redisTemplate.opsForZSet().range(getKey(redisKeyBuild), start, end);
        return parseObjectSet(range, clazz);
    }

    public <T> Set<ZSetOperations.TypedTuple<T>> rangeWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz) {
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().rangeWithScores(getKey(redisKeyBuild), start, end);
        return parseTypedTupleSet(tuples, clazz);
    }

    // ============================ 7. 内部私有辅助方法 (核心优化点) ============================

    /**
     * 获取 Redis Key 字符串
     */
    private String getKey(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        return redisKeyBuild.getRelKey();
    }

    /**
     * 统一对象转 JSON 字符串
     */
    private String toJson(Object value) {
        if (value == null) return null;
        return value instanceof String ? (String) value : JSON.toJSONString(value);
    }

    /**
     * 批量对象转 JSON 字符串列表
     */
    private List<String> toJsonList(List<?> values) {
        if (CacheUtil.isEmpty(values)) return new ArrayList<>();
        return values.stream().map(this::toJson).collect(Collectors.toList());
    }

    /**
     * 统一 JSON 字符串转对象
     */
    private <T> T toBean(Object value, Class<T> clazz) {
        if (value == null) return null;
        String strVal = (String) value;
        if (String.class.isAssignableFrom(clazz)) {
            return (T) strVal;
        }
        // 使用 CacheUtil 的 buildType (你原本的代码逻辑) 或者直接 JSON.parseObject
        // 这里为了兼容复杂泛型，保留你原有的思路，但简化写法
        return JSON.parseObject(strVal, CacheUtil.buildType(clazz));
    }

    /**
     * 统一 JSON 字符串转 List
     */
    private <T> List<T> toList(String value, Class<T> clazz) {
        if (StringUtil.isEmpty(value)) return new ArrayList<>();
        return JSON.parseArray(value, clazz);
    }

    /**
     * 批量解析 List
     */
    private <T> List<T> parseObjectList(Collection<String> sources, Class<T> clazz) {
        if (CacheUtil.isEmpty(sources)) return new ArrayList<>();
        return sources.stream().map(s -> toBean(s, clazz)).collect(Collectors.toList());
    }

    /**
     * 批量解析 Set
     */
    private <T> Set<T> parseObjectSet(Collection<String> sources, Class<T> clazz) {
        if (CacheUtil.isEmpty(sources)) return new HashSet<>();
        return sources.stream().map(s -> toBean(s, clazz)).collect(Collectors.toSet());
    }

    /**
     * 解析带分数的 ZSet 结果
     */
    private <T> Set<ZSetOperations.TypedTuple<T>> parseTypedTupleSet(Set<ZSetOperations.TypedTuple<String>> sources, Class<T> clazz) {
        if (CacheUtil.isEmpty(sources)) return new HashSet<>();
        return sources.stream()
                .map(t -> new DefaultTypedTuple<>(toBean(t.getValue(), clazz), t.getScore()))
                .collect(Collectors.toSet());
    }

    // ============================ 8. 获取底层 RedisTemplate 实例 ============================
    public RedisTemplate<String, Object> getInstance() {
        return redisTemplate2;
    }
}