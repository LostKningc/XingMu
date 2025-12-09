package top.ashher.xingmu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ashher.xingmu.entity.UserMobile;
import top.ashher.xingmu.mapper.UserMobileMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMobileService {

    @Autowired
    private UserMobileMapper userMobileMapper;

    public List<String> getAllMobile() {
        // 1. 针对 UserMobile 表查询
        LambdaQueryWrapper<UserMobile> wrapper = Wrappers.lambdaQuery();

        // 2. 只查 mobile 字段 (这一步依然很重要，进一步减少传输)
        wrapper.select(UserMobile::getMobile);

        // 3. 执行查询 (MyBatis-Plus 会根据分片规则去查 d_user_mobile_0 和 _1)
        List<Object> result = userMobileMapper.selectObjs(wrapper);

        return result.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }
}
