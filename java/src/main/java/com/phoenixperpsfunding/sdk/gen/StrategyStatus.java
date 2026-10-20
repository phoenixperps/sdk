// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** waiting: armed, nothing placed yet. working: running. completed: done as asked. canceled: you ended it. ended: it stopped for end_reason. Open: a value from a newer API version is kept as is. */
public final class StrategyStatus {
    /** waiting */
    public static final StrategyStatus WAITING = new StrategyStatus("waiting");

    /** working */
    public static final StrategyStatus WORKING = new StrategyStatus("working");

    /** completed */
    public static final StrategyStatus COMPLETED = new StrategyStatus("completed");

    /** canceled */
    public static final StrategyStatus CANCELED = new StrategyStatus("canceled");

    /** ended */
    public static final StrategyStatus ENDED = new StrategyStatus("ended");

    private final String value;

    private StrategyStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static StrategyStatus of(String value) {
        switch (value) {
            case "waiting":
                return WAITING;
            case "working":
                return WORKING;
            case "completed":
                return COMPLETED;
            case "canceled":
                return CANCELED;
            case "ended":
                return ENDED;
            default:
                return new StrategyStatus(value);
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
        return o instanceof StrategyStatus && ((StrategyStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
