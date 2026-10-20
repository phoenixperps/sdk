// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Isolated: each position holds its own margin. Cross: positions share the balance. Open: a value from a newer API version is kept as is. */
public final class MarginMode {
    /** isolated */
    public static final MarginMode ISOLATED = new MarginMode("isolated");

    /** cross */
    public static final MarginMode CROSS = new MarginMode("cross");

    private final String value;

    private MarginMode(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static MarginMode of(String value) {
        switch (value) {
            case "isolated":
                return ISOLATED;
            case "cross":
                return CROSS;
            default:
                return new MarginMode(value);
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
        return o instanceof MarginMode && ((MarginMode) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
