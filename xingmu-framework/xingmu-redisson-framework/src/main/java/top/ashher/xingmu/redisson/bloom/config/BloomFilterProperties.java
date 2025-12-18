package top.ashher.xingmu.redisson.bloom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = BloomFilterProperties.PREFIX)
public class BloomFilterProperties {

    public static final String PREFIX = "bloom-filter";

    private Map<String, FilterConfig> configs = new HashMap<>();

    @Data
    public static class FilterConfig {
        // 默认值
        private Long expectedInsertions = 20000L;
        private Double falseProbability = 0.01D;
    }
}
