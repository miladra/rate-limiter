package com.payconiq.stock.aop;

import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Milad Ranjbari
 * @version 2022.6.1
 * @since 6/01/22
 * Handle rate control and idempotence when add this annotation on method
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
public @interface RateController {

    int rateLimit() default 2; // this's threshold.

    String parameterName() default "X-Request-Id";

    /**
     * if response status is equal httpStatusResponse, then the condition is applied;
     */
    HttpStatus httpStatusResponse() default HttpStatus.OK;
}
