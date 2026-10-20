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

/** Move the stop-loss to the entry price (plus offset_ticks) once the price has gone your way by trigger_distance, trigger_percent or to trigger_price. Without a price, the stop is placed only then. null removes it. */
public final class ExitsBatchOperationBreakEven {
    @JsonProperty("trigger_distance")
    private BigDecimal triggerDistance;

    @JsonProperty("trigger_percent")
    private BigDecimal triggerPercent;

    @JsonProperty("trigger_price")
    private BigDecimal triggerPrice;

    @JsonProperty("offset_ticks")
    private Long offsetTicks;

    /** How far from the entry. */
    public BigDecimal triggerDistance() {
        return triggerDistance;
    }

    /** Sets trigger_distance. */
    public ExitsBatchOperationBreakEven triggerDistance(BigDecimal value) {
        this.triggerDistance = value;
        return this;
    }

    /** How far from the entry, in percent. */
    public BigDecimal triggerPercent() {
        return triggerPercent;
    }

    /** Sets trigger_percent. */
    public ExitsBatchOperationBreakEven triggerPercent(BigDecimal value) {
        this.triggerPercent = value;
        return this;
    }

    /** Or this price. */
    public BigDecimal triggerPrice() {
        return triggerPrice;
    }

    /** Sets trigger_price. */
    public ExitsBatchOperationBreakEven triggerPrice(BigDecimal value) {
        this.triggerPrice = value;
        return this;
    }

    /** Ticks beyond the entry, on the profit side. */
    public Long offsetTicks() {
        return offsetTicks;
    }

    /** Sets offset_ticks. */
    public ExitsBatchOperationBreakEven offsetTicks(Long value) {
        this.offsetTicks = value;
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
        if (!(other instanceof ExitsBatchOperationBreakEven)) {
            return false;
        }
        ExitsBatchOperationBreakEven o = (ExitsBatchOperationBreakEven) other;
        return Objects.equals(triggerDistance, o.triggerDistance)
            && Objects.equals(triggerPercent, o.triggerPercent)
            && Objects.equals(triggerPrice, o.triggerPrice)
            && Objects.equals(offsetTicks, o.offsetTicks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triggerDistance, triggerPercent, triggerPrice, offsetTicks);
    }

    @Override
    public String toString() {
        return "ExitsBatchOperationBreakEven{" + "trigger_distance=" + triggerDistance + ", trigger_percent=" + triggerPercent + ", trigger_price=" + triggerPrice + ", offset_ticks=" + offsetTicks + "}";
    }
}
