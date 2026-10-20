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

/** ExitsBatchOperation */
public final class ExitsBatchOperation {
    @JsonProperty("op")
    private ExitsBatchOperationOp op;

    @JsonProperty("id")
    private String id;

    @JsonProperty("group")
    private ExitsLegGroup group;

    @JsonProperty("type")
    private CreateStrategyOrderType type;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("limit_price")
    private BigDecimal limitPrice;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("trail")
    private ExitsBatchOperationTrail trail;

    @JsonProperty("break_even")
    private ExitsBatchOperationBreakEven breakEven;

    @JsonProperty("clear")
    private List<ExitsBatchOperationClear> clear;

    /** What to do. */
    public ExitsBatchOperationOp op() {
        return op;
    }

    /** Sets op. */
    public ExitsBatchOperation op(ExitsBatchOperationOp value) {
        this.op = value;
        return this;
    }

    /** modify, cancel: the leg. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public ExitsBatchOperation id(String value) {
        this.id = value;
        return this;
    }

    /** place: the group of the new leg. */
    public ExitsLegGroup group() {
        return group;
    }

    /** Sets group. */
    public ExitsBatchOperation group(ExitsLegGroup value) {
        this.group = value;
        return this;
    }

    /** place: limit for a take-profit (the default); stop_market (the default) or stop_limit for a stop-loss. */
    public CreateStrategyOrderType type() {
        return type;
    }

    /** Sets type. */
    public ExitsBatchOperation type(CreateStrategyOrderType value) {
        this.type = value;
        return this;
    }

    /** The take-profit price, or the stop price. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public ExitsBatchOperation price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** The limit of a stop_limit leg. */
    public BigDecimal limitPrice() {
        return limitPrice;
    }

    /** Sets limit_price. */
    public ExitsBatchOperation limitPrice(BigDecimal value) {
        this.limitPrice = value;
        return this;
    }

    /** The size; absent on a place: what the group leaves of the position. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public ExitsBatchOperation size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** A stop-loss that follows the price: { distance } (a price difference) or { percent }, with activation_price to start only once the price reaches it. Without a price and an activation price it starts from the price now. null removes it. */
    public ExitsBatchOperationTrail trail() {
        return trail;
    }

    /** Sets trail. */
    public ExitsBatchOperation trail(ExitsBatchOperationTrail value) {
        this.trail = value;
        return this;
    }

    /** Move the stop-loss to the entry price (plus offset_ticks) once the price has gone your way by trigger_distance, trigger_percent or to trigger_price. Without a price, the stop is placed only then. null removes it. */
    public ExitsBatchOperationBreakEven breakEven() {
        return breakEven;
    }

    /** Sets break_even. */
    public ExitsBatchOperation breakEven(ExitsBatchOperationBreakEven value) {
        this.breakEven = value;
        return this;
    }

    /** modify: the settings to remove from a stop-loss (the same as sending them null). */
    public List<ExitsBatchOperationClear> clear() {
        return clear;
    }

    /** Sets clear. */
    public ExitsBatchOperation clear(List<ExitsBatchOperationClear> value) {
        this.clear = value;
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
        if (!(other instanceof ExitsBatchOperation)) {
            return false;
        }
        ExitsBatchOperation o = (ExitsBatchOperation) other;
        return Objects.equals(op, o.op)
            && Objects.equals(id, o.id)
            && Objects.equals(group, o.group)
            && Objects.equals(type, o.type)
            && Objects.equals(price, o.price)
            && Objects.equals(limitPrice, o.limitPrice)
            && Objects.equals(size, o.size)
            && Objects.equals(trail, o.trail)
            && Objects.equals(breakEven, o.breakEven)
            && Objects.equals(clear, o.clear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, id, group, type, price, limitPrice, size, trail, breakEven, clear);
    }

    @Override
    public String toString() {
        return "ExitsBatchOperation{" + "op=" + op + ", id=" + id + ", group=" + group + ", type=" + type + ", price=" + price + ", limit_price=" + limitPrice + ", size=" + size + ", trail=" + trail + ", break_even=" + breakEven + ", clear=" + clear + "}";
    }
}
