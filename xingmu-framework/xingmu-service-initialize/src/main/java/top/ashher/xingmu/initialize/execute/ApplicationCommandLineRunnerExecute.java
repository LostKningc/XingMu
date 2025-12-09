package top.ashher.xingmu.initialize.execute;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import top.ashher.xingmu.initialize.execute.base.AbstractApplicationExecute;

import static top.ashher.xingmu.initialize.constant.InitializeHandlerType.APPLICATION_COMMAND_LINE_RUNNER;

public class ApplicationCommandLineRunnerExecute extends AbstractApplicationExecute implements CommandLineRunner {

    public ApplicationCommandLineRunnerExecute(ConfigurableApplicationContext applicationContext){
        super(applicationContext);
    }

    @Override
    public void run(final String... args) {
        execute();
    }

    @Override
    public String type() {
        return APPLICATION_COMMAND_LINE_RUNNER;
    }
}
