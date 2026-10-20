// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** What happens when it is reached. Open: a value from a newer API version is kept as is. */
public final class OnBreach {
    /** fail_account */
    public static final OnBreach FAIL_ACCOUNT = new OnBreach("fail_account");

    /** flatten */
    public static final OnBreach FLATTEN = new OnBreach("flatten");

    /** flatten_and_pause */
    public static final OnBreach FLATTEN_AND_PAUSE = new OnBreach("flatten_and_pause");

    private final String value;

    private OnBreach(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static OnBreach of(String value) {
        switch (value) {
            case "fail_account":
                return FAIL_ACCOUNT;
            case "flatten":
                return FLATTEN;
            case "flatten_and_pause":
                return FLATTEN_AND_PAUSE;
            default:
                return new OnBreach(value);
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
        return o instanceof OnBreach && ((OnBreach) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
