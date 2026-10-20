// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** pending: being opened; active: trading. Open: a value from a newer API version is kept as is. */
public final class AccountStatus {
    /** pending */
    public static final AccountStatus PENDING = new AccountStatus("pending");

    /** active */
    public static final AccountStatus ACTIVE = new AccountStatus("active");

    /** passed */
    public static final AccountStatus PASSED = new AccountStatus("passed");

    /** failed */
    public static final AccountStatus FAILED = new AccountStatus("failed");

    /** closed */
    public static final AccountStatus CLOSED = new AccountStatus("closed");

    /** suspended */
    public static final AccountStatus SUSPENDED = new AccountStatus("suspended");

    private final String value;

    private AccountStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static AccountStatus of(String value) {
        switch (value) {
            case "pending":
                return PENDING;
            case "active":
                return ACTIVE;
            case "passed":
                return PASSED;
            case "failed":
                return FAILED;
            case "closed":
                return CLOSED;
            case "suspended":
                return SUSPENDED;
            default:
                return new AccountStatus(value);
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
        return o instanceof AccountStatus && ((AccountStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
