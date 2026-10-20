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

/** ProgressObjectives */
public final class ProgressObjectives {
    @JsonProperty("profit_target")
    private ProgressObjectivesProfitTarget profitTarget;

    @JsonProperty("min_trading_days")
    private ProgressObjectivesMinTradingDays minTradingDays;

    @JsonProperty("min_calendar_days")
    private ProgressObjectivesMinCalendarDays minCalendarDays;

    /** The profit target. */
    public ProgressObjectivesProfitTarget profitTarget() {
        return profitTarget;
    }

    /** Sets profit_target. */
    public ProgressObjectives profitTarget(ProgressObjectivesProfitTarget value) {
        this.profitTarget = value;
        return this;
    }

    /** The min trading days. */
    public ProgressObjectivesMinTradingDays minTradingDays() {
        return minTradingDays;
    }

    /** Sets min_trading_days. */
    public ProgressObjectives minTradingDays(ProgressObjectivesMinTradingDays value) {
        this.minTradingDays = value;
        return this;
    }

    /** The min calendar days. */
    public ProgressObjectivesMinCalendarDays minCalendarDays() {
        return minCalendarDays;
    }

    /** Sets min_calendar_days. */
    public ProgressObjectives minCalendarDays(ProgressObjectivesMinCalendarDays value) {
        this.minCalendarDays = value;
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
        if (!(other instanceof ProgressObjectives)) {
            return false;
        }
        ProgressObjectives o = (ProgressObjectives) other;
        return Objects.equals(profitTarget, o.profitTarget)
            && Objects.equals(minTradingDays, o.minTradingDays)
            && Objects.equals(minCalendarDays, o.minCalendarDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profitTarget, minTradingDays, minCalendarDays);
    }

    @Override
    public String toString() {
        return "ProgressObjectives{" + "profit_target=" + profitTarget + ", min_trading_days=" + minTradingDays + ", min_calendar_days=" + minCalendarDays + "}";
    }
}
