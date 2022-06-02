package com.payconiq.stock.aop;

import com.github.benmanes.caffeine.cache.Cache;
import com.payconiq.stock.aop.RateController;
import com.payconiq.stock.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @author Milad Ranjbari
 * @version 2022.6.1
 * @since 6/01/22
 * This class handle Rate Controller/Idempotence for every endpoint we used it asa annotation above it
 */
@Aspect //let know Spring that this is an Aspect class
@Component //Spring will consider this class as a Spring bean
@Order(1)
@Slf4j
public class RateControllersAspectHandler {
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final Cache<String, Integer> cache;
    private final MessageSource messages;

    public RateControllersAspectHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, MessageSource messages, Cache<String, Integer> cache) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.messages = messages;
        this.cache = cache;
    }

    protected RateController getAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method methodAnnotated = methodSignature.getMethod();
        return methodAnnotated.getAnnotation(RateController.class);
    }

    @Around("@annotation(com.payconiq.stock.aop.RateController)") //define the logic to execute
    public Object rateHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        RateController annotation = getAnnotation(joinPoint);
        String parameterName = annotation.parameterName();
        String parameterValue = httpServletRequest.getHeader(parameterName);
        String lang = httpServletRequest.getParameter("lang");
        if(lang==null) lang="en";
        String cacheKey = parameterName + ":" + parameterValue;
        Integer cacheValue = cache.getIfPresent(cacheKey);
        if (cacheValue == null)
            cacheValue = annotation.rateLimit();
        else {
            cacheValue--;
        }
        //Set helper Header for client information
        httpServletResponse.setHeader(parameterName + "-X-RateLimit-Remaining", cacheValue + "");
        httpServletResponse.setHeader(parameterName + "-X-RateLimit-Limit", annotation.rateLimit() + "");
        log.debug("#RateControllers->read cache key: " + cacheKey + " ;value: " + cacheValue + " ;" + annotation.httpStatusResponse().value());
        if (cacheValue < 1) {
            throw new BadRequestAlertException(String.format(messages.getMessage("request.id.duplicated", null, new Locale(lang)) + ": " + parameterValue), "customer", "request.id.duplicated");
        }
        Object result = joinPoint.proceed();
        // if response be equal expected response then condition applied
        // For example: if use this annotation for opening account, it means if account opened (20 returned) then applied limitation for next request
        // maybe user send another value incorrect and get 400 status.
        if (result instanceof ResponseEntity) {
            ResponseEntity responseEntity = (ResponseEntity) result;
            if (responseEntity.getStatusCode().value() == annotation.httpStatusResponse().value()) {
                cache.put(cacheKey, cacheValue);
            }
            log.debug("#RateControllers->write cache key: " + cacheKey + " ;value: " + cacheValue);
        }

        return result;
    }

}
