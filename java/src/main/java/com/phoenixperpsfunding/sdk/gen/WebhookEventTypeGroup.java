// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** The group that receives it; null: every endpoint (the test). Open: a value from a newer API version is kept as is. */
public final class WebhookEventTypeGroup {
    /** orders */
    public static final WebhookEventTypeGroup ORDERS = new WebhookEventTypeGroup("orders");

    /** positions */
    public static final WebhookEventTypeGroup POSITIONS = new WebhookEventTypeGroup("positions");

    /** account */
    public static final WebhookEventTypeGroup ACCOUNT = new WebhookEventTypeGroup("account");

    /** risk */
    public static final WebhookEventTypeGroup RISK = new WebhookEventTypeGroup("risk");

    private final String value;

    private WebhookEventTypeGroup(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static WebhookEventTypeGroup of(String value) {
        switch (value) {
            case "orders":
                return ORDERS;
            case "positions":
                return POSITIONS;
            case "account":
                return ACCOUNT;
            case "risk":
                return RISK;
            default:
                return new WebhookEventTypeGroup(value);
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
        return o instanceof WebhookEventTypeGroup && ((WebhookEventTypeGroup) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
