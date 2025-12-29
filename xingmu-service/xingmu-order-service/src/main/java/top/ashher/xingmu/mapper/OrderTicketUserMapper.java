package top.ashher.xingmu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.ashher.xingmu.entity.OrderTicketUser;
import top.ashher.xingmu.entity.OrderTicketUserAggregate;

import java.util.List;

@Mapper
public interface OrderTicketUserMapper extends BaseMapper<OrderTicketUser> {
    List<OrderTicketUserAggregate> selectOrderTicketUserAggregate(@Param("orderNumberList") List<Long> orderNumberList);
}
