package top.ashher.xingmu.client;

import org.springframework.stereotype.Component;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.dto.AccountOrderCountDto;
import top.ashher.xingmu.dto.OrderCreateDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.vo.AccountOrderCountVo;

@Component
public class OrderClientFallback implements OrderClient {

    @Override
    public ApiResponse<String> create(final OrderCreateDto orderCreateDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }

    @Override
    public ApiResponse<AccountOrderCountVo> accountOrderCount(final AccountOrderCountDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
