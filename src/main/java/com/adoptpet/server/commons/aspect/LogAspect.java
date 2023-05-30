//package com.adoptpet.server.commons.aspect;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//import java.util.Objects;
//
//@Aspect
//@Slf4j
//@Component
//public class LogAspect {
//
//    @Pointcut("execution(* com.adoptpet.server.adopt.controller..*.*(..))")
//    public void beforeExecute() {}
//
//    @Before("beforeExecute()")
//    public void requestLogging(JoinPoint joinPoint) {
//
//        // 실행되는 메서드 이름을 가져오고 출력
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        log.info(method.getName() + " 메서드 요청 시작");
//
//        // 메서드에 들어가는 매개변수 배열을 읽어온다.
//        Object[] paramArgs = joinPoint.getArgs();
//
//        // 매개변수 배열의 종류와 값을 출력한다.
//        if (Objects.nonNull(paramArgs)) {
//            for (Object object : paramArgs) {
//                log.info("type = {}", object.getClass().getSimpleName());
//                log.info("value = {}", object);
//            }
//        }
//
//        log.info("요청 종료 , 종료된 메서드 => {}", method.getName());
//
//    }
//
//}
