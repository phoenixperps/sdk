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

/** RuleProfitTarget */
public final class RuleProfitTarget {
    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("pct")
    private BigDecimal pct;

    @JsonProperty("requires_flat")
    private Boolean requiresFlat;

    /** The profit to reach, in USD. */
    public BigDecimal amount() {
        return amount;
    }

    /** Sets amount. */
    public RuleProfitTarget amount(BigDecimal value) {
        this.amount = value;
        return this;
    }

    /** The same target in percent. */
    public BigDecimal pct() {
        return pct;
    }

    /** Sets pct. */
    public RuleProfitTarget pct(BigDecimal value) {
        this.pct = value;
        return this;
    }

    /** Positions must be closed when it is reached. */
    public Boolean requiresFlat() {
        return requiresFlat;
    }

    /** Sets requires_flat. */
    public RuleProfitTarget requiresFlat(Boolean value) {
        this.requiresFlat = value;
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
        if (!(other instanceof RuleProfitTarget)) {
            return false;
        }
        RuleProfitTarget o = (RuleProfitTarget) other;
        return Objects.equals(amount, o.amount)
            && Objects.equals(pct, o.pct)
            && Objects.equals(requiresFlat, o.requiresFlat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, pct, requiresFlat);
    }

    @Override
    public String toString() {
        return "RuleProfitTarget{" + "amount=" + amount + ", pct=" + pct + ", requires_flat=" + requiresFlat + "}";
    }
}
