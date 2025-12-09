package top.ashher.xingmu.service.composite.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.dto.UserRegisterDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.service.composite.AbstractUserRegisterCheckHandler;
import top.ashher.xingmu.service.tool.RequestCounter;

/**
 * 用户注册次数检查处理器
 */
@Component
public class UserRegisterCountCheckHandler extends AbstractUserRegisterCheckHandler {

    @Autowired
    private RequestCounter requestCounter;

    @Override
    protected void doExecute(final UserRegisterDto param) {
        boolean result = requestCounter.onRequest();
        if (result) {
            throw new XingMuFrameException(BaseCode.USER_REGISTER_FREQUENCY);
        }
    }

    @Override
    public Integer executeParentOrder() {
        return 1;
    }

    @Override
    public Integer executeTier() {
        return 2;
    }

    @Override
    public Integer executeOrder() {
        return 1;
    }
}
