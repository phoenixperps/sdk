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

/** The query of list deliveries. */
public final class WebhookEndpointsListDeliveriesParams {
    @JsonProperty("limit")
    private Long limit;

    @JsonProperty("cursor")
    private String cursor;

    /** Page size. */
    public Long limit() {
        return limit;
    }

    /** Sets limit. */
    public WebhookEndpointsListDeliveriesParams limit(Long value) {
        this.limit = value;
        return this;
    }

    /** next_cursor from the previous page, with the same filters. */
    public String cursor() {
        return cursor;
    }

    /** Sets cursor. */
    public WebhookEndpointsListDeliveriesParams cursor(String value) {
        this.cursor = value;
        return this;
    }

    /** The query string parameters. */
    public Map<String, String> toQuery() {
        Map<String, String> q = new LinkedHashMap<>();
        if (limit != null) {
            q.put("limit", String.valueOf(limit));
        }
        if (cursor != null) {
            q.put("cursor", String.valueOf(cursor));
        }
        return q;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof WebhookEndpointsListDeliveriesParams)) {
            return false;
        }
        WebhookEndpointsListDeliveriesParams o = (WebhookEndpointsListDeliveriesParams) other;
        return Objects.equals(limit, o.limit)
            && Objects.equals(cursor, o.cursor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, cursor);
    }

    @Override
    public String toString() {
        return "WebhookEndpointsListDeliveriesParams{" + "limit=" + limit + ", cursor=" + cursor + "}";
    }
}
