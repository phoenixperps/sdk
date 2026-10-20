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

/** OrderExitsTakeProfit */
public final class OrderExitsTakeProfit {
    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("distance")
    private BigDecimal distance;

    @JsonProperty("percent")
    private BigDecimal percent;

    @JsonProperty("size_pct")
    private BigDecimal sizePct;

    /** An exact price. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public OrderExitsTakeProfit price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** A price difference from the fill price. */
    public BigDecimal distance() {
        return distance;
    }

    /** Sets distance. */
    public OrderExitsTakeProfit distance(BigDecimal value) {
        this.distance = value;
        return this;
    }

    /** A percent of the fill price. */
    public BigDecimal percent() {
        return percent;
    }

    /** Sets percent. */
    public OrderExitsTakeProfit percent(BigDecimal value) {
        this.percent = value;
        return this;
    }

    /** Its share of the filled size, in percent (absent: the take-profits split it evenly). */
    public BigDecimal sizePct() {
        return sizePct;
    }

    /** Sets size_pct. */
    public OrderExitsTakeProfit sizePct(BigDecimal value) {
        this.sizePct = value;
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
        if (!(other instanceof OrderExitsTakeProfit)) {
            return false;
        }
        OrderExitsTakeProfit o = (OrderExitsTakeProfit) other;
        return Objects.equals(price, o.price)
            && Objects.equals(distance, o.distance)
            && Objects.equals(percent, o.percent)
            && Objects.equals(sizePct, o.sizePct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, distance, percent, sizePct);
    }

    @Override
    public String toString() {
        return "OrderExitsTakeProfit{" + "price=" + price + ", distance=" + distance + ", percent=" + percent + ", size_pct=" + sizePct + "}";
    }
}
