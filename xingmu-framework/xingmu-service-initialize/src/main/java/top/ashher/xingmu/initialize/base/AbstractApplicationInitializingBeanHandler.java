package top.ashher.xingmu.initialize.base;

import static top.ashher.xingmu.initialize.constant.InitializeHandlerType.APPLICATION_INITIALIZING_BEAN;

public abstract class AbstractApplicationInitializingBeanHandler implements InitializeHandler {

    @Override
    public String type() {
        return APPLICATION_INITIALIZING_BEAN;
    }
}
