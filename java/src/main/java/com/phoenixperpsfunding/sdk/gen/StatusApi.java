// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Whether this environment takes requests. Open: a value from a newer API version is kept as is. */
public final class StatusApi {
    /** operational */
    public static final StatusApi OPERATIONAL = new StatusApi("operational");

    /** disabled */
    public static final StatusApi DISABLED = new StatusApi("disabled");

    private final String value;

    private StatusApi(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static StatusApi of(String value) {
        switch (value) {
            case "operational":
                return OPERATIONAL;
            case "disabled":
                return DISABLED;
            default:
                return new StatusApi(value);
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
        return o instanceof StatusApi && ((StatusApi) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
