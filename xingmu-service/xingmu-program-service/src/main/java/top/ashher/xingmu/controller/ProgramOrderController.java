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
import top.ashher.xingmu.dto.ProgramOrderCreateDto;
import top.ashher.xingmu.service.ProgramOrderService;

@RestController
@RequestMapping("/program/order")
@Tag(name = "program-order", description = "节目订单")
public class ProgramOrderController {

    @Autowired
    private ProgramOrderService programOrderService;

    @Operation(summary  = "购票")
    @PostMapping(value = "/create")
    public ApiResponse<String> create(@Valid @RequestBody ProgramOrderCreateDto programOrderCreateDto) {
        return ApiResponse.ok(programOrderService.createOrder(programOrderCreateDto));
    }
}
