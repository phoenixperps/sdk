// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** The order's status; waiting: a stop-loss not placed yet (it waits for its activation price or its break-even). Open: a value from a newer API version is kept as is. */
public final class ExitsLegStatus {
    /** pending */
    public static final ExitsLegStatus PENDING = new ExitsLegStatus("pending");

    /** working */
    public static final ExitsLegStatus WORKING = new ExitsLegStatus("working");

    /** filled */
    public static final ExitsLegStatus FILLED = new ExitsLegStatus("filled");

    /** canceled */
    public static final ExitsLegStatus CANCELED = new ExitsLegStatus("canceled");

    /** rejected */
    public static final ExitsLegStatus REJECTED = new ExitsLegStatus("rejected");

    /** expired */
    public static final ExitsLegStatus EXPIRED = new ExitsLegStatus("expired");

    /** unconfirmed */
    public static final ExitsLegStatus UNCONFIRMED = new ExitsLegStatus("unconfirmed");

    /** waiting */
    public static final ExitsLegStatus WAITING = new ExitsLegStatus("waiting");

    private final String value;

    private ExitsLegStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static ExitsLegStatus of(String value) {
        switch (value) {
            case "pending":
                return PENDING;
            case "working":
                return WORKING;
            case "filled":
                return FILLED;
            case "canceled":
                return CANCELED;
            case "rejected":
                return REJECTED;
            case "expired":
                return EXPIRED;
            case "unconfirmed":
                return UNCONFIRMED;
            case "waiting":
                return WAITING;
            default:
                return new ExitsLegStatus(value);
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
        return o instanceof ExitsLegStatus && ((ExitsLegStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
