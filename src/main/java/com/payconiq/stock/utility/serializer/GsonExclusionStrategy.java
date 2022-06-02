package com.payconiq.stock.utility.serializer;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * @author Milad Ranjbari
 * @version 1.0.0
 * @since 11/11/2019
 * extracted from paypal
 */
public class GsonExclusionStrategy implements ExclusionStrategy {

    public boolean shouldSkipField(FieldAttributes f) {
        return (f.getAnnotation(GsonExcludeField.class) != null) ;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

}