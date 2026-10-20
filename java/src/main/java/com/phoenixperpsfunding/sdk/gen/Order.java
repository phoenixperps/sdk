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

/** Order */
public final class Order {
    @JsonProperty("id")
    private String id;

    @JsonProperty("client_order_id")
    private String clientOrderId;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("market")
    private String market;

    @JsonProperty("side")
    private OrderSide side;

    @JsonProperty("type")
    private OrderType type;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("filled_size")
    private BigDecimal filledSize;

    @JsonProperty("limit_price")
    private BigDecimal limitPrice;

    @JsonProperty("stop_price")
    private BigDecimal stopPrice;

    @JsonProperty("average_fill_price")
    private BigDecimal averageFillPrice;

    @JsonProperty("fee")
    private BigDecimal fee;

    @JsonProperty("reduce_only")
    private Boolean reduceOnly;

    @JsonProperty("time_in_force")
    private TimeInForce timeInForce;

    @JsonProperty("expires_at")
    private Long expiresAt;

    @JsonProperty("post_only")
    private Boolean postOnly;

    @JsonProperty("source")
    private OrderSource source;

    @JsonProperty("strategy_id")
    private String strategyId;

    @JsonProperty("exits")
    private Map<String, Object> exits;

    @JsonProperty("cancel_reason")
    private OrderCancelReason cancelReason;

    @JsonProperty("reject")
    private OrderReject reject;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("filled_at")
    private Long filledAt;

    @JsonProperty("canceled_at")
    private Long canceledAt;

    @JsonProperty("updated_at")
    private Long updatedAt;

    /** The order. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public Order id(String value) {
        this.id = value;
        return this;
    }

    /** Your own id for it, when it was placed through the API. */
    public String clientOrderId() {
        return clientOrderId;
    }

    /** Sets client_order_id. */
    public Order clientOrderId(String value) {
        this.clientOrderId = value;
        return this;
    }

    /** The account number. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public Order accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public Order market(String value) {
        this.market = value;
        return this;
    }

    /** The order's side; null for an order placed elsewhere before the API saw it. */
    public OrderSide side() {
        return side;
    }

    /** Sets side. */
    public Order side(OrderSide value) {
        this.side = value;
        return this;
    }

    /** The order's type. */
    public OrderType type() {
        return type;
    }

    /** Sets type. */
    public Order type(OrderType value) {
        this.type = value;
        return this;
    }

    /** unconfirmed: the engine has not confirmed its last state yet. */
    public OrderStatus status() {
        return status;
    }

    /** Sets status. */
    public Order status(OrderStatus value) {
        this.status = value;
        return this;
    }

    /** The size, in the base asset. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public Order size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** How much filled. */
    public BigDecimal filledSize() {
        return filledSize;
    }

    /** Sets filled_size. */
    public Order filledSize(BigDecimal value) {
        this.filledSize = value;
        return this;
    }

    /** Its limit price. */
    public BigDecimal limitPrice() {
        return limitPrice;
    }

    /** Sets limit_price. */
    public Order limitPrice(BigDecimal value) {
        this.limitPrice = value;
        return this;
    }

    /** Its trigger price. */
    public BigDecimal stopPrice() {
        return stopPrice;
    }

    /** Sets stop_price. */
    public Order stopPrice(BigDecimal value) {
        this.stopPrice = value;
        return this;
    }

    /** The average fill price. */
    public BigDecimal averageFillPrice() {
        return averageFillPrice;
    }

    /** Sets average_fill_price. */
    public Order averageFillPrice(BigDecimal value) {
        this.averageFillPrice = value;
        return this;
    }

    /** The fee its fill paid. */
    public BigDecimal fee() {
        return fee;
    }

    /** Sets fee. */
    public Order fee(BigDecimal value) {
        this.fee = value;
        return this;
    }

    /** It may only reduce a position; null when not known (placed elsewhere). */
    public Boolean reduceOnly() {
        return reduceOnly;
    }

    /** Sets reduce_only. */
    public Order reduceOnly(Boolean value) {
        this.reduceOnly = value;
        return this;
    }

    /** How long it rests. */
    public TimeInForce timeInForce() {
        return timeInForce;
    }

    /** Sets time_in_force. */
    public Order timeInForce(TimeInForce value) {
        this.timeInForce = value;
        return this;
    }

    /** A gtd order: when it expires (it ends expired then); null otherwise. */
    public Long expiresAt() {
        return expiresAt;
    }

    /** Sets expires_at. */
    public Order expiresAt(Long value) {
        this.expiresAt = value;
        return this;
    }

    /** It may only rest, never fill at once; null when not known (placed elsewhere). */
    public Boolean postOnly() {
        return postOnly;
    }

    /** Sets post_only. */
    public Order postOnly(Boolean value) {
        this.postOnly = value;
        return this;
    }

    /** Where it was placed: this API, an AI assistant (the MCP server), a trading platform, a strategy, a TradingView alert, or the system (a liquidation). */
    public OrderSource source() {
        return source;
    }

    /** Sets source. */
    public Order source(OrderSource value) {
        this.source = value;
        return this;
    }

    /** The strategy that placed it (source strategy). */
    public String strategyId() {
        return strategyId;
    }

    /** Sets strategy_id. */
    public Order strategyId(String value) {
        this.strategyId = value;
        return this;
    }

    /** Exits attached to it (orders placed through the API). */
    public Map<String, Object> exits() {
        return exits;
    }

    /** Sets exits. */
    public Order exits(Map<String, Object> value) {
        this.exits = value;
        return this;
    }

    /** Why it was canceled. */
    public OrderCancelReason cancelReason() {
        return cancelReason;
    }

    /** Sets cancel_reason. */
    public Order cancelReason(OrderCancelReason value) {
        this.cancelReason = value;
        return this;
    }

    /** The reject. */
    public OrderReject reject() {
        return reject;
    }

    /** Sets reject. */
    public Order reject(OrderReject value) {
        this.reject = value;
        return this;
    }

    /** When it was placed. */
    public Long createdAt() {
        return createdAt;
    }

    /** Sets created_at. */
    public Order createdAt(Long value) {
        this.createdAt = value;
        return this;
    }

    /** When it filled. */
    public Long filledAt() {
        return filledAt;
    }

    /** Sets filled_at. */
    public Order filledAt(Long value) {
        this.filledAt = value;
        return this;
    }

    /** When it was canceled. */
    public Long canceledAt() {
        return canceledAt;
    }

    /** Sets canceled_at. */
    public Order canceledAt(Long value) {
        this.canceledAt = value;
        return this;
    }

    /** Its latest change. */
    public Long updatedAt() {
        return updatedAt;
    }

    /** Sets updated_at. */
    public Order updatedAt(Long value) {
        this.updatedAt = value;
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
        if (!(other instanceof Order)) {
            return false;
        }
        Order o = (Order) other;
        return Objects.equals(id, o.id)
            && Objects.equals(clientOrderId, o.clientOrderId)
            && Objects.equals(accountId, o.accountId)
            && Objects.equals(market, o.market)
            && Objects.equals(side, o.side)
            && Objects.equals(type, o.type)
            && Objects.equals(status, o.status)
            && Objects.equals(size, o.size)
            && Objects.equals(filledSize, o.filledSize)
            && Objects.equals(limitPrice, o.limitPrice)
            && Objects.equals(stopPrice, o.stopPrice)
            && Objects.equals(averageFillPrice, o.averageFillPrice)
            && Objects.equals(fee, o.fee)
            && Objects.equals(reduceOnly, o.reduceOnly)
            && Objects.equals(timeInForce, o.timeInForce)
            && Objects.equals(expiresAt, o.expiresAt)
            && Objects.equals(postOnly, o.postOnly)
            && Objects.equals(source, o.source)
            && Objects.equals(strategyId, o.strategyId)
            && Objects.equals(exits, o.exits)
            && Objects.equals(cancelReason, o.cancelReason)
            && Objects.equals(reject, o.reject)
            && Objects.equals(createdAt, o.createdAt)
            && Objects.equals(filledAt, o.filledAt)
            && Objects.equals(canceledAt, o.canceledAt)
            && Objects.equals(updatedAt, o.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientOrderId, accountId, market, side, type, status, size, filledSize, limitPrice, stopPrice, averageFillPrice, fee, reduceOnly, timeInForce, expiresAt, postOnly, source, strategyId, exits, cancelReason, reject, createdAt, filledAt, canceledAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", client_order_id=" + clientOrderId + ", account_id=" + accountId + ", market=" + market + ", side=" + side + ", type=" + type + ", status=" + status + ", size=" + size + ", filled_size=" + filledSize + ", limit_price=" + limitPrice + ", stop_price=" + stopPrice + ", average_fill_price=" + averageFillPrice + ", fee=" + fee + ", reduce_only=" + reduceOnly + ", time_in_force=" + timeInForce + ", expires_at=" + expiresAt + ", post_only=" + postOnly + ", source=" + source + ", strategy_id=" + strategyId + ", exits=" + exits + ", cancel_reason=" + cancelReason + ", reject=" + reject + ", created_at=" + createdAt + ", filled_at=" + filledAt + ", canceled_at=" + canceledAt + ", updated_at=" + updatedAt + "}";
    }
}
