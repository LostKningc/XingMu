package top.ashher.xingmu.service.composite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.design.composite.AbstractComposite;
import top.ashher.xingmu.dto.ProgramGetDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.enums.CompositeCheckType;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.redisson.bloom.config.BloomFilterManager;
import top.ashher.xingmu.redisson.bloom.handler.BloomFilterHandler;

@Component
public class ProgramBloomFilterCheckHandler extends AbstractComposite<ProgramGetDto> {

    @Autowired
    private BloomFilterManager bloomFilterManager;

    @Override
    protected void doExecute(final ProgramGetDto param) {
        boolean contains = bloomFilterManager.getFilter("xingmu-program-id-bf").contains(String.valueOf(param.getId()));
        if (!contains) {
            throw new XingMuFrameException(BaseCode.PROGRAM_NOT_EXIST);
        }
    }

    @Override
    public String type() {
        return CompositeCheckType.PROGRAM_DETAIL_CHECK.getValue();
    }

    @Override
    public Integer executeParentOrder() {
        return 0;
    }

    @Override
    public Integer executeTier() {
        return 1;
    }

    @Override
    public Integer executeOrder() {
        return 1;
    }
}
