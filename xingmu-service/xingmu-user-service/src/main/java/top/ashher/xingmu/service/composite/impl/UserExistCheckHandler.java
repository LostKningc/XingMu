package top.ashher.xingmu.service.composite.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.dto.UserRegisterDto;
import top.ashher.xingmu.service.UserService;
import top.ashher.xingmu.service.composite.AbstractUserRegisterCheckHandler;

/**
 * 用户存在检查处理器
 */
@Slf4j
@Component
public class UserExistCheckHandler extends AbstractUserRegisterCheckHandler {

    @Autowired
    private UserService userService;

    @Override
    public void doExecute(final UserRegisterDto userRegisterDto) {
        userService.doExist(userRegisterDto.getMobile());
        log.info("用户存在检查通过，mobile:{}", userRegisterDto.getMobile());
    }

    @Override
    public Integer executeParentOrder() {
        return 2;
    }

    @Override
    public Integer executeTier() {
        return 2;
    }

    @Override
    public Integer executeOrder() {
        return 3;
    }
}
