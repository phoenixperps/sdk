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

/** EquityHistoryLive */
public final class EquityHistoryLive {
    @JsonProperty("time")
    private Long time;

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("equity")
    private BigDecimal equity;

    /** When the live figures were read. */
    public Long time() {
        return time;
    }

    /** Sets time. */
    public EquityHistoryLive time(Long value) {
        this.time = value;
        return this;
    }

    /** The balance now. */
    public BigDecimal balance() {
        return balance;
    }

    /** Sets balance. */
    public EquityHistoryLive balance(BigDecimal value) {
        this.balance = value;
        return this;
    }

    /** The equity now. */
    public BigDecimal equity() {
        return equity;
    }

    /** Sets equity. */
    public EquityHistoryLive equity(BigDecimal value) {
        this.equity = value;
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
        if (!(other instanceof EquityHistoryLive)) {
            return false;
        }
        EquityHistoryLive o = (EquityHistoryLive) other;
        return Objects.equals(time, o.time)
            && Objects.equals(balance, o.balance)
            && Objects.equals(equity, o.equity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, balance, equity);
    }

    @Override
    public String toString() {
        return "EquityHistoryLive{" + "time=" + time + ", balance=" + balance + ", equity=" + equity + "}";
    }
}
