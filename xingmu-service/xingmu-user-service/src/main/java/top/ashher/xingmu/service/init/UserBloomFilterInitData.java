package top.ashher.xingmu.service.init;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.redisson.bloom.config.BloomFilterManager;
import top.ashher.xingmu.redisson.bloom.handler.BloomFilterHandler;
import top.ashher.xingmu.service.UserMobileService;
import top.ashher.xingmu.initialize.base.AbstractApplicationCommandLineRunnerHandler;

import java.util.List;
import java.util.concurrent.Executor;

@Component
public class UserBloomFilterInitData extends AbstractApplicationCommandLineRunnerHandler {

    @Autowired
    private BloomFilterManager bloomFilterManager;

    @Autowired
    private UserMobileService userMobileService;

    @Qualifier("businessExecutor")
    @Autowired
    private Executor executor;

    @Override
    public Integer executeOrder() {
        return 1;
    }

    @Override
    public void executeInit(final ConfigurableApplicationContext context) {
        executor.execute(() -> {
            List<String> allMobile = userMobileService.getAllMobile();
            if (CollectionUtil.isNotEmpty(allMobile)) {
                allMobile.forEach(mobile -> bloomFilterManager.getFilter("xingmu-user-id-bf").add(mobile));
            }
        });
    }
}
