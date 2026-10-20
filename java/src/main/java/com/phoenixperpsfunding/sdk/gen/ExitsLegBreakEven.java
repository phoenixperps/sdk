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

/** A stop-loss moved to the entry price once the price has gone your way far enough. */
public final class ExitsLegBreakEven {
    @JsonProperty("trigger_distance")
    private BigDecimal triggerDistance;

    @JsonProperty("trigger_percent")
    private BigDecimal triggerPercent;

    @JsonProperty("trigger_price")
    private BigDecimal triggerPrice;

    @JsonProperty("offset_ticks")
    private Long offsetTicks;

    @JsonProperty("done")
    private Boolean done;

    /** How far from the entry the price must go (or null: another trigger). */
    public BigDecimal triggerDistance() {
        return triggerDistance;
    }

    /** Sets trigger_distance. */
    public ExitsLegBreakEven triggerDistance(BigDecimal value) {
        this.triggerDistance = value;
        return this;
    }

    /** The same, in percent of the entry. */
    public BigDecimal triggerPercent() {
        return triggerPercent;
    }

    /** Sets trigger_percent. */
    public ExitsLegBreakEven triggerPercent(BigDecimal value) {
        this.triggerPercent = value;
        return this;
    }

    /** Or once the price reaches this. */
    public BigDecimal triggerPrice() {
        return triggerPrice;
    }

    /** Sets trigger_price. */
    public ExitsLegBreakEven triggerPrice(BigDecimal value) {
        this.triggerPrice = value;
        return this;
    }

    /** Ticks beyond the entry, on the profit side. */
    public Long offsetTicks() {
        return offsetTicks;
    }

    /** Sets offset_ticks. */
    public ExitsLegBreakEven offsetTicks(Long value) {
        this.offsetTicks = value;
        return this;
    }

    /** The stop-loss was moved to the entry already. */
    public Boolean done() {
        return done;
    }

    /** Sets done. */
    public ExitsLegBreakEven done(Boolean value) {
        this.done = value;
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
        if (!(other instanceof ExitsLegBreakEven)) {
            return false;
        }
        ExitsLegBreakEven o = (ExitsLegBreakEven) other;
        return Objects.equals(triggerDistance, o.triggerDistance)
            && Objects.equals(triggerPercent, o.triggerPercent)
            && Objects.equals(triggerPrice, o.triggerPrice)
            && Objects.equals(offsetTicks, o.offsetTicks)
            && Objects.equals(done, o.done);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triggerDistance, triggerPercent, triggerPrice, offsetTicks, done);
    }

    @Override
    public String toString() {
        return "ExitsLegBreakEven{" + "trigger_distance=" + triggerDistance + ", trigger_percent=" + triggerPercent + ", trigger_price=" + triggerPrice + ", offset_ticks=" + offsetTicks + ", done=" + done + "}";
    }
}
