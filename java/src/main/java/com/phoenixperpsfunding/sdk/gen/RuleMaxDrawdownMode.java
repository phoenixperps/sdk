// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** How the reference moves: end_of_day and end_of_trade follow the highest balance, static stays at the start, intraday_equity follows the highest equity. Open: a value from a newer API version is kept as is. */
public final class RuleMaxDrawdownMode {
    /** end_of_day */
    public static final RuleMaxDrawdownMode END_OF_DAY = new RuleMaxDrawdownMode("end_of_day");

    /** end_of_trade */
    public static final RuleMaxDrawdownMode END_OF_TRADE = new RuleMaxDrawdownMode("end_of_trade");

    /** static */
    public static final RuleMaxDrawdownMode STATIC = new RuleMaxDrawdownMode("static");

    /** intraday_equity */
    public static final RuleMaxDrawdownMode INTRADAY_EQUITY = new RuleMaxDrawdownMode("intraday_equity");

    private final String value;

    private RuleMaxDrawdownMode(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static RuleMaxDrawdownMode of(String value) {
        switch (value) {
            case "end_of_day":
                return END_OF_DAY;
            case "end_of_trade":
                return END_OF_TRADE;
            case "static":
                return STATIC;
            case "intraday_equity":
                return INTRADAY_EQUITY;
            default:
                return new RuleMaxDrawdownMode(value);
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
        return o instanceof RuleMaxDrawdownMode && ((RuleMaxDrawdownMode) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
