// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** conditional, with another trigger_market: send the order once it trades at or above trigger_price, or at or below it. On this market it is set by the side and not given. Open: a value from a newer API version is kept as is. */
public final class CreateStrategyTriggerDirection {
    /** above */
    public static final CreateStrategyTriggerDirection ABOVE = new CreateStrategyTriggerDirection("above");

    /** below */
    public static final CreateStrategyTriggerDirection BELOW = new CreateStrategyTriggerDirection("below");

    private final String value;

    private CreateStrategyTriggerDirection(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static CreateStrategyTriggerDirection of(String value) {
        switch (value) {
            case "above":
                return ABOVE;
            case "below":
                return BELOW;
            default:
                return new CreateStrategyTriggerDirection(value);
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
        return o instanceof CreateStrategyTriggerDirection && ((CreateStrategyTriggerDirection) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
