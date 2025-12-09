package top.ashher.xingmu.initialize.execute;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import top.ashher.xingmu.initialize.execute.base.AbstractApplicationExecute;

import static top.ashher.xingmu.initialize.constant.InitializeHandlerType.APPLICATION_EVENT_LISTENER;

public class ApplicationStartEventListenerExecute extends AbstractApplicationExecute implements ApplicationListener<ApplicationStartedEvent> {

    public ApplicationStartEventListenerExecute(ConfigurableApplicationContext applicationContext){
        super(applicationContext);
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        execute();
    }

    @Override
    public String type() {
        return APPLICATION_EVENT_LISTENER;
    }
}
