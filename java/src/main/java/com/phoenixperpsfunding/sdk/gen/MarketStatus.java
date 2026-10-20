// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Whether the market trades now. Open: a value from a newer API version is kept as is. */
public final class MarketStatus {
    /** open */
    public static final MarketStatus OPEN = new MarketStatus("open");

    /** halted */
    public static final MarketStatus HALTED = new MarketStatus("halted");

    /** closed */
    public static final MarketStatus CLOSED = new MarketStatus("closed");

    private final String value;

    private MarketStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static MarketStatus of(String value) {
        switch (value) {
            case "open":
                return OPEN;
            case "halted":
                return HALTED;
            case "closed":
                return CLOSED;
            default:
                return new MarketStatus(value);
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
        return o instanceof MarketStatus && ((MarketStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
