package top.ashher.xingmu.client;

import top.ashher.xingmu.common.ApiResponse;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.dto.NotifyDto;
import top.ashher.xingmu.dto.PayDto;
import top.ashher.xingmu.dto.RefundDto;
import top.ashher.xingmu.dto.TradeCheckDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.vo.NotifyVo;
import top.ashher.xingmu.vo.TradeCheckVo;

@Component
public class PayClientFallback implements PayClient{

    @Override
    public ApiResponse<String> commonPay(final PayDto payDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }

    @Override
    public ApiResponse<NotifyVo> notify(final NotifyDto notifyDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }

    @Override
    public ApiResponse<TradeCheckVo> tradeCheck(final TradeCheckDto tradeCheckDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }

    @Override
    public ApiResponse<String> refund(final RefundDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
