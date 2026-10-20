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

/** As a position's exits take it: { distance } or { percent }, with activation_price to start only once the price reaches it. */
public final class OrderExitsStopLossTrail {
    @JsonProperty("distance")
    private BigDecimal distance;

    @JsonProperty("percent")
    private BigDecimal percent;

    @JsonProperty("activation_price")
    private BigDecimal activationPrice;

    /** How far behind the best price. */
    public BigDecimal distance() {
        return distance;
    }

    /** Sets distance. */
    public OrderExitsStopLossTrail distance(BigDecimal value) {
        this.distance = value;
        return this;
    }

    /** How far behind, in percent of the best price. */
    public BigDecimal percent() {
        return percent;
    }

    /** Sets percent. */
    public OrderExitsStopLossTrail percent(BigDecimal value) {
        this.percent = value;
        return this;
    }

    /** Start once the price reaches this. */
    public BigDecimal activationPrice() {
        return activationPrice;
    }

    /** Sets activation_price. */
    public OrderExitsStopLossTrail activationPrice(BigDecimal value) {
        this.activationPrice = value;
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
        if (!(other instanceof OrderExitsStopLossTrail)) {
            return false;
        }
        OrderExitsStopLossTrail o = (OrderExitsStopLossTrail) other;
        return Objects.equals(distance, o.distance)
            && Objects.equals(percent, o.percent)
            && Objects.equals(activationPrice, o.activationPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, percent, activationPrice);
    }

    @Override
    public String toString() {
        return "OrderExitsStopLossTrail{" + "distance=" + distance + ", percent=" + percent + ", activation_price=" + activationPrice + "}";
    }
}
