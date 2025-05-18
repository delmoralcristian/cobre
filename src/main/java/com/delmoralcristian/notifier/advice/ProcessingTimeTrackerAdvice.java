package com.delmoralcristian.notifier.advice;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ProcessingTimeTrackerAdvice {

    @Around("@annotation(com.delmoralcristian.notifier.advice.TrackProcessingTime)")
    public Object processingTrackTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        var startTime = System.currentTimeMillis();
        var objectProceeded = proceedingJoinPoint.proceed();
        var timeToExecute = System.currentTimeMillis() - startTime;
        var simpleDateFormat = new SimpleDateFormat("mm:ss.SSS");
        var logMessage = "Method name: "
            + proceedingJoinPoint.getSignature()
            + " time taken to execute: "
            + simpleDateFormat.format(new Date(timeToExecute));
        log.info(logMessage);
        return objectProceeded;
    }
}
