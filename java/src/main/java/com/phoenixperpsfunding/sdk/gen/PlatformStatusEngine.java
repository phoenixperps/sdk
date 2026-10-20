// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Whether the trading engine is reachable. Open: a value from a newer API version is kept as is. */
public final class PlatformStatusEngine {
    /** up */
    public static final PlatformStatusEngine UP = new PlatformStatusEngine("up");

    /** down */
    public static final PlatformStatusEngine DOWN = new PlatformStatusEngine("down");

    private final String value;

    private PlatformStatusEngine(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static PlatformStatusEngine of(String value) {
        switch (value) {
            case "up":
                return UP;
            case "down":
                return DOWN;
            default:
                return new PlatformStatusEngine(value);
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
        return o instanceof PlatformStatusEngine && ((PlatformStatusEngine) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
