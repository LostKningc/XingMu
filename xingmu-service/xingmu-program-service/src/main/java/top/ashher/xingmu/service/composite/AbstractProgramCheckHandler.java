package top.ashher.xingmu.service.composite;

import top.ashher.xingmu.design.composite.AbstractComposite;
import top.ashher.xingmu.dto.ProgramOrderCreateDto;
import top.ashher.xingmu.enums.CompositeCheckType;

public abstract class AbstractProgramCheckHandler extends AbstractComposite<ProgramOrderCreateDto> {

    @Override
    public String type() {
        return CompositeCheckType.PROGRAM_ORDER_CREATE_CHECK.getValue();
    }
}
