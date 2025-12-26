package top.ashher.xingmu.service.delaysend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.context.DelayQueueContext;
import top.ashher.xingmu.util.SpringUtil;

import static top.ashher.xingmu.service.constant.OrderConstant.*;

@Slf4j
@Component
public class DelayOperateProgramDataSend {

    @Autowired
    private DelayQueueContext delayQueueContext;

    public void sendMessage(String message){
        try {
            delayQueueContext.sendMessage(SpringUtil.getPrefixDistinctionName() + "-" + DELAY_OPERATE_PROGRAM_DATA_TOPIC,
                    message, DELAY_OPERATE_PROGRAM_DATA_TIME, DELAY_OPERATE_PROGRAM_DATA_TIME_UNIT);
        }catch (Exception e) {
            log.error("send message error message : {}",message,e);
        }

    }
}
