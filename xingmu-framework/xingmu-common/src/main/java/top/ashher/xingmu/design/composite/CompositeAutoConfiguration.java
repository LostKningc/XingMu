package top.ashher.xingmu.design.composite;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompositeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CompositeContainer.class) // 最佳实践：允许用户自定义 Bean 覆盖默认配置
    public CompositeContainer<?> compositeContainer() {
        return new CompositeContainer<>();
    }
}
