package top.ashher.xingmu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.ashher.xingmu.dto.TicketCategoryCountDto;
import top.ashher.xingmu.entity.TicketCategory;
import top.ashher.xingmu.entity.TicketCategoryAggregate;

import java.util.List;

@Mapper
public interface TicketCategoryMapper extends BaseMapper<TicketCategory> {

    /**
     * 票档统计
     * @param programIdList 参数
     * @return 结果
     * */
    List<TicketCategoryAggregate> selectAggregateList(@Param("programIdList") List<Long> programIdList);

    /**
     * 更新数量
     * @param number 数量
     * @param id id
     * @return 结果
     * */
    int updateRemainNumber(@Param("number")Long number,@Param("id")Long id);

    /**
     * 批量更新数量
     * @param ticketCategoryCountDtoList 参数
     * @param programId 参数
     * @return 结果
     * */
    int batchUpdateRemainNumber(@Param("ticketCategoryCountDtoList")
                                List<TicketCategoryCountDto> ticketCategoryCountDtoList,
                                @Param("programId")
                                Long programId);
}
