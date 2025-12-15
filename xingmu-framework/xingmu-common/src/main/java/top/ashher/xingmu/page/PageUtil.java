package top.ashher.xingmu.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.ashher.xingmu.dto.BasePageDto;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageUtil {

    /**
     * 获取 MyBatis-Plus 分页参数
     */
    public static <T> Page<T> getPageParams(BasePageDto basePageDto) {
        return new Page<>(basePageDto.getPageNumber(), basePageDto.getPageSize());
    }

    /**
     * 核心转换方法：IPage<Entity> -> PageVo<Vo>
     */
    public static <T, R> PageVo<R> convertPage(IPage<T> sourcePage, Function<T, R> mapper) {
        if (sourcePage == null) {
            return new PageVo<>(1, 10, 0, Collections.emptyList());
        }

        // 利用 Stream 转换列表
        List<R> collected = sourcePage.getRecords().stream()
                .map(mapper)
                .collect(Collectors.toList());

        return new PageVo<>(
                sourcePage.getCurrent(),
                sourcePage.getSize(),
                sourcePage.getTotal(),
                collected
        );
    }
}
