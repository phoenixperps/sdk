// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** ExitsBatchOperationClear Open: a value from a newer API version is kept as is. */
public final class ExitsBatchOperationClear {
    /** trail */
    public static final ExitsBatchOperationClear TRAIL = new ExitsBatchOperationClear("trail");

    /** break_even */
    public static final ExitsBatchOperationClear BREAK_EVEN = new ExitsBatchOperationClear("break_even");

    private final String value;

    private ExitsBatchOperationClear(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static ExitsBatchOperationClear of(String value) {
        switch (value) {
            case "trail":
                return TRAIL;
            case "break_even":
                return BREAK_EVEN;
            default:
                return new ExitsBatchOperationClear(value);
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
        return o instanceof ExitsBatchOperationClear && ((ExitsBatchOperationClear) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
