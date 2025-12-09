package top.ashher.xingmu.initialize.config;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.ashher.xingmu.initialize.execute.ApplicationCommandLineRunnerExecute;
import top.ashher.xingmu.initialize.execute.ApplicationInitializingBeanExecute;
import top.ashher.xingmu.initialize.execute.ApplicationPostConstructExecute;
import top.ashher.xingmu.initialize.execute.ApplicationStartEventListenerExecute;

@Configuration
public class InitializeAutoConfig {

    @Bean
    public ApplicationInitializingBeanExecute applicationInitializingBeanExecute(
            ConfigurableApplicationContext applicationContext){
        return new ApplicationInitializingBeanExecute(applicationContext);
    }

    @Bean
    public ApplicationPostConstructExecute applicationPostConstructExecute(
            ConfigurableApplicationContext applicationContext){
        return new ApplicationPostConstructExecute(applicationContext);
    }

    @Bean
    public ApplicationStartEventListenerExecute applicationStartEventListenerExecute(
            ConfigurableApplicationContext applicationContext){
        return new ApplicationStartEventListenerExecute(applicationContext);
    }

    @Bean
    public ApplicationCommandLineRunnerExecute applicationCommandLineRunnerExecute(
            ConfigurableApplicationContext applicationContext){
        return new ApplicationCommandLineRunnerExecute(applicationContext);
    }
}
