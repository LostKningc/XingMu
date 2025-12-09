package top.ashher.xingmu.service.composite;

import top.ashher.xingmu.design.composite.AbstractComposite;
import top.ashher.xingmu.dto.UserRegisterDto;
import top.ashher.xingmu.enums.CompositeCheckType;

public abstract class AbstractUserRegisterCheckHandler extends AbstractComposite<UserRegisterDto> {

    @Override
    public String type() {
        return CompositeCheckType.USER_REGISTER_CHECK.getValue();
    }
}
