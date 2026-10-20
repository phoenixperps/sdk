// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** The order's type. Open: a value from a newer API version is kept as is. */
public final class OrderType {
    /** market */
    public static final OrderType MARKET = new OrderType("market");

    /** limit */
    public static final OrderType LIMIT = new OrderType("limit");

    /** stop_market */
    public static final OrderType STOP_MARKET = new OrderType("stop_market");

    /** stop_limit */
    public static final OrderType STOP_LIMIT = new OrderType("stop_limit");

    /** take_market */
    public static final OrderType TAKE_MARKET = new OrderType("take_market");

    /** take_limit */
    public static final OrderType TAKE_LIMIT = new OrderType("take_limit");

    private final String value;

    private OrderType(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static OrderType of(String value) {
        switch (value) {
            case "market":
                return MARKET;
            case "limit":
                return LIMIT;
            case "stop_market":
                return STOP_MARKET;
            case "stop_limit":
                return STOP_LIMIT;
            case "take_market":
                return TAKE_MARKET;
            case "take_limit":
                return TAKE_LIMIT;
            default:
                return new OrderType(value);
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
        return o instanceof OrderType && ((OrderType) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
