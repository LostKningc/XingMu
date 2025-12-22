package top.ashher.xingmu.init;

import cn.hutool.core.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import top.ashher.xingmu.context.DelayQueueBasePart;
import top.ashher.xingmu.context.DelayQueuePart;
import top.ashher.xingmu.core.ConsumerTask;
import top.ashher.xingmu.core.DelayConsumerQueue;

import java.util.Map;

@AllArgsConstructor
public class DelayQueueInitHandler implements ApplicationListener<ApplicationStartedEvent> {

    private final DelayQueueBasePart delayQueueBasePart;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        Map<String, ConsumerTask> consumerTaskMap = event.getApplicationContext().getBeansOfType(ConsumerTask.class);
        if (CollectionUtil.isEmpty(consumerTaskMap)) {
            return;
        }
        for (ConsumerTask consumerTask : consumerTaskMap.values()) {
            DelayQueuePart delayQueuePart = new DelayQueuePart(delayQueueBasePart,consumerTask);
            Integer isolationRegionCount = delayQueuePart.getDelayQueueBasePart().getDelayQueueProperties()
                    .getIsolationRegionCount();

            for(int i = 0; i < isolationRegionCount; i++) {
                DelayConsumerQueue delayConsumerQueue = new DelayConsumerQueue(delayQueuePart,
                        delayQueuePart.getConsumerTask().topic() + "-" + i);
                delayConsumerQueue.listenStart();
            }
        }
    }
}
