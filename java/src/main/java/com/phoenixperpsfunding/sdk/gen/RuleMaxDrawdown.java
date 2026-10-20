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

/** RuleMaxDrawdown */
public final class RuleMaxDrawdown {
    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("pct")
    private BigDecimal pct;

    @JsonProperty("mode")
    private RuleMaxDrawdownMode mode;

    /** The most the account may fall from its reference, in USD. */
    public BigDecimal amount() {
        return amount;
    }

    /** Sets amount. */
    public RuleMaxDrawdown amount(BigDecimal value) {
        this.amount = value;
        return this;
    }

    /** The same limit in percent. */
    public BigDecimal pct() {
        return pct;
    }

    /** Sets pct. */
    public RuleMaxDrawdown pct(BigDecimal value) {
        this.pct = value;
        return this;
    }

    /** How the reference moves: end_of_day and end_of_trade follow the highest balance, static stays at the start, intraday_equity follows the highest equity. */
    public RuleMaxDrawdownMode mode() {
        return mode;
    }

    /** Sets mode. */
    public RuleMaxDrawdown mode(RuleMaxDrawdownMode value) {
        this.mode = value;
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
        if (!(other instanceof RuleMaxDrawdown)) {
            return false;
        }
        RuleMaxDrawdown o = (RuleMaxDrawdown) other;
        return Objects.equals(amount, o.amount)
            && Objects.equals(pct, o.pct)
            && Objects.equals(mode, o.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, pct, mode);
    }

    @Override
    public String toString() {
        return "RuleMaxDrawdown{" + "amount=" + amount + ", pct=" + pct + ", mode=" + mode + "}";
    }
}
