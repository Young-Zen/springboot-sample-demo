package com.sz.springbootsample.demo.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sz.springbootsample.demo.dto.ResponseResultDTO;
import com.sz.springbootsample.demo.enums.ResponseCodeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理类
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseResultDTO handleException(Exception e) {
        log.error("应用程序抛出异常", e);
        return ResponseResultDTO.fail(
                ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(),
                e.getClass().getName() + e.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public ResponseResultDTO handleBaseException(BaseException e) {
        log.warn("业务异常", e);
        return ResponseResultDTO.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResultDTO handleAccessDeniedException(AccessDeniedException e) {
        log.warn("鉴权异常", e);
        return ResponseResultDTO.fail(ResponseCodeEnum.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResultDTO handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.warn("请求体校验异常", e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Map<String, String> errors = new HashMap<>(allErrors.size());
        allErrors.forEach(
                (error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return ResponseResultDTO.fail(ResponseCodeEnum.ARGUMENT_VALID_FAIL).setData(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResultDTO handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("请求参数校验异常", e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            sb.append(constraintViolation.getMessage()).append("\n");
        }
        return ResponseResultDTO.fail(
                ResponseCodeEnum.ARGUMENT_VALID_FAIL.getCode(), sb.toString());
    }
}
