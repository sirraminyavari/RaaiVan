package com.raaivan.web.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DBActionDuration {
    @Pointcut("@annotation(com.raaivan.web.aspects.annotations.RVDBCall)")
    public void DBCall() {
    }

    @Around("DBCall()")
    public Object calculateDuration(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object returnValue = joinPoint.proceed();
        long stopTime = System.currentTimeMillis();

        StringBuilder message = new StringBuilder("Procedure Name: ");

        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if(arg instanceof String){
                message.append((String) arg);
                break;
            }
        }

        message.append(", Duration: " + (stopTime - startTime) + " miliseconds.");

        System.out.println(message.toString());

        return returnValue;
    }
}
