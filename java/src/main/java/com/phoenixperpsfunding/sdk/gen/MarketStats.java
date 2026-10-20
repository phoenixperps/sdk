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

/** A market's 24-hour figures and open interest. A figure not known yet is null. */
public final class MarketStats {
    @JsonProperty("market")
    private String market;

    @JsonProperty("last")
    private BigDecimal last;

    @JsonProperty("change_24h")
    private BigDecimal change24h;

    @JsonProperty("change_24h_pct")
    private BigDecimal change24hPct;

    @JsonProperty("high_24h")
    private BigDecimal high24h;

    @JsonProperty("low_24h")
    private BigDecimal low24h;

    @JsonProperty("volume_24h")
    private BigDecimal volume24h;

    @JsonProperty("notional_volume_24h")
    private BigDecimal notionalVolume24h;

    @JsonProperty("open_interest")
    private BigDecimal openInterest;

    @JsonProperty("notional_open_interest")
    private BigDecimal notionalOpenInterest;

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public MarketStats market(String value) {
        this.market = value;
        return this;
    }

    /** The last trade. */
    public BigDecimal last() {
        return last;
    }

    /** Sets last. */
    public MarketStats last(BigDecimal value) {
        this.last = value;
        return this;
    }

    /** The change over 24 hours. */
    public BigDecimal change24h() {
        return change24h;
    }

    /** Sets change_24h. */
    public MarketStats change24h(BigDecimal value) {
        this.change24h = value;
        return this;
    }

    /** That change in percent. */
    public BigDecimal change24hPct() {
        return change24hPct;
    }

    /** Sets change_24h_pct. */
    public MarketStats change24hPct(BigDecimal value) {
        this.change24hPct = value;
        return this;
    }

    /** The highest in 24 hours. */
    public BigDecimal high24h() {
        return high24h;
    }

    /** Sets high_24h. */
    public MarketStats high24h(BigDecimal value) {
        this.high24h = value;
        return this;
    }

    /** The lowest in 24 hours. */
    public BigDecimal low24h() {
        return low24h;
    }

    /** Sets low_24h. */
    public MarketStats low24h(BigDecimal value) {
        this.low24h = value;
        return this;
    }

    /** The size traded in 24 hours, in the base asset. */
    public BigDecimal volume24h() {
        return volume24h;
    }

    /** Sets volume_24h. */
    public MarketStats volume24h(BigDecimal value) {
        this.volume24h = value;
        return this;
    }

    /** The value traded in 24 hours, in USD. */
    public BigDecimal notionalVolume24h() {
        return notionalVolume24h;
    }

    /** Sets notional_volume_24h. */
    public MarketStats notionalVolume24h(BigDecimal value) {
        this.notionalVolume24h = value;
        return this;
    }

    /** The market's open interest: the size held open in positions, in the base asset. */
    public BigDecimal openInterest() {
        return openInterest;
    }

    /** Sets open_interest. */
    public MarketStats openInterest(BigDecimal value) {
        this.openInterest = value;
        return this;
    }

    /** That open interest in USD, at the last price. */
    public BigDecimal notionalOpenInterest() {
        return notionalOpenInterest;
    }

    /** Sets notional_open_interest. */
    public MarketStats notionalOpenInterest(BigDecimal value) {
        this.notionalOpenInterest = value;
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
        if (!(other instanceof MarketStats)) {
            return false;
        }
        MarketStats o = (MarketStats) other;
        return Objects.equals(market, o.market)
            && Objects.equals(last, o.last)
            && Objects.equals(change24h, o.change24h)
            && Objects.equals(change24hPct, o.change24hPct)
            && Objects.equals(high24h, o.high24h)
            && Objects.equals(low24h, o.low24h)
            && Objects.equals(volume24h, o.volume24h)
            && Objects.equals(notionalVolume24h, o.notionalVolume24h)
            && Objects.equals(openInterest, o.openInterest)
            && Objects.equals(notionalOpenInterest, o.notionalOpenInterest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, last, change24h, change24hPct, high24h, low24h, volume24h, notionalVolume24h, openInterest, notionalOpenInterest);
    }

    @Override
    public String toString() {
        return "MarketStats{" + "market=" + market + ", last=" + last + ", change_24h=" + change24h + ", change_24h_pct=" + change24hPct + ", high_24h=" + high24h + ", low_24h=" + low24h + ", volume_24h=" + volume24h + ", notional_volume_24h=" + notionalVolume24h + ", open_interest=" + openInterest + ", notional_open_interest=" + notionalOpenInterest + "}";
    }
}
