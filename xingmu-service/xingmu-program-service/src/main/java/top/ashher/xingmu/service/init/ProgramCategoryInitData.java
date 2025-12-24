package top.ashher.xingmu.service.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.initialize.base.AbstractApplicationPostConstructHandler;
import top.ashher.xingmu.service.ProgramCategoryService;

import java.util.concurrent.Executor;

@Component
public class ProgramCategoryInitData extends AbstractApplicationPostConstructHandler {

    @Autowired
    private ProgramCategoryService programCategoryService;
    @Qualifier("businessExecutor")
    @Autowired
    private Executor taskExecutor;


    @Override
    public Integer executeOrder() {
        return 1;
    }

    @Override
    public void executeInit(final ConfigurableApplicationContext context) {
        taskExecutor.execute(() -> {
            programCategoryService.programCategoryRedisDataInit();
        });
    }
}
