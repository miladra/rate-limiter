package com.payconiq.stock.utility.serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Milad Ranjbari
 * @version 1.0.0
 * @since 11/11/2019
 * extracted from paypal
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GsonExcludeField {
}
