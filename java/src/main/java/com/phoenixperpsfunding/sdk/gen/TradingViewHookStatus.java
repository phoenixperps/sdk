// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** paused: alerts are refused (link_paused). Open: a value from a newer API version is kept as is. */
public final class TradingViewHookStatus {
    /** active */
    public static final TradingViewHookStatus ACTIVE = new TradingViewHookStatus("active");

    /** paused */
    public static final TradingViewHookStatus PAUSED = new TradingViewHookStatus("paused");

    private final String value;

    private TradingViewHookStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static TradingViewHookStatus of(String value) {
        switch (value) {
            case "active":
                return ACTIVE;
            case "paused":
                return PAUSED;
            default:
                return new TradingViewHookStatus(value);
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
        return o instanceof TradingViewHookStatus && ((TradingViewHookStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
