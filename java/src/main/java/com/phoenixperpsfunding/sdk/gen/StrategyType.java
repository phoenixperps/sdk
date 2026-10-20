// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** exits: a position's take-profit and stop-loss legs. bracket: an order with exits once it fills. oco: two orders, the first fill cancels the other. twap, vwap: an order in slices over time. scaled: a ladder of limits. chase: a limit that follows the price. Open: a value from a newer API version is kept as is. */
public final class StrategyType {
    /** exits */
    public static final StrategyType EXITS = new StrategyType("exits");

    /** bracket */
    public static final StrategyType BRACKET = new StrategyType("bracket");

    /** oco */
    public static final StrategyType OCO = new StrategyType("oco");

    /** twap */
    public static final StrategyType TWAP = new StrategyType("twap");

    /** vwap */
    public static final StrategyType VWAP = new StrategyType("vwap");

    /** scaled */
    public static final StrategyType SCALED = new StrategyType("scaled");

    /** chase */
    public static final StrategyType CHASE = new StrategyType("chase");

    private final String value;

    private StrategyType(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static StrategyType of(String value) {
        switch (value) {
            case "exits":
                return EXITS;
            case "bracket":
                return BRACKET;
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
                return new StrategyType(value);
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
        return o instanceof StrategyType && ((StrategyType) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
