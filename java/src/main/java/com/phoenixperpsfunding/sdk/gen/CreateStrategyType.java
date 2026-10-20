// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** trailing_stop: a stop-loss that follows the price. break_even: the stop-losses move to the entry once the price has gone your way far enough. oco: two orders, the first fill cancels the other. twap, vwap: an order in market slices over time (vwap sized by yesterday's volume over the same hours). scaled: a ladder of limits. chase: a limit that follows the bid or ask. Open: a value from a newer API version is kept as is. */
public final class CreateStrategyType {
    /** trailing_stop */
    public static final CreateStrategyType TRAILING_STOP = new CreateStrategyType("trailing_stop");

    /** break_even */
    public static final CreateStrategyType BREAK_EVEN = new CreateStrategyType("break_even");

    /** oco */
    public static final CreateStrategyType OCO = new CreateStrategyType("oco");

    /** twap */
    public static final CreateStrategyType TWAP = new CreateStrategyType("twap");

    /** vwap */
    public static final CreateStrategyType VWAP = new CreateStrategyType("vwap");

    /** scaled */
    public static final CreateStrategyType SCALED = new CreateStrategyType("scaled");

    /** chase */
    public static final CreateStrategyType CHASE = new CreateStrategyType("chase");

    private final String value;

    private CreateStrategyType(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static CreateStrategyType of(String value) {
        switch (value) {
            case "trailing_stop":
                return TRAILING_STOP;
            case "break_even":
                return BREAK_EVEN;
            case "oco":
                return OCO;
            case "twap":
                return TWAP;
            case "vwap":
                return VWAP;
            case "scaled":
                return SCALED;
            case "chase":
                return CHASE;
            default:
                return new CreateStrategyType(value);
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
        return o instanceof CreateStrategyType && ((CreateStrategyType) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
