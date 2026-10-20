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

/** CancelAllResult */
public final class CancelAllResult {
    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("market")
    private String market;

    @JsonProperty("canceled")
    private Long canceled;

    /** The account number. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public CancelAllResult accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** The market, or null for every market. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public CancelAllResult market(String value) {
        this.market = value;
        return this;
    }

    /** How many working orders were canceled. */
    public Long canceled() {
        return canceled;
    }

    /** Sets canceled. */
    public CancelAllResult canceled(Long value) {
        this.canceled = value;
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
        if (!(other instanceof CancelAllResult)) {
            return false;
        }
        CancelAllResult o = (CancelAllResult) other;
        return Objects.equals(accountId, o.accountId)
            && Objects.equals(market, o.market)
            && Objects.equals(canceled, o.canceled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, market, canceled);
    }

    @Override
    public String toString() {
        return "CancelAllResult{" + "account_id=" + accountId + ", market=" + market + ", canceled=" + canceled + "}";
    }
}
