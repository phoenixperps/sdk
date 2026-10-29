// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** When it trades: around the clock, its underlying market's sessions (Standard Market Hours, closed on weekends), or FX hours. Open: a value from a newer API version is kept as is. */
public final class TradingHours {
    /** 24/7 */
    public static final TradingHours V_24_7 = new TradingHours("24/7");

    /** standard */
    public static final TradingHours STANDARD = new TradingHours("standard");

    /** fx */
    public static final TradingHours FX = new TradingHours("fx");

    private final String value;

    private TradingHours(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static TradingHours of(String value) {
        switch (value) {
            case "24/7":
                return V_24_7;
            case "standard":
                return STANDARD;
            case "fx":
                return FX;
            default:
                return new TradingHours(value);
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
        return o instanceof TradingHours && ((TradingHours) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
