package com.sz.springboot_sample.demo.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Yanghj
 * @date 1/2/2020
 */
public class Pointcuts {
    @Pointcut("((@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)) && execution(public * com.sz..*.*(..)))")
    public void controllerAspect() {
    }

    @Pointcut("execution(* com.baomidou.mybatisplus.*.service.*.*(..))")
    public void serviceMybatisAspect() {
    }

    @Pointcut("execution(* com.sz..service.*.*(..)) || serviceMybatisAspect()")
    public void serviceAspect() {
    }

    @Pointcut("execution(* com.baomidou.mybatisplus.core.mapper.*.*(..))")
    public void daoMybatisAspect() {
    }

    @Pointcut("execution(* com.sz..dao.*.*(..)) || daoMybatisAspect()")
    public void daoAspect() {
    }
}
