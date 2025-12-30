package top.ashher.xingmu.service;


import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ashher.xingmu.dto.NotifyDto;
import top.ashher.xingmu.dto.PayDto;
import top.ashher.xingmu.dto.RefundDto;
import top.ashher.xingmu.entity.PayBill;
import top.ashher.xingmu.entity.RefundBill;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.enums.PayBillStatus;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.mapper.PayBillMapper;
import top.ashher.xingmu.mapper.RefundBillMapper;
import top.ashher.xingmu.redisson.servicelock.annotion.ServiceLock;
import top.ashher.xingmu.service.pay.PayResult;
import top.ashher.xingmu.service.pay.PayStrategyContext;
import top.ashher.xingmu.service.pay.PayStrategyHandler;
import top.ashher.xingmu.service.pay.RefundResult;
import top.ashher.xingmu.util.DateUtils;
import top.ashher.xingmu.vo.NotifyVo;

import java.util.Map;
import java.util.Objects;

import static top.ashher.xingmu.constant.Constant.ALIPAY_NOTIFY_FAILURE_RESULT;
import static top.ashher.xingmu.constant.Constant.ALIPAY_NOTIFY_SUCCESS_RESULT;
import static top.ashher.xingmu.redisson.servicelock.core.DistributedLockConstants.COMMON_PAY_LOCK;

@Slf4j
@Service
public class PayService {

    @Autowired
    private PayBillMapper payBillMapper;
    @Autowired
    private RefundBillMapper refundBillMapper;
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

    @Transactional(rollbackFor = Exception.class)
    public NotifyVo notify(NotifyDto notifyDto){
        NotifyVo notifyVo = new NotifyVo();
        log.info("回调通知参数 ===> {}", JSON.toJSONString(notifyDto));
        Map<String, String> params = notifyDto.getParams();

        PayStrategyHandler payStrategyHandler = payStrategyContext.get(notifyDto.getChannel());
        boolean signVerifyResult = payStrategyHandler.signVerify(params);
        if (!signVerifyResult) {
            notifyVo.setPayResult(ALIPAY_NOTIFY_FAILURE_RESULT);
            return notifyVo;
        }
        LambdaQueryWrapper<PayBill> payBillLambdaQueryWrapper =
                Wrappers.lambdaQuery(PayBill.class).eq(PayBill::getOutOrderNo, params.get("out_trade_no"));
        PayBill payBill = payBillMapper.selectOne(payBillLambdaQueryWrapper);
        if (Objects.isNull(payBill)) {
            log.error("账单为空 notifyDto : {}",JSON.toJSONString(notifyDto));
            notifyVo.setPayResult(ALIPAY_NOTIFY_SUCCESS_RESULT);
            return notifyVo;
        }
        if (Objects.equals(payBill.getPayBillStatus(), PayBillStatus.PAY.getCode())) {
            log.info("账单已支付 notifyDto : {}",JSON.toJSONString(notifyDto));
            notifyVo.setOutTradeNo(payBill.getOutOrderNo());
            notifyVo.setPayResult(ALIPAY_NOTIFY_SUCCESS_RESULT);
            return notifyVo;
        }
        if (Objects.equals(payBill.getPayBillStatus(), PayBillStatus.CANCEL.getCode())) {
            log.info("账单已取消 notifyDto : {}",JSON.toJSONString(notifyDto));
            notifyVo.setOutTradeNo(payBill.getOutOrderNo());
            notifyVo.setPayResult(ALIPAY_NOTIFY_SUCCESS_RESULT);
            return notifyVo;
        }
        if (Objects.equals(payBill.getPayBillStatus(), PayBillStatus.REFUND.getCode())) {
            log.info("账单已退单 notifyDto : {}",JSON.toJSONString(notifyDto));
            notifyVo.setOutTradeNo(payBill.getOutOrderNo());
            notifyVo.setPayResult(ALIPAY_NOTIFY_SUCCESS_RESULT);
            return notifyVo;
        }
        boolean dataVerify = payStrategyHandler.dataVerify(notifyDto.getParams(), payBill);
        if (!dataVerify) {
            notifyVo.setPayResult(ALIPAY_NOTIFY_FAILURE_RESULT);
            return notifyVo;
        }
        PayBill updatePayBill = new PayBill();
        updatePayBill.setPayBillStatus(PayBillStatus.PAY.getCode());
        LambdaUpdateWrapper<PayBill> payBillLambdaUpdateWrapper =
                Wrappers.lambdaUpdate(PayBill.class).eq(PayBill::getOutOrderNo, params.get("out_trade_no"));
        payBillMapper.update(updatePayBill,payBillLambdaUpdateWrapper);
        notifyVo.setOutTradeNo(payBill.getOutOrderNo());
        notifyVo.setPayResult(ALIPAY_NOTIFY_SUCCESS_RESULT);
        return notifyVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public String refund(RefundDto refundDto) {
        PayBill payBill = payBillMapper.selectOne(Wrappers.lambdaQuery(PayBill.class)
                .eq(PayBill::getOutOrderNo, refundDto.getOrderNumber()));
        if (Objects.isNull(payBill)) {
            throw new XingMuFrameException(BaseCode.PAY_BILL_NOT_EXIST);
        }

        if (!Objects.equals(payBill.getPayBillStatus(), PayBillStatus.PAY.getCode()) && !Objects.equals(payBill.getPayBillStatus(), PayBillStatus.NO_PAY.getCode())) {
            throw new XingMuFrameException(BaseCode.PAY_BILL_IS_NOT_PAY_STATUS);
        }

        if (refundDto.getAmount().compareTo(payBill.getPayAmount()) > 0) {
            throw new XingMuFrameException(BaseCode.REFUND_AMOUNT_GREATER_THAN_PAY_AMOUNT);
        }

        PayStrategyHandler payStrategyHandler = payStrategyContext.get(refundDto.getChannel());
        RefundResult refundResult =
                payStrategyHandler.refund(refundDto.getOrderNumber(), refundDto.getAmount(), refundDto.getReason());
        if (refundResult.isSuccess()) {
            PayBill updatePayBill = new PayBill();
            updatePayBill.setId(payBill.getId());
            updatePayBill.setPayBillStatus(PayBillStatus.REFUND.getCode());
            payBillMapper.updateById(updatePayBill);
            RefundBill refundBill = new RefundBill();
            refundBill.setId(uidGenerator.getUID());
            refundBill.setOutOrderNo(payBill.getOutOrderNo());
            refundBill.setPayBillId(payBill.getId());
            refundBill.setRefundAmount(refundDto.getAmount());
            refundBill.setRefundStatus(2);
            refundBill.setRefundTime(DateUtils.now());
            refundBill.setReason(refundDto.getReason());
            refundBillMapper.insert(refundBill);
            return refundBill.getOutOrderNo();
        }else {
            throw new XingMuFrameException(refundResult.getMessage());
        }
    }
}
