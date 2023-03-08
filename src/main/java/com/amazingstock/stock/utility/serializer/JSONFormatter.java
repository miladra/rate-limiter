package com.amazingstock.stock.utility.serializer;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSONFormatter converts objects to JSON representation and vice-versa. This
 * class depends on Google's GSON library to do the transformation. This class
 * is not thread-safe.
 * Extracted from Paypal
 */
public final class JSONFormatter {

    /**
     * FieldNamingPolicy used by the underlying Gson library. Alter this
     * property to set a fieldnamingpolicy other than
     * LOWER_CASE_WITH_UNDERSCORES used by PayPal REST APIs
     */
    private static final FieldNamingPolicy FIELD_NAMING_POLICY = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
    /**
     * Gson
     */
    public static Gson GSON = new GsonBuilder()
//            .serializeNulls()
            .setPrettyPrinting()
            .setExclusionStrategies(new GsonExclusionStrategy())
            .setFieldNamingPolicy(FIELD_NAMING_POLICY).create();

    /*
     * JSONFormatter is coupled to the stubs generated using the SDK generator.
     * Since PayPal REST APIs support only JSON, this class is bound to the
     * stubs for their json representation.
     */
    private JSONFormatter() {
    }

    /**
     * Converts a Raw Type to JSON String
     *
     * @param <T> Type to be converted
     * @param t   Object of the type
     * @return JSON representation
     */
    public static <T> String toJSON(T t) {
        if (t != null)
            return GSON.toJson(t);
        return "{}";
    }
}
