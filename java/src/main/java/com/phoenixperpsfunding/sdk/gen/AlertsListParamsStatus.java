// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** AlertsListParamsStatus Open: a value from a newer API version is kept as is. */
public final class AlertsListParamsStatus {
    /** all */
    public static final AlertsListParamsStatus ALL = new AlertsListParamsStatus("all");

    /** armed */
    public static final AlertsListParamsStatus ARMED = new AlertsListParamsStatus("armed");

    /** triggered */
    public static final AlertsListParamsStatus TRIGGERED = new AlertsListParamsStatus("triggered");

    /** disabled */
    public static final AlertsListParamsStatus DISABLED = new AlertsListParamsStatus("disabled");

    private final String value;

    private AlertsListParamsStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static AlertsListParamsStatus of(String value) {
        switch (value) {
            case "all":
                return ALL;
            case "armed":
                return ARMED;
            case "triggered":
                return TRIGGERED;
            case "disabled":
                return DISABLED;
            default:
                return new AlertsListParamsStatus(value);
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
        return o instanceof AlertsListParamsStatus && ((AlertsListParamsStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
