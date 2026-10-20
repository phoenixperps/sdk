// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** The environment of the host you called. Open: a value from a newer API version is kept as is. */
public final class PlatformEnvironment {
    /** live */
    public static final PlatformEnvironment LIVE = new PlatformEnvironment("live");

    /** sandbox */
    public static final PlatformEnvironment SANDBOX = new PlatformEnvironment("sandbox");

    private final String value;

    private PlatformEnvironment(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static PlatformEnvironment of(String value) {
        switch (value) {
            case "live":
                return LIVE;
            case "sandbox":
                return SANDBOX;
            default:
                return new PlatformEnvironment(value);
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
        return o instanceof PlatformEnvironment && ((PlatformEnvironment) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
