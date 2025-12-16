package top.ashher.xingmu.client;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.dto.TicketUserListDto;
import top.ashher.xingmu.dto.UserGetAndTicketUserListDto;
import org.springframework.cloud.openfeign.FeignClient;
import top.ashher.xingmu.dto.UserIdDto;
import top.ashher.xingmu.vo.TicketUserVo;
import top.ashher.xingmu.vo.UserGetAndTicketUserListVo;
import top.ashher.xingmu.vo.UserVo;

import java.util.List;

import static top.ashher.xingmu.constant.Constant.SPRING_INJECT_PREFIX_DISTINCTION_NAME;

@Component
@FeignClient(value = SPRING_INJECT_PREFIX_DISTINCTION_NAME+"-"+"user-service",fallback = UserClientFallback.class)
public interface UserClient {

    /**
     * 查询用户(通过id)
     * @param dto 参数
     * @return 结果
     * */
    @PostMapping(value = "/user/getById")
    ApiResponse<UserVo> getById(UserIdDto dto);


    /**
     * 查询购票人(通过userId)
     * @param dto 参数
     * @return 结果
     * */
    @PostMapping(value = "/ticket/user/list")
    ApiResponse<List<TicketUserVo>> list(TicketUserListDto dto);

    /**
     * 查询用户和购票人集合
     * @param dto 参数
     * @return 结果
     */
    @PostMapping(value = "/user/get/user/ticket/list")
    ApiResponse<UserGetAndTicketUserListVo> getUserAndTicketUserList(UserGetAndTicketUserListDto dto);

}
