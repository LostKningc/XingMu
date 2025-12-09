package top.ashher.xingmu.service.composite.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ashher.xingmu.dto.UserRegisterDto;
import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.service.composite.AbstractUserRegisterCheckHandler;
import top.ashher.xingmu.util.StringUtil;

/**
 * 用户注册验证码验证处理器
 */
@Slf4j
@Component
public class UserRegisterVerifyCaptcha extends AbstractUserRegisterCheckHandler {

//    @Autowired
//    private CaptchaHandle captchaHandle;
//
//    @Autowired
//    private RedisCache redisCache;

    @Override
    protected void doExecute(UserRegisterDto param) {
//        String password = param.getPassword();
//        String confirmPassword = param.getConfirmPassword();
//        if (!password.equals(confirmPassword)) {
//            throw new XingMuFrameException(BaseCode.TWO_PASSWORDS_DIFFERENT);
//        }
//        String verifyCaptcha = redisCache.get(RedisKeyBuild.createRedisKey(RedisKeyManage.VERIFY_CAPTCHA_ID,param.getCaptchaId()), String.class);
//        if (StringUtil.isEmpty(verifyCaptcha)) {
//            throw new XingMuFrameException(BaseCode.VERIFY_CAPTCHA_ID_NOT_EXIST);
//        }
//        if (VerifyCaptcha.YES.getValue().equals(verifyCaptcha)) {
//            if (StringUtil.isEmpty(param.getCaptchaVerification())) {
//                throw new XingMuFrameException(BaseCode.VERIFY_CAPTCHA_EMPTY);
//            }
//            log.info("传入的captchaVerification:{}",param.getCaptchaVerification());
//            CaptchaVO captchaVO = new CaptchaVO();
//            captchaVO.setCaptchaVerification(param.getCaptchaVerification());
//            ResponseModel responseModel = captchaHandle.verification(captchaVO);
//            if (!responseModel.isSuccess()) {
//                throw new XingMuFrameException(responseModel.getRepCode(),responseModel.getRepMsg());
//            }
//        }
    }

    @Override
    public Integer executeParentOrder() {
        return 0;
    }

    @Override
    public Integer executeTier() {
        return 1;
    }

    @Override
    public Integer executeOrder() {
        return 2;
    }
}
