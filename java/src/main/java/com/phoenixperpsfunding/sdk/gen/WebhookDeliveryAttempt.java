// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** WebhookDeliveryAttempt */
public final class WebhookDeliveryAttempt {
    @JsonProperty("attempt")
    private Long attempt;

    @JsonProperty("at")
    private Long at;

    @JsonProperty("http_status")
    private Long httpStatus;

    @JsonProperty("error")
    private String error;

    @JsonProperty("duration_ms")
    private Long durationMs;

    @JsonProperty("response")
    private String response;

    /** Which try. */
    public Long attempt() {
        return attempt;
    }

    /** Sets attempt. */
    public WebhookDeliveryAttempt attempt(Long value) {
        this.attempt = value;
        return this;
    }

    /** When. */
    public Long at() {
        return at;
    }

    /** Sets at. */
    public WebhookDeliveryAttempt at(Long value) {
        this.at = value;
        return this;
    }

    /** What your endpoint answered; null when it could not be reached. */
    public Long httpStatus() {
        return httpStatus;
    }

    /** Sets http_status. */
    public WebhookDeliveryAttempt httpStatus(Long value) {
        this.httpStatus = value;
        return this;
    }

    /** Why it failed, when it did (timeout, redirect, private_address, http_500, ...). */
    public String error() {
        return error;
    }

    /** Sets error. */
    public WebhookDeliveryAttempt error(String value) {
        this.error = value;
        return this;
    }

    /** How long it took. */
    public Long durationMs() {
        return durationMs;
    }

    /** Sets duration_ms. */
    public WebhookDeliveryAttempt durationMs(Long value) {
        this.durationMs = value;
        return this;
    }

    /** The start of the answer (up to 512 bytes). */
    public String response() {
        return response;
    }

    /** Sets response. */
    public WebhookDeliveryAttempt response(String value) {
        this.response = value;
        return this;
    }

    @JsonIgnore
    private final Map<String, Object> extra = new LinkedHashMap<>();

    /** Fields this SDK version does not know yet (a newer API's), kept as they came. */
    @JsonAnyGetter
    public Map<String, Object> extra() {
        return extra;
    }

    @JsonAnySetter
    void putExtra(String key, Object value) {
        extra.put(key, value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof WebhookDeliveryAttempt)) {
            return false;
        }
        WebhookDeliveryAttempt o = (WebhookDeliveryAttempt) other;
        return Objects.equals(attempt, o.attempt)
            && Objects.equals(at, o.at)
            && Objects.equals(httpStatus, o.httpStatus)
            && Objects.equals(error, o.error)
            && Objects.equals(durationMs, o.durationMs)
            && Objects.equals(response, o.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, at, httpStatus, error, durationMs, response);
    }

    @Override
    public String toString() {
        return "WebhookDeliveryAttempt{" + "attempt=" + attempt + ", at=" + at + ", http_status=" + httpStatus + ", error=" + error + ", duration_ms=" + durationMs + ", response=" + response + "}";
    }
}
