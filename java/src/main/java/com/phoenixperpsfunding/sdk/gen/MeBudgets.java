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

/** MeBudgets */
public final class MeBudgets {
    @JsonProperty("read_per_min")
    private Long readPerMin;

    @JsonProperty("trade_per_min")
    private Long tradePerMin;

    /** Reads a minute for this key. */
    public Long readPerMin() {
        return readPerMin;
    }

    /** Sets read_per_min. */
    public MeBudgets readPerMin(Long value) {
        this.readPerMin = value;
        return this;
    }

    /** Trading calls a minute for this key. */
    public Long tradePerMin() {
        return tradePerMin;
    }

    /** Sets trade_per_min. */
    public MeBudgets tradePerMin(Long value) {
        this.tradePerMin = value;
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
        if (!(other instanceof MeBudgets)) {
            return false;
        }
        MeBudgets o = (MeBudgets) other;
        return Objects.equals(readPerMin, o.readPerMin)
            && Objects.equals(tradePerMin, o.tradePerMin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readPerMin, tradePerMin);
    }

    @Override
    public String toString() {
        return "MeBudgets{" + "read_per_min=" + readPerMin + ", trade_per_min=" + tradePerMin + "}";
    }
}
