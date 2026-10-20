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

/** ProgressLimits */
public final class ProgressLimits {
    @JsonProperty("daily_loss")
    private LimitStanding dailyLoss;

    @JsonProperty("max_drawdown")
    private LimitStanding maxDrawdown;

    /** The daily loss. */
    public LimitStanding dailyLoss() {
        return dailyLoss;
    }

    /** Sets daily_loss. */
    public ProgressLimits dailyLoss(LimitStanding value) {
        this.dailyLoss = value;
        return this;
    }

    /** The max drawdown. */
    public LimitStanding maxDrawdown() {
        return maxDrawdown;
    }

    /** Sets max_drawdown. */
    public ProgressLimits maxDrawdown(LimitStanding value) {
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
        if (!(other instanceof ProgressLimits)) {
            return false;
        }
        ProgressLimits o = (ProgressLimits) other;
        return Objects.equals(dailyLoss, o.dailyLoss)
            && Objects.equals(maxDrawdown, o.maxDrawdown);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dailyLoss, maxDrawdown);
    }

    @Override
    public String toString() {
        return "ProgressLimits{" + "daily_loss=" + dailyLoss + ", max_drawdown=" + maxDrawdown + "}";
    }
}
