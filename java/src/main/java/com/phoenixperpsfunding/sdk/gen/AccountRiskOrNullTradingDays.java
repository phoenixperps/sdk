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

/** AccountRiskOrNullTradingDays */
public final class AccountRiskOrNullTradingDays {
    @JsonProperty("count")
    private Long count;

    @JsonProperty("required")
    private Long required;

    /** Days with a trade so far. */
    public Long count() {
        return count;
    }

    /** Sets count. */
    public AccountRiskOrNullTradingDays count(Long value) {
        this.count = value;
        return this;
    }

    /** Days the rule needs. */
    public Long required() {
        return required;
    }

    /** Sets required. */
    public AccountRiskOrNullTradingDays required(Long value) {
        this.required = value;
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
        if (!(other instanceof AccountRiskOrNullTradingDays)) {
            return false;
        }
        AccountRiskOrNullTradingDays o = (AccountRiskOrNullTradingDays) other;
        return Objects.equals(count, o.count)
            && Objects.equals(required, o.required);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, required);
    }

    @Override
    public String toString() {
        return "AccountRiskOrNullTradingDays{" + "count=" + count + ", required=" + required + "}";
    }
}
