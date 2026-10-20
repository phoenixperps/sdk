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

/** LimitsPageLimit */
public final class LimitsPageLimit {
    @JsonProperty("default")
    private Long defaultValue;

    @JsonProperty("max")
    private Long max;

    /** Items a page holds by default. */
    public Long defaultValue() {
        return defaultValue;
    }

    /** Sets default. */
    public LimitsPageLimit defaultValue(Long value) {
        this.defaultValue = value;
        return this;
    }

    /** The most a page may hold. */
    public Long max() {
        return max;
    }

    /** Sets max. */
    public LimitsPageLimit max(Long value) {
        this.max = value;
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
        if (!(other instanceof LimitsPageLimit)) {
            return false;
        }
        LimitsPageLimit o = (LimitsPageLimit) other;
        return Objects.equals(defaultValue, o.defaultValue)
            && Objects.equals(max, o.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultValue, max);
    }

    @Override
    public String toString() {
        return "LimitsPageLimit{" + "default=" + defaultValue + ", max=" + max + "}";
    }
}
