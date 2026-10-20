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

/** The query of list strategies. */
public final class StrategiesListParams {
    @JsonProperty("status")
    private StrategiesListParamsStatus status;

    @JsonProperty("market")
    private String market;

    @JsonProperty("client_strategy_id")
    private String clientStrategyId;

    @JsonProperty("limit")
    private Long limit;

    @JsonProperty("cursor")
    private String cursor;

    /** active: waiting or working. ended: completed, canceled or ended. all: both. */
    public StrategiesListParamsStatus status() {
        return status;
    }

    /** Sets status. */
    public StrategiesListParams status(StrategiesListParamsStatus value) {
        this.status = value;
        return this;
    }

    /** Only this market. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public StrategiesListParams market(String value) {
        this.market = value;
        return this;
    }

    /** The strategy you created with this id. */
    public String clientStrategyId() {
        return clientStrategyId;
    }

    /** Sets client_strategy_id. */
    public StrategiesListParams clientStrategyId(String value) {
        this.clientStrategyId = value;
        return this;
    }

    /** Page size. */
    public Long limit() {
        return limit;
    }

    /** Sets limit. */
    public StrategiesListParams limit(Long value) {
        this.limit = value;
        return this;
    }

    /** next_cursor from the previous page, with the same filters. */
    public String cursor() {
        return cursor;
    }

    /** Sets cursor. */
    public StrategiesListParams cursor(String value) {
        this.cursor = value;
        return this;
    }

    /** The query string parameters. */
    public Map<String, String> toQuery() {
        Map<String, String> q = new LinkedHashMap<>();
        if (status != null) {
            q.put("status", String.valueOf(status));
        }
        if (market != null) {
            q.put("market", String.valueOf(market));
        }
        if (clientStrategyId != null) {
            q.put("client_strategy_id", String.valueOf(clientStrategyId));
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
        if (!(other instanceof StrategiesListParams)) {
            return false;
        }
        StrategiesListParams o = (StrategiesListParams) other;
        return Objects.equals(status, o.status)
            && Objects.equals(market, o.market)
            && Objects.equals(clientStrategyId, o.clientStrategyId)
            && Objects.equals(limit, o.limit)
            && Objects.equals(cursor, o.cursor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, market, clientStrategyId, limit, cursor);
    }

    @Override
    public String toString() {
        return "StrategiesListParams{" + "status=" + status + ", market=" + market + ", client_strategy_id=" + clientStrategyId + ", limit=" + limit + ", cursor=" + cursor + "}";
    }
}
