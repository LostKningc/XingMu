package top.ashher.xingmu.service.init;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.redisson.bloom.handler.BloomFilterHandler;
import top.ashher.xingmu.service.UserService;

import java.util.List;

@Component
public class UserBloomFilterInitData extends AbstractApplicationPostConstructHandler {

    @Autowired
    private BloomFilterHandler bloomFilterHandler;

    @Autowired
    private UserService userService;


    @Override
    public Integer executeOrder() {
        return 1;
    }

    @Override
    public void executeInit(final ConfigurableApplicationContext context) {
        BusinessThreadPool.execute(() -> {
            List<String> allMobile = userService.getAllMobile();
            if (CollectionUtil.isNotEmpty(allMobile)) {
                allMobile.forEach(mobile -> bloomFilterHandler.add(mobile));
            }
        });
    }
}
