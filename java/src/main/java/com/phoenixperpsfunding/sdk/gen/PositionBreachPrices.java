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

/** PositionBreachPrices */
public final class PositionBreachPrices {
    @JsonProperty("daily_loss")
    private BigDecimal dailyLoss;

    @JsonProperty("max_drawdown")
    private BigDecimal maxDrawdown;

    /** The price at which this position alone would breach the daily loss limit. */
    public BigDecimal dailyLoss() {
        return dailyLoss;
    }

    /** Sets daily_loss. */
    public PositionBreachPrices dailyLoss(BigDecimal value) {
        this.dailyLoss = value;
        return this;
    }

    /** The price at which it alone would breach the max drawdown. */
    public BigDecimal maxDrawdown() {
        return maxDrawdown;
    }

    /** Sets max_drawdown. */
    public PositionBreachPrices maxDrawdown(BigDecimal value) {
        this.maxDrawdown = value;
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
        if (!(other instanceof PositionBreachPrices)) {
            return false;
        }
        PositionBreachPrices o = (PositionBreachPrices) other;
        return Objects.equals(dailyLoss, o.dailyLoss)
            && Objects.equals(maxDrawdown, o.maxDrawdown);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyLoss, maxDrawdown);
    }

    @Override
    public String toString() {
        return "PositionBreachPrices{" + "daily_loss=" + dailyLoss + ", max_drawdown=" + maxDrawdown + "}";
    }
}
