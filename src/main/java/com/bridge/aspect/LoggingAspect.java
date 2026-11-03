package com.bridge.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Arrays;

@Aspect
//@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.bridge.workflow.contract.WorkflowParticipant.prepare(..))")
    public void beforePrepare(JoinPoint jp) {
        log.info("Before: {}", jp.getSignature());
        log.info("Method : {}", jp.getSignature().getName());
        log.info("Class : {}", jp.getTarget().getClass().getSimpleName());
        log.info("Arguments : {}", Arrays.toString(jp.getArgs()));
    }
}
