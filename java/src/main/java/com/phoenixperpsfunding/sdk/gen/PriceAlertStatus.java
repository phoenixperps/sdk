// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** armed: it waits for its level. triggered: it fired (rearm arms it again). disabled: its account or market went away before it fired. Open: a value from a newer API version is kept as is. */
public final class PriceAlertStatus {
    /** armed */
    public static final PriceAlertStatus ARMED = new PriceAlertStatus("armed");

    /** triggered */
    public static final PriceAlertStatus TRIGGERED = new PriceAlertStatus("triggered");

    /** disabled */
    public static final PriceAlertStatus DISABLED = new PriceAlertStatus("disabled");

    private final String value;

    private PriceAlertStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static PriceAlertStatus of(String value) {
        switch (value) {
            case "armed":
                return ARMED;
            case "triggered":
                return TRIGGERED;
            case "disabled":
                return DISABLED;
            default:
                return new PriceAlertStatus(value);
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
        return o instanceof PriceAlertStatus && ((PriceAlertStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
