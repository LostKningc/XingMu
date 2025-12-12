package top.ashher.xingmu.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ashher.xingmu.design.composite.CompositeContainer;
import top.ashher.xingmu.dto.UserLoginDto;
import top.ashher.xingmu.dto.UserRegisterDto;
import top.ashher.xingmu.entity.User;
import top.ashher.xingmu.entity.UserEmail;
import top.ashher.xingmu.entity.UserMobile;
import top.ashher.xingmu.enums.CompositeCheckType;
import top.ashher.xingmu.jwt.TokenUtil;
import top.ashher.xingmu.mapper.UserEmailMapper;
import top.ashher.xingmu.mapper.UserMapper;
import top.ashher.xingmu.dto.UserIdDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.mapper.UserMobileMapper;
import top.ashher.xingmu.redis.cache.RedisCache;
import top.ashher.xingmu.redis.key.RedisKeyBuild;
import top.ashher.xingmu.redis.key.RedisKeyManage;
import top.ashher.xingmu.redisson.bloom.handler.BloomFilterHandler;
import top.ashher.xingmu.redisson.lockinfo.LockType;
import top.ashher.xingmu.redisson.servicelock.annotion.ServiceLock;
import top.ashher.xingmu.util.StringUtil;
import top.ashher.xingmu.vo.UserLoginVo;
import top.ashher.xingmu.vo.UserVo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static top.ashher.xingmu.redisson.servicelock.core.DistributedLockConstants.REGISTER_USER_LOCK;

@Slf4j
@Service
@MapperScan("top.ashher.xingmu.mapper")
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserMobileMapper userMobileMapper;
    @Autowired
    private CompositeContainer<UserRegisterDto> compositeContainer;
    @Autowired
    private UidGenerator uidGenerator;
    @Autowired
    private BloomFilterHandler bloomFilterHandler;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserEmailMapper userEmailMapper;

    @Value("${xingmu.jwt.secret}")
    private String tokenSecret;
    @Value("${xingmu.jwt.expire}")
    private Long tokenExpireTime;

    private static final Integer ERROR_COUNT_THRESHOLD = 5;


    public UserVo getById(UserIdDto userIdDto) {
        User user = userMapper.selectById(userIdDto.getId());
        if (Objects.isNull(user)) {
            throw new XingMuFrameException(BaseCode.USER_EMPTY);
        }
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user,userVo);
        return userVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @ServiceLock(lockType= LockType.Write,name = REGISTER_USER_LOCK,keys = {"#userRegisterDto.mobile"})
    public Boolean register(UserRegisterDto userRegisterDto) {
        compositeContainer.execute(CompositeCheckType.USER_REGISTER_CHECK.getValue(),userRegisterDto);
        log.info("注册手机号:{}",userRegisterDto.getMobile());
        //用户表添加
        User user = new User();
        BeanUtils.copyProperties(userRegisterDto,user);
        user.setId(uidGenerator.getUID());
        userMapper.insert(user);
        //用户手机表添加
        UserMobile userMobile = new UserMobile();
        userMobile.setId(uidGenerator.getUID());
        userMobile.setUserId(user.getId());
        userMobile.setMobile(userRegisterDto.getMobile());
        userMobileMapper.insert(userMobile);
        bloomFilterHandler.add(userMobile.getMobile());
        return true;
    }

    /**
     * 登录
     * @param userLoginDto 登录入参
     * @return 用户信息
     * */
    public UserLoginVo login(UserLoginDto userLoginDto) {
        String mobile = userLoginDto.getMobile();
        String email = userLoginDto.getEmail();
        String password = userLoginDto.getPassword();

        if (StringUtil.isEmpty(mobile) && StringUtil.isEmpty(email)) {
            throw new XingMuFrameException(BaseCode.USER_MOBILE_AND_EMAIL_NOT_EXIST);
        }

        Long userId;
        RedisKeyBuild errorKeyBuild;

        if (StringUtil.isNotEmpty(mobile)) {
            errorKeyBuild = RedisKeyBuild.createRedisKey(RedisKeyManage.LOGIN_USER_MOBILE_ERROR, mobile);
            checkErrorCount(errorKeyBuild);

            UserMobile userMobile = userMobileMapper.selectOne(Wrappers.lambdaQuery(UserMobile.class).eq(UserMobile::getMobile, mobile));
            if (Objects.isNull(userMobile)) {
                recordError(errorKeyBuild); // 记录错误
                throw new XingMuFrameException(BaseCode.USER_MOBILE_EMPTY);
            }
            userId = userMobile.getUserId();
        } else {
            errorKeyBuild = RedisKeyBuild.createRedisKey(RedisKeyManage.LOGIN_USER_EMAIL_ERROR, email);
            checkErrorCount(errorKeyBuild); // 检查锁定

            UserEmail userEmail = userEmailMapper.selectOne(Wrappers.lambdaQuery(UserEmail.class).eq(UserEmail::getEmail, email));
            if (Objects.isNull(userEmail)) {
                recordError(errorKeyBuild); // 记录错误
                throw new XingMuFrameException(BaseCode.USER_EMAIL_NOT_EXIST);
            }
            userId = userEmail.getUserId();
        }

        // --- 步骤 2: 校验密码 ---
        User user = userMapper.selectById(userId);

        if (Objects.isNull(user) || !user.getPassword().equals(password)) {
            recordError(errorKeyBuild); // 记录错误
            throw new XingMuFrameException(BaseCode.NAME_PASSWORD_ERROR);
        }

        // --- 步骤 3: 登录成功 ---

        redisCache.del(errorKeyBuild);
        RedisKeyBuild loginKeyBuild = RedisKeyBuild.createRedisKey(RedisKeyManage.USER_LOGIN, "WEB", user.getId());

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUserId(userId);

        String token = createToken(user.getId(), tokenSecret);
        userLoginVo.setToken(token);

        redisCache.set(
                loginKeyBuild,
                token,
                tokenExpireTime,
                TimeUnit.MINUTES
        );

        return userLoginVo;
    }

    private void checkErrorCount(RedisKeyBuild keyBuild) {
        // 新版 get 方法签名：get(RedisKeyBuild, Class<T>)
        String errorCountStr = redisCache.get(keyBuild, String.class);

        if (StringUtil.isNotEmpty(errorCountStr) && Integer.parseInt(errorCountStr) >= ERROR_COUNT_THRESHOLD) {
            throw new XingMuFrameException(BaseCode.ERROR_COUNT_TOO_MANY);
        }
    }

    private void recordError(RedisKeyBuild keyBuild) {
        if (keyBuild == null) return;
        // 新版 incrBy 和 expire 直接使用对象
        redisCache.incrBy(keyBuild, 1);
        redisCache.expire(keyBuild, 1, TimeUnit.MINUTES);
    }


    public void doExist(String mobile){
        boolean contains = bloomFilterHandler.contains(mobile);
        if (contains) {
            LambdaQueryWrapper<UserMobile> queryWrapper = Wrappers.lambdaQuery(UserMobile.class)
                    .eq(UserMobile::getMobile, mobile);
            UserMobile userMobile = userMobileMapper.selectOne(queryWrapper);
            if (Objects.nonNull(userMobile)) {
                throw new XingMuFrameException(BaseCode.USER_EXIST);
            }
        }
    }

    public String createToken(Long userId,String tokenSecret){
        Map<String,Object> map = new HashMap<>(4);
        map.put("userId",userId);
        return TokenUtil.createToken(String.valueOf(uidGenerator.getUID()), JSON.toJSONString(map),tokenExpireTime * 60 * 1000,tokenSecret);
    }

}
