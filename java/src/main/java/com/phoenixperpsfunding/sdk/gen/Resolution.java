// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** One point per closed trade, or per trading day. Open: a value from a newer API version is kept as is. */
public final class Resolution {
    /** trade */
    public static final Resolution TRADE = new Resolution("trade");

    /** day */
    public static final Resolution DAY = new Resolution("day");

    private final String value;

    private Resolution(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static Resolution of(String value) {
        switch (value) {
            case "trade":
                return TRADE;
            case "day":
                return DAY;
            default:
                return new Resolution(value);
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
        return o instanceof Resolution && ((Resolution) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
