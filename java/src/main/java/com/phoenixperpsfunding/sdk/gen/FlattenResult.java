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

/** FlattenResult */
public final class FlattenResult {
    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("orders_canceled")
    private Long ordersCanceled;

    @JsonProperty("closes")
    private List<Order> closes;

    /** The account number. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public FlattenResult accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** How many working orders were canceled. */
    public Long ordersCanceled() {
        return ordersCanceled;
    }

    /** Sets orders_canceled. */
    public FlattenResult ordersCanceled(Long value) {
        this.ordersCanceled = value;
        return this;
    }

    /** The orders that closed the positions. */
    public List<Order> closes() {
        return closes;
    }

    /** Sets closes. */
    public FlattenResult closes(List<Order> value) {
        this.closes = value;
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
        if (!(other instanceof FlattenResult)) {
            return false;
        }
        FlattenResult o = (FlattenResult) other;
        return Objects.equals(accountId, o.accountId)
            && Objects.equals(ordersCanceled, o.ordersCanceled)
            && Objects.equals(closes, o.closes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, ordersCanceled, closes);
    }

    @Override
    public String toString() {
        return "FlattenResult{" + "account_id=" + accountId + ", orders_canceled=" + ordersCanceled + ", closes=" + closes + "}";
    }
}
