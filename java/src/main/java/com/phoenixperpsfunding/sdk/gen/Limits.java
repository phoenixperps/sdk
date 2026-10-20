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

/** Limits */
public final class Limits {
    @JsonProperty("environment")
    private PlatformEnvironment environment;

    @JsonProperty("rate")
    private LimitsRate rate;

    @JsonProperty("keys")
    private LimitsKeys keys;

    @JsonProperty("orders")
    private LimitsOrders orders;

    @JsonProperty("sandbox")
    private LimitsSandbox sandbox;

    @JsonProperty("strategies")
    private LimitsStrategies strategies;

    @JsonProperty("body_max_bytes")
    private Long bodyMaxBytes;

    @JsonProperty("page_limit")
    private LimitsPageLimit pageLimit;

    /** The environment of the host you called. */
    public PlatformEnvironment environment() {
        return environment;
    }

    /** Sets environment. */
    public Limits environment(PlatformEnvironment value) {
        this.environment = value;
        return this;
    }

    /** The request budgets. A request takes from every budget it belongs to. */
    public LimitsRate rate() {
        return rate;
    }

    /** Sets rate. */
    public Limits rate(LimitsRate value) {
        this.rate = value;
        return this;
    }

    /** The keys. */
    public LimitsKeys keys() {
        return keys;
    }

    /** Sets keys. */
    public Limits keys(LimitsKeys value) {
        this.keys = value;
        return this;
    }

    /** The orders. */
    public LimitsOrders orders() {
        return orders;
    }

    /** Sets orders. */
    public Limits orders(LimitsOrders value) {
        this.orders = value;
        return this;
    }

    /** The sandbox. */
    public LimitsSandbox sandbox() {
        return sandbox;
    }

    /** Sets sandbox. */
    public Limits sandbox(LimitsSandbox value) {
        this.sandbox = value;
        return this;
    }

    /** The strategies. */
    public LimitsStrategies strategies() {
        return strategies;
    }

    /** Sets strategies. */
    public Limits strategies(LimitsStrategies value) {
        this.strategies = value;
        return this;
    }

    /** The largest request body. */
    public Long bodyMaxBytes() {
        return bodyMaxBytes;
    }

    /** Sets body_max_bytes. */
    public Limits bodyMaxBytes(Long value) {
        this.bodyMaxBytes = value;
        return this;
    }

    /** The page limit. */
    public LimitsPageLimit pageLimit() {
        return pageLimit;
    }

    /** Sets page_limit. */
    public Limits pageLimit(LimitsPageLimit value) {
        this.pageLimit = value;
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
        if (!(other instanceof Limits)) {
            return false;
        }
        Limits o = (Limits) other;
        return Objects.equals(environment, o.environment)
            && Objects.equals(rate, o.rate)
            && Objects.equals(keys, o.keys)
            && Objects.equals(orders, o.orders)
            && Objects.equals(sandbox, o.sandbox)
            && Objects.equals(strategies, o.strategies)
            && Objects.equals(bodyMaxBytes, o.bodyMaxBytes)
            && Objects.equals(pageLimit, o.pageLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(environment, rate, keys, orders, sandbox, strategies, bodyMaxBytes, pageLimit);
    }

    @Override
    public String toString() {
        return "Limits{" + "environment=" + environment + ", rate=" + rate + ", keys=" + keys + ", orders=" + orders + ", sandbox=" + sandbox + ", strategies=" + strategies + ", body_max_bytes=" + bodyMaxBytes + ", page_limit=" + pageLimit + "}";
    }
}
