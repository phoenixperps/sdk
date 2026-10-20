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

/** A trailing stop-loss: how it follows the price. */
public final class ExitsLegTrail {
    @JsonProperty("distance")
    private BigDecimal distance;

    @JsonProperty("percent")
    private BigDecimal percent;

    @JsonProperty("activation_price")
    private BigDecimal activationPrice;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("best_price")
    private BigDecimal bestPrice;

    /** How far behind the best price (or null: percent). */
    public BigDecimal distance() {
        return distance;
    }

    /** Sets distance. */
    public ExitsLegTrail distance(BigDecimal value) {
        this.distance = value;
        return this;
    }

    /** How far behind, in percent of the best price (or null: distance). */
    public BigDecimal percent() {
        return percent;
    }

    /** Sets percent. */
    public ExitsLegTrail percent(BigDecimal value) {
        this.percent = value;
        return this;
    }

    /** It starts once the price reaches this; null: from the start. */
    public BigDecimal activationPrice() {
        return activationPrice;
    }

    /** Sets activation_price. */
    public ExitsLegTrail activationPrice(BigDecimal value) {
        this.activationPrice = value;
        return this;
    }

    /** It follows the price now (its activation price was reached, or it had none). */
    public Boolean active() {
        return active;
    }

    /** Sets active. */
    public ExitsLegTrail active(Boolean value) {
        this.active = value;
        return this;
    }

    /** The best price since it started, which the stop follows. */
    public BigDecimal bestPrice() {
        return bestPrice;
    }

    /** Sets best_price. */
    public ExitsLegTrail bestPrice(BigDecimal value) {
        this.bestPrice = value;
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
        if (!(other instanceof ExitsLegTrail)) {
            return false;
        }
        ExitsLegTrail o = (ExitsLegTrail) other;
        return Objects.equals(distance, o.distance)
            && Objects.equals(percent, o.percent)
            && Objects.equals(activationPrice, o.activationPrice)
            && Objects.equals(active, o.active)
            && Objects.equals(bestPrice, o.bestPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, percent, activationPrice, active, bestPrice);
    }

    @Override
    public String toString() {
        return "ExitsLegTrail{" + "distance=" + distance + ", percent=" + percent + ", activation_price=" + activationPrice + ", active=" + active + ", best_price=" + bestPrice + "}";
    }
}
