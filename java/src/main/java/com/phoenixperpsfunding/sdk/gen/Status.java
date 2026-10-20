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

/** Status */
public final class Status {
    @JsonProperty("environment")
    private PlatformEnvironment environment;

    @JsonProperty("api")
    private StatusApi api;

    @JsonProperty("engine")
    private StatusEngine engine;

    @JsonProperty("account_data")
    private StatusAccountData accountData;

    @JsonProperty("market_data")
    private StatusMarketData marketData;

    @JsonProperty("features")
    private StatusFeatures features;

    @JsonProperty("server_time")
    private Long serverTime;

    /** The environment of the host you called. */
    public PlatformEnvironment environment() {
        return environment;
    }

    /** Sets environment. */
    public Status environment(PlatformEnvironment value) {
        this.environment = value;
        return this;
    }

    /** Whether this environment takes requests. */
    public StatusApi api() {
        return api;
    }

    /** Sets api. */
    public Status api(StatusApi value) {
        this.api = value;
        return this;
    }

    /** The engine. */
    public StatusEngine engine() {
        return engine;
    }

    /** Sets engine. */
    public Status engine(StatusEngine value) {
        this.engine = value;
        return this;
    }

    /** The account data. */
    public StatusAccountData accountData() {
        return accountData;
    }

    /** Sets account_data. */
    public Status accountData(StatusAccountData value) {
        this.accountData = value;
        return this;
    }

    /** The market data. */
    public StatusMarketData marketData() {
        return marketData;
    }

    /** Sets market_data. */
    public Status marketData(StatusMarketData value) {
        this.marketData = value;
        return this;
    }

    /** The features. */
    public StatusFeatures features() {
        return features;
    }

    /** Sets features. */
    public Status features(StatusFeatures value) {
        this.features = value;
        return this;
    }

    /** The time on the server. */
    public Long serverTime() {
        return serverTime;
    }

    /** Sets server_time. */
    public Status serverTime(Long value) {
        this.serverTime = value;
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
        if (!(other instanceof Status)) {
            return false;
        }
        Status o = (Status) other;
        return Objects.equals(environment, o.environment)
            && Objects.equals(api, o.api)
            && Objects.equals(engine, o.engine)
            && Objects.equals(accountData, o.accountData)
            && Objects.equals(marketData, o.marketData)
            && Objects.equals(features, o.features)
            && Objects.equals(serverTime, o.serverTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(environment, api, engine, accountData, marketData, features, serverTime);
    }

    @Override
    public String toString() {
        return "Status{" + "environment=" + environment + ", api=" + api + ", engine=" + engine + ", account_data=" + accountData + ", market_data=" + marketData + ", features=" + features + ", server_time=" + serverTime + "}";
    }
}
