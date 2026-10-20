// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Where it stands. Open: a value from a newer API version is kept as is. */
public final class LimitStandingStatus {
    /** ok */
    public static final LimitStandingStatus OK = new LimitStandingStatus("ok");

    /** warning */
    public static final LimitStandingStatus WARNING = new LimitStandingStatus("warning");

    /** critical */
    public static final LimitStandingStatus CRITICAL = new LimitStandingStatus("critical");

    /** breached */
    public static final LimitStandingStatus BREACHED = new LimitStandingStatus("breached");

    private final String value;

    private LimitStandingStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static LimitStandingStatus of(String value) {
        switch (value) {
            case "ok":
                return OK;
            case "warning":
                return WARNING;
            case "critical":
                return CRITICAL;
            case "breached":
                return BREACHED;
            default:
                return new LimitStandingStatus(value);
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
        return o instanceof LimitStandingStatus && ((LimitStandingStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
