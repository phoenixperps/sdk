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

/** WebhookDelivery */
public final class WebhookDelivery {
    @JsonProperty("id")
    private String id;

    @JsonProperty("endpoint_id")
    private String endpointId;

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("test")
    private Boolean test;

    @JsonProperty("status")
    private WebhookDeliveryStatus status;

    @JsonProperty("attempts")
    private List<WebhookDeliveryAttempt> attempts;

    @JsonProperty("next_attempt_at")
    private Long nextAttemptAt;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("delivered_at")
    private Long deliveredAt;

    /** The delivery. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public WebhookDelivery id(String value) {
        this.id = value;
        return this;
    }

    /** Its endpoint. */
    public String endpointId() {
        return endpointId;
    }

    /** Sets endpoint_id. */
    public WebhookDelivery endpointId(String value) {
        this.endpointId = value;
        return this;
    }

    /** The event sent (the same id when it is sent again). */
    public String eventId() {
        return eventId;
    }

    /** Sets event_id. */
    public WebhookDelivery eventId(String value) {
        this.eventId = value;
        return this;
    }

    /** The event's type. */
    public String eventType() {
        return eventType;
    }

    /** Sets event_type. */
    public WebhookDelivery eventType(String value) {
        this.eventType = value;
        return this;
    }

    /** Its account. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public WebhookDelivery accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** A test. */
    public Boolean test() {
        return test;
    }

    /** Sets test. */
    public WebhookDelivery test(Boolean value) {
        this.test = value;
        return this;
    }

    /** pending: waiting for its next try. failed: every try failed. canceled: the endpoint was disabled or deleted. */
    public WebhookDeliveryStatus status() {
        return status;
    }

    /** Sets status. */
    public WebhookDelivery status(WebhookDeliveryStatus value) {
        this.status = value;
        return this;
    }

    /** Every try, oldest first. */
    public List<WebhookDeliveryAttempt> attempts() {
        return attempts;
    }

    /** Sets attempts. */
    public WebhookDelivery attempts(List<WebhookDeliveryAttempt> value) {
        this.attempts = value;
        return this;
    }

    /** When it is tried again. */
    public Long nextAttemptAt() {
        return nextAttemptAt;
    }

    /** Sets next_attempt_at. */
    public WebhookDelivery nextAttemptAt(Long value) {
        this.nextAttemptAt = value;
        return this;
    }

    /** When it was queued. */
    public Long createdAt() {
        return createdAt;
    }

    /** Sets created_at. */
    public WebhookDelivery createdAt(Long value) {
        this.createdAt = value;
        return this;
    }

    /** When your endpoint took it. */
    public Long deliveredAt() {
        return deliveredAt;
    }

    /** Sets delivered_at. */
    public WebhookDelivery deliveredAt(Long value) {
        this.deliveredAt = value;
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
        if (!(other instanceof WebhookDelivery)) {
            return false;
        }
        WebhookDelivery o = (WebhookDelivery) other;
        return Objects.equals(id, o.id)
            && Objects.equals(endpointId, o.endpointId)
            && Objects.equals(eventId, o.eventId)
            && Objects.equals(eventType, o.eventType)
            && Objects.equals(accountId, o.accountId)
            && Objects.equals(test, o.test)
            && Objects.equals(status, o.status)
            && Objects.equals(attempts, o.attempts)
            && Objects.equals(nextAttemptAt, o.nextAttemptAt)
            && Objects.equals(createdAt, o.createdAt)
            && Objects.equals(deliveredAt, o.deliveredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, endpointId, eventId, eventType, accountId, test, status, attempts, nextAttemptAt, createdAt, deliveredAt);
    }

    @Override
    public String toString() {
        return "WebhookDelivery{" + "id=" + id + ", endpoint_id=" + endpointId + ", event_id=" + eventId + ", event_type=" + eventType + ", account_id=" + accountId + ", test=" + test + ", status=" + status + ", attempts=" + attempts + ", next_attempt_at=" + nextAttemptAt + ", created_at=" + createdAt + ", delivered_at=" + deliveredAt + "}";
    }
}
