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

/** AccountRiskOrNullExposure */
public final class AccountRiskOrNullExposure {
    @JsonProperty("gross")
    private BigDecimal gross;

    @JsonProperty("long")
    private BigDecimal longValue;

    @JsonProperty("short")
    private BigDecimal shortValue;

    @JsonProperty("effective_leverage")
    private BigDecimal effectiveLeverage;

    @JsonProperty("markets")
    private List<AccountRiskOrNullExposureMarket> markets;

    /** The value of every open position. */
    public BigDecimal gross() {
        return gross;
    }

    /** Sets gross. */
    public AccountRiskOrNullExposure gross(BigDecimal value) {
        this.gross = value;
        return this;
    }

    /** The value of the long ones. */
    public BigDecimal longValue() {
        return longValue;
    }

    /** Sets long. */
    public AccountRiskOrNullExposure longValue(BigDecimal value) {
        this.longValue = value;
        return this;
    }

    /** The value of the short ones. */
    public BigDecimal shortValue() {
        return shortValue;
    }

    /** Sets short. */
    public AccountRiskOrNullExposure shortValue(BigDecimal value) {
        this.shortValue = value;
        return this;
    }

    /** Gross exposure over equity. */
    public BigDecimal effectiveLeverage() {
        return effectiveLeverage;
    }

    /** Sets effective_leverage. */
    public AccountRiskOrNullExposure effectiveLeverage(BigDecimal value) {
        this.effectiveLeverage = value;
        return this;
    }

    /** Per market, largest first. */
    public List<AccountRiskOrNullExposureMarket> markets() {
        return markets;
    }

    /** Sets markets. */
    public AccountRiskOrNullExposure markets(List<AccountRiskOrNullExposureMarket> value) {
        this.markets = value;
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
        if (!(other instanceof AccountRiskOrNullExposure)) {
            return false;
        }
        AccountRiskOrNullExposure o = (AccountRiskOrNullExposure) other;
        return Objects.equals(gross, o.gross)
            && Objects.equals(longValue, o.longValue)
            && Objects.equals(shortValue, o.shortValue)
            && Objects.equals(effectiveLeverage, o.effectiveLeverage)
            && Objects.equals(markets, o.markets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gross, longValue, shortValue, effectiveLeverage, markets);
    }

    @Override
    public String toString() {
        return "AccountRiskOrNullExposure{" + "gross=" + gross + ", long=" + longValue + ", short=" + shortValue + ", effective_leverage=" + effectiveLeverage + ", markets=" + markets + "}";
    }
}
