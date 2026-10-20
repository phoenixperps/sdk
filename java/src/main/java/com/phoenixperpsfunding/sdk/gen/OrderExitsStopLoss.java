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

/** A stop on the loss side, at price, or at distance or percent from the fill; it may trail and move to break-even (as in a position's exits). */
public final class OrderExitsStopLoss {
    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("distance")
    private BigDecimal distance;

    @JsonProperty("percent")
    private BigDecimal percent;

    @JsonProperty("type")
    private OrderExitsStopLossType type;

    @JsonProperty("limit_offset_ticks")
    private Long limitOffsetTicks;

    @JsonProperty("trail")
    private OrderExitsStopLossTrail trail;

    @JsonProperty("break_even")
    private OrderExitsStopLossBreakEven breakEven;

    /** An exact price. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public OrderExitsStopLoss price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** A price difference from the fill price. */
    public BigDecimal distance() {
        return distance;
    }

    /** Sets distance. */
    public OrderExitsStopLoss distance(BigDecimal value) {
        this.distance = value;
        return this;
    }

    /** A percent of the fill price. */
    public BigDecimal percent() {
        return percent;
    }

    /** Sets percent. */
    public OrderExitsStopLoss percent(BigDecimal value) {
        this.percent = value;
        return this;
    }

    /** The stop's type (default stop_market). */
    public OrderExitsStopLossType type() {
        return type;
    }

    /** Sets type. */
    public OrderExitsStopLoss type(OrderExitsStopLossType value) {
        this.type = value;
        return this;
    }

    /** stop_limit: how many ticks past the stop its limit sits. */
    public Long limitOffsetTicks() {
        return limitOffsetTicks;
    }

    /** Sets limit_offset_ticks. */
    public OrderExitsStopLoss limitOffsetTicks(Long value) {
        this.limitOffsetTicks = value;
        return this;
    }

    /** As a position's exits take it: { distance } or { percent }, with activation_price to start only once the price reaches it. */
    public OrderExitsStopLossTrail trail() {
        return trail;
    }

    /** Sets trail. */
    public OrderExitsStopLoss trail(OrderExitsStopLossTrail value) {
        this.trail = value;
        return this;
    }

    /** As a position's exits take it: one of trigger_distance, trigger_percent, trigger_price, and offset_ticks. */
    public OrderExitsStopLossBreakEven breakEven() {
        return breakEven;
    }

    /** Sets break_even. */
    public OrderExitsStopLoss breakEven(OrderExitsStopLossBreakEven value) {
        this.breakEven = value;
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
        if (!(other instanceof OrderExitsStopLoss)) {
            return false;
        }
        OrderExitsStopLoss o = (OrderExitsStopLoss) other;
        return Objects.equals(price, o.price)
            && Objects.equals(distance, o.distance)
            && Objects.equals(percent, o.percent)
            && Objects.equals(type, o.type)
            && Objects.equals(limitOffsetTicks, o.limitOffsetTicks)
            && Objects.equals(trail, o.trail)
            && Objects.equals(breakEven, o.breakEven);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, distance, percent, type, limitOffsetTicks, trail, breakEven);
    }

    @Override
    public String toString() {
        return "OrderExitsStopLoss{" + "price=" + price + ", distance=" + distance + ", percent=" + percent + ", type=" + type + ", limit_offset_ticks=" + limitOffsetTicks + ", trail=" + trail + ", break_even=" + breakEven + "}";
    }
}
