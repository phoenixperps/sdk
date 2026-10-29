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

/** MarketOption */
public final class MarketOption {
    @JsonProperty("market")
    private String market;

    @JsonProperty("venue")
    private String venue;

    @JsonProperty("trading_hours")
    private TradingHours tradingHours;

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public MarketOption market(String value) {
        this.market = value;
        return this;
    }

    /** Its venue. */
    public String venue() {
        return venue;
    }

    /** Sets venue. */
    public MarketOption venue(String value) {
        this.venue = value;
        return this;
    }

    /** When it trades. */
    public TradingHours tradingHours() {
        return tradingHours;
    }

    /** Sets trading_hours. */
    public MarketOption tradingHours(TradingHours value) {
        this.tradingHours = value;
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
        if (!(other instanceof MarketOption)) {
            return false;
        }
        MarketOption o = (MarketOption) other;
        return Objects.equals(market, o.market)
            && Objects.equals(venue, o.venue)
            && Objects.equals(tradingHours, o.tradingHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, venue, tradingHours);
    }

    @Override
    public String toString() {
        return "MarketOption{" + "market=" + market + ", venue=" + venue + ", trading_hours=" + tradingHours + "}";
    }
}
