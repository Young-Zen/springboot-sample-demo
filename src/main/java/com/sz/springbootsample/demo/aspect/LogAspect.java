package com.sz.springbootsample.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
                    "!com.sz.springbootsample.demo.aspect.Pointcuts.ignoreTracingAspect()"
                            + "&&(com.sz.springbootsample.demo.aspect.Pointcuts.controllerAspect()"
                            + "||com.sz.springbootsample.demo.aspect.Pointcuts.serviceAspect()"
                            + "||com.sz.springbootsample.demo.aspect.Pointcuts.daoAspect())")
    public void methodBefore(JoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            log.debug("LogAspect before advice: {}", joinPoint.getSignature());
        }
        LogDTO logDTO = LogHolder.getLogDto();
        logDTO.setLogStep(logDTO.getLogStep() + 1).setAdviceCount(logDTO.getAdviceCount() + 1);
        if (logDTO.getIsIgnoreTracing() || !logProperties.getParam()) {
            return;
        }
        if (log.isInfoEnabled()) {
            log.info(
                    "第{}步，[{}] 参数：{}",
                    logDTO.getLogStep(),
                    joinPoint.getSignature().toShortString(),
                    AspectUtils.getInstance().getMethodParams(joinPoint));
        }
    }

    /**
     * 方法后置增强，打印方法返回结果
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(
            value =
                    "!com.sz.springbootsample.demo.aspect.Pointcuts.ignoreTracingAspect()"
                            + "&&(com.sz.springbootsample.demo.aspect.Pointcuts.controllerAspect()"
                            + "||com.sz.springbootsample.demo.aspect.Pointcuts.serviceAspect()"
                            + "||com.sz.springbootsample.demo.aspect.Pointcuts.daoAspect())",
            returning = "result")
    public void methodAfterReturning(JoinPoint joinPoint, Object result) {
        if (log.isDebugEnabled()) {
            log.debug("LogAspect after returning advice: {}", joinPoint.getSignature());
        }
        LogDTO logDTO = LogHolder.getLogDto();
        logDTO.setAdviceCount(logDTO.getAdviceCount() + 1);
        if (logDTO.getIsIgnoreTracing() || !logProperties.getResult()) {
            return;
        }
        if (log.isInfoEnabled()) {
            log.info("[{}] 结果：{}", joinPoint.getSignature().toShortString(), result);
        }
    }

    @AfterThrowing(
            value =
                    "!com.sz.springbootsample.demo.aspect.Pointcuts.ignoreTracingAspect()"
                            + "&&com.sz.springbootsample.demo.aspect.Pointcuts.controllerAspect()",
            throwing = "cause")
    public void methodAfterThrowing(JoinPoint joinPoint, Throwable cause) {
        if (log.isDebugEnabled()) {
            log.debug("LogAspect after throwing advice: {}", joinPoint.getSignature());
        }
        LogDTO logDTO = LogHolder.getLogDto();
        logDTO.setAdviceCount(logDTO.getAdviceCount() + 1).setIsThrowing(true);
    }
}
