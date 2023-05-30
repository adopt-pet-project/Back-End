package com.adoptpet.server.commons.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("execution(* com.adoptpet.server.adopt.controller..*.*(..))")
    public void adoptBeforeExecute() {}

    @Pointcut("execution(* com.adoptpet.server.community.controller..*.*(..))")
    public void communityBeforeExecute() {}

    @Pointcut("execution(* com.adoptpet.server.user.controller..*.*(..))")
    public void userBeforeExecute() {}

    @Before("adoptBeforeExecute() || userBeforeExecute() || communityBeforeExecute()")
    public void requestLogging(JoinPoint joinPoint) {

        // 실행되는 메서드 이름을 가져오고 출력
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info(method.getName() + "() 메서드 요청 시작");

        log.info("요청 시작 시간 = {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 메서드에 들어가는 매개변수 배열을 읽어온다.
        Object[] paramArgs = joinPoint.getArgs();

        // 매개변수 배열의 종류와 값을 출력한다.
        for (Object object : paramArgs) {
            if (Objects.nonNull(object)) {
                log.info("parameter type => {}", object.getClass().getSimpleName());
                log.info("parameter value => {}", object);
            }
        }

        log.info("요청 종료 시간 = {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("요청 종료 , 종료된 메서드 => {}", method.getName());

    }

    @AfterThrowing(pointcut = "adoptBeforeExecute() || userBeforeExecute() || communityBeforeExecute()", throwing = "exception")
    public void exceptionLogging(JoinPoint joinPoint, Exception exception) {
        // 실행되는 메서드 이름을 가져오고 출력
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.error(method.getName() + "() 메서드 처리 중 예외 발생!!");

        log.error("예외 발생 시간 = {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}
