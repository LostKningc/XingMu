package top.ashher.xingmu.initialize.execute;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ConfigurableApplicationContext;
import top.ashher.xingmu.initialize.execute.base.AbstractApplicationExecute;

import static top.ashher.xingmu.initialize.constant.InitializeHandlerType.APPLICATION_INITIALIZING_BEAN;

public class ApplicationInitializingBeanExecute extends AbstractApplicationExecute implements InitializingBean {

    public ApplicationInitializingBeanExecute(ConfigurableApplicationContext applicationContext){
        super(applicationContext);
    }

    @Override
    public void afterPropertiesSet() {
        execute();
    }

    @Override
    public String type() {
        return APPLICATION_INITIALIZING_BEAN;
    }
}
