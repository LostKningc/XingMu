package top.ashher.xingmu.util;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static top.ashher.xingmu.constant.Constant.DEFAULT_PREFIX_DISTINCTION_NAME;
import static top.ashher.xingmu.constant.Constant.PREFIX_DISTINCTION_NAME;

/**
 * Spring 工具类
 */
public class SpringUtil implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static ConfigurableApplicationContext configurableApplicationContext;


    public static String getPrefixDistinctionName(){
        return configurableApplicationContext.getEnvironment().getProperty(PREFIX_DISTINCTION_NAME,
                DEFAULT_PREFIX_DISTINCTION_NAME);
    }

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        configurableApplicationContext = applicationContext;
    }
}
