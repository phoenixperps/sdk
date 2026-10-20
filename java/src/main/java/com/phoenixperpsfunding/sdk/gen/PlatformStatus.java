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

/** PlatformStatus */
public final class PlatformStatus {
    @JsonProperty("engine")
    private PlatformStatusEngine engine;

    @JsonProperty("engine_started_at")
    private Long engineStartedAt;

    @JsonProperty("market_data")
    private PlatformStatusMarketData marketData;

    @JsonProperty("webhooks")
    private PlatformStatusWebhooks webhooks;

    /** Whether the trading engine is reachable. */
    public PlatformStatusEngine engine() {
        return engine;
    }

    /** Sets engine. */
    public PlatformStatus engine(PlatformStatusEngine value) {
        this.engine = value;
        return this;
    }

    /** When the trading engine last started. */
    public Long engineStartedAt() {
        return engineStartedAt;
    }

    /** Sets engine_started_at. */
    public PlatformStatus engineStartedAt(Long value) {
        this.engineStartedAt = value;
        return this;
    }

    /** Whether the market-data stream is live, with every price feed. */
    public PlatformStatusMarketData marketData() {
        return marketData;
    }

    /** Sets market_data. */
    public PlatformStatus marketData(PlatformStatusMarketData value) {
        this.marketData = value;
        return this;
    }

    /** Whether outbound webhooks are being delivered. */
    public PlatformStatusWebhooks webhooks() {
        return webhooks;
    }

    /** Sets webhooks. */
    public PlatformStatus webhooks(PlatformStatusWebhooks value) {
        this.webhooks = value;
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
        if (!(other instanceof PlatformStatus)) {
            return false;
        }
        PlatformStatus o = (PlatformStatus) other;
        return Objects.equals(engine, o.engine)
            && Objects.equals(engineStartedAt, o.engineStartedAt)
            && Objects.equals(marketData, o.marketData)
            && Objects.equals(webhooks, o.webhooks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(engine, engineStartedAt, marketData, webhooks);
    }

    @Override
    public String toString() {
        return "PlatformStatus{" + "engine=" + engine + ", engine_started_at=" + engineStartedAt + ", market_data=" + marketData + ", webhooks=" + webhooks + "}";
    }
}
