package top.ashher.xingmu.service.pay;

import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.exception.XingMuFrameException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class PayStrategyContext {

    private final Map<String,PayStrategyHandler> payStrategyHandlerMap = new HashMap<>();

    public void put(String channel,PayStrategyHandler payStrategyHandler){
        payStrategyHandlerMap.put(channel,payStrategyHandler);
    }

    public PayStrategyHandler get(String channel){
        return Optional.ofNullable(payStrategyHandlerMap.get(channel)).orElseThrow(
                () -> new XingMuFrameException(BaseCode.PAY_STRATEGY_NOT_EXIST));
    }
}
