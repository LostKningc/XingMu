package top.ashher.xingmu.service.init;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.initialize.base.AbstractApplicationCommandLineRunnerHandler;
import top.ashher.xingmu.redisson.bloom.config.BloomFilterManager;
import top.ashher.xingmu.service.ProgramService;

import java.util.List;

@Component
public class ProgramBloomFilterInit extends AbstractApplicationCommandLineRunnerHandler {

    @Autowired
    private ProgramService programService;

    @Autowired
    private BloomFilterManager bloomFilterManager;

    @Override
    public Integer executeOrder() {
        return 4;
    }

    @Override
    public void executeInit(final ConfigurableApplicationContext context) {
        List<Long> allProgramIdList = programService.getAllProgramIdList();
        if (CollectionUtil.isEmpty(allProgramIdList)) {
            return;
        }
        allProgramIdList.forEach(programId -> bloomFilterManager.getFilter("xingmu-program-id-bf").add(String.valueOf(programId)));
    }
}
