package top.ashher.xingmu.service;


import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ashher.xingmu.dto.PayDto;
import top.ashher.xingmu.entity.PayBill;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.enums.PayBillStatus;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.mapper.PayBillMapper;
import top.ashher.xingmu.redisson.servicelock.annotion.ServiceLock;
import top.ashher.xingmu.service.pay.PayResult;
import top.ashher.xingmu.service.pay.PayStrategyContext;
import top.ashher.xingmu.service.pay.PayStrategyHandler;
import top.ashher.xingmu.util.DateUtils;

import java.util.Objects;

import static top.ashher.xingmu.redisson.servicelock.core.DistributedLockConstants.COMMON_PAY_LOCK;

@Slf4j
@Service
public class PayService {

    @Autowired
    private PayBillMapper payBillMapper;
    @Autowired
    private PayStrategyContext payStrategyContext;
    @Autowired
    private UidGenerator uidGenerator;

    @ServiceLock(name = COMMON_PAY_LOCK,keys = {"#payDto.orderNumber"})
    @Transactional(rollbackFor = Exception.class)
    public String commonPay(PayDto payDto) {
        LambdaQueryWrapper<PayBill> payBillLambdaQueryWrapper =
                Wrappers.lambdaQuery(PayBill.class).eq(PayBill::getOutOrderNo, payDto.getOrderNumber());
        PayBill payBill = payBillMapper.selectOne(payBillLambdaQueryWrapper);
        if (Objects.nonNull(payBill) && !Objects.equals(payBill.getPayBillStatus(), PayBillStatus.NO_PAY.getCode())) {
            throw new XingMuFrameException(BaseCode.PAY_BILL_IS_NOT_NO_PAY);
        }
        PayStrategyHandler payStrategyHandler = payStrategyContext.get(payDto.getChannel());
        PayResult pay = payStrategyHandler.pay(String.valueOf(payDto.getOrderNumber()), payDto.getPrice(),
                payDto.getSubject(),payDto.getNotifyUrl(),payDto.getReturnUrl());
        if (pay.isSuccess()) {
            if (Objects.isNull(payBill)){
                payBill = new PayBill();
                payBill.setId(uidGenerator.getUID());
                payBill.setOutOrderNo(String.valueOf(payDto.getOrderNumber()));
                payBill.setPayChannel(payDto.getChannel());
                payBill.setPayScene("生产");
                payBill.setSubject(payDto.getSubject());
                payBill.setPayAmount(payDto.getPrice());
                payBill.setPayBillType(payDto.getPayBillType());
                payBill.setPayBillStatus(PayBillStatus.NO_PAY.getCode());
                payBill.setPayTime(DateUtils.now());
                payBillMapper.insert(payBill);
            }else {
                PayBill updatePayBill = new PayBill();
                updatePayBill.setId(payBill.getId());
                updatePayBill.setPayTime(DateUtils.now());
                updatePayBill.setPayChannel(payDto.getChannel());
                payBillMapper.updateById(updatePayBill);
            }
        }
        return pay.getBody();
    }
}
