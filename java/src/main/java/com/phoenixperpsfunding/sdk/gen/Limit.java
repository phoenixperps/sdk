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

/** A loss limit and the room left to it. */
public final class Limit {
    @JsonProperty("limit")
    private BigDecimal limit;

    @JsonProperty("floor")
    private BigDecimal floor;

    @JsonProperty("room")
    private BigDecimal room;

    @JsonProperty("used_pct")
    private BigDecimal usedPct;

    @JsonProperty("resets_at")
    private Long resetsAt;

    @JsonProperty("on_breach")
    private OnBreach onBreach;

    @JsonProperty("estimated")
    private Boolean estimated;

    @JsonProperty("mode")
    private RuleMaxDrawdownMode mode;

    @JsonProperty("at_least")
    private Boolean atLeast;

    @JsonProperty("lifted")
    private Boolean lifted;

    /** The limit, in USD. */
    public BigDecimal limit() {
        return limit;
    }

    /** Sets limit. */
    public Limit limit(BigDecimal value) {
        this.limit = value;
        return this;
    }

    /** The equity at which it is breached. */
    public BigDecimal floor() {
        return floor;
    }

    /** Sets floor. */
    public Limit floor(BigDecimal value) {
        this.floor = value;
        return this;
    }

    /** How much more the account may lose before it. */
    public BigDecimal room() {
        return room;
    }

    /** Sets room. */
    public Limit room(BigDecimal value) {
        this.room = value;
        return this;
    }

    /** How much of it is used, in percent. */
    public BigDecimal usedPct() {
        return usedPct;
    }

    /** Sets used_pct. */
    public Limit usedPct(BigDecimal value) {
        this.usedPct = value;
        return this;
    }

    /** Daily loss only: when it resets (midnight New York). */
    public Long resetsAt() {
        return resetsAt;
    }

    /** Sets resets_at. */
    public Limit resetsAt(Long value) {
        this.resetsAt = value;
        return this;
    }

    /** Daily loss only: what happens when it is reached. */
    public OnBreach onBreach() {
        return onBreach;
    }

    /** Sets on_breach. */
    public Limit onBreach(OnBreach value) {
        this.onBreach = value;
        return this;
    }

    /** Daily loss only: the day's start is estimated (the engine does not publish it). */
    public Boolean estimated() {
        return estimated;
    }

    /** Sets estimated. */
    public Limit estimated(Boolean value) {
        this.estimated = value;
        return this;
    }

    /** Max drawdown only: how the reference moves. */
    public RuleMaxDrawdownMode mode() {
        return mode;
    }

    /** Sets mode. */
    public Limit mode(RuleMaxDrawdownMode value) {
        this.mode = value;
        return this;
    }

    /** Max drawdown only: the engine may hold a higher floor than shown (it follows equity highs). */
    public Boolean atLeast() {
        return atLeast;
    }

    /** Sets at_least. */
    public Limit atLeast(Boolean value) {
        this.atLeast = value;
        return this;
    }

    /** The rule no longer applies this limit (its cut-off was reached). */
    public Boolean lifted() {
        return lifted;
    }

    /** Sets lifted. */
    public Limit lifted(Boolean value) {
        this.lifted = value;
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
        if (!(other instanceof Limit)) {
            return false;
        }
        Limit o = (Limit) other;
        return Objects.equals(limit, o.limit)
            && Objects.equals(floor, o.floor)
            && Objects.equals(room, o.room)
            && Objects.equals(usedPct, o.usedPct)
            && Objects.equals(resetsAt, o.resetsAt)
            && Objects.equals(onBreach, o.onBreach)
            && Objects.equals(estimated, o.estimated)
            && Objects.equals(mode, o.mode)
            && Objects.equals(atLeast, o.atLeast)
            && Objects.equals(lifted, o.lifted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, floor, room, usedPct, resetsAt, onBreach, estimated, mode, atLeast, lifted);
    }

    @Override
    public String toString() {
        return "Limit{" + "limit=" + limit + ", floor=" + floor + ", room=" + room + ", used_pct=" + usedPct + ", resets_at=" + resetsAt + ", on_breach=" + onBreach + ", estimated=" + estimated + ", mode=" + mode + ", at_least=" + atLeast + ", lifted=" + lifted + "}";
    }
}
