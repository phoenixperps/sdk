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

/** The limits you set on the key. */
public final class MeControls {
    @JsonProperty("markets")
    private List<String> markets;

    @JsonProperty("reduce_only")
    private Boolean reduceOnly;

    @JsonProperty("max_order_usd")
    private BigDecimal maxOrderUsd;

    @JsonProperty("max_position_usd")
    private BigDecimal maxPositionUsd;

    @JsonProperty("daily_loss_usd")
    private BigDecimal dailyLossUsd;

    /** The markets the key may trade; empty: every market. */
    public List<String> markets() {
        return markets;
    }

    /** Sets markets. */
    public MeControls markets(List<String> value) {
        this.markets = value;
        return this;
    }

    /** The key may only reduce positions. */
    public Boolean reduceOnly() {
        return reduceOnly;
    }

    /** Sets reduce_only. */
    public MeControls reduceOnly(Boolean value) {
        this.reduceOnly = value;
        return this;
    }

    /** The largest order the key may place, in USD. */
    public BigDecimal maxOrderUsd() {
        return maxOrderUsd;
    }

    /** Sets max_order_usd. */
    public MeControls maxOrderUsd(BigDecimal value) {
        this.maxOrderUsd = value;
        return this;
    }

    /** The largest position the key may hold, in USD. */
    public BigDecimal maxPositionUsd() {
        return maxPositionUsd;
    }

    /** Sets max_position_usd. */
    public MeControls maxPositionUsd(BigDecimal value) {
        this.maxPositionUsd = value;
        return this;
    }

    /** The loss in a trading day at which the key stops trading the account. */
    public BigDecimal dailyLossUsd() {
        return dailyLossUsd;
    }

    /** Sets daily_loss_usd. */
    public MeControls dailyLossUsd(BigDecimal value) {
        this.dailyLossUsd = value;
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
        if (!(other instanceof MeControls)) {
            return false;
        }
        MeControls o = (MeControls) other;
        return Objects.equals(markets, o.markets)
            && Objects.equals(reduceOnly, o.reduceOnly)
            && Objects.equals(maxOrderUsd, o.maxOrderUsd)
            && Objects.equals(maxPositionUsd, o.maxPositionUsd)
            && Objects.equals(dailyLossUsd, o.dailyLossUsd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(markets, reduceOnly, maxOrderUsd, maxPositionUsd, dailyLossUsd);
    }

    @Override
    public String toString() {
        return "MeControls{" + "markets=" + markets + ", reduce_only=" + reduceOnly + ", max_order_usd=" + maxOrderUsd + ", max_position_usd=" + maxPositionUsd + ", daily_loss_usd=" + dailyLossUsd + "}";
    }
}
