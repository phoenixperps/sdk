// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** unconfirmed: the engine has not confirmed its last state yet. Open: a value from a newer API version is kept as is. */
public final class OrderStatus {
    /** pending */
    public static final OrderStatus PENDING = new OrderStatus("pending");

    /** working */
    public static final OrderStatus WORKING = new OrderStatus("working");

    /** filled */
    public static final OrderStatus FILLED = new OrderStatus("filled");

    /** canceled */
    public static final OrderStatus CANCELED = new OrderStatus("canceled");

    /** rejected */
    public static final OrderStatus REJECTED = new OrderStatus("rejected");

    /** expired */
    public static final OrderStatus EXPIRED = new OrderStatus("expired");

    /** unconfirmed */
    public static final OrderStatus UNCONFIRMED = new OrderStatus("unconfirmed");

    private final String value;

    private OrderStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static OrderStatus of(String value) {
        switch (value) {
            case "pending":
                return PENDING;
            case "working":
                return WORKING;
            case "filled":
                return FILLED;
            case "canceled":
                return CANCELED;
            case "rejected":
                return REJECTED;
            case "expired":
                return EXPIRED;
            case "unconfirmed":
                return UNCONFIRMED;
            default:
                return new OrderStatus(value);
        }
    }

    /** The value as the API writes it. */
    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof OrderStatus && ((OrderStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
