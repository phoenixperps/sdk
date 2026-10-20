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

/** PositionExits */
public final class PositionExits {
    @JsonProperty("take_profit")
    private List<ExitLeg> takeProfit;

    @JsonProperty("stop_loss")
    private List<ExitLeg> stopLoss;

    /** Limit orders on the closing side. */
    public List<ExitLeg> takeProfit() {
        return takeProfit;
    }

    /** Sets take_profit. */
    public PositionExits takeProfit(List<ExitLeg> value) {
        this.takeProfit = value;
        return this;
    }

    /** Stop orders on the closing side. */
    public List<ExitLeg> stopLoss() {
        return stopLoss;
    }

    /** Sets stop_loss. */
    public PositionExits stopLoss(List<ExitLeg> value) {
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
        if (!(other instanceof PositionExits)) {
            return false;
        }
        PositionExits o = (PositionExits) other;
        return Objects.equals(takeProfit, o.takeProfit)
            && Objects.equals(stopLoss, o.stopLoss);
    }

    @Override
    public int hashCode() {
        return Objects.hash(takeProfit, stopLoss);
    }

    @Override
    public String toString() {
        return "PositionExits{" + "take_profit=" + takeProfit + ", stop_loss=" + stopLoss + "}";
    }
}
