package top.ashher.xingmu.service.composite.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.dto.ProgramGetDto;
import top.ashher.xingmu.dto.ProgramOrderCreateDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.enums.BusinessStatus;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.service.ProgramService;
import top.ashher.xingmu.service.composite.AbstractProgramCheckHandler;
import top.ashher.xingmu.vo.ProgramVo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 详情检查处理器
 */
@Component
public class ProgramDetailCheckHandler extends AbstractProgramCheckHandler {

    @Autowired
    private ProgramService programService;

    @Override
    protected void doExecute(final ProgramOrderCreateDto programOrderCreateDto) {
        ProgramGetDto programGetDto = new ProgramGetDto();
        programGetDto.setId(programOrderCreateDto.getProgramId());
        ProgramVo programVo = programService.detail(programGetDto);
        if (programVo.getPermitChooseSeat().equals(BusinessStatus.NO.getCode())) {
            if (Objects.nonNull(programOrderCreateDto.getSeatDtoList())) {
                throw new XingMuFrameException(BaseCode.PROGRAM_NOT_ALLOW_CHOOSE_SEAT);
            }
        }
        Integer seatCount = Optional.ofNullable(programOrderCreateDto.getSeatDtoList()).map(List::size).orElse(0);
        Integer ticketCount = Optional.ofNullable(programOrderCreateDto.getTicketCount()).orElse(0);
        if (seatCount > programVo.getPerOrderLimitPurchaseCount() || ticketCount > programVo.getPerOrderLimitPurchaseCount()) {
            throw new XingMuFrameException(BaseCode.PER_ORDER_PURCHASE_COUNT_OVER_LIMIT);
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
        return 2;
    }
}
