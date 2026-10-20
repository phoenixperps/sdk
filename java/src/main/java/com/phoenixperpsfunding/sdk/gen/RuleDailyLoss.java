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

/** RuleDailyLoss */
public final class RuleDailyLoss {
    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("pct")
    private BigDecimal pct;

    @JsonProperty("basis")
    private RuleDailyLossBasis basis;

    @JsonProperty("on_breach")
    private OnBreach onBreach;

    /** The most the account may lose in a trading day, in USD. */
    public BigDecimal amount() {
        return amount;
    }

    /** Sets amount. */
    public RuleDailyLoss amount(BigDecimal value) {
        this.amount = value;
        return this;
    }

    /** The same limit in percent of its basis. */
    public BigDecimal pct() {
        return pct;
    }

    /** Sets pct. */
    public RuleDailyLoss pct(BigDecimal value) {
        this.pct = value;
        return this;
    }

    /** What the day's loss is measured from. */
    public RuleDailyLossBasis basis() {
        return basis;
    }

    /** Sets basis. */
    public RuleDailyLoss basis(RuleDailyLossBasis value) {
        this.basis = value;
        return this;
    }

    /** What happens when it is reached. */
    public OnBreach onBreach() {
        return onBreach;
    }

    /** Sets on_breach. */
    public RuleDailyLoss onBreach(OnBreach value) {
        this.onBreach = value;
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
        if (!(other instanceof RuleDailyLoss)) {
            return false;
        }
        RuleDailyLoss o = (RuleDailyLoss) other;
        return Objects.equals(amount, o.amount)
            && Objects.equals(pct, o.pct)
            && Objects.equals(basis, o.basis)
            && Objects.equals(onBreach, o.onBreach);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, pct, basis, onBreach);
    }

    @Override
    public String toString() {
        return "RuleDailyLoss{" + "amount=" + amount + ", pct=" + pct + ", basis=" + basis + ", on_breach=" + onBreach + "}";
    }
}
