package com.sz.springbootsample.demo.exception;

import com.sz.springbootsample.demo.enums.ResponseCodeEnum;
import lombok.Data;

/**
 * 业务异常类
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Data
public class BaseException extends RuntimeException {
    /**
     * 异常编码
     */
    private String code;

    /**
     * 异常消息
     */
    private String msg;

    public BaseException() {
        this.code = ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        this.msg = ResponseCodeEnum.INTERNAL_SERVER_ERROR.getMsg();
    }

    public BaseException(String msg) {
        super(msg);
        this.code = ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
    }

    public BaseException(Throwable cause) {
        super(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getMsg(), cause);
        this.code = ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        this.msg = ResponseCodeEnum.INTERNAL_SERVER_ERROR.getMsg();
    }

    public BaseException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BaseException(String msg, Throwable cause) {
        super(msg, cause);
        this.code = ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
    }

    public BaseException(ResponseCodeEnum responseCodeEnum, Throwable cause) {
        super(responseCodeEnum.getMsg(), cause);
        this.code = responseCodeEnum.getCode();
        this.msg = responseCodeEnum.getMsg();
    }

    public BaseException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }
}