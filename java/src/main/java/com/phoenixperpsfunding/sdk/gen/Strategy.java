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

/** Strategy */
public final class Strategy {
    @JsonProperty("id")
    private String id;

    @JsonProperty("client_strategy_id")
    private String clientStrategyId;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("type")
    private StrategyType type;

    @JsonProperty("market")
    private String market;

    @JsonProperty("side")
    private OrderSide side;

    @JsonProperty("status")
    private StrategyStatus status;

    @JsonProperty("end_reason")
    private StrategyEndReason endReason;

    @JsonProperty("params")
    private Map<String, Object> params;

    @JsonProperty("progress")
    private Map<String, Object> progress;

    @JsonProperty("orders")
    private List<Order> orders;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;

    @JsonProperty("ended_at")
    private Long endedAt;

    /** The strategy. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public Strategy id(String value) {
        this.id = value;
        return this;
    }

    /** Your own id for it. */
    public String clientStrategyId() {
        return clientStrategyId;
    }

    /** Sets client_strategy_id. */
    public Strategy clientStrategyId(String value) {
        this.clientStrategyId = value;
        return this;
    }

    /** The account number. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public Strategy accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** exits: a position's take-profit and stop-loss legs. bracket: an order with exits once it fills. oco: two orders, the first fill cancels the other. twap, vwap: an order in slices over time. scaled: a ladder of limits. chase: a limit that follows the price. */
    public StrategyType type() {
        return type;
    }

    /** Sets type. */
    public Strategy type(StrategyType value) {
        this.type = value;
        return this;
    }

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public Strategy market(String value) {
        this.market = value;
        return this;
    }

    /** The side of its orders (exits: the closing side). */
    public OrderSide side() {
        return side;
    }

    /** Sets side. */
    public Strategy side(OrderSide value) {
        this.side = value;
        return this;
    }

    /** waiting: armed, nothing placed yet. working: running. completed: done as asked. canceled: you ended it. ended: it stopped for end_reason. */
    public StrategyStatus status() {
        return status;
    }

    /** Sets status. */
    public Strategy status(StrategyStatus value) {
        this.status = value;
        return this;
    }

    /** Why it stopped. */
    public StrategyEndReason endReason() {
        return endReason;
    }

    /** Sets end_reason. */
    public Strategy endReason(StrategyEndReason value) {
        this.endReason = value;
        return this;
    }

    /** What it was created with. */
    public Map<String, Object> params() {
        return params;
    }

    /** Sets params. */
    public Strategy params(Map<String, Object> value) {
        this.params = value;
        return this;
    }

    /** How far it is (by type). */
    public Map<String, Object> progress() {
        return progress;
    }

    /** Sets progress. */
    public Strategy progress(Map<String, Object> value) {
        this.progress = value;
        return this;
    }

    /** Every order it placed, oldest first. */
    public List<Order> orders() {
        return orders;
    }

    /** Sets orders. */
    public Strategy orders(List<Order> value) {
        this.orders = value;
        return this;
    }

    /** When it was created. */
    public Long createdAt() {
        return createdAt;
    }

    /** Sets created_at. */
    public Strategy createdAt(Long value) {
        this.createdAt = value;
        return this;
    }

    /** Its latest change. */
    public Long updatedAt() {
        return updatedAt;
    }

    /** Sets updated_at. */
    public Strategy updatedAt(Long value) {
        this.updatedAt = value;
        return this;
    }

    /** When it stopped. */
    public Long endedAt() {
        return endedAt;
    }

    /** Sets ended_at. */
    public Strategy endedAt(Long value) {
        this.endedAt = value;
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
        if (!(other instanceof Strategy)) {
            return false;
        }
        Strategy o = (Strategy) other;
        return Objects.equals(id, o.id)
            && Objects.equals(clientStrategyId, o.clientStrategyId)
            && Objects.equals(accountId, o.accountId)
            && Objects.equals(type, o.type)
            && Objects.equals(market, o.market)
            && Objects.equals(side, o.side)
            && Objects.equals(status, o.status)
            && Objects.equals(endReason, o.endReason)
            && Objects.equals(params, o.params)
            && Objects.equals(progress, o.progress)
            && Objects.equals(orders, o.orders)
            && Objects.equals(createdAt, o.createdAt)
            && Objects.equals(updatedAt, o.updatedAt)
            && Objects.equals(endedAt, o.endedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientStrategyId, accountId, type, market, side, status, endReason, params, progress, orders, createdAt, updatedAt, endedAt);
    }

    @Override
    public String toString() {
        return "Strategy{" + "id=" + id + ", client_strategy_id=" + clientStrategyId + ", account_id=" + accountId + ", type=" + type + ", market=" + market + ", side=" + side + ", status=" + status + ", end_reason=" + endReason + ", params=" + params + ", progress=" + progress + ", orders=" + orders + ", created_at=" + createdAt + ", updated_at=" + updatedAt + ", ended_at=" + endedAt + "}";
    }
}
