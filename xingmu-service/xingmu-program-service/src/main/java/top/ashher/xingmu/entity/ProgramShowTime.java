package top.ashher.xingmu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ashher.xingmu.database.entity.BaseEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("x_program_show_time")
public class ProgramShowTime extends BaseEntity implements Serializable {

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
     * 演出时间
     */
    private Date showTime;

    /**
     * 演出时间(精确到天)
     */
    private Date showDayTime;

    /**
     * 演出时间所在的星期
     */
    private String showWeekTime;
}
