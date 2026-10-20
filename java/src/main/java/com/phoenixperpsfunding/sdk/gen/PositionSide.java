// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Long or short. Open: a value from a newer API version is kept as is. */
public final class PositionSide {
    /** long */
    public static final PositionSide LONG = new PositionSide("long");

    /** short */
    public static final PositionSide SHORT = new PositionSide("short");

    private final String value;

    private PositionSide(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static PositionSide of(String value) {
        switch (value) {
            case "long":
                return LONG;
            case "short":
                return SHORT;
            default:
                return new PositionSide(value);
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
        return o instanceof PositionSide && ((PositionSide) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
