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

/** RuleConsistency */
public final class RuleConsistency {
    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("pct")
    private BigDecimal pct;

    /** The most one day may bring, in USD. */
    public BigDecimal amount() {
        return amount;
    }

    /** Sets amount. */
    public RuleConsistency amount(BigDecimal value) {
        this.amount = value;
        return this;
    }

    /** The most one day may bring, in percent of the total. */
    public BigDecimal pct() {
        return pct;
    }

    /** Sets pct. */
    public RuleConsistency pct(BigDecimal value) {
        this.pct = value;
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
        if (!(other instanceof RuleConsistency)) {
            return false;
        }
        RuleConsistency o = (RuleConsistency) other;
        return Objects.equals(amount, o.amount)
            && Objects.equals(pct, o.pct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, pct);
    }

    @Override
    public String toString() {
        return "RuleConsistency{" + "amount=" + amount + ", pct=" + pct + "}";
    }
}
