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

/** LimitsAlerts */
public final class LimitsAlerts {
    @JsonProperty("armed_per_account")
    private Long armedPerAccount;

    @JsonProperty("armed_per_customer")
    private Long armedPerCustomer;

    @JsonProperty("kept_days")
    private Long keptDays;

    /** Price alerts armed at once on one account. */
    public Long armedPerAccount() {
        return armedPerAccount;
    }

    /** Sets armed_per_account. */
    public LimitsAlerts armedPerAccount(Long value) {
        this.armedPerAccount = value;
        return this;
    }

    /** Price alerts armed at once across your accounts. */
    public Long armedPerCustomer() {
        return armedPerCustomer;
    }

    /** Sets armed_per_customer. */
    public LimitsAlerts armedPerCustomer(Long value) {
        this.armedPerCustomer = value;
        return this;
    }

    /** How long an alert that fired (or was disabled) stays listed, in days. */
    public Long keptDays() {
        return keptDays;
    }

    /** Sets kept_days. */
    public LimitsAlerts keptDays(Long value) {
        this.keptDays = value;
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
        if (!(other instanceof LimitsAlerts)) {
            return false;
        }
        LimitsAlerts o = (LimitsAlerts) other;
        return Objects.equals(armedPerAccount, o.armedPerAccount)
            && Objects.equals(armedPerCustomer, o.armedPerCustomer)
            && Objects.equals(keptDays, o.keptDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(armedPerAccount, armedPerCustomer, keptDays);
    }

    @Override
    public String toString() {
        return "LimitsAlerts{" + "armed_per_account=" + armedPerAccount + ", armed_per_customer=" + armedPerCustomer + ", kept_days=" + keptDays + "}";
    }
}
