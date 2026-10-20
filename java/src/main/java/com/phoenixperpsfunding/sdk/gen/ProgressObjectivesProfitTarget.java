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

/** ProgressObjectivesProfitTarget */
public final class ProgressObjectivesProfitTarget {
    @JsonProperty("goal_balance")
    private BigDecimal goalBalance;

    @JsonProperty("remaining")
    private BigDecimal remaining;

    @JsonProperty("progress_pct")
    private BigDecimal progressPct;

    @JsonProperty("requires_flat")
    private Boolean requiresFlat;

    @JsonProperty("status")
    private ProgressObjectivesProfitTargetStatus status;

    /** The balance that reaches it. */
    public BigDecimal goalBalance() {
        return goalBalance;
    }

    /** Sets goal_balance. */
    public ProgressObjectivesProfitTarget goalBalance(BigDecimal value) {
        this.goalBalance = value;
        return this;
    }

    /** Still to make. */
    public BigDecimal remaining() {
        return remaining;
    }

    /** Sets remaining. */
    public ProgressObjectivesProfitTarget remaining(BigDecimal value) {
        this.remaining = value;
        return this;
    }

    /** How far, in percent. */
    public BigDecimal progressPct() {
        return progressPct;
    }

    /** Sets progress_pct. */
    public ProgressObjectivesProfitTarget progressPct(BigDecimal value) {
        this.progressPct = value;
        return this;
    }

    /** Positions must be closed when it is reached. */
    public Boolean requiresFlat() {
        return requiresFlat;
    }

    /** Sets requires_flat. */
    public ProgressObjectivesProfitTarget requiresFlat(Boolean value) {
        this.requiresFlat = value;
        return this;
    }

    /** Whether it is reached. */
    public ProgressObjectivesProfitTargetStatus status() {
        return status;
    }

    /** Sets status. */
    public ProgressObjectivesProfitTarget status(ProgressObjectivesProfitTargetStatus value) {
        this.status = value;
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
        if (!(other instanceof ProgressObjectivesProfitTarget)) {
            return false;
        }
        ProgressObjectivesProfitTarget o = (ProgressObjectivesProfitTarget) other;
        return Objects.equals(goalBalance, o.goalBalance)
            && Objects.equals(remaining, o.remaining)
            && Objects.equals(progressPct, o.progressPct)
            && Objects.equals(requiresFlat, o.requiresFlat)
            && Objects.equals(status, o.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goalBalance, remaining, progressPct, requiresFlat, status);
    }

    @Override
    public String toString() {
        return "ProgressObjectivesProfitTarget{" + "goal_balance=" + goalBalance + ", remaining=" + remaining + ", progress_pct=" + progressPct + ", requires_flat=" + requiresFlat + ", status=" + status + "}";
    }
}
