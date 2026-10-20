// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** How long it rests. Open: a value from a newer API version is kept as is. */
public final class TimeInForce {
    /** gtc */
    public static final TimeInForce GTC = new TimeInForce("gtc");

    /** gtd */
    public static final TimeInForce GTD = new TimeInForce("gtd");

    /** ioc */
    public static final TimeInForce IOC = new TimeInForce("ioc");

    /** fok */
    public static final TimeInForce FOK = new TimeInForce("fok");

    private final String value;

    private TimeInForce(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static TimeInForce of(String value) {
        switch (value) {
            case "gtc":
                return GTC;
            case "gtd":
                return GTD;
            case "ioc":
                return IOC;
            case "fok":
                return FOK;
            default:
                return new TimeInForce(value);
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
        return o instanceof TimeInForce && ((TimeInForce) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
