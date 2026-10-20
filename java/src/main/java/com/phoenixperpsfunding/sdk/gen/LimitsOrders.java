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

/** LimitsOrders */
public final class LimitsOrders {
    @JsonProperty("max_working_per_account")
    private Long maxWorkingPerAccount;

    @JsonProperty("max_working_per_customer")
    private Long maxWorkingPerCustomer;

    @JsonProperty("new_per_min_account")
    private Long newPerMinAccount;

    @JsonProperty("new_per_day_account")
    private Long newPerDayAccount;

    @JsonProperty("price_band_pct")
    private BigDecimal priceBandPct;

    @JsonProperty("quote_max_age_s")
    private Long quoteMaxAgeS;

    @JsonProperty("quote_max_age_slow_s")
    private Long quoteMaxAgeSlowS;

    @JsonProperty("gtd_min_seconds")
    private Long gtdMinSeconds;

    @JsonProperty("gtd_max_days")
    private Long gtdMaxDays;

    /** Working orders an account may hold. */
    public Long maxWorkingPerAccount() {
        return maxWorkingPerAccount;
    }

    /** Sets max_working_per_account. */
    public LimitsOrders maxWorkingPerAccount(Long value) {
        this.maxWorkingPerAccount = value;
        return this;
    }

    /** Working orders across your accounts. */
    public Long maxWorkingPerCustomer() {
        return maxWorkingPerCustomer;
    }

    /** Sets max_working_per_customer. */
    public LimitsOrders maxWorkingPerCustomer(Long value) {
        this.maxWorkingPerCustomer = value;
        return this;
    }

    /** New orders a minute on one account. */
    public Long newPerMinAccount() {
        return newPerMinAccount;
    }

    /** Sets new_per_min_account. */
    public LimitsOrders newPerMinAccount(Long value) {
        this.newPerMinAccount = value;
        return this;
    }

    /** New orders a day on one account. */
    public Long newPerDayAccount() {
        return newPerDayAccount;
    }

    /** Sets new_per_day_account. */
    public LimitsOrders newPerDayAccount(Long value) {
        this.newPerDayAccount = value;
        return this;
    }

    /** How far from the market price a priced order may be, in percent. */
    public BigDecimal priceBandPct() {
        return priceBandPct;
    }

    /** Sets price_band_pct. */
    public LimitsOrders priceBandPct(BigDecimal value) {
        this.priceBandPct = value;
        return this;
    }

    /** The oldest price an order is checked against, in seconds. */
    public Long quoteMaxAgeS() {
        return quoteMaxAgeS;
    }

    /** Sets quote_max_age_s. */
    public LimitsOrders quoteMaxAgeS(Long value) {
        this.quoteMaxAgeS = value;
        return this;
    }

    /** The oldest unchanged price of a quiet market an order is checked against, while the price feed lives (other markets move), in seconds. */
    public Long quoteMaxAgeSlowS() {
        return quoteMaxAgeSlowS;
    }

    /** Sets quote_max_age_slow_s. */
    public LimitsOrders quoteMaxAgeSlowS(Long value) {
        this.quoteMaxAgeSlowS = value;
        return this;
    }

    /** The nearest a gtd order may expire, in seconds from now. */
    public Long gtdMinSeconds() {
        return gtdMinSeconds;
    }

    /** Sets gtd_min_seconds. */
    public LimitsOrders gtdMinSeconds(Long value) {
        this.gtdMinSeconds = value;
        return this;
    }

    /** The farthest, in days from now. */
    public Long gtdMaxDays() {
        return gtdMaxDays;
    }

    /** Sets gtd_max_days. */
    public LimitsOrders gtdMaxDays(Long value) {
        this.gtdMaxDays = value;
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
        if (!(other instanceof LimitsOrders)) {
            return false;
        }
        LimitsOrders o = (LimitsOrders) other;
        return Objects.equals(maxWorkingPerAccount, o.maxWorkingPerAccount)
            && Objects.equals(maxWorkingPerCustomer, o.maxWorkingPerCustomer)
            && Objects.equals(newPerMinAccount, o.newPerMinAccount)
            && Objects.equals(newPerDayAccount, o.newPerDayAccount)
            && Objects.equals(priceBandPct, o.priceBandPct)
            && Objects.equals(quoteMaxAgeS, o.quoteMaxAgeS)
            && Objects.equals(quoteMaxAgeSlowS, o.quoteMaxAgeSlowS)
            && Objects.equals(gtdMinSeconds, o.gtdMinSeconds)
            && Objects.equals(gtdMaxDays, o.gtdMaxDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxWorkingPerAccount, maxWorkingPerCustomer, newPerMinAccount, newPerDayAccount, priceBandPct, quoteMaxAgeS, quoteMaxAgeSlowS, gtdMinSeconds, gtdMaxDays);
    }

    @Override
    public String toString() {
        return "LimitsOrders{" + "max_working_per_account=" + maxWorkingPerAccount + ", max_working_per_customer=" + maxWorkingPerCustomer + ", new_per_min_account=" + newPerMinAccount + ", new_per_day_account=" + newPerDayAccount + ", price_band_pct=" + priceBandPct + ", quote_max_age_s=" + quoteMaxAgeS + ", quote_max_age_slow_s=" + quoteMaxAgeSlowS + ", gtd_min_seconds=" + gtdMinSeconds + ", gtd_max_days=" + gtdMaxDays + "}";
    }
}
