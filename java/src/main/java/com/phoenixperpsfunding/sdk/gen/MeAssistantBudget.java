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

/** The AI assistant's own budget, on top of the others. */
public final class MeAssistantBudget {
    @JsonProperty("reads_per_min")
    private Long readsPerMin;

    @JsonProperty("writes_per_min")
    private Long writesPerMin;

    @JsonProperty("writes_per_hour")
    private Long writesPerHour;

    /** Reads a minute. */
    public Long readsPerMin() {
        return readsPerMin;
    }

    /** Sets reads_per_min. */
    public MeAssistantBudget readsPerMin(Long value) {
        this.readsPerMin = value;
        return this;
    }

    /** Trading calls a minute. */
    public Long writesPerMin() {
        return writesPerMin;
    }

    /** Sets writes_per_min. */
    public MeAssistantBudget writesPerMin(Long value) {
        this.writesPerMin = value;
        return this;
    }

    /** Trading calls an hour. */
    public Long writesPerHour() {
        return writesPerHour;
    }

    /** Sets writes_per_hour. */
    public MeAssistantBudget writesPerHour(Long value) {
        this.writesPerHour = value;
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
        if (!(other instanceof MeAssistantBudget)) {
            return false;
        }
        MeAssistantBudget o = (MeAssistantBudget) other;
        return Objects.equals(readsPerMin, o.readsPerMin)
            && Objects.equals(writesPerMin, o.writesPerMin)
            && Objects.equals(writesPerHour, o.writesPerHour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readsPerMin, writesPerMin, writesPerHour);
    }

    @Override
    public String toString() {
        return "MeAssistantBudget{" + "reads_per_min=" + readsPerMin + ", writes_per_min=" + writesPerMin + ", writes_per_hour=" + writesPerHour + "}";
    }
}
