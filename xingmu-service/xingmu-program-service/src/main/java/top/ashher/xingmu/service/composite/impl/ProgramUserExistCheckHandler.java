package top.ashher.xingmu.service.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.client.OrderClient;
import top.ashher.xingmu.client.UserClient;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.dto.AccountOrderCountDto;
import top.ashher.xingmu.dto.ProgramGetDto;
import top.ashher.xingmu.dto.ProgramOrderCreateDto;
import top.ashher.xingmu.dto.TicketUserListDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.redis.cache.RedisCache;
import top.ashher.xingmu.redis.key.RedisKeyBuild;
import top.ashher.xingmu.redis.key.RedisKeyManage;
import top.ashher.xingmu.service.ProgramService;
import top.ashher.xingmu.service.composite.AbstractProgramCheckHandler;
import top.ashher.xingmu.vo.AccountOrderCountVo;
import top.ashher.xingmu.vo.ProgramVo;
import top.ashher.xingmu.vo.TicketUserVo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 * Program user existence check handler
 */
@Slf4j
@Component
public class ProgramUserExistCheckHandler extends AbstractProgramCheckHandler {

    @Autowired
    private UserClient userClient;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private ProgramService programService;

    @Override
    protected void doExecute(ProgramOrderCreateDto programOrderCreateDto) {
        List<TicketUserVo> ticketUserVoList = redisCache.getValueIsList(RedisKeyBuild.createRedisKey(
                RedisKeyManage.TICKET_USER_LIST, programOrderCreateDto.getUserId()), TicketUserVo.class);
        if (CollectionUtil.isEmpty(ticketUserVoList)) {
            TicketUserListDto ticketUserListDto = new TicketUserListDto();
            ticketUserListDto.setUserId(programOrderCreateDto.getUserId());
            ApiResponse<List<TicketUserVo>> apiResponse = userClient.list(ticketUserListDto);
            if (Objects.equals(apiResponse.getCode(), BaseCode.SUCCESS.getCode())) {
                ticketUserVoList = apiResponse.getData();
            }else {
                log.error("user client rpc getUserAndTicketUserList select response : {}", JSON.toJSONString(apiResponse));
                throw new XingMuFrameException(apiResponse);
            }
        }
        if (CollectionUtil.isEmpty(ticketUserVoList)) {
            throw new XingMuFrameException(BaseCode.TICKET_USER_EMPTY);
        }
        Map<Long, TicketUserVo> ticketUserVoMap = ticketUserVoList.stream()
                .collect(Collectors.toMap(TicketUserVo::getId, ticketUserVo -> ticketUserVo, (v1, v2) -> v2));
        for (Long ticketUserId : programOrderCreateDto.getTicketUserIdList()) {
            if (Objects.isNull(ticketUserVoMap.get(ticketUserId))) {
                throw new XingMuFrameException(BaseCode.TICKET_USER_EMPTY);
            }
        }
        ProgramGetDto programGetDto = new ProgramGetDto();
        programGetDto.setId(programOrderCreateDto.getProgramId());
        ProgramVo programVo = programService.detail(programGetDto);
        if (Objects.isNull(programVo)) {
            throw new XingMuFrameException(BaseCode.PROGRAM_NOT_EXIST);
        }

    }

    @Override
    public Integer executeParentOrder() {
        return 1;
    }

    @Override
    public Integer executeTier() {
        return 2;
    }

    @Override
    public Integer executeOrder() {
        return 3;
    }
}
