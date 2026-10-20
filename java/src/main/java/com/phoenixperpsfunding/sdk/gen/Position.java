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

/** Position */
public final class Position {
    @JsonProperty("id")
    private String id;

    @JsonProperty("market")
    private String market;

    @JsonProperty("side")
    private PositionSide side;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("entry_price")
    private BigDecimal entryPrice;

    @JsonProperty("mark_price")
    private BigDecimal markPrice;

    @JsonProperty("notional")
    private BigDecimal notional;

    @JsonProperty("unrealized_pnl")
    private BigDecimal unrealizedPnl;

    @JsonProperty("leverage")
    private Long leverage;

    @JsonProperty("margin_mode")
    private MarginMode marginMode;

    @JsonProperty("initial_margin")
    private BigDecimal initialMargin;

    @JsonProperty("added_margin")
    private BigDecimal addedMargin;

    @JsonProperty("margin")
    private BigDecimal margin;

    @JsonProperty("liquidation")
    private PositionLiquidation liquidation;

    @JsonProperty("breach_prices")
    private PositionBreachPrices breachPrices;

    @JsonProperty("exits")
    private PositionExits exits;

    @JsonProperty("protected")
    private Boolean protectedValue;

    @JsonProperty("opened_at")
    private Long openedAt;

    /** This position, while it stays open on the same side. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public Position id(String value) {
        this.id = value;
        return this;
    }

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public Position market(String value) {
        this.market = value;
        return this;
    }

    /** Long or short. */
    public PositionSide side() {
        return side;
    }

    /** Sets side. */
    public Position side(PositionSide value) {
        this.side = value;
        return this;
    }

    /** The size, in the base asset. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public Position size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** The average entry price. */
    public BigDecimal entryPrice() {
        return entryPrice;
    }

    /** Sets entry_price. */
    public Position entryPrice(BigDecimal value) {
        this.entryPrice = value;
        return this;
    }

    /** The market price it is valued at. */
    public BigDecimal markPrice() {
        return markPrice;
    }

    /** Sets mark_price. */
    public Position markPrice(BigDecimal value) {
        this.markPrice = value;
        return this;
    }

    /** Its value at that price. */
    public BigDecimal notional() {
        return notional;
    }

    /** Sets notional. */
    public Position notional(BigDecimal value) {
        this.notional = value;
        return this;
    }

    /** Its open P&amp;L. */
    public BigDecimal unrealizedPnl() {
        return unrealizedPnl;
    }

    /** Sets unrealized_pnl. */
    public Position unrealizedPnl(BigDecimal value) {
        this.unrealizedPnl = value;
        return this;
    }

    /** Its leverage. */
    public Long leverage() {
        return leverage;
    }

    /** Sets leverage. */
    public Position leverage(Long value) {
        this.leverage = value;
        return this;
    }

    /** Its margin mode. */
    public MarginMode marginMode() {
        return marginMode;
    }

    /** Sets margin_mode. */
    public Position marginMode(MarginMode value) {
        this.marginMode = value;
        return this;
    }

    /** The margin it took at entry. */
    public BigDecimal initialMargin() {
        return initialMargin;
    }

    /** Sets initial_margin. */
    public Position initialMargin(BigDecimal value) {
        this.initialMargin = value;
        return this;
    }

    /** Margin you added to it. */
    public BigDecimal addedMargin() {
        return addedMargin;
    }

    /** Sets added_margin. */
    public Position addedMargin(BigDecimal value) {
        this.addedMargin = value;
        return this;
    }

    /** What it holds of the balance. */
    public BigDecimal margin() {
        return margin;
    }

    /** Sets margin. */
    public Position margin(BigDecimal value) {
        this.margin = value;
        return this;
    }

    /** The liquidation. */
    public PositionLiquidation liquidation() {
        return liquidation;
    }

    /** Sets liquidation. */
    public Position liquidation(PositionLiquidation value) {
        this.liquidation = value;
        return this;
    }

    /** The breach prices. */
    public PositionBreachPrices breachPrices() {
        return breachPrices;
    }

    /** Sets breach_prices. */
    public Position breachPrices(PositionBreachPrices value) {
        this.breachPrices = value;
        return this;
    }

    /** The exits. */
    public PositionExits exits() {
        return exits;
    }

    /** Sets exits. */
    public Position exits(PositionExits value) {
        this.exits = value;
        return this;
    }

    /** At least one stop loss rests on it. */
    public Boolean protectedValue() {
        return protectedValue;
    }

    /** Sets protected. */
    public Position protectedValue(Boolean value) {
        this.protectedValue = value;
        return this;
    }

    /** When it opened. */
    public Long openedAt() {
        return openedAt;
    }

    /** Sets opened_at. */
    public Position openedAt(Long value) {
        this.openedAt = value;
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
        if (!(other instanceof Position)) {
            return false;
        }
        Position o = (Position) other;
        return Objects.equals(id, o.id)
            && Objects.equals(market, o.market)
            && Objects.equals(side, o.side)
            && Objects.equals(size, o.size)
            && Objects.equals(entryPrice, o.entryPrice)
            && Objects.equals(markPrice, o.markPrice)
            && Objects.equals(notional, o.notional)
            && Objects.equals(unrealizedPnl, o.unrealizedPnl)
            && Objects.equals(leverage, o.leverage)
            && Objects.equals(marginMode, o.marginMode)
            && Objects.equals(initialMargin, o.initialMargin)
            && Objects.equals(addedMargin, o.addedMargin)
            && Objects.equals(margin, o.margin)
            && Objects.equals(liquidation, o.liquidation)
            && Objects.equals(breachPrices, o.breachPrices)
            && Objects.equals(exits, o.exits)
            && Objects.equals(protectedValue, o.protectedValue)
            && Objects.equals(openedAt, o.openedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, market, side, size, entryPrice, markPrice, notional, unrealizedPnl, leverage, marginMode, initialMargin, addedMargin, margin, liquidation, breachPrices, exits, protectedValue, openedAt);
    }

    @Override
    public String toString() {
        return "Position{" + "id=" + id + ", market=" + market + ", side=" + side + ", size=" + size + ", entry_price=" + entryPrice + ", mark_price=" + markPrice + ", notional=" + notional + ", unrealized_pnl=" + unrealizedPnl + ", leverage=" + leverage + ", margin_mode=" + marginMode + ", initial_margin=" + initialMargin + ", added_margin=" + addedMargin + ", margin=" + margin + ", liquidation=" + liquidation + ", breach_prices=" + breachPrices + ", exits=" + exits + ", protected=" + protectedValue + ", opened_at=" + openedAt + "}";
    }
}
