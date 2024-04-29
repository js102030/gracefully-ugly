package com.gracefullyugly.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAndSuccessLoggingAspect {

    @Pointcut("within(com.gracefullyugly..*)")
    public void applicationPackagePointcut() {
    }

    @Pointcut("within(com.gracefullyugly..*)")
    public void withinTargetPackage() {
    }

    @Around("applicationPackagePointcut()")
    public Object logPerformanceAndSuccess(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            log.info("⭐ {} 성공, 실행 시간 : {}ms", joinPoint.getSignature(), System.currentTimeMillis() - startTime);
        } catch (Throwable throwable) {
            log.error("\uD83D\uDC1B {} 실패, 실행 시간 : {}ms, 에러 메시지: {}", joinPoint.getSignature(),
                    System.currentTimeMillis() - startTime, throwable.getMessage());
            throw throwable;
        }
        return result;
    }

    @Before("withinTargetPackage()")
    public void logMethodStart(JoinPoint joinPoint) {
        log.info("\uD83C\uDFC3 시작 : {}", joinPoint.getSignature().toShortString());
    }

    @After("withinTargetPackage()")
    public void logMethodEnd(JoinPoint joinPoint) {
        log.info("\uD83D\uDD1A 종료 : {}", joinPoint.getSignature().toShortString());
    }

}
