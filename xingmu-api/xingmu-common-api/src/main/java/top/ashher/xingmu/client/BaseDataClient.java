package top.ashher.xingmu.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.dto.AreaGetDto;
import top.ashher.xingmu.dto.AreaSelectDto;
import top.ashher.xingmu.vo.AreaVo;
import top.ashher.xingmu.vo.TokenDataVo;

import java.util.List;

import static top.ashher.xingmu.constant.Constant.SPRING_INJECT_PREFIX_DISTINCTION_NAME;


@Component
@FeignClient(value = SPRING_INJECT_PREFIX_DISTINCTION_NAME+"-"+"base-data-service",fallback  = BaseDataClientFallback.class)
public interface BaseDataClient {

    /**
     * 查询token数据
     * @return 结果
     * */
    @PostMapping(value = "/get")
    ApiResponse<TokenDataVo> get();

    /**
     * 根据id集合查询地区列表
     * @param dto 参数
     * @return 结果
     * */
    @PostMapping(value = "/area/selectByIdList")
    ApiResponse<List<AreaVo>> selectByIdList(AreaSelectDto dto);

    /**
     * 根据id查询地区
     * @param dto 参数
     * @return 结果
     * */
    @PostMapping(value = "/area/getById")
    ApiResponse<AreaVo> getById(AreaGetDto dto);
}