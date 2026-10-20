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

/** Progress */
public final class Progress {
    @JsonProperty("stage")
    private AccountStage stage;

    @JsonProperty("objectives")
    private ProgressObjectives objectives;

    @JsonProperty("limits")
    private ProgressLimits limits;

    @JsonProperty("payout")
    private ProgressPayout payout;

    @JsonProperty("as_of")
    private Long asOf;

    /** Where the account is in the programme. */
    public AccountStage stage() {
        return stage;
    }

    /** Sets stage. */
    public Progress stage(AccountStage value) {
        this.stage = value;
        return this;
    }

    /** The objectives. */
    public ProgressObjectives objectives() {
        return objectives;
    }

    /** Sets objectives. */
    public Progress objectives(ProgressObjectives value) {
        this.objectives = value;
        return this;
    }

    /** The limits. */
    public ProgressLimits limits() {
        return limits;
    }

    /** Sets limits. */
    public Progress limits(ProgressLimits value) {
        this.limits = value;
        return this;
    }

    /** The payout. */
    public ProgressPayout payout() {
        return payout;
    }

    /** Sets payout. */
    public Progress payout(ProgressPayout value) {
        this.payout = value;
        return this;
    }

    /** When the oldest figure here was read. */
    public Long asOf() {
        return asOf;
    }

    /** Sets as_of. */
    public Progress asOf(Long value) {
        this.asOf = value;
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
        if (!(other instanceof Progress)) {
            return false;
        }
        Progress o = (Progress) other;
        return Objects.equals(stage, o.stage)
            && Objects.equals(objectives, o.objectives)
            && Objects.equals(limits, o.limits)
            && Objects.equals(payout, o.payout)
            && Objects.equals(asOf, o.asOf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stage, objectives, limits, payout, asOf);
    }

    @Override
    public String toString() {
        return "Progress{" + "stage=" + stage + ", objectives=" + objectives + ", limits=" + limits + ", payout=" + payout + ", as_of=" + asOf + "}";
    }
}
