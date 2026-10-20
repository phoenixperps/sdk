// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** What the day's loss is measured from. Open: a value from a newer API version is kept as is. */
public final class RuleDailyLossBasis {
    /** peak_balance */
    public static final RuleDailyLossBasis PEAK_BALANCE = new RuleDailyLossBasis("peak_balance");

    /** start_balance */
    public static final RuleDailyLossBasis START_BALANCE = new RuleDailyLossBasis("start_balance");

    /** peak_equity */
    public static final RuleDailyLossBasis PEAK_EQUITY = new RuleDailyLossBasis("peak_equity");

    /** day_start_balance */
    public static final RuleDailyLossBasis DAY_START_BALANCE = new RuleDailyLossBasis("day_start_balance");

    /** day_start_equity */
    public static final RuleDailyLossBasis DAY_START_EQUITY = new RuleDailyLossBasis("day_start_equity");

    private final String value;

    private RuleDailyLossBasis(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static RuleDailyLossBasis of(String value) {
        switch (value) {
            case "peak_balance":
                return PEAK_BALANCE;
            case "start_balance":
                return START_BALANCE;
            case "peak_equity":
                return PEAK_EQUITY;
            case "day_start_balance":
                return DAY_START_BALANCE;
            case "day_start_equity":
                return DAY_START_EQUITY;
            default:
                return new RuleDailyLossBasis(value);
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
        return o instanceof RuleDailyLossBasis && ((RuleDailyLossBasis) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
