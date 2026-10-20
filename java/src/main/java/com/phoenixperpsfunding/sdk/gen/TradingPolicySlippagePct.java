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

/** TradingPolicySlippagePct */
public final class TradingPolicySlippagePct {
    @JsonProperty("default")
    private BigDecimal defaultValue;

    @JsonProperty("min")
    private BigDecimal min;

    @JsonProperty("max")
    private BigDecimal max;

    /** The bound a market order gets when it names none, in percent. */
    public BigDecimal defaultValue() {
        return defaultValue;
    }

    /** Sets default. */
    public TradingPolicySlippagePct defaultValue(BigDecimal value) {
        this.defaultValue = value;
        return this;
    }

    /** The tightest it may ask. */
    public BigDecimal min() {
        return min;
    }

    /** Sets min. */
    public TradingPolicySlippagePct min(BigDecimal value) {
        this.min = value;
        return this;
    }

    /** The widest it may ask. */
    public BigDecimal max() {
        return max;
    }

    /** Sets max. */
    public TradingPolicySlippagePct max(BigDecimal value) {
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
        if (!(other instanceof TradingPolicySlippagePct)) {
            return false;
        }
        TradingPolicySlippagePct o = (TradingPolicySlippagePct) other;
        return Objects.equals(defaultValue, o.defaultValue)
            && Objects.equals(min, o.min)
            && Objects.equals(max, o.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultValue, min, max);
    }

    @Override
    public String toString() {
        return "TradingPolicySlippagePct{" + "default=" + defaultValue + ", min=" + min + ", max=" + max + "}";
    }
}
