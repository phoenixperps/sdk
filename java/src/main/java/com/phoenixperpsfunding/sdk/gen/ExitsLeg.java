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

/** ExitsLeg */
public final class ExitsLeg {
    @JsonProperty("id")
    private String id;

    @JsonProperty("group")
    private ExitsLegGroup group;

    @JsonProperty("type")
    private CreateStrategyOrderType type;

    @JsonProperty("status")
    private ExitsLegStatus status;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("limit_price")
    private BigDecimal limitPrice;

    @JsonProperty("trail")
    private ExitsLegTrail trail;

    @JsonProperty("break_even")
    private ExitsLegBreakEven breakEven;

    /** The leg: an order of this API, also listed under the account's orders. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public ExitsLeg id(String value) {
        this.id = value;
        return this;
    }

    /** take_profit: a limit on the profit side. stop_loss: a stop on the loss side. */
    public ExitsLegGroup group() {
        return group;
    }

    /** Sets group. */
    public ExitsLeg group(ExitsLegGroup value) {
        this.group = value;
        return this;
    }

    /** take_profit legs are limits; stop_loss legs are stop_market or stop_limit. */
    public CreateStrategyOrderType type() {
        return type;
    }

    /** Sets type. */
    public ExitsLeg type(CreateStrategyOrderType value) {
        this.type = value;
        return this;
    }

    /** The order's status; waiting: a stop-loss not placed yet (it waits for its activation price or its break-even). */
    public ExitsLegStatus status() {
        return status;
    }

    /** Sets status. */
    public ExitsLeg status(ExitsLegStatus value) {
        this.status = value;
        return this;
    }

    /** Its size. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public ExitsLeg size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** The take-profit price, or the stop price. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public ExitsLeg price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** The limit of a stop_limit leg. */
    public BigDecimal limitPrice() {
        return limitPrice;
    }

    /** Sets limit_price. */
    public ExitsLeg limitPrice(BigDecimal value) {
        this.limitPrice = value;
        return this;
    }

    /** A trailing stop-loss: how it follows the price. */
    public ExitsLegTrail trail() {
        return trail;
    }

    /** Sets trail. */
    public ExitsLeg trail(ExitsLegTrail value) {
        this.trail = value;
        return this;
    }

    /** A stop-loss moved to the entry price once the price has gone your way far enough. */
    public ExitsLegBreakEven breakEven() {
        return breakEven;
    }

    /** Sets break_even. */
    public ExitsLeg breakEven(ExitsLegBreakEven value) {
        this.breakEven = value;
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
        if (!(other instanceof ExitsLeg)) {
            return false;
        }
        ExitsLeg o = (ExitsLeg) other;
        return Objects.equals(id, o.id)
            && Objects.equals(group, o.group)
            && Objects.equals(type, o.type)
            && Objects.equals(status, o.status)
            && Objects.equals(size, o.size)
            && Objects.equals(price, o.price)
            && Objects.equals(limitPrice, o.limitPrice)
            && Objects.equals(trail, o.trail)
            && Objects.equals(breakEven, o.breakEven);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group, type, status, size, price, limitPrice, trail, breakEven);
    }

    @Override
    public String toString() {
        return "ExitsLeg{" + "id=" + id + ", group=" + group + ", type=" + type + ", status=" + status + ", size=" + size + ", price=" + price + ", limit_price=" + limitPrice + ", trail=" + trail + ", break_even=" + breakEven + "}";
    }
}
