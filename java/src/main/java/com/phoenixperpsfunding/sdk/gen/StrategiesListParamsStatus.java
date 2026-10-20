// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** StrategiesListParamsStatus Open: a value from a newer API version is kept as is. */
public final class StrategiesListParamsStatus {
    /** all */
    public static final StrategiesListParamsStatus ALL = new StrategiesListParamsStatus("all");

    /** active */
    public static final StrategiesListParamsStatus ACTIVE = new StrategiesListParamsStatus("active");

    /** ended */
    public static final StrategiesListParamsStatus ENDED = new StrategiesListParamsStatus("ended");

    private final String value;

    private StrategiesListParamsStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static StrategiesListParamsStatus of(String value) {
        switch (value) {
            case "all":
                return ALL;
            case "active":
                return ACTIVE;
            case "ended":
                return ENDED;
            default:
                return new StrategiesListParamsStatus(value);
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
        return o instanceof StrategiesListParamsStatus && ((StrategiesListParamsStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
