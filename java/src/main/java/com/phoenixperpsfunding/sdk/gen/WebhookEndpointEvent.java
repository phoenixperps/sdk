// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** A group of events. Open: a value from a newer API version is kept as is. */
public final class WebhookEndpointEvent {
    /** orders */
    public static final WebhookEndpointEvent ORDERS = new WebhookEndpointEvent("orders");

    /** positions */
    public static final WebhookEndpointEvent POSITIONS = new WebhookEndpointEvent("positions");

    /** account */
    public static final WebhookEndpointEvent ACCOUNT = new WebhookEndpointEvent("account");

    /** risk */
    public static final WebhookEndpointEvent RISK = new WebhookEndpointEvent("risk");

    private final String value;

    private WebhookEndpointEvent(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static WebhookEndpointEvent of(String value) {
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
                return new WebhookEndpointEvent(value);
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
        return o instanceof WebhookEndpointEvent && ((WebhookEndpointEvent) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
