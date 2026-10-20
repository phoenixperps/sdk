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

/** TradingPolicyOrders */
public final class TradingPolicyOrders {
    @JsonProperty("max_working_per_account")
    private Long maxWorkingPerAccount;

    @JsonProperty("new_per_min_account")
    private Long newPerMinAccount;

    @JsonProperty("price_band_pct")
    private BigDecimal priceBandPct;

    @JsonProperty("quote_max_age_s")
    private Long quoteMaxAgeS;

    @JsonProperty("quote_max_age_slow_s")
    private Long quoteMaxAgeSlowS;

    /** Working orders the account may hold. */
    public Long maxWorkingPerAccount() {
        return maxWorkingPerAccount;
    }

    /** Sets max_working_per_account. */
    public TradingPolicyOrders maxWorkingPerAccount(Long value) {
        this.maxWorkingPerAccount = value;
        return this;
    }

    /** New orders a minute. */
    public Long newPerMinAccount() {
        return newPerMinAccount;
    }

    /** Sets new_per_min_account. */
    public TradingPolicyOrders newPerMinAccount(Long value) {
        this.newPerMinAccount = value;
        return this;
    }

    /** How far from the market price a priced order may be, in percent. */
    public BigDecimal priceBandPct() {
        return priceBandPct;
    }

    /** Sets price_band_pct. */
    public TradingPolicyOrders priceBandPct(BigDecimal value) {
        this.priceBandPct = value;
        return this;
    }

    /** The oldest price an order is checked against, in seconds. */
    public Long quoteMaxAgeS() {
        return quoteMaxAgeS;
    }

    /** Sets quote_max_age_s. */
    public TradingPolicyOrders quoteMaxAgeS(Long value) {
        this.quoteMaxAgeS = value;
        return this;
    }

    /** The oldest unchanged price of a quiet market an order is checked against, while the price feed lives (other markets move), in seconds. */
    public Long quoteMaxAgeSlowS() {
        return quoteMaxAgeSlowS;
    }

    /** Sets quote_max_age_slow_s. */
    public TradingPolicyOrders quoteMaxAgeSlowS(Long value) {
        this.quoteMaxAgeSlowS = value;
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
        if (!(other instanceof TradingPolicyOrders)) {
            return false;
        }
        TradingPolicyOrders o = (TradingPolicyOrders) other;
        return Objects.equals(maxWorkingPerAccount, o.maxWorkingPerAccount)
            && Objects.equals(newPerMinAccount, o.newPerMinAccount)
            && Objects.equals(priceBandPct, o.priceBandPct)
            && Objects.equals(quoteMaxAgeS, o.quoteMaxAgeS)
            && Objects.equals(quoteMaxAgeSlowS, o.quoteMaxAgeSlowS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxWorkingPerAccount, newPerMinAccount, priceBandPct, quoteMaxAgeS, quoteMaxAgeSlowS);
    }

    @Override
    public String toString() {
        return "TradingPolicyOrders{" + "max_working_per_account=" + maxWorkingPerAccount + ", new_per_min_account=" + newPerMinAccount + ", price_band_pct=" + priceBandPct + ", quote_max_age_s=" + quoteMaxAgeS + ", quote_max_age_slow_s=" + quoteMaxAgeSlowS + "}";
    }
}
