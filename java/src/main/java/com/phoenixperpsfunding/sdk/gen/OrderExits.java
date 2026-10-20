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

/** Exits placed when the order fills: the stop-loss for the filled size first, then the take-profits. From then on they are the position's exits. An empty object removes them. */
public final class OrderExits {
    @JsonProperty("take_profit")
    private List<OrderExitsTakeProfit> takeProfit;

    @JsonProperty("stop_loss")
    private OrderExitsStopLoss stopLoss;

    /** Limit orders on the profit side, each at price, or at distance or percent from the fill. */
    public List<OrderExitsTakeProfit> takeProfit() {
        return takeProfit;
    }

    /** Sets take_profit. */
    public OrderExits takeProfit(List<OrderExitsTakeProfit> value) {
        this.takeProfit = value;
        return this;
    }

    /** A stop on the loss side, at price, or at distance or percent from the fill; it may trail and move to break-even (as in a position's exits). */
    public OrderExitsStopLoss stopLoss() {
        return stopLoss;
    }

    /** Sets stop_loss. */
    public OrderExits stopLoss(OrderExitsStopLoss value) {
        this.stopLoss = value;
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
        if (!(other instanceof OrderExits)) {
            return false;
        }
        OrderExits o = (OrderExits) other;
        return Objects.equals(takeProfit, o.takeProfit)
            && Objects.equals(stopLoss, o.stopLoss);
    }

    @Override
    public int hashCode() {
        return Objects.hash(takeProfit, stopLoss);
    }

    @Override
    public String toString() {
        return "OrderExits{" + "take_profit=" + takeProfit + ", stop_loss=" + stopLoss + "}";
    }
}
