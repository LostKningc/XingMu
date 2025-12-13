package top.ashher.xingmu.service.tool;

import top.ashher.xingmu.enums.BaseCode;
import top.ashher.xingmu.exception.XingMuFrameException;
import top.ashher.xingmu.threadlocal.BaseParameterHolder;
import top.ashher.xingmu.util.StringUtil;

import static top.ashher.xingmu.constant.Constant.USER_ID;

public class ServiceUtil {
    public static Long getRealUserId(){
        String userIdStr = BaseParameterHolder.getParameter(USER_ID);
        if (StringUtil.isEmpty(userIdStr)) {
            throw new XingMuFrameException(BaseCode.LOGIN_USER_NOT_EXIST);
        }
        return Long.parseLong(userIdStr);
    }
}
