package com.sz.springbootsample.demo.enums;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
public enum ResponseCodeEnum {
    /**
     * 通用操作成功
     */
    OK("0", "操作成功"),

    /**
     * 通用操作失败
     */
    FAIL("1", "操作失败"),

    /**
     * 无权访问
     */
    FORBIDDEN("403", "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND("404", "资源不存在"),

    /**
     * 系统内部异常
     */
    INTERNAL_SERVER_ERROR("500", "系统内部异常");

    ResponseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 编码
     */
    private String code;

    /**
     * 消息
     */
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
