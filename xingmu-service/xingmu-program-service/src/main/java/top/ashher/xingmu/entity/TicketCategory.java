package top.ashher.xingmu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.ashher.xingmu.database.entity.BaseEntity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("x_ticket_category")
public class TicketCategory extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 节目表id
     */
    private Long programId;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 总数量
     * */
    private Long totalNumber;

    /**
     * 剩余数量
     * */
    private Long remainNumber;


}
