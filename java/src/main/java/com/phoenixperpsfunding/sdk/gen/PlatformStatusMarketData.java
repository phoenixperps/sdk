// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Whether the market-data stream is live, with every price feed. Open: a value from a newer API version is kept as is. */
public final class PlatformStatusMarketData {
    /** up */
    public static final PlatformStatusMarketData UP = new PlatformStatusMarketData("up");

    /** down */
    public static final PlatformStatusMarketData DOWN = new PlatformStatusMarketData("down");

    private final String value;

    private PlatformStatusMarketData(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static PlatformStatusMarketData of(String value) {
        switch (value) {
            case "up":
                return UP;
            case "down":
                return DOWN;
            default:
                return new PlatformStatusMarketData(value);
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
        return o instanceof PlatformStatusMarketData && ((PlatformStatusMarketData) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
