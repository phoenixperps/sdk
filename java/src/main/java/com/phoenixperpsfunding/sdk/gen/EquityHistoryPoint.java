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

/** EquityHistoryPoint */
public final class EquityHistoryPoint {
    @JsonProperty("time")
    private Long time;

    @JsonProperty("date")
    private String date;

    @JsonProperty("balance")
    private BigDecimal balance;

    /** When. */
    public Long time() {
        return time;
    }

    /** Sets time. */
    public EquityHistoryPoint time(Long value) {
        this.time = value;
        return this;
    }

    /** The trading day (New York), with resolution day. */
    public String date() {
        return date;
    }

    /** Sets date. */
    public EquityHistoryPoint date(String value) {
        this.date = value;
        return this;
    }

    /** The balance then. */
    public BigDecimal balance() {
        return balance;
    }

    /** Sets balance. */
    public EquityHistoryPoint balance(BigDecimal value) {
        this.balance = value;
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
        if (!(other instanceof EquityHistoryPoint)) {
            return false;
        }
        EquityHistoryPoint o = (EquityHistoryPoint) other;
        return Objects.equals(time, o.time)
            && Objects.equals(date, o.date)
            && Objects.equals(balance, o.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, date, balance);
    }

    @Override
    public String toString() {
        return "EquityHistoryPoint{" + "time=" + time + ", date=" + date + ", balance=" + balance + "}";
    }
}
