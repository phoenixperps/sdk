// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** The stop's type (default stop_market). Open: a value from a newer API version is kept as is. */
public final class OrderExitsStopLossType {
    /** stop_market */
    public static final OrderExitsStopLossType STOP_MARKET = new OrderExitsStopLossType("stop_market");

    /** stop_limit */
    public static final OrderExitsStopLossType STOP_LIMIT = new OrderExitsStopLossType("stop_limit");

    private final String value;

    private OrderExitsStopLossType(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static OrderExitsStopLossType of(String value) {
        switch (value) {
            case "stop_market":
                return STOP_MARKET;
            case "stop_limit":
                return STOP_LIMIT;
            default:
                return new OrderExitsStopLossType(value);
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
        return o instanceof OrderExitsStopLossType && ((OrderExitsStopLossType) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
