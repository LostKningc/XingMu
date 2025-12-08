package top.ashher.xingmu.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ashher.xingmu.common.ApiResponse;
import top.ashher.xingmu.enums.BaseCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class XingMuFrameException extends BaseException{
    private Integer code;

    public XingMuFrameException() {
        super();
    }

    public XingMuFrameException(String message) {
        super(message);
    }


    public XingMuFrameException(String code, String message) {
        super(message);
        this.code = Integer.parseInt(code);
    }

    public XingMuFrameException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public XingMuFrameException(BaseCode baseCode) {
        super(baseCode.getMsg());
        this.code = baseCode.getCode();
    }

    public XingMuFrameException(ApiResponse<?> apiResponse) {
        super(apiResponse.getMessage());
        this.code = apiResponse.getCode();
    }

    public XingMuFrameException(Throwable cause) {
        super(cause);
    }

    public XingMuFrameException(String message, Throwable cause) {
        super(message, cause);
    }

    public XingMuFrameException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getMessage() {
        return super.getMessage();
    }
}
