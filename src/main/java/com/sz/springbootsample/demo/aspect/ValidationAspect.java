package com.sz.springbootsample.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author Yanghj
 * @date 2023/6/25 16:03
 */
@Aspect
@Order(2)
@Component
public class ValidationAspect {

    /**
     * joinPoint.getTarget()方法返回被代理的目标对象，即被代理对象的实例。如果目标对象没有实现接口，则返回目标对象本身。如果目标对象实现了接口，则返回代理对象。
     * joinPoint.getThis()方法返回代理对象本身。如果目标对象没有实现接口，则返回代理对象。如果目标对象实现了接口，则返回代理对象的一个实现了目标对象接口的代理对象。
     * 因此，如果目标对象没有实现接口，则joinPoint.getTarget()和joinPoint.getThis()方法返回的是同一个对象。如果目标对象实现了接口，则joinPoint.getTarget()方法返回的是目标对象本身，而joinPoint.getThis()方法返回的是代理对象的一个实现了目标对象接口的代理对象。
     * 在使用validator.forExecutables().validateParameters() 方法进行方法参数校验时，应该根据具体情况选择传入joinPoint.getTarget()或joinPoint.getThis()方法返回的对象。如果目标对象没有实现接口，则两者返回的是同一个对象，可以任选一个。
     * 如果目标对象实现了接口，则应该传入joinPoint.getTarget()方法返回的目标对象本身，以确保校验器能够正确地校验方法参数。
     * @param joinPoint
     */
    @Before("execution(* com.sz.springbootsample.demo.util.ParamValidateUtil.verifyParams(..))")
    public void before(JoinPoint joinPoint) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Set<ConstraintViolation<Object>> constraintViolations = validator.forExecutables().validateParameters(joinPoint.getTarget(), signature.getMethod(), joinPoint.getArgs());
        if (!constraintViolations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
                sb.append(constraintViolation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
