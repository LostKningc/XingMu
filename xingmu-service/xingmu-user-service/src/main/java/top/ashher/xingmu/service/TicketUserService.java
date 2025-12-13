package top.ashher.xingmu.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ashher.xingmu.dto.TicketUserDto;
import top.ashher.xingmu.dto.TicketUserIdDto;
import top.ashher.xingmu.dto.TicketUserListDto;
import top.ashher.xingmu.entity.TicketUser;
import top.ashher.xingmu.entity.User;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.mapper.TicketUserMapper;
import top.ashher.xingmu.mapper.UserMapper;
import top.ashher.xingmu.redis.cache.RedisCache;
import top.ashher.xingmu.redis.key.RedisKeyBuild;
import top.ashher.xingmu.redis.key.RedisKeyManage;
import top.ashher.xingmu.redisson.lockinfo.LockType;
import top.ashher.xingmu.redisson.servicelock.annotion.ServiceLock;
import top.ashher.xingmu.service.tool.ServiceUtil;
import top.ashher.xingmu.vo.TicketUserVo;

import java.util.List;
import java.util.Objects;

import static top.ashher.xingmu.redisson.servicelock.core.DistributedLockConstants.ADD_TICKET_USER_LOCK;


@Service
public class TicketUserService extends ServiceImpl<TicketUserMapper, TicketUser> {

    @Autowired
    private TicketUserMapper ticketUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UidGenerator uidGenerator;

    @Autowired
    private RedisCache redisCache;

    @Value("${xingmu.cache.ticket-user.expire-minute:30}")
    private Integer cacheExpireMinute;

    public List<TicketUserVo> list(TicketUserListDto ticketUserListDto) {
        ticketUserListDto.setUserId(ServiceUtil.getRealUserId());
        //先从缓存中查询
        List<TicketUserVo> ticketUserVoList = redisCache.getValueIsList(RedisKeyBuild.createRedisKey(
                RedisKeyManage.TICKET_USER_LIST, ticketUserListDto.getUserId()), TicketUserVo.class);
        if (CollectionUtil.isNotEmpty(ticketUserVoList)) {
            return ticketUserVoList;
        }
        LambdaQueryWrapper<TicketUser> ticketUserLambdaQueryWrapper = Wrappers.lambdaQuery(TicketUser.class)
                .eq(TicketUser::getUserId, ticketUserListDto.getUserId());
        List<TicketUser> ticketUsers = ticketUserMapper.selectList(ticketUserLambdaQueryWrapper);
        List<TicketUserVo> result = BeanUtil.copyToList(ticketUsers, TicketUserVo.class);
        //放入缓存
        if (CollectionUtil.isNotEmpty(result)) {
            redisCache.set(RedisKeyBuild.createRedisKey(
                    RedisKeyManage.TICKET_USER_LIST, ticketUserListDto.getUserId()), result, cacheExpireMinute, java.util.concurrent.TimeUnit.MINUTES);
        }
        return result;
    }

    @ServiceLock(lockType = LockType.Write, name = ADD_TICKET_USER_LOCK, keys = {"#ticketUserDto.userId", "#ticketUserDto.idType", "#ticketUserDto.idNumber"})
    @Transactional(rollbackFor = Exception.class)
    public void add(TicketUserDto ticketUserDto) {
        //ticketUserDto.setUserId(ServiceUtil.getRealUserId());
        //为了保证锁的粒度，查询真实用户移动到Controller层
        User user = userMapper.selectById(ticketUserDto.getUserId());
        if (Objects.isNull(user)) {
            throw new XingMuFrameException(BaseCode.USER_EMPTY);
        }
        LambdaQueryWrapper<TicketUser> ticketUserLambdaQueryWrapper = Wrappers.lambdaQuery(TicketUser.class)
                .eq(TicketUser::getUserId, ticketUserDto.getUserId())
                .eq(TicketUser::getIdType, ticketUserDto.getIdType())
                .eq(TicketUser::getIdNumber, ticketUserDto.getIdNumber());
        TicketUser ticketUser = ticketUserMapper.selectOne(ticketUserLambdaQueryWrapper);
        if (Objects.nonNull(ticketUser)) {
            throw new XingMuFrameException(BaseCode.TICKET_USER_EXIST);
        }
        TicketUser addTicketUser = new TicketUser();
        BeanUtil.copyProperties(ticketUserDto,addTicketUser);
        addTicketUser.setId(uidGenerator.getUID());
        ticketUserMapper.insert(addTicketUser);
        delTicketUserVoListCache(String.valueOf(ticketUserDto.getUserId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(TicketUserIdDto ticketUserIdDto) {
        TicketUser ticketUser = ticketUserMapper.selectById(ticketUserIdDto.getId());
        if (Objects.isNull(ticketUser)) {
            throw new XingMuFrameException(BaseCode.TICKET_USER_EMPTY);
        }
        ticketUserMapper.deleteById(ticketUserIdDto.getId());
        delTicketUserVoListCache(String.valueOf(ticketUser.getUserId()));
    }

    public void delTicketUserVoListCache(String userId){
        redisCache.del(RedisKeyBuild.createRedisKey(RedisKeyManage.TICKET_USER_LIST, userId));
    }
}
