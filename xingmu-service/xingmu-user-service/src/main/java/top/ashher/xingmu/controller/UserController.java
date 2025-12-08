package top.ashher.xingmu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ashher.xingmu.service.UserService;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.dto.UserIdDto;
import top.ashher.xingmu.vo.UserVo;


@RestController
@RequestMapping("/user")
@Tag(name = "user", description = "用户")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary  = "查询(通过id)")
    @PostMapping(value = "/get/id")
    public ApiResponse<UserVo> getById(@Valid @RequestBody UserIdDto userIdDto){
        return ApiResponse.ok(userService.getById(userIdDto));
    }
}
