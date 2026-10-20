// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** What to do. Open: a value from a newer API version is kept as is. */
public final class ExitsBatchOperationOp {
    /** place */
    public static final ExitsBatchOperationOp PLACE = new ExitsBatchOperationOp("place");

    /** modify */
    public static final ExitsBatchOperationOp MODIFY = new ExitsBatchOperationOp("modify");

    /** cancel */
    public static final ExitsBatchOperationOp CANCEL = new ExitsBatchOperationOp("cancel");

    private final String value;

    private ExitsBatchOperationOp(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static ExitsBatchOperationOp of(String value) {
        switch (value) {
            case "place":
                return PLACE;
            case "modify":
                return MODIFY;
            case "cancel":
                return CANCEL;
            default:
                return new ExitsBatchOperationOp(value);
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
        return o instanceof ExitsBatchOperationOp && ((ExitsBatchOperationOp) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
