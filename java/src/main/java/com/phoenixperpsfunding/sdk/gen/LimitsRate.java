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

/** The request budgets. A request takes from every budget it belongs to. */
public final class LimitsRate {
    @JsonProperty("public_per_ip")
    private LimitsRatePublicPerIp publicPerIp;

    @JsonProperty("per_ip")
    private LimitsRatePerIp perIp;

    @JsonProperty("per_key_read")
    private LimitsRatePerKeyRead perKeyRead;

    @JsonProperty("per_key_trade")
    private LimitsRatePerKeyTrade perKeyTrade;

    @JsonProperty("per_account_trade")
    private LimitsRatePerAccountTrade perAccountTrade;

    @JsonProperty("per_customer")
    private LimitsRatePerCustomer perCustomer;

    @JsonProperty("bulk")
    private LimitsRateBulk bulk;

    @JsonProperty("what_if")
    private LimitsRateWhatIf whatIf;

    /** The public per ip. */
    public LimitsRatePublicPerIp publicPerIp() {
        return publicPerIp;
    }

    /** Sets public_per_ip. */
    public LimitsRate publicPerIp(LimitsRatePublicPerIp value) {
        this.publicPerIp = value;
        return this;
    }

    /** The per ip. */
    public LimitsRatePerIp perIp() {
        return perIp;
    }

    /** Sets per_ip. */
    public LimitsRate perIp(LimitsRatePerIp value) {
        this.perIp = value;
        return this;
    }

    /** The per key read. */
    public LimitsRatePerKeyRead perKeyRead() {
        return perKeyRead;
    }

    /** Sets per_key_read. */
    public LimitsRate perKeyRead(LimitsRatePerKeyRead value) {
        this.perKeyRead = value;
        return this;
    }

    /** The per key trade. */
    public LimitsRatePerKeyTrade perKeyTrade() {
        return perKeyTrade;
    }

    /** Sets per_key_trade. */
    public LimitsRate perKeyTrade(LimitsRatePerKeyTrade value) {
        this.perKeyTrade = value;
        return this;
    }

    /** The per account trade. */
    public LimitsRatePerAccountTrade perAccountTrade() {
        return perAccountTrade;
    }

    /** Sets per_account_trade. */
    public LimitsRate perAccountTrade(LimitsRatePerAccountTrade value) {
        this.perAccountTrade = value;
        return this;
    }

    /** The per customer. */
    public LimitsRatePerCustomer perCustomer() {
        return perCustomer;
    }

    /** Sets per_customer. */
    public LimitsRate perCustomer(LimitsRatePerCustomer value) {
        this.perCustomer = value;
        return this;
    }

    /** The bulk. */
    public LimitsRateBulk bulk() {
        return bulk;
    }

    /** Sets bulk. */
    public LimitsRate bulk(LimitsRateBulk value) {
        this.bulk = value;
        return this;
    }

    /** The what if. */
    public LimitsRateWhatIf whatIf() {
        return whatIf;
    }

    /** Sets what_if. */
    public LimitsRate whatIf(LimitsRateWhatIf value) {
        this.whatIf = value;
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
        if (!(other instanceof LimitsRate)) {
            return false;
        }
        LimitsRate o = (LimitsRate) other;
        return Objects.equals(publicPerIp, o.publicPerIp)
            && Objects.equals(perIp, o.perIp)
            && Objects.equals(perKeyRead, o.perKeyRead)
            && Objects.equals(perKeyTrade, o.perKeyTrade)
            && Objects.equals(perAccountTrade, o.perAccountTrade)
            && Objects.equals(perCustomer, o.perCustomer)
            && Objects.equals(bulk, o.bulk)
            && Objects.equals(whatIf, o.whatIf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicPerIp, perIp, perKeyRead, perKeyTrade, perAccountTrade, perCustomer, bulk, whatIf);
    }

    @Override
    public String toString() {
        return "LimitsRate{" + "public_per_ip=" + publicPerIp + ", per_ip=" + perIp + ", per_key_read=" + perKeyRead + ", per_key_trade=" + perKeyTrade + ", per_account_trade=" + perAccountTrade + ", per_customer=" + perCustomer + ", bulk=" + bulk + ", what_if=" + whatIf + "}";
    }
}
