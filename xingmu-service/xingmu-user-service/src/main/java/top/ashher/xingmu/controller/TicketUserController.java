package top.ashher.xingmu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.dto.TicketUserDto;
import top.ashher.xingmu.dto.TicketUserIdDto;
import top.ashher.xingmu.dto.TicketUserListDto;
import top.ashher.xingmu.service.TicketUserService;
import top.ashher.xingmu.service.tool.ServiceUtil;
import top.ashher.xingmu.vo.TicketUserVo;

import java.util.List;

@RestController
@RequestMapping("/ticket/user")
@Tag(name = "ticket-user", description = "购票人")
public class TicketUserController {

    @Autowired
    private TicketUserService ticketUserService;

    @Operation(summary  = "查询购票人列表")
    @PostMapping(value = "/list")
    public ApiResponse<List<TicketUserVo>> list(@Valid @RequestBody TicketUserListDto ticketUserListDto){
        return ApiResponse.ok(ticketUserService.list(ticketUserListDto));
    }

    @Operation(summary  = "添加购票人")
    @PostMapping(value = "/add")
    public ApiResponse<Void> add(@Valid @RequestBody TicketUserDto ticketUserDto){
        //为了保证锁的粒度，查询真实用户移动到Controller层
        ticketUserDto.setUserId(ServiceUtil.getRealUserId());
        ticketUserService.add(ticketUserDto);
        return ApiResponse.ok();
    }

    @Operation(summary  = "删除购票人")
    @PostMapping(value = "/delete")
    public ApiResponse<Void> delete(@Valid @RequestBody TicketUserIdDto ticketUserIdDto){
        ticketUserService.delete(ticketUserIdDto);
        return ApiResponse.ok();
    }
}
