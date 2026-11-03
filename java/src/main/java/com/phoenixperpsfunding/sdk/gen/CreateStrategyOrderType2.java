// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Two orders that rest. Open: a value from a newer API version is kept as is. */
public final class CreateStrategyOrderType2 {
    /** limit */
    public static final CreateStrategyOrderType2 LIMIT = new CreateStrategyOrderType2("limit");

    /** stop_market */
    public static final CreateStrategyOrderType2 STOP_MARKET = new CreateStrategyOrderType2("stop_market");

    /** stop_limit */
    public static final CreateStrategyOrderType2 STOP_LIMIT = new CreateStrategyOrderType2("stop_limit");

    private final String value;

    private CreateStrategyOrderType2(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static CreateStrategyOrderType2 of(String value) {
        switch (value) {
            case "limit":
                return LIMIT;
            case "stop_market":
                return STOP_MARKET;
            case "stop_limit":
                return STOP_LIMIT;
            default:
                return new CreateStrategyOrderType2(value);
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
        return o instanceof CreateStrategyOrderType2 && ((CreateStrategyOrderType2) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
