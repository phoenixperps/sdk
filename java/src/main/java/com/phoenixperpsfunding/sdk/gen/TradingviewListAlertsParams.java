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

/** The query of list a link's alerts. */
public final class TradingviewListAlertsParams {
    @JsonProperty("status")
    private String status;

    @JsonProperty("limit")
    private Long limit;

    @JsonProperty("cursor")
    private String cursor;

    /** Only alerts with this status. */
    public String status() {
        return status;
    }

    /** Sets status. */
    public TradingviewListAlertsParams status(String value) {
        this.status = value;
        return this;
    }

    /** Page size. */
    public Long limit() {
        return limit;
    }

    /** Sets limit. */
    public TradingviewListAlertsParams limit(Long value) {
        this.limit = value;
        return this;
    }

    /** next_cursor from the previous page, with the same filters. */
    public String cursor() {
        return cursor;
    }

    /** Sets cursor. */
    public TradingviewListAlertsParams cursor(String value) {
        this.cursor = value;
        return this;
    }

    /** The query string parameters. */
    public Map<String, String> toQuery() {
        Map<String, String> q = new LinkedHashMap<>();
        if (status != null) {
            q.put("status", String.valueOf(status));
        }
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
        if (!(other instanceof TradingviewListAlertsParams)) {
            return false;
        }
        TradingviewListAlertsParams o = (TradingviewListAlertsParams) other;
        return Objects.equals(status, o.status)
            && Objects.equals(limit, o.limit)
            && Objects.equals(cursor, o.cursor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, limit, cursor);
    }

    @Override
    public String toString() {
        return "TradingviewListAlertsParams{" + "status=" + status + ", limit=" + limit + ", cursor=" + cursor + "}";
    }
}
