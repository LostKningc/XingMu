package top.ashher.xingmu.initialize.base;

import static top.ashher.xingmu.initialize.constant.InitializeHandlerType.APPLICATION_POST_CONSTRUCT;

public abstract class AbstractApplicationPostConstructHandler implements InitializeHandler {

    @Override
    public String type() {
        return APPLICATION_POST_CONSTRUCT;
    }
}
