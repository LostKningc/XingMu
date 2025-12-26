package top.ashher.xingmu.service.kafka;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.dto.OrderCreateDto;
import top.ashher.xingmu.dto.OrderTicketUserCreateDto;
import top.ashher.xingmu.enums.OrderStatus;
import top.ashher.xingmu.service.OrderService;
import top.ashher.xingmu.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static top.ashher.xingmu.constant.Constant.SPRING_INJECT_PREFIX_DISTINCTION_NAME;

@Slf4j
@Component
public class CreateOrderConsumer {

    @Autowired
    private OrderService orderService;

    @Value("${damai.order.max-delay-time:5000}")
    private long maxDelayTime;

    @KafkaListener(topics = {SPRING_INJECT_PREFIX_DISTINCTION_NAME + "-" + "${spring.kafka.topic:create_order}"})
    public void consumerOrderMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {
        String value = consumerRecord.value();
        if (StringUtil.isEmpty(value)) {
            ack.acknowledge(); // 空消息直接提交，不阻塞
            return;
        }

        OrderCreateDto orderCreateDto = null;
        try {
            orderCreateDto = JSON.parseObject(value, OrderCreateDto.class);

            // 优化2：时间校验逻辑
            long createOrderTime = orderCreateDto.getCreateOrderTime().getTime();
            long currentTime = System.currentTimeMillis();
            long delayTime = currentTime - createOrderTime;

            // 监控打点：建议这里接入 Prometheus 监控 delayTime 指标
            log.info("Kafka消费-创建订单 | 订单号:{} | 延迟:{}ms", orderCreateDto.getOrderNumber(), delayTime);

            // 逻辑分支 A: 超时丢弃 (流量熔断)
            if (delayTime > maxDelayTime) {
                log.warn("Kafka消费-消息超时丢弃 | 订单号:{} | 延迟:{}ms > 阈值:{}ms | 执行回滚",
                        orderCreateDto.getOrderNumber(), delayTime, maxDelayTime);
                this.rollbackSeatStatus(orderCreateDto);
                ack.acknowledge();
            }
            // 逻辑分支 B: 正常落库
            else {
                String orderNumber = orderService.createMq(orderCreateDto);
                log.info("Kafka消费-落库成功 | 订单号:{}", orderNumber);
                ack.acknowledge();
            }

        } catch (Exception e) {
            log.error("Kafka消费-发生异常 | 订单号:{} | 执行兜底回滚",
                    (orderCreateDto != null ? orderCreateDto.getOrderNumber() : "Unknown"), e);
            boolean rollbackSuccess = false;
            // 优化3：异常兜底回滚 (关键！)
            // 如果数据库挂了，必须把 Redis 里的库存释放回去，否则库存永久泄露
            if (orderCreateDto != null) {
                try {
                    this.rollbackSeatStatus(orderCreateDto);
                    rollbackSuccess = true;
                } catch (Exception ex) {
                    log.error("严重事故：回滚失败，停止ACK，等待重试或进入DLQ", ex);
                    throw new RuntimeException("Rollback failed, preserve message", ex);
                    // 此时可以不提交 ack，让消息进入死信队列(DLQ)
                }
            }
            if (rollbackSuccess || orderCreateDto == null) {
                ack.acknowledge();
            }
        }
    }

    /**
     * 提取回滚逻辑，复用代码
     */
    private void rollbackSeatStatus(OrderCreateDto orderCreateDto) {
        // 1. 按票档分组
        Map<Long, List<OrderTicketUserCreateDto>> groupedParams =
                orderCreateDto.getOrderTicketUserCreateDtoList().stream()
                        .collect(Collectors.groupingBy(OrderTicketUserCreateDto::getTicketCategoryId));

        // 2. 转换结构
        Map<Long, List<Long>> seatMap = new HashMap<>(groupedParams.size());
        groupedParams.forEach((k, v) ->
                seatMap.put(k, v.stream().map(OrderTicketUserCreateDto::getSeatId).collect(Collectors.toList()))
        );

        // 3. 调用 Service 进行 Redis 回滚 (状态改为 NO_SOLD)
        orderService.updateProgramRelatedDataMq(orderCreateDto.getProgramId(), seatMap, OrderStatus.CANCEL);
    }
}
