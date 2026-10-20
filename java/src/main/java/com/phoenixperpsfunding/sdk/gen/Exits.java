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

/** Exits */
public final class Exits {
    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("market")
    private String market;

    @JsonProperty("strategy_id")
    private String strategyId;

    @JsonProperty("position")
    private ExitsPosition position;

    @JsonProperty("take_profit")
    private List<ExitsLeg> takeProfit;

    @JsonProperty("stop_loss")
    private List<ExitsLeg> stopLoss;

    @JsonProperty("resize_with_position")
    private Boolean resizeWithPosition;

    /** The account number. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public Exits accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public Exits market(String value) {
        this.market = value;
        return this;
    }

    /** The strategy that keeps these exits (type exits); null when there are none. */
    public String strategyId() {
        return strategyId;
    }

    /** Sets strategy_id. */
    public Exits strategyId(String value) {
        this.strategyId = value;
        return this;
    }

    /** The open position; null when flat. */
    public ExitsPosition position() {
        return position;
    }

    /** Sets position. */
    public Exits position(ExitsPosition value) {
        this.position = value;
        return this;
    }

    /** The take-profit legs: together at most the position. */
    public List<ExitsLeg> takeProfit() {
        return takeProfit;
    }

    /** Sets take_profit. */
    public Exits takeProfit(List<ExitsLeg> value) {
        this.takeProfit = value;
        return this;
    }

    /** The stop-loss legs: together at most the position. */
    public List<ExitsLeg> stopLoss() {
        return stopLoss;
    }

    /** Sets stop_loss. */
    public Exits stopLoss(List<ExitsLeg> value) {
        this.stopLoss = value;
        return this;
    }

    /** The legs grow with the position, keeping their share of it (rounded down to the size step). */
    public Boolean resizeWithPosition() {
        return resizeWithPosition;
    }

    /** Sets resize_with_position. */
    public Exits resizeWithPosition(Boolean value) {
        this.resizeWithPosition = value;
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
        if (!(other instanceof Exits)) {
            return false;
        }
        Exits o = (Exits) other;
        return Objects.equals(accountId, o.accountId)
            && Objects.equals(market, o.market)
            && Objects.equals(strategyId, o.strategyId)
            && Objects.equals(position, o.position)
            && Objects.equals(takeProfit, o.takeProfit)
            && Objects.equals(stopLoss, o.stopLoss)
            && Objects.equals(resizeWithPosition, o.resizeWithPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, market, strategyId, position, takeProfit, stopLoss, resizeWithPosition);
    }

    @Override
    public String toString() {
        return "Exits{" + "account_id=" + accountId + ", market=" + market + ", strategy_id=" + strategyId + ", position=" + position + ", take_profit=" + takeProfit + ", stop_loss=" + stopLoss + ", resize_with_position=" + resizeWithPosition + "}";
    }
}
