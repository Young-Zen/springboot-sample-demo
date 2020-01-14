package com.sz.springbootsample.demo.dto;

import com.sz.springbootsample.demo.enums.ResponseCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 响应结果对象
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "响应返回结果")
public class ResponseResultDTO {
    @ApiModelProperty("响应码")
    private String code;
    @ApiModelProperty("响应消息")
    private String msg;
    @ApiModelProperty("响应数据")
    private Object data;

    public <T> T getData(Class<T> clazz) {
        if (clazz.isInstance(data)) {
            return clazz.cast(data);
        }
        return null;
    }

    public static ResponseResultDTO ok() {
        ResponseResultDTO responseResultDTO = new ResponseResultDTO();
        responseResultDTO.setCode(ResponseCodeEnum.OK.getCode());
        responseResultDTO.setMsg(ResponseCodeEnum.OK.getMsg());
        return responseResultDTO;
    }

    public static ResponseResultDTO ok(String msg) {
        ResponseResultDTO responseResultDTO = ok();
        responseResultDTO.setMsg(msg);
        return responseResultDTO;
    }

    public static ResponseResultDTO ok(Object data) {
        ResponseResultDTO responseResultDTO = ok();
        responseResultDTO.setData(data);
        return responseResultDTO;
    }

    public static ResponseResultDTO ok(String msg, Object data) {
        ResponseResultDTO responseResultDTO = ok();
        responseResultDTO.setMsg(msg);
        responseResultDTO.setData(data);
        return responseResultDTO;
    }

    public static ResponseResultDTO ok(String code, String msg, Object data) {
        ResponseResultDTO responseResultDTO = ok();
        responseResultDTO.setCode(code);
        responseResultDTO.setMsg(msg);
        responseResultDTO.setData(data);
        return responseResultDTO;
    }

    public static ResponseResultDTO ok(String code, String msg) {
        ResponseResultDTO responseResultDTO = ok();
        responseResultDTO.setCode(code);
        responseResultDTO.setMsg(msg);
        return responseResultDTO;
    }

    public static ResponseResultDTO fail() {
        ResponseResultDTO responseResultDTO = new ResponseResultDTO();
        responseResultDTO.setCode(ResponseCodeEnum.FAIL.getCode());
        responseResultDTO.setMsg(ResponseCodeEnum.FAIL.getMsg());
        return responseResultDTO;
    }

    public static ResponseResultDTO fail(ResponseCodeEnum responseCodeEnum) {
        ResponseResultDTO responseResultDTO = new ResponseResultDTO();
        responseResultDTO.setCode(responseCodeEnum.getCode());
        responseResultDTO.setMsg(responseCodeEnum.getMsg());
        return responseResultDTO;
    }

    public static ResponseResultDTO fail(String msg) {
        ResponseResultDTO responseResultDTO = fail();
        responseResultDTO.setMsg(msg);
        return responseResultDTO;
    }

    public static ResponseResultDTO fail(String errCode, String msg) {
        ResponseResultDTO responseResultDTO = new ResponseResultDTO();
        responseResultDTO.setCode(errCode);
        responseResultDTO.setMsg(msg);
        return responseResultDTO;
    }

    public static ResponseResultDTO fail(String errCode, String msg, Object data) {
        ResponseResultDTO responseResultDTO = new ResponseResultDTO();
        responseResultDTO.setCode(errCode);
        responseResultDTO.setMsg(msg);
        responseResultDTO.setData(data);
        return responseResultDTO;
    }
}
