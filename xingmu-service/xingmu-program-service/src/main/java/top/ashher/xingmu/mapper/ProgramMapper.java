package top.ashher.xingmu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import top.ashher.xingmu.dto.ProgramListDto;
import top.ashher.xingmu.dto.ProgramPageListDto;
import top.ashher.xingmu.entity.Program;
import org.apache.ibatis.annotations.Param;
import top.ashher.xingmu.entity.ProgramJoinShowTime;

import java.util.List;

@Mapper
public interface ProgramMapper extends BaseMapper<Program> {

    /**
     * 主页查询
     * @param programListDto 参数
     * @return 结果
     * */
    List<Program> selectHomeList(@Param("programListDto") ProgramListDto programListDto);

    /**
     * 分页查询
     * @param page 分页对象
     * @param programPageListDto 参数
     * @return 结果
     * */
    IPage<ProgramJoinShowTime> selectPage(IPage<ProgramJoinShowTime> page,
                                          @Param("programPageListDto") ProgramPageListDto programPageListDto);
}
