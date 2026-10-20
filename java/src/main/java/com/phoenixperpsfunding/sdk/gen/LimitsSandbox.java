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

/** LimitsSandbox */
public final class LimitsSandbox {
    @JsonProperty("accounts_for_life")
    private Long accountsForLife;

    @JsonProperty("start_balance")
    private BigDecimal startBalance;

    @JsonProperty("parked_after_hours")
    private Long parkedAfterHours;

    /** Sandbox accounts you may open, ever. */
    public Long accountsForLife() {
        return accountsForLife;
    }

    /** Sets accounts_for_life. */
    public LimitsSandbox accountsForLife(Long value) {
        this.accountsForLife = value;
        return this;
    }

    /** The balance a sandbox account starts with. */
    public BigDecimal startBalance() {
        return startBalance;
    }

    /** Sets start_balance. */
    public LimitsSandbox startBalance(BigDecimal value) {
        this.startBalance = value;
        return this;
    }

    /** Hours without use before a sandbox account is parked. */
    public Long parkedAfterHours() {
        return parkedAfterHours;
    }

    /** Sets parked_after_hours. */
    public LimitsSandbox parkedAfterHours(Long value) {
        this.parkedAfterHours = value;
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
        if (!(other instanceof LimitsSandbox)) {
            return false;
        }
        LimitsSandbox o = (LimitsSandbox) other;
        return Objects.equals(accountsForLife, o.accountsForLife)
            && Objects.equals(startBalance, o.startBalance)
            && Objects.equals(parkedAfterHours, o.parkedAfterHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountsForLife, startBalance, parkedAfterHours);
    }

    @Override
    public String toString() {
        return "LimitsSandbox{" + "accounts_for_life=" + accountsForLife + ", start_balance=" + startBalance + ", parked_after_hours=" + parkedAfterHours + "}";
    }
}
