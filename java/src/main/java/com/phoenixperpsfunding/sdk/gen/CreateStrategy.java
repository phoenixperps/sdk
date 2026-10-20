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

/** A new strategy. trailing_stop and break_even act on the position's exits: the answer is that exits strategy. */
public final class CreateStrategy {
    @JsonProperty("type")
    private CreateStrategyType type;

    @JsonProperty("market")
    private String market;

    @JsonProperty("side")
    private OrderSide side;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("distance")
    private BigDecimal distance;

    @JsonProperty("percent")
    private BigDecimal percent;

    @JsonProperty("activation_price")
    private BigDecimal activationPrice;

    @JsonProperty("initial_stop_price")
    private BigDecimal initialStopPrice;

    @JsonProperty("replace_stops")
    private Boolean replaceStops;

    @JsonProperty("trigger_distance")
    private BigDecimal triggerDistance;

    @JsonProperty("trigger_percent")
    private BigDecimal triggerPercent;

    @JsonProperty("trigger_price")
    private BigDecimal triggerPrice;

    @JsonProperty("offset_ticks")
    private Long offsetTicks;

    @JsonProperty("orders")
    private List<CreateStrategyOrder> orders;

    @JsonProperty("exits")
    private OrderExits exits;

    @JsonProperty("duration_minutes")
    private Long durationMinutes;

    @JsonProperty("interval_seconds")
    private Long intervalSeconds;

    @JsonProperty("randomize")
    private Boolean randomize;

    @JsonProperty("reduce_only")
    private Boolean reduceOnly;

    @JsonProperty("max_price")
    private BigDecimal maxPrice;

    @JsonProperty("min_price")
    private BigDecimal minPrice;

    @JsonProperty("max_slippage_pct")
    private BigDecimal maxSlippagePct;

    @JsonProperty("start_price")
    private BigDecimal startPrice;

    @JsonProperty("end_price")
    private BigDecimal endPrice;

    @JsonProperty("rungs")
    private Long rungs;

    @JsonProperty("distribution")
    private CreateStrategyDistribution distribution;

    @JsonProperty("post_only")
    private Boolean postOnly;

    @JsonProperty("all_or_none")
    private Boolean allOrNone;

    @JsonProperty("allow_marketable")
    private Boolean allowMarketable;

    @JsonProperty("max_minutes")
    private Long maxMinutes;

    @JsonProperty("on_expiry")
    private CreateStrategyOnExpiry onExpiry;

    @JsonProperty("allow_cross")
    private Boolean allowCross;

    @JsonProperty("client_strategy_id")
    private String clientStrategyId;

    /** trailing_stop: a stop-loss that follows the price. break_even: the stop-losses move to the entry once the price has gone your way far enough. oco: two orders, the first fill cancels the other. twap, vwap: an order in market slices over time (vwap sized by yesterday's volume over the same hours). scaled: a ladder of limits. chase: a limit that follows the bid or ask. */
    public CreateStrategyType type() {
        return type;
    }

    /** Sets type. */
    public CreateStrategy type(CreateStrategyType value) {
        this.type = value;
        return this;
    }

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public CreateStrategy market(String value) {
        this.market = value;
        return this;
    }

    /** The order's side. */
    public OrderSide side() {
        return side;
    }

    /** Sets side. */
    public CreateStrategy side(OrderSide value) {
        this.side = value;
        return this;
    }

    /** trailing_stop: its size (absent: what the stop-losses leave of the position). twap, vwap, scaled, chase: the whole size. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public CreateStrategy size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** trailing_stop: how far behind the best price (or percent). */
    public BigDecimal distance() {
        return distance;
    }

    /** Sets distance. */
    public CreateStrategy distance(BigDecimal value) {
        this.distance = value;
        return this;
    }

    /** trailing_stop: how far behind, in percent of the best price. */
    public BigDecimal percent() {
        return percent;
    }

    /** Sets percent. */
    public CreateStrategy percent(BigDecimal value) {
        this.percent = value;
        return this;
    }

    /** trailing_stop: start once the price reaches this (until then, initial_stop_price rests, or nothing). */
    public BigDecimal activationPrice() {
        return activationPrice;
    }

    /** Sets activation_price. */
    public CreateStrategy activationPrice(BigDecimal value) {
        this.activationPrice = value;
        return this;
    }

    /** trailing_stop: where the stop rests until the trail moves it (absent: from the price now). */
    public BigDecimal initialStopPrice() {
        return initialStopPrice;
    }

    /** Sets initial_stop_price. */
    public CreateStrategy initialStopPrice(BigDecimal value) {
        this.initialStopPrice = value;
        return this;
    }

    /** trailing_stop: cancel the position's other stop-losses first. */
    public Boolean replaceStops() {
        return replaceStops;
    }

    /** Sets replace_stops. */
    public CreateStrategy replaceStops(Boolean value) {
        this.replaceStops = value;
        return this;
    }

    /** break_even: how far from the entry the price must go. */
    public BigDecimal triggerDistance() {
        return triggerDistance;
    }

    /** Sets trigger_distance. */
    public CreateStrategy triggerDistance(BigDecimal value) {
        this.triggerDistance = value;
        return this;
    }

    /** break_even: the same, in percent of the entry. */
    public BigDecimal triggerPercent() {
        return triggerPercent;
    }

    /** Sets trigger_percent. */
    public CreateStrategy triggerPercent(BigDecimal value) {
        this.triggerPercent = value;
        return this;
    }

    /** break_even: or once the price reaches this. */
    public BigDecimal triggerPrice() {
        return triggerPrice;
    }

    /** Sets trigger_price. */
    public CreateStrategy triggerPrice(BigDecimal value) {
        this.triggerPrice = value;
        return this;
    }

    /** break_even: ticks beyond the entry, on the profit side. chase: ticks behind the bid (buy) or ask (sell). */
    public Long offsetTicks() {
        return offsetTicks;
    }

    /** Sets offset_ticks. */
    public CreateStrategy offsetTicks(Long value) {
        this.offsetTicks = value;
        return this;
    }

    /** oco: the two orders. */
    public List<CreateStrategyOrder> orders() {
        return orders;
    }

    /** Sets orders. */
    public CreateStrategy orders(List<CreateStrategyOrder> value) {
        this.orders = value;
        return this;
    }

    /** The exits. */
    public OrderExits exits() {
        return exits;
    }

    /** Sets exits. */
    public CreateStrategy exits(OrderExits value) {
        this.exits = value;
        return this;
    }

    /** twap, vwap: how long the schedule runs (bounds in the runner settings, see GET /v1/limits). */
    public Long durationMinutes() {
        return durationMinutes;
    }

    /** Sets duration_minutes. */
    public CreateStrategy durationMinutes(Long value) {
        this.durationMinutes = value;
        return this;
    }

    /** twap, vwap: time between slices; at most half the duration. Default: 60 (within those bounds). */
    public Long intervalSeconds() {
        return intervalSeconds;
    }

    /** Sets interval_seconds. */
    public CreateStrategy intervalSeconds(Long value) {
        this.intervalSeconds = value;
        return this;
    }

    /** twap, vwap: vary each slice's time and size a little (the sizes still add up to size). */
    public Boolean randomize() {
        return randomize;
    }

    /** Sets randomize. */
    public CreateStrategy randomize(Boolean value) {
        this.randomize = value;
        return this;
    }

    /** twap, vwap, scaled, chase: only reduce your position (a twap ends when the position is closed). */
    public Boolean reduceOnly() {
        return reduceOnly;
    }

    /** Sets reduce_only. */
    public CreateStrategy reduceOnly(Boolean value) {
        this.reduceOnly = value;
        return this;
    }

    /** twap, vwap (buy): slices wait while the price is above this. chase (buy): its cap. */
    public BigDecimal maxPrice() {
        return maxPrice;
    }

    /** Sets max_price. */
    public CreateStrategy maxPrice(BigDecimal value) {
        this.maxPrice = value;
        return this;
    }

    /** twap, vwap (sell): slices wait while the price is below this. chase (sell): its floor. */
    public BigDecimal minPrice() {
        return minPrice;
    }

    /** Sets min_price. */
    public CreateStrategy minPrice(BigDecimal value) {
        this.minPrice = value;
        return this;
    }

    /** twap, vwap: each slice's slippage budget, in percent. */
    public BigDecimal maxSlippagePct() {
        return maxSlippagePct;
    }

    /** Sets max_slippage_pct. */
    public CreateStrategy maxSlippagePct(BigDecimal value) {
        this.maxSlippagePct = value;
        return this;
    }

    /** scaled: the first rung. */
    public BigDecimal startPrice() {
        return startPrice;
    }

    /** Sets start_price. */
    public CreateStrategy startPrice(BigDecimal value) {
        this.startPrice = value;
        return this;
    }

    /** scaled: the last rung. */
    public BigDecimal endPrice() {
        return endPrice;
    }

    /** Sets end_price. */
    public CreateStrategy endPrice(BigDecimal value) {
        this.endPrice = value;
        return this;
    }

    /** scaled: how many limits (at most the runner setting). */
    public Long rungs() {
        return rungs;
    }

    /** Sets rungs. */
    public CreateStrategy rungs(Long value) {
        this.rungs = value;
        return this;
    }

    /** scaled: equal sizes, growing toward end_price, or shrinking. */
    public CreateStrategyDistribution distribution() {
        return distribution;
    }

    /** Sets distribution. */
    public CreateStrategy distribution(CreateStrategyDistribution value) {
        this.distribution = value;
        return this;
    }

    /** scaled: each rung refused if it would fill at once. */
    public Boolean postOnly() {
        return postOnly;
    }

    /** Sets post_only. */
    public CreateStrategy postOnly(Boolean value) {
        this.postOnly = value;
        return this;
    }

    /** scaled: one rung refused cancels the others. */
    public Boolean allOrNone() {
        return allOrNone;
    }

    /** Sets all_or_none. */
    public CreateStrategy allOrNone(Boolean value) {
        this.allOrNone = value;
        return this;
    }

    /** scaled: rungs through the price may fill at once (refused otherwise, rung_would_fill). */
    public Boolean allowMarketable() {
        return allowMarketable;
    }

    /** Sets allow_marketable. */
    public CreateStrategy allowMarketable(Boolean value) {
        this.allowMarketable = value;
        return this;
    }

    /** chase: how long it follows the price. */
    public Long maxMinutes() {
        return maxMinutes;
    }

    /** Sets max_minutes. */
    public CreateStrategy maxMinutes(Long value) {
        this.maxMinutes = value;
        return this;
    }

    /** chase: at max_minutes, cancel it, or send what is left at the market. */
    public CreateStrategyOnExpiry onExpiry() {
        return onExpiry;
    }

    /** Sets on_expiry. */
    public CreateStrategy onExpiry(CreateStrategyOnExpiry value) {
        this.onExpiry = value;
        return this;
    }

    /** chase: it may rest through the last price (it would then fill at once). */
    public Boolean allowCross() {
        return allowCross;
    }

    /** Sets allow_cross. */
    public CreateStrategy allowCross(Boolean value) {
        this.allowCross = value;
        return this;
    }

    /** Your own id for the strategy (oco, twap, vwap, scaled, chase): unique in this environment. */
    public String clientStrategyId() {
        return clientStrategyId;
    }

    /** Sets client_strategy_id. */
    public CreateStrategy clientStrategyId(String value) {
        this.clientStrategyId = value;
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
        if (!(other instanceof CreateStrategy)) {
            return false;
        }
        CreateStrategy o = (CreateStrategy) other;
        return Objects.equals(type, o.type)
            && Objects.equals(market, o.market)
            && Objects.equals(side, o.side)
            && Objects.equals(size, o.size)
            && Objects.equals(distance, o.distance)
            && Objects.equals(percent, o.percent)
            && Objects.equals(activationPrice, o.activationPrice)
            && Objects.equals(initialStopPrice, o.initialStopPrice)
            && Objects.equals(replaceStops, o.replaceStops)
            && Objects.equals(triggerDistance, o.triggerDistance)
            && Objects.equals(triggerPercent, o.triggerPercent)
            && Objects.equals(triggerPrice, o.triggerPrice)
            && Objects.equals(offsetTicks, o.offsetTicks)
            && Objects.equals(orders, o.orders)
            && Objects.equals(exits, o.exits)
            && Objects.equals(durationMinutes, o.durationMinutes)
            && Objects.equals(intervalSeconds, o.intervalSeconds)
            && Objects.equals(randomize, o.randomize)
            && Objects.equals(reduceOnly, o.reduceOnly)
            && Objects.equals(maxPrice, o.maxPrice)
            && Objects.equals(minPrice, o.minPrice)
            && Objects.equals(maxSlippagePct, o.maxSlippagePct)
            && Objects.equals(startPrice, o.startPrice)
            && Objects.equals(endPrice, o.endPrice)
            && Objects.equals(rungs, o.rungs)
            && Objects.equals(distribution, o.distribution)
            && Objects.equals(postOnly, o.postOnly)
            && Objects.equals(allOrNone, o.allOrNone)
            && Objects.equals(allowMarketable, o.allowMarketable)
            && Objects.equals(maxMinutes, o.maxMinutes)
            && Objects.equals(onExpiry, o.onExpiry)
            && Objects.equals(allowCross, o.allowCross)
            && Objects.equals(clientStrategyId, o.clientStrategyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, market, side, size, distance, percent, activationPrice, initialStopPrice, replaceStops, triggerDistance, triggerPercent, triggerPrice, offsetTicks, orders, exits, durationMinutes, intervalSeconds, randomize, reduceOnly, maxPrice, minPrice, maxSlippagePct, startPrice, endPrice, rungs, distribution, postOnly, allOrNone, allowMarketable, maxMinutes, onExpiry, allowCross, clientStrategyId);
    }

    @Override
    public String toString() {
        return "CreateStrategy{" + "type=" + type + ", market=" + market + ", side=" + side + ", size=" + size + ", distance=" + distance + ", percent=" + percent + ", activation_price=" + activationPrice + ", initial_stop_price=" + initialStopPrice + ", replace_stops=" + replaceStops + ", trigger_distance=" + triggerDistance + ", trigger_percent=" + triggerPercent + ", trigger_price=" + triggerPrice + ", offset_ticks=" + offsetTicks + ", orders=" + orders + ", exits=" + exits + ", duration_minutes=" + durationMinutes + ", interval_seconds=" + intervalSeconds + ", randomize=" + randomize + ", reduce_only=" + reduceOnly + ", max_price=" + maxPrice + ", min_price=" + minPrice + ", max_slippage_pct=" + maxSlippagePct + ", start_price=" + startPrice + ", end_price=" + endPrice + ", rungs=" + rungs + ", distribution=" + distribution + ", post_only=" + postOnly + ", all_or_none=" + allOrNone + ", allow_marketable=" + allowMarketable + ", max_minutes=" + maxMinutes + ", on_expiry=" + onExpiry + ", allow_cross=" + allowCross + ", client_strategy_id=" + clientStrategyId + "}";
    }
}
