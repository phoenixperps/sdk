// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** market: now, at the best price within your worst price. limit: at your price or better. stop_market: a market order once the last price reaches the stop. stop_limit: a limit once the last price reaches the stop. Open: a value from a newer API version is kept as is. */
public final class CreateOrderType {
    /** market */
    public static final CreateOrderType MARKET = new CreateOrderType("market");

    /** limit */
    public static final CreateOrderType LIMIT = new CreateOrderType("limit");

    /** stop_market */
    public static final CreateOrderType STOP_MARKET = new CreateOrderType("stop_market");

    /** stop_limit */
    public static final CreateOrderType STOP_LIMIT = new CreateOrderType("stop_limit");

    private final String value;

    private CreateOrderType(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static CreateOrderType of(String value) {
        switch (value) {
            case "market":
                return MARKET;
            case "limit":
                return LIMIT;
            case "stop_market":
                return STOP_MARKET;
            case "stop_limit":
                return STOP_LIMIT;
            default:
                return new CreateOrderType(value);
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
        return o instanceof CreateOrderType && ((CreateOrderType) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
