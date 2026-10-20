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

/** LimitsRatePerAccountTrade */
public final class LimitsRatePerAccountTrade {
    @JsonProperty("per_min")
    private Long perMin;

    @JsonProperty("burst")
    private Long burst;

    /** Requests a minute. */
    public Long perMin() {
        return perMin;
    }

    /** Sets per_min. */
    public LimitsRatePerAccountTrade perMin(Long value) {
        this.perMin = value;
        return this;
    }

    /** Requests at once. */
    public Long burst() {
        return burst;
    }

    /** Sets burst. */
    public LimitsRatePerAccountTrade burst(Long value) {
        this.burst = value;
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
        if (!(other instanceof LimitsRatePerAccountTrade)) {
            return false;
        }
        LimitsRatePerAccountTrade o = (LimitsRatePerAccountTrade) other;
        return Objects.equals(perMin, o.perMin)
            && Objects.equals(burst, o.burst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(perMin, burst);
    }

    @Override
    public String toString() {
        return "LimitsRatePerAccountTrade{" + "per_min=" + perMin + ", burst=" + burst + "}";
    }
}
