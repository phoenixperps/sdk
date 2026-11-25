// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Closes, flattens, cancel-alls and margin changes over the last minute: degraded when the engine was too busy to take one (engine_busy). Open: a value from a newer API version is kept as is. */
public final class StatusEngineWrites {
    /** operational */
    public static final StatusEngineWrites OPERATIONAL = new StatusEngineWrites("operational");

    /** degraded */
    public static final StatusEngineWrites DEGRADED = new StatusEngineWrites("degraded");

    private final String value;

    private StatusEngineWrites(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static StatusEngineWrites of(String value) {
        switch (value) {
            case "operational":
                return OPERATIONAL;
            case "degraded":
                return DEGRADED;
            default:
                return new StatusEngineWrites(value);
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
        return o instanceof StatusEngineWrites && ((StatusEngineWrites) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
