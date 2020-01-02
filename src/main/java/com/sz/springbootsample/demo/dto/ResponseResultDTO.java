package com.sz.springbootsample.demo.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sz.springbootsample.demo.enums.ResponseCodeEnum;
import lombok.Data;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@Data
public class ResponseResultDTO {
    private String code;
    private String msg;
    private Object data;

    public <T> T getData(Class<T> clazz) {
        return JSONObject.parseObject(JSON.toJSONString(data), clazz);
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
