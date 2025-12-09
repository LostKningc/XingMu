package top.ashher.xingmu.service;

import cn.hutool.core.bean.BeanUtil;
import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ashher.xingmu.design.composite.CompositeContainer;
import top.ashher.xingmu.dto.UserRegisterDto;
import top.ashher.xingmu.entity.User;
import top.ashher.xingmu.entity.UserMobile;
import top.ashher.xingmu.enums.CompositeCheckType;
import top.ashher.xingmu.mapper.UserMapper;
import top.ashher.xingmu.dto.UserIdDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.mapper.UserMobileMapper;
import top.ashher.xingmu.redisson.bloom.handler.BloomFilterHandler;
import top.ashher.xingmu.vo.UserVo;

import java.util.Objects;

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
    //@ServiceLock(lockType= LockType.Write,name = REGISTER_USER_LOCK,keys = {"#userRegisterDto.mobile"})
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

}
