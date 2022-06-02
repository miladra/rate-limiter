package com.payconiq.stock.utility.serializer;


import lombok.EqualsAndHashCode;

/**
 * @author Milad Ranjbari
 * @version 1.0.0
 * @since 11/11/2019
 * extracted from Paypal
 */

@EqualsAndHashCode
public class GSONModel {

    /**
     * Returns a JSON string corresponding to object state
     *
     * @return JSON representation
     */
    public String toJSON() {
        return JSONFormatter.toJSON(this);
    }

    @Override
    public String toString() {
        return toJSON();
    }

}
