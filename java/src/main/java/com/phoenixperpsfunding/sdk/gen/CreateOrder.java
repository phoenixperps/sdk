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

/** A new order. */
public final class CreateOrder {
    @JsonProperty("market")
    private String market;

    @JsonProperty("side")
    private OrderSide side;

    @JsonProperty("type")
    private CreateOrderType type;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("notional_usd")
    private BigDecimal notionalUsd;

    @JsonProperty("limit_price")
    private BigDecimal limitPrice;

    @JsonProperty("stop_price")
    private BigDecimal stopPrice;

    @JsonProperty("worst_price")
    private BigDecimal worstPrice;

    @JsonProperty("max_slippage_pct")
    private BigDecimal maxSlippagePct;

    @JsonProperty("reduce_only")
    private Boolean reduceOnly;

    @JsonProperty("post_only")
    private Boolean postOnly;

    @JsonProperty("time_in_force")
    private TimeInForce timeInForce;

    @JsonProperty("expires_at")
    private Long expiresAt;

    @JsonProperty("client_order_id")
    private String clientOrderId;

    @JsonProperty("exits")
    private OrderExits exits;

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public CreateOrder market(String value) {
        this.market = value;
        return this;
    }

    /** The order's side. */
    public OrderSide side() {
        return side;
    }

    /** Sets side. */
    public CreateOrder side(OrderSide value) {
        this.side = value;
        return this;
    }

    /** market: now, at the best price within your worst price. limit: at your price or better. stop_market: a market order once the last price reaches the stop. stop_limit: a limit once the last price reaches the stop. */
    public CreateOrderType type() {
        return type;
    }

    /** Sets type. */
    public CreateOrder type(CreateOrderType value) {
        this.type = value;
        return this;
    }

    /** The size, in the base asset: a multiple of the market's size step. Give size or notional_usd. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public CreateOrder size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** The size as a value in USD, rounded down to the size step. */
    public BigDecimal notionalUsd() {
        return notionalUsd;
    }

    /** Sets notional_usd. */
    public CreateOrder notionalUsd(BigDecimal value) {
        this.notionalUsd = value;
        return this;
    }

    /** The limit price (limit and stop_limit): a multiple of the tick size. */
    public BigDecimal limitPrice() {
        return limitPrice;
    }

    /** Sets limit_price. */
    public CreateOrder limitPrice(BigDecimal value) {
        this.limitPrice = value;
        return this;
    }

    /** The trigger price (stop_market and stop_limit). */
    public BigDecimal stopPrice() {
        return stopPrice;
    }

    /** Sets stop_price. */
    public CreateOrder stopPrice(BigDecimal value) {
        this.stopPrice = value;
        return this;
    }

    /** Market orders: the worst price you accept. Give worst_price or max_slippage_pct. */
    public BigDecimal worstPrice() {
        return worstPrice;
    }

    /** Sets worst_price. */
    public CreateOrder worstPrice(BigDecimal value) {
        this.worstPrice = value;
        return this;
    }

    /** Market orders: how far from the best price you accept, in percent (the default and bounds are in GET /v1/markets). */
    public BigDecimal maxSlippagePct() {
        return maxSlippagePct;
    }

    /** Sets max_slippage_pct. */
    public CreateOrder maxSlippagePct(BigDecimal value) {
        this.maxSlippagePct = value;
        return this;
    }

    /** It may only reduce or close your position, never open or add to one. */
    public Boolean reduceOnly() {
        return reduceOnly;
    }

    /** Sets reduce_only. */
    public CreateOrder reduceOnly(Boolean value) {
        this.reduceOnly = value;
        return this;
    }

    /** Limit orders: refused if it would fill at once. */
    public Boolean postOnly() {
        return postOnly;
    }

    /** Sets post_only. */
    public CreateOrder postOnly(Boolean value) {
        this.postOnly = value;
        return this;
    }

    /** gtc: until canceled. gtd: until expires_at. ioc and fok: fill now or end (a fill is always the whole order). Default: ioc for market orders, gtc otherwise. */
    public TimeInForce timeInForce() {
        return timeInForce;
    }

    /** Sets time_in_force. */
    public CreateOrder timeInForce(TimeInForce value) {
        this.timeInForce = value;
        return this;
    }

    /** With gtd: when it ends, from orders.gtd_min_seconds to orders.gtd_max_days ahead (GET /v1/limits). */
    public Long expiresAt() {
        return expiresAt;
    }

    /** Sets expires_at. */
    public CreateOrder expiresAt(Long value) {
        this.expiresAt = value;
        return this;
    }

    /** Your own id for the order: unique in this environment, 1 to 64 letters, digits and . _ : - */
    public String clientOrderId() {
        return clientOrderId;
    }

    /** Sets client_order_id. */
    public CreateOrder clientOrderId(String value) {
        this.clientOrderId = value;
        return this;
    }

    /** The exits. */
    public OrderExits exits() {
        return exits;
    }

    /** Sets exits. */
    public CreateOrder exits(OrderExits value) {
        this.exits = value;
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
        if (!(other instanceof CreateOrder)) {
            return false;
        }
        CreateOrder o = (CreateOrder) other;
        return Objects.equals(market, o.market)
            && Objects.equals(side, o.side)
            && Objects.equals(type, o.type)
            && Objects.equals(size, o.size)
            && Objects.equals(notionalUsd, o.notionalUsd)
            && Objects.equals(limitPrice, o.limitPrice)
            && Objects.equals(stopPrice, o.stopPrice)
            && Objects.equals(worstPrice, o.worstPrice)
            && Objects.equals(maxSlippagePct, o.maxSlippagePct)
            && Objects.equals(reduceOnly, o.reduceOnly)
            && Objects.equals(postOnly, o.postOnly)
            && Objects.equals(timeInForce, o.timeInForce)
            && Objects.equals(expiresAt, o.expiresAt)
            && Objects.equals(clientOrderId, o.clientOrderId)
            && Objects.equals(exits, o.exits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, side, type, size, notionalUsd, limitPrice, stopPrice, worstPrice, maxSlippagePct, reduceOnly, postOnly, timeInForce, expiresAt, clientOrderId, exits);
    }

    @Override
    public String toString() {
        return "CreateOrder{" + "market=" + market + ", side=" + side + ", type=" + type + ", size=" + size + ", notional_usd=" + notionalUsd + ", limit_price=" + limitPrice + ", stop_price=" + stopPrice + ", worst_price=" + worstPrice + ", max_slippage_pct=" + maxSlippagePct + ", reduce_only=" + reduceOnly + ", post_only=" + postOnly + ", time_in_force=" + timeInForce + ", expires_at=" + expiresAt + ", client_order_id=" + clientOrderId + ", exits=" + exits + "}";
    }
}
