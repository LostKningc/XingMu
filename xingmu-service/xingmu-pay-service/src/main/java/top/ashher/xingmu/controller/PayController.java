package top.ashher.xingmu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.dto.*;
import top.ashher.xingmu.service.PayService;
import top.ashher.xingmu.vo.NotifyVo;
import top.ashher.xingmu.vo.PayBillVo;
import top.ashher.xingmu.vo.TradeCheckVo;

@RestController
@RequestMapping("/pay")
@Tag(name = "pay", description = "支付")
public class PayController {

    @Autowired
    private PayService payService;

    @Operation(summary  = "支付")
    @PostMapping(value = "/common/pay")
    public ApiResponse<String> commonPay(@Valid @RequestBody PayDto payDto) {
        return ApiResponse.ok(payService.commonPay(payDto));
    }

    @Operation(summary  = "支付后回到通知")
    @PostMapping(value = "/notify")
    public ApiResponse<NotifyVo> notify(@Valid @RequestBody NotifyDto notifyDto) {
        return ApiResponse.ok(payService.notify(notifyDto));
    }

    @Operation(summary  = "支付状态查询")
    @PostMapping(value = "/trade/check")
    public ApiResponse<TradeCheckVo> tradeCheck(@Valid @RequestBody TradeCheckDto tradeCheckDto) {
        return ApiResponse.ok(payService.tradeCheck(tradeCheckDto));
    }

    @Operation(summary  = "退款")
    @PostMapping(value = "/refund")
    public ApiResponse<String> refund(@Valid @RequestBody RefundDto refundDto) {
        return ApiResponse.ok(payService.refund(refundDto));
    }
//
//    @Operation(summary  = "账单详情查询")
//    @PostMapping(value = "/detail")
//    public ApiResponse<PayBillVo> detail(@Valid @RequestBody PayBillDto payBillDto) {
//        return ApiResponse.ok(payService.detail(payBillDto));
//    }
}
