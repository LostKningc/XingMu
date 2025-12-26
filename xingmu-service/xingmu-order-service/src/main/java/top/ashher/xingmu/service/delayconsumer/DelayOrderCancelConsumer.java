package top.ashher.xingmu.service.delayconsumer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.core.ConsumerTask;
import top.ashher.xingmu.dto.DelayOrderCancelDto;
import top.ashher.xingmu.dto.OrderCancelDto;
import top.ashher.xingmu.service.OrderService;
import top.ashher.xingmu.util.SpringUtil;
import top.ashher.xingmu.util.StringUtil;

import static top.ashher.xingmu.service.constant.OrderConstant.DELAY_ORDER_CANCEL_TOPIC;

@Slf4j
@Component
public class DelayOrderCancelConsumer implements ConsumerTask {

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(String content) {
        log.info("延迟订单取消消息进行消费 content : {}", content);
        if (StringUtil.isEmpty(content)) {
            log.error("延迟队列消息不存在");
            return;
        }
        DelayOrderCancelDto delayOrderCancelDto = JSON.parseObject(content, DelayOrderCancelDto.class);

        //取消订单
        OrderCancelDto orderCancelDto = new OrderCancelDto();
        orderCancelDto.setOrderNumber(delayOrderCancelDto.getOrderNumber());
        boolean cancel = orderService.cancel(orderCancelDto);
        if (cancel) {
            log.info("延迟订单取消成功 orderCancelDto : {}",content);
        }else {
            log.error("延迟订单取消失败 orderCancelDto : {}",content);
        }
    }

    @Override
    public String topic() {
        return SpringUtil.getPrefixDistinctionName() + "-" + DELAY_ORDER_CANCEL_TOPIC;
    }
}