package top.ashher.xingmu.service;


import cn.hutool.core.bean.BeanUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ashher.xingmu.dto.OrderCreateDto;
import top.ashher.xingmu.dto.OrderTicketUserCreateDto;
import top.ashher.xingmu.entity.Order;
import top.ashher.xingmu.entity.OrderTicketUser;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.enums.OrderStatus;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.mapper.OrderMapper;
import top.ashher.xingmu.redis.cache.RedisCache;
import top.ashher.xingmu.redis.key.RedisKeyBuild;
import top.ashher.xingmu.redis.key.RedisKeyManage;
import top.ashher.xingmu.redisson.repeatexecute.annotion.RepeatExecuteLimit;
import top.ashher.xingmu.redisson.servicelock.annotion.ServiceLock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static top.ashher.xingmu.redisson.repeatexecute.constant.RepeatExecuteLimitConstants.CANCEL_PROGRAM_ORDER;
import static top.ashher.xingmu.redisson.repeatexecute.constant.RepeatExecuteLimitConstants.CREATE_PROGRAM_ORDER_MQ;

@Slf4j
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private OrderTicketUserService orderTicketUserService;
    @Autowired
    private UidGenerator uidGenerator;

    @Transactional(rollbackFor = Exception.class)
    public String create(OrderCreateDto orderCreateDto) {
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper =
                Wrappers.lambdaQuery(Order.class).eq(Order::getOrderNumber, orderCreateDto.getOrderNumber());
        Order oldOrder = orderMapper.selectOne(orderLambdaQueryWrapper);
        if (Objects.nonNull(oldOrder)) {
            throw new XingMuFrameException(BaseCode.ORDER_EXIST);
        }
        Order order = new Order();
        BeanUtil.copyProperties(orderCreateDto,order);
        //order.setDistributionMode("电子票");
        //order.setTakeTicketMode("请使用购票人身份证直接入场");
        List<OrderTicketUser> orderTicketUserList = new ArrayList<>();
        for (OrderTicketUserCreateDto orderTicketUserCreateDto : orderCreateDto.getOrderTicketUserCreateDtoList()) {
            OrderTicketUser orderTicketUser = new OrderTicketUser();
            BeanUtil.copyProperties(orderTicketUserCreateDto,orderTicketUser);
            orderTicketUser.setId(uidGenerator.getUID());
            orderTicketUserList.add(orderTicketUser);
        }
        orderMapper.insert(order);
        orderTicketUserService.saveBatch(orderTicketUserList);
        return String.valueOf(order.getOrderNumber());
    }

//    @RepeatExecuteLimit(name = CANCEL_PROGRAM_ORDER,keys = {"#orderCancelDto.orderNumber"})
//    @ServiceLock(name = UPDATE_ORDER_STATUS_LOCK,keys = {"#orderCancelDto.orderNumber"})
//    @Transactional(rollbackFor = Exception.class)
//    public boolean cancel(OrderCancelDto orderCancelDto){
//        updateOrderRelatedData(orderCancelDto.getOrderNumber(), OrderStatus.CANCEL);
//        return true;
//    }

    @RepeatExecuteLimit(name = CREATE_PROGRAM_ORDER_MQ,keys = {"#orderCreateDto.orderNumber"})
    @Transactional(rollbackFor = Exception.class)
    public String createMq(OrderCreateDto orderCreateDto){
        String orderNumber = create(orderCreateDto);
        redisCache.set(RedisKeyBuild.createRedisKey(RedisKeyManage.ORDER_MQ,orderNumber),orderNumber,1, TimeUnit.MINUTES);
        return orderNumber;
    }
}
