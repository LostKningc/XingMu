package top.ashher.xingmu.initialize.base;

import static top.ashher.xingmu.initialize.constant.InitializeHandlerType.APPLICATION_COMMAND_LINE_RUNNER;

public abstract class AbstractApplicationCommandLineRunnerHandler implements InitializeHandler {

    @Override
    public String type() {
        return APPLICATION_COMMAND_LINE_RUNNER;
    }
}
