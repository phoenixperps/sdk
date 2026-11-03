// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** above: it fires once the price reaches the level from below; below: from above. Set from the price when the level was set. Open: a value from a newer API version is kept as is. */
public final class PriceAlertCondition {
    /** above */
    public static final PriceAlertCondition ABOVE = new PriceAlertCondition("above");

    /** below */
    public static final PriceAlertCondition BELOW = new PriceAlertCondition("below");

    private final String value;

    private PriceAlertCondition(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static PriceAlertCondition of(String value) {
        switch (value) {
            case "above":
                return ABOVE;
            case "below":
                return BELOW;
            default:
                return new PriceAlertCondition(value);
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
        return o instanceof PriceAlertCondition && ((PriceAlertCondition) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
