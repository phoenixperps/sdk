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

/** ClosedPositionLot */
public final class ClosedPositionLot {
    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("entry_price")
    private BigDecimal entryPrice;

    @JsonProperty("exit_price")
    private BigDecimal exitPrice;

    @JsonProperty("pnl")
    private BigDecimal pnl;

    @JsonProperty("fees")
    private BigDecimal fees;

    @JsonProperty("opened_at")
    private Long openedAt;

    @JsonProperty("closed_at")
    private Long closedAt;

    /** The size. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public ClosedPositionLot size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** Entry. */
    public BigDecimal entryPrice() {
        return entryPrice;
    }

    /** Sets entry_price. */
    public ClosedPositionLot entryPrice(BigDecimal value) {
        this.entryPrice = value;
        return this;
    }

    /** Exit. */
    public BigDecimal exitPrice() {
        return exitPrice;
    }

    /** Sets exit_price. */
    public ClosedPositionLot exitPrice(BigDecimal value) {
        this.exitPrice = value;
        return this;
    }

    /** P&amp;L after fees. */
    public BigDecimal pnl() {
        return pnl;
    }

    /** Sets pnl. */
    public ClosedPositionLot pnl(BigDecimal value) {
        this.pnl = value;
        return this;
    }

    /** Fees. */
    public BigDecimal fees() {
        return fees;
    }

    /** Sets fees. */
    public ClosedPositionLot fees(BigDecimal value) {
        this.fees = value;
        return this;
    }

    /** Opened. */
    public Long openedAt() {
        return openedAt;
    }

    /** Sets opened_at. */
    public ClosedPositionLot openedAt(Long value) {
        this.openedAt = value;
        return this;
    }

    /** Closed. */
    public Long closedAt() {
        return closedAt;
    }

    /** Sets closed_at. */
    public ClosedPositionLot closedAt(Long value) {
        this.closedAt = value;
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
        if (!(other instanceof ClosedPositionLot)) {
            return false;
        }
        ClosedPositionLot o = (ClosedPositionLot) other;
        return Objects.equals(size, o.size)
            && Objects.equals(entryPrice, o.entryPrice)
            && Objects.equals(exitPrice, o.exitPrice)
            && Objects.equals(pnl, o.pnl)
            && Objects.equals(fees, o.fees)
            && Objects.equals(openedAt, o.openedAt)
            && Objects.equals(closedAt, o.closedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, entryPrice, exitPrice, pnl, fees, openedAt, closedAt);
    }

    @Override
    public String toString() {
        return "ClosedPositionLot{" + "size=" + size + ", entry_price=" + entryPrice + ", exit_price=" + exitPrice + ", pnl=" + pnl + ", fees=" + fees + ", opened_at=" + openedAt + ", closed_at=" + closedAt + "}";
    }
}
