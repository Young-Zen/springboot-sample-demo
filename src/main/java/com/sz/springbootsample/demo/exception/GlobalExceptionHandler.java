package com.sz.springbootsample.demo.exception;

import com.sz.springbootsample.demo.dto.ResponseResultDTO;
import com.sz.springbootsample.demo.enums.ResponseCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseResultDTO handlerException(Exception e) {
        return ResponseResultDTO.fail(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), e.getClass().getName());
    }

    @ExceptionHandler(BaseException.class)
    public ResponseResultDTO handlerBaseException(BaseException e) {
        return ResponseResultDTO.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResultDTO handlerAccessDeniedException(AccessDeniedException e) {
        return ResponseResultDTO.fail(ResponseCodeEnum.FORBIDDEN);
    }
}
