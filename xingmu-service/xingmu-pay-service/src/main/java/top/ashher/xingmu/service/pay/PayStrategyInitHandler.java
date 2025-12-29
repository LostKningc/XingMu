package top.ashher.xingmu.service.pay;

import lombok.AllArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import top.ashher.xingmu.initialize.base.AbstractApplicationInitializingBeanHandler;

import java.util.Map;

@AllArgsConstructor
public class PayStrategyInitHandler extends AbstractApplicationInitializingBeanHandler {

    private final PayStrategyContext payStrategyContext;

    @Override
    public Integer executeOrder() {
        return 1;
    }

    @Override
    public void executeInit(ConfigurableApplicationContext context) {
        Map<String, PayStrategyHandler> payStrategyHandlerMap = context.getBeansOfType(PayStrategyHandler.class);
        for (Map.Entry<String, PayStrategyHandler> entry : payStrategyHandlerMap.entrySet()) {
            PayStrategyHandler payStrategyHandler = entry.getValue();
            payStrategyContext.put(payStrategyHandler.getChannel(),payStrategyHandler);
        }
    }
}
