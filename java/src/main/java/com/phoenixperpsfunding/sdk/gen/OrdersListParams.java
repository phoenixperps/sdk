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

/** The query of list orders. */
public final class OrdersListParams {
    @JsonProperty("status")
    private OrdersListParamsStatus status;

    @JsonProperty("market")
    private String market;

    @JsonProperty("client_order_id")
    private String clientOrderId;

    @JsonProperty("limit")
    private Long limit;

    @JsonProperty("cursor")
    private String cursor;

    /** working: the complete set of working orders. history: past orders. all: both. */
    public OrdersListParamsStatus status() {
        return status;
    }

    /** Sets status. */
    public OrdersListParams status(OrdersListParamsStatus value) {
        this.status = value;
        return this;
    }

    /** Only this market. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public OrdersListParams market(String value) {
        this.market = value;
        return this;
    }

    /** The order you placed through the API with this id. */
    public String clientOrderId() {
        return clientOrderId;
    }

    /** Sets client_order_id. */
    public OrdersListParams clientOrderId(String value) {
        this.clientOrderId = value;
        return this;
    }

    /** Page size. */
    public Long limit() {
        return limit;
    }

    /** Sets limit. */
    public OrdersListParams limit(Long value) {
        this.limit = value;
        return this;
    }

    /** next_cursor from the previous page, with the same filters. */
    public String cursor() {
        return cursor;
    }

    /** Sets cursor. */
    public OrdersListParams cursor(String value) {
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
        if (clientOrderId != null) {
            q.put("client_order_id", String.valueOf(clientOrderId));
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
        if (!(other instanceof OrdersListParams)) {
            return false;
        }
        OrdersListParams o = (OrdersListParams) other;
        return Objects.equals(status, o.status)
            && Objects.equals(market, o.market)
            && Objects.equals(clientOrderId, o.clientOrderId)
            && Objects.equals(limit, o.limit)
            && Objects.equals(cursor, o.cursor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, market, clientOrderId, limit, cursor);
    }

    @Override
    public String toString() {
        return "OrdersListParams{" + "status=" + status + ", market=" + market + ", client_order_id=" + clientOrderId + ", limit=" + limit + ", cursor=" + cursor + "}";
    }
}
