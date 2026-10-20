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

/** StatusFeatures */
public final class StatusFeatures {
    @JsonProperty("stream")
    private Boolean stream;

    @JsonProperty("market_data")
    private Boolean marketData;

    @JsonProperty("webhooks")
    private Boolean webhooks;

    @JsonProperty("tradingview")
    private Boolean tradingview;

    @JsonProperty("mcp")
    private Boolean mcp;

    /** The account stream. */
    public Boolean stream() {
        return stream;
    }

    /** Sets stream. */
    public StatusFeatures stream(Boolean value) {
        this.stream = value;
        return this;
    }

    /** Market data. */
    public Boolean marketData() {
        return marketData;
    }

    /** Sets market_data. */
    public StatusFeatures marketData(Boolean value) {
        this.marketData = value;
        return this;
    }

    /** Webhooks. */
    public Boolean webhooks() {
        return webhooks;
    }

    /** Sets webhooks. */
    public StatusFeatures webhooks(Boolean value) {
        this.webhooks = value;
        return this;
    }

    /** TradingView alerts. */
    public Boolean tradingview() {
        return tradingview;
    }

    /** Sets tradingview. */
    public StatusFeatures tradingview(Boolean value) {
        this.tradingview = value;
        return this;
    }

    /** AI assistants (MCP). */
    public Boolean mcp() {
        return mcp;
    }

    /** Sets mcp. */
    public StatusFeatures mcp(Boolean value) {
        this.mcp = value;
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
        if (!(other instanceof StatusFeatures)) {
            return false;
        }
        StatusFeatures o = (StatusFeatures) other;
        return Objects.equals(stream, o.stream)
            && Objects.equals(marketData, o.marketData)
            && Objects.equals(webhooks, o.webhooks)
            && Objects.equals(tradingview, o.tradingview)
            && Objects.equals(mcp, o.mcp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stream, marketData, webhooks, tradingview, mcp);
    }

    @Override
    public String toString() {
        return "StatusFeatures{" + "stream=" + stream + ", market_data=" + marketData + ", webhooks=" + webhooks + ", tradingview=" + tradingview + ", mcp=" + mcp + "}";
    }
}
