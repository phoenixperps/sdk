// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Account figures over the last minute: degraded when more than 5% of them were older than usual (stale) or could not be read (state_unavailable). Open: a value from a newer API version is kept as is. */
public final class StatusAccountDataState {
    /** operational */
    public static final StatusAccountDataState OPERATIONAL = new StatusAccountDataState("operational");

    /** degraded */
    public static final StatusAccountDataState DEGRADED = new StatusAccountDataState("degraded");

    private final String value;

    private StatusAccountDataState(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static StatusAccountDataState of(String value) {
        switch (value) {
            case "operational":
                return OPERATIONAL;
            case "degraded":
                return DEGRADED;
            default:
                return new StatusAccountDataState(value);
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
        return o instanceof StatusAccountDataState && ((StatusAccountDataState) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
