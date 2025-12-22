package top.ashher.xingmu.context;

import lombok.Data;
import top.ashher.xingmu.core.ConsumerTask;

/**
 * 延时队列组成部分
 * */
@Data
public class DelayQueuePart {

    private final DelayQueueBasePart delayQueueBasePart;

    private final ConsumerTask consumerTask;

    public DelayQueuePart(DelayQueueBasePart delayQueueBasePart, ConsumerTask consumerTask){
        this.delayQueueBasePart = delayQueueBasePart;
        this.consumerTask = consumerTask;
    }
}
