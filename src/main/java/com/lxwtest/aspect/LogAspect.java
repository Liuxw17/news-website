package com.lxwtest.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 切面
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.lxwtest.controller.*Controller.*(..))")//括号里的是正则表达式 【我觉得这里是切面的精髓】
    public void beforeMenthod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for(Object arg:joinPoint.getArgs()){//得到调用这个方法的全部参数
            sb.append("arg:"+arg.toString()+"|");
        }
//        logger.info("before time：",new Data());
        logger.info("before method:"+ sb.toString());
    }
    @After("execution(* com.lxwtest.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint){
        logger.info("after method:");
    }
}
