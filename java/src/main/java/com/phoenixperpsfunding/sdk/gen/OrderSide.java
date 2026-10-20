// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** The order's side; null for an order placed elsewhere before the API saw it. Open: a value from a newer API version is kept as is. */
public final class OrderSide {
    /** buy */
    public static final OrderSide BUY = new OrderSide("buy");

    /** sell */
    public static final OrderSide SELL = new OrderSide("sell");

    private final String value;

    private OrderSide(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static OrderSide of(String value) {
        switch (value) {
            case "buy":
                return BUY;
            case "sell":
                return SELL;
            default:
                return new OrderSide(value);
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
        return o instanceof OrderSide && ((OrderSide) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
