package top.ashher.xingmu.client;

import org.springframework.stereotype.Component;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.dto.TicketUserListDto;
import top.ashher.xingmu.dto.UserGetAndTicketUserListDto;
import top.ashher.xingmu.dto.UserIdDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.vo.TicketUserVo;
import top.ashher.xingmu.vo.UserGetAndTicketUserListVo;
import top.ashher.xingmu.vo.UserVo;

import java.util.List;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public ApiResponse<UserVo> getById(final UserIdDto userIdDto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }

    @Override
    public ApiResponse<List<TicketUserVo>> list(final TicketUserListDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }

    @Override
    public ApiResponse<UserGetAndTicketUserListVo> getUserAndTicketUserList(final UserGetAndTicketUserListDto dto) {
        return ApiResponse.error(BaseCode.SYSTEM_ERROR);
    }
}
