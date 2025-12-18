package top.ashher.xingmu.initialize.base;

import static top.ashher.xingmu.initialize.constant.InitializeHandlerType.APPLICATION_EVENT_LISTENER;

public abstract class AbstractApplicationStartEventListenerHandler implements InitializeHandler {

    @Override
    public String type() {
        return APPLICATION_EVENT_LISTENER;
    }
}
