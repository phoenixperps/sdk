// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** signal: an opposite alert reverses, a repeat is skipped. add: a repeat adds. target: the strategy's position size is followed. Open: a value from a newer API version is kept as is. */
public final class TradingViewHookPositionMode {
    /** signal */
    public static final TradingViewHookPositionMode SIGNAL = new TradingViewHookPositionMode("signal");

    /** add */
    public static final TradingViewHookPositionMode ADD = new TradingViewHookPositionMode("add");

    /** target */
    public static final TradingViewHookPositionMode TARGET = new TradingViewHookPositionMode("target");

    private final String value;

    private TradingViewHookPositionMode(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static TradingViewHookPositionMode of(String value) {
        switch (value) {
            case "signal":
                return SIGNAL;
            case "add":
                return ADD;
            case "target":
                return TARGET;
            default:
                return new TradingViewHookPositionMode(value);
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
        return o instanceof TradingViewHookPositionMode && ((TradingViewHookPositionMode) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
