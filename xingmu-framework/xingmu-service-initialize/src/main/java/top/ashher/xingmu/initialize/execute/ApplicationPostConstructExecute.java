package top.ashher.xingmu.initialize.execute;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ConfigurableApplicationContext;
import top.ashher.xingmu.initialize.execute.base.AbstractApplicationExecute;

import static top.ashher.xingmu.initialize.constant.InitializeHandlerType.APPLICATION_POST_CONSTRUCT;

public class ApplicationPostConstructExecute extends AbstractApplicationExecute {

    public ApplicationPostConstructExecute(ConfigurableApplicationContext applicationContext){
        super(applicationContext);
    }

    @PostConstruct
    public void postConstructExecute() {
        execute();
    }

    @Override
    public String type() {
        return APPLICATION_POST_CONSTRUCT;
    }
}
