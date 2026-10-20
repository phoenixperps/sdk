// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** How serious. Open: a value from a newer API version is kept as is. */
public final class WarningLevel {
    /** info */
    public static final WarningLevel INFO = new WarningLevel("info");

    /** warning */
    public static final WarningLevel WARNING = new WarningLevel("warning");

    /** critical */
    public static final WarningLevel CRITICAL = new WarningLevel("critical");

    private final String value;

    private WarningLevel(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static WarningLevel of(String value) {
        switch (value) {
            case "info":
                return INFO;
            case "warning":
                return WARNING;
            case "critical":
                return CRITICAL;
            default:
                return new WarningLevel(value);
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
        return o instanceof WarningLevel && ((WarningLevel) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
