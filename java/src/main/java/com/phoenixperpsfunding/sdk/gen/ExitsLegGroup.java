// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** take_profit: a limit on the profit side. stop_loss: a stop on the loss side. Open: a value from a newer API version is kept as is. */
public final class ExitsLegGroup {
    /** take_profit */
    public static final ExitsLegGroup TAKE_PROFIT = new ExitsLegGroup("take_profit");

    /** stop_loss */
    public static final ExitsLegGroup STOP_LOSS = new ExitsLegGroup("stop_loss");

    private final String value;

    private ExitsLegGroup(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static ExitsLegGroup of(String value) {
        switch (value) {
            case "take_profit":
                return TAKE_PROFIT;
            case "stop_loss":
                return STOP_LOSS;
            default:
                return new ExitsLegGroup(value);
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
        return o instanceof ExitsLegGroup && ((ExitsLegGroup) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
