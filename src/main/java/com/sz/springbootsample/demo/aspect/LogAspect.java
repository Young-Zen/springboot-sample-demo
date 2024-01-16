package com.sz.springbootsample.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sz.springbootsample.demo.config.property.LogProperties;
import com.sz.springbootsample.demo.dto.LogDTO;
import com.sz.springbootsample.demo.thread.threadlocal.LogHolder;
import com.sz.springbootsample.demo.util.AspectUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志切面类
 *
 * @author Yanghj
 * @date 1/2/2020
 */
@Aspect
@Order(1)
@ConditionalOnProperty(name = "custom.log.enable", havingValue = "true", matchIfMissing = true)
@Component
@Slf4j
public class LogAspect {

    @Autowired private LogProperties logProperties;

    /**
     * 方法前置通知，打印方法入参
     *
     * @param joinPoint
     */
    @Before(
            value =
                    "com.sz.springbootsample.demo.aspect.Pointcuts.controllerAspect()"
                            + "||com.sz.springbootsample.demo.aspect.Pointcuts.serviceAspect()"
                            + "||com.sz.springbootsample.demo.aspect.Pointcuts.daoAspect()")
    public void methodBefore(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature() + "LogAspect before advice");
        LogDTO logDTO = LogHolder.getLogDto();
        logDTO.setLogStep(logDTO.getLogStep() + 1).setAdviceCount(logDTO.getAdviceCount() + 1);
        //        LogHolder.setLogDto(logDTO);
        if (logDTO.getIsIgnoreTracing() || !logProperties.getParam()) {
            return;
        }
        log.info(
                "第{}步，方法名：{}，参数：{}",
                logDTO.getLogStep(),
                joinPoint.getSignature(),
                AspectUtils.getInstance().getMethodParams(joinPoint));
    }

    /**
     * 方法后置增强，打印方法返回结果
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(
            value =
                    "com.sz.springbootsample.demo.aspect.Pointcuts.controllerAspect()"
                            + "||com.sz.springbootsample.demo.aspect.Pointcuts.serviceAspect()"
                            + "||com.sz.springbootsample.demo.aspect.Pointcuts.daoAspect()",
            returning = "result")
    public void methodAferReturning(JoinPoint joinPoint, Object result) {
        System.out.println(joinPoint.getSignature() + "LogAspect after returning advice");
        LogDTO logDTO = LogHolder.getLogDto();
        logDTO.setAdviceCount(logDTO.getAdviceCount() + 1);
        //        LogHolder.setLogDto(logDTO);
        if (logDTO.getIsIgnoreTracing() || !logProperties.getResult()) {
            return;
        }
        log.info("结果：[{}] {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(
            value = "com.sz.springbootsample.demo.aspect.Pointcuts.controllerAspect()",
            throwing = "cause")
    public void methodAfterThrowing(JoinPoint joinPoint, Throwable cause) {
        System.out.println(joinPoint.getSignature() + "LogAspect after throwing advice");
        LogDTO logDTO = LogHolder.getLogDto();
        logDTO.setAdviceCount(logDTO.getAdviceCount() + 1).setIsThrowing(true);
        //        LogHolder.setLogDto(logDTO);
    }
}
