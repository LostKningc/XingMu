package top.ashher.xingmu.client;

import org.springframework.stereotype.Component;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.dto.AreaGetDto;
import top.ashher.xingmu.dto.AreaSelectDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.vo.AreaVo;
import top.ashher.xingmu.vo.TokenDataVo;

import java.util.List;

@Component
public class BaseDataClientFallback implements BaseDataClient{

    @Override
    public ApiResponse<TokenDataVo> get() {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }

    @Override
    public ApiResponse<List<AreaVo>> selectByIdList(final AreaSelectDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }

    @Override
    public ApiResponse<AreaVo> getById(final AreaGetDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
