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

/** ClosedPosition */
public final class ClosedPosition {
    @JsonProperty("market")
    private String market;

    @JsonProperty("side")
    private PositionSide side;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("entry_price")
    private BigDecimal entryPrice;

    @JsonProperty("exit_price")
    private BigDecimal exitPrice;

    @JsonProperty("gross_pnl")
    private BigDecimal grossPnl;

    @JsonProperty("fees")
    private BigDecimal fees;

    @JsonProperty("pnl")
    private BigDecimal pnl;

    @JsonProperty("opened_at")
    private Long openedAt;

    @JsonProperty("closed_at")
    private Long closedAt;

    @JsonProperty("lots")
    private List<ClosedPositionLot> lots;

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public ClosedPosition market(String value) {
        this.market = value;
        return this;
    }

    /** Long or short. */
    public PositionSide side() {
        return side;
    }

    /** Sets side. */
    public ClosedPosition side(PositionSide value) {
        this.side = value;
        return this;
    }

    /** The size closed. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public ClosedPosition size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** The average entry price. */
    public BigDecimal entryPrice() {
        return entryPrice;
    }

    /** Sets entry_price. */
    public ClosedPosition entryPrice(BigDecimal value) {
        this.entryPrice = value;
        return this;
    }

    /** The average exit price. */
    public BigDecimal exitPrice() {
        return exitPrice;
    }

    /** Sets exit_price. */
    public ClosedPosition exitPrice(BigDecimal value) {
        this.exitPrice = value;
        return this;
    }

    /** The P&amp;L before fees. */
    public BigDecimal grossPnl() {
        return grossPnl;
    }

    /** Sets gross_pnl. */
    public ClosedPosition grossPnl(BigDecimal value) {
        this.grossPnl = value;
        return this;
    }

    /** The fees. */
    public BigDecimal fees() {
        return fees;
    }

    /** Sets fees. */
    public ClosedPosition fees(BigDecimal value) {
        this.fees = value;
        return this;
    }

    /** The P&amp;L after fees. */
    public BigDecimal pnl() {
        return pnl;
    }

    /** Sets pnl. */
    public ClosedPosition pnl(BigDecimal value) {
        this.pnl = value;
        return this;
    }

    /** When it opened. */
    public Long openedAt() {
        return openedAt;
    }

    /** Sets opened_at. */
    public ClosedPosition openedAt(Long value) {
        this.openedAt = value;
        return this;
    }

    /** When it was fully closed. */
    public Long closedAt() {
        return closedAt;
    }

    /** Sets closed_at. */
    public ClosedPosition closedAt(Long value) {
        this.closedAt = value;
        return this;
    }

    /** The pieces it was closed in. */
    public List<ClosedPositionLot> lots() {
        return lots;
    }

    /** Sets lots. */
    public ClosedPosition lots(List<ClosedPositionLot> value) {
        this.lots = value;
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
        if (!(other instanceof ClosedPosition)) {
            return false;
        }
        ClosedPosition o = (ClosedPosition) other;
        return Objects.equals(market, o.market)
            && Objects.equals(side, o.side)
            && Objects.equals(size, o.size)
            && Objects.equals(entryPrice, o.entryPrice)
            && Objects.equals(exitPrice, o.exitPrice)
            && Objects.equals(grossPnl, o.grossPnl)
            && Objects.equals(fees, o.fees)
            && Objects.equals(pnl, o.pnl)
            && Objects.equals(openedAt, o.openedAt)
            && Objects.equals(closedAt, o.closedAt)
            && Objects.equals(lots, o.lots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, side, size, entryPrice, exitPrice, grossPnl, fees, pnl, openedAt, closedAt, lots);
    }

    @Override
    public String toString() {
        return "ClosedPosition{" + "market=" + market + ", side=" + side + ", size=" + size + ", entry_price=" + entryPrice + ", exit_price=" + exitPrice + ", gross_pnl=" + grossPnl + ", fees=" + fees + ", pnl=" + pnl + ", opened_at=" + openedAt + ", closed_at=" + closedAt + ", lots=" + lots + "}";
    }
}
