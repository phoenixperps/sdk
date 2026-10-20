// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** chase: at max_minutes, cancel it, or send what is left at the market. Open: a value from a newer API version is kept as is. */
public final class CreateStrategyOnExpiry {
    /** cancel */
    public static final CreateStrategyOnExpiry CANCEL = new CreateStrategyOnExpiry("cancel");

    /** market */
    public static final CreateStrategyOnExpiry MARKET = new CreateStrategyOnExpiry("market");

    private final String value;

    private CreateStrategyOnExpiry(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static CreateStrategyOnExpiry of(String value) {
        switch (value) {
            case "cancel":
                return CANCEL;
            case "market":
                return MARKET;
            default:
                return new CreateStrategyOnExpiry(value);
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
        return o instanceof CreateStrategyOnExpiry && ((CreateStrategyOnExpiry) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
