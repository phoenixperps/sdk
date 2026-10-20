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

/** AccountRiskOrNullExposureMarket */
public final class AccountRiskOrNullExposureMarket {
    @JsonProperty("market")
    private String market;

    @JsonProperty("notional")
    private BigDecimal notional;

    @JsonProperty("leverage")
    private BigDecimal leverage;

    @JsonProperty("cap")
    private BigDecimal cap;

    @JsonProperty("cap_used_pct")
    private BigDecimal capUsedPct;

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public AccountRiskOrNullExposureMarket market(String value) {
        this.market = value;
        return this;
    }

    /** The value held on it. */
    public BigDecimal notional() {
        return notional;
    }

    /** Sets notional. */
    public AccountRiskOrNullExposureMarket notional(BigDecimal value) {
        this.notional = value;
        return this;
    }

    /** That value over equity. */
    public BigDecimal leverage() {
        return leverage;
    }

    /** Sets leverage. */
    public AccountRiskOrNullExposureMarket leverage(BigDecimal value) {
        this.leverage = value;
        return this;
    }

    /** The exposure cap of the market. */
    public BigDecimal cap() {
        return cap;
    }

    /** Sets cap. */
    public AccountRiskOrNullExposureMarket cap(BigDecimal value) {
        this.cap = value;
        return this;
    }

    /** How much of the cap is used, counting your other accounts on the same rule, in percent. */
    public BigDecimal capUsedPct() {
        return capUsedPct;
    }

    /** Sets cap_used_pct. */
    public AccountRiskOrNullExposureMarket capUsedPct(BigDecimal value) {
        this.capUsedPct = value;
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
        if (!(other instanceof AccountRiskOrNullExposureMarket)) {
            return false;
        }
        AccountRiskOrNullExposureMarket o = (AccountRiskOrNullExposureMarket) other;
        return Objects.equals(market, o.market)
            && Objects.equals(notional, o.notional)
            && Objects.equals(leverage, o.leverage)
            && Objects.equals(cap, o.cap)
            && Objects.equals(capUsedPct, o.capUsedPct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, notional, leverage, cap, capUsedPct);
    }

    @Override
    public String toString() {
        return "AccountRiskOrNullExposureMarket{" + "market=" + market + ", notional=" + notional + ", leverage=" + leverage + ", cap=" + cap + ", cap_used_pct=" + capUsedPct + "}";
    }
}
