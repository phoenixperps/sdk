// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** disabled: we stopped after failures in a row; turn it on again from your dashboard. Open: a value from a newer API version is kept as is. */
public final class WebhookEndpointStatus {
    /** active */
    public static final WebhookEndpointStatus ACTIVE = new WebhookEndpointStatus("active");

    /** paused */
    public static final WebhookEndpointStatus PAUSED = new WebhookEndpointStatus("paused");

    /** disabled */
    public static final WebhookEndpointStatus DISABLED = new WebhookEndpointStatus("disabled");

    private final String value;

    private WebhookEndpointStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static WebhookEndpointStatus of(String value) {
        switch (value) {
            case "active":
                return ACTIVE;
            case "paused":
                return PAUSED;
            case "disabled":
                return DISABLED;
            default:
                return new WebhookEndpointStatus(value);
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
        return o instanceof WebhookEndpointStatus && ((WebhookEndpointStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
