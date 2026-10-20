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

/** DailyResult */
public final class DailyResult {
    @JsonProperty("date")
    private String date;

    @JsonProperty("trades")
    private Long trades;

    @JsonProperty("gross_pnl")
    private BigDecimal grossPnl;

    @JsonProperty("fees")
    private BigDecimal fees;

    @JsonProperty("pnl")
    private BigDecimal pnl;

    @JsonProperty("withdrawn")
    private BigDecimal withdrawn;

    @JsonProperty("balance_end")
    private BigDecimal balanceEnd;

    /** The trading day (midnight to midnight New York). */
    public String date() {
        return date;
    }

    /** Sets date. */
    public DailyResult date(String value) {
        this.date = value;
        return this;
    }

    /** Trades closed that day. */
    public Long trades() {
        return trades;
    }

    /** Sets trades. */
    public DailyResult trades(Long value) {
        this.trades = value;
        return this;
    }

    /** Their P&amp;L before fees. */
    public BigDecimal grossPnl() {
        return grossPnl;
    }

    /** Sets gross_pnl. */
    public DailyResult grossPnl(BigDecimal value) {
        this.grossPnl = value;
        return this;
    }

    /** Their fees. */
    public BigDecimal fees() {
        return fees;
    }

    /** Sets fees. */
    public DailyResult fees(BigDecimal value) {
        this.fees = value;
        return this;
    }

    /** Their P&amp;L after fees. */
    public BigDecimal pnl() {
        return pnl;
    }

    /** Sets pnl. */
    public DailyResult pnl(BigDecimal value) {
        this.pnl = value;
        return this;
    }

    /** Paid out that day. */
    public BigDecimal withdrawn() {
        return withdrawn;
    }

    /** Sets withdrawn. */
    public DailyResult withdrawn(BigDecimal value) {
        this.withdrawn = value;
        return this;
    }

    /** The balance at the day's end. */
    public BigDecimal balanceEnd() {
        return balanceEnd;
    }

    /** Sets balance_end. */
    public DailyResult balanceEnd(BigDecimal value) {
        this.balanceEnd = value;
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
        if (!(other instanceof DailyResult)) {
            return false;
        }
        DailyResult o = (DailyResult) other;
        return Objects.equals(date, o.date)
            && Objects.equals(trades, o.trades)
            && Objects.equals(grossPnl, o.grossPnl)
            && Objects.equals(fees, o.fees)
            && Objects.equals(pnl, o.pnl)
            && Objects.equals(withdrawn, o.withdrawn)
            && Objects.equals(balanceEnd, o.balanceEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, trades, grossPnl, fees, pnl, withdrawn, balanceEnd);
    }

    @Override
    public String toString() {
        return "DailyResult{" + "date=" + date + ", trades=" + trades + ", gross_pnl=" + grossPnl + ", fees=" + fees + ", pnl=" + pnl + ", withdrawn=" + withdrawn + ", balance_end=" + balanceEnd + "}";
    }
}
