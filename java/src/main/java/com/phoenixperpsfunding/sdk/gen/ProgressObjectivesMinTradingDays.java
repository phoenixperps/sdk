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

/** ProgressObjectivesMinTradingDays */
public final class ProgressObjectivesMinTradingDays {
    @JsonProperty("required")
    private Long required;

    @JsonProperty("count")
    private Long count;

    @JsonProperty("status")
    private ProgressObjectivesProfitTargetStatus status;

    /** Days the rule needs. */
    public Long required() {
        return required;
    }

    /** Sets required. */
    public ProgressObjectivesMinTradingDays required(Long value) {
        this.required = value;
        return this;
    }

    /** Days with a trade so far. */
    public Long count() {
        return count;
    }

    /** Sets count. */
    public ProgressObjectivesMinTradingDays count(Long value) {
        this.count = value;
        return this;
    }

    /** Whether it is reached. */
    public ProgressObjectivesProfitTargetStatus status() {
        return status;
    }

    /** Sets status. */
    public ProgressObjectivesMinTradingDays status(ProgressObjectivesProfitTargetStatus value) {
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
        if (!(other instanceof ProgressObjectivesMinTradingDays)) {
            return false;
        }
        ProgressObjectivesMinTradingDays o = (ProgressObjectivesMinTradingDays) other;
        return Objects.equals(required, o.required)
            && Objects.equals(count, o.count)
            && Objects.equals(status, o.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(required, count, status);
    }

    @Override
    public String toString() {
        return "ProgressObjectivesMinTradingDays{" + "required=" + required + ", count=" + count + ", status=" + status + "}";
    }
}
