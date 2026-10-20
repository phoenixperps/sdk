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

/** TradingViewHook */
public final class TradingViewHook {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("environment")
    private PlatformEnvironment environment;

    @JsonProperty("status")
    private TradingViewHookStatus status;

    @JsonProperty("markets")
    private List<String> markets;

    @JsonProperty("reduce_only")
    private Boolean reduceOnly;

    @JsonProperty("max_order_usd")
    private BigDecimal maxOrderUsd;

    @JsonProperty("max_position_usd")
    private BigDecimal maxPositionUsd;

    @JsonProperty("daily_loss_usd")
    private BigDecimal dailyLossUsd;

    @JsonProperty("sizing")
    private TradingViewHookSizing sizing;

    @JsonProperty("position_mode")
    private TradingViewHookPositionMode positionMode;

    /** The link. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public TradingViewHook id(String value) {
        this.id = value;
        return this;
    }

    /** Its name. */
    public String name() {
        return name;
    }

    /** Sets name. */
    public TradingViewHook name(String value) {
        this.name = value;
        return this;
    }

    /** The account number. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public TradingViewHook accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** Its environment. */
    public PlatformEnvironment environment() {
        return environment;
    }

    /** Sets environment. */
    public TradingViewHook environment(PlatformEnvironment value) {
        this.environment = value;
        return this;
    }

    /** paused: alerts are refused (link_paused). */
    public TradingViewHookStatus status() {
        return status;
    }

    /** Sets status. */
    public TradingViewHook status(TradingViewHookStatus value) {
        this.status = value;
        return this;
    }

    /** The markets it may trade; empty: every market. */
    public List<String> markets() {
        return markets;
    }

    /** Sets markets. */
    public TradingViewHook markets(List<String> value) {
        this.markets = value;
        return this;
    }

    /** It may only reduce positions. */
    public Boolean reduceOnly() {
        return reduceOnly;
    }

    /** Sets reduce_only. */
    public TradingViewHook reduceOnly(Boolean value) {
        this.reduceOnly = value;
        return this;
    }

    /** Its largest order, in USD. */
    public BigDecimal maxOrderUsd() {
        return maxOrderUsd;
    }

    /** Sets max_order_usd. */
    public TradingViewHook maxOrderUsd(BigDecimal value) {
        this.maxOrderUsd = value;
        return this;
    }

    /** Its largest position, in USD. */
    public BigDecimal maxPositionUsd() {
        return maxPositionUsd;
    }

    /** Sets max_position_usd. */
    public TradingViewHook maxPositionUsd(BigDecimal value) {
        this.maxPositionUsd = value;
        return this;
    }

    /** Its daily loss stop: no new position once today's loss reaches it. */
    public BigDecimal dailyLossUsd() {
        return dailyLossUsd;
    }

    /** Sets daily_loss_usd. */
    public TradingViewHook dailyLossUsd(BigDecimal value) {
        this.dailyLossUsd = value;
        return this;
    }

    /** The sizing. */
    public TradingViewHookSizing sizing() {
        return sizing;
    }

    /** Sets sizing. */
    public TradingViewHook sizing(TradingViewHookSizing value) {
        this.sizing = value;
        return this;
    }

    /** signal: an opposite alert reverses, a repeat is skipped. add: a repeat adds. target: the strategy's position size is followed. */
    public TradingViewHookPositionMode positionMode() {
        return positionMode;
    }

    /** Sets position_mode. */
    public TradingViewHook positionMode(TradingViewHookPositionMode value) {
        this.positionMode = value;
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
        if (!(other instanceof TradingViewHook)) {
            return false;
        }
        TradingViewHook o = (TradingViewHook) other;
        return Objects.equals(id, o.id)
            && Objects.equals(name, o.name)
            && Objects.equals(accountId, o.accountId)
            && Objects.equals(environment, o.environment)
            && Objects.equals(status, o.status)
            && Objects.equals(markets, o.markets)
            && Objects.equals(reduceOnly, o.reduceOnly)
            && Objects.equals(maxOrderUsd, o.maxOrderUsd)
            && Objects.equals(maxPositionUsd, o.maxPositionUsd)
            && Objects.equals(dailyLossUsd, o.dailyLossUsd)
            && Objects.equals(sizing, o.sizing)
            && Objects.equals(positionMode, o.positionMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, accountId, environment, status, markets, reduceOnly, maxOrderUsd, maxPositionUsd, dailyLossUsd, sizing, positionMode);
    }

    @Override
    public String toString() {
        return "TradingViewHook{" + "id=" + id + ", name=" + name + ", account_id=" + accountId + ", environment=" + environment + ", status=" + status + ", markets=" + markets + ", reduce_only=" + reduceOnly + ", max_order_usd=" + maxOrderUsd + ", max_position_usd=" + maxPositionUsd + ", daily_loss_usd=" + dailyLossUsd + ", sizing=" + sizing + ", position_mode=" + positionMode + "}";
    }
}
