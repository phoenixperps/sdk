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

/** A level on a market, told once when the price reaches it (alert.triggered: the account stream, webhooks, GET /v1/events). It never places or changes an order. */
public final class PriceAlert {
    @JsonProperty("id")
    private String id;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("market")
    private String market;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("condition")
    private PriceAlertCondition condition;

    @JsonProperty("note")
    private String note;

    @JsonProperty("status")
    private PriceAlertStatus status;

    @JsonProperty("price_source")
    private PriceAlertPriceSource priceSource;

    @JsonProperty("trigger_price")
    private BigDecimal triggerPrice;

    @JsonProperty("triggered_at")
    private Long triggeredAt;

    @JsonProperty("disabled_reason")
    private PriceAlertDisabledReason disabledReason;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;

    /** The alert. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public PriceAlert id(String value) {
        this.id = value;
        return this;
    }

    /** The account number. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public PriceAlert accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** A market id: the asset and USD; an option adds its venue or hours after a dot. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public PriceAlert market(String value) {
        this.market = value;
        return this;
    }

    /** Its level. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public PriceAlert price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** above: it fires once the price reaches the level from below; below: from above. Set from the price when the level was set. */
    public PriceAlertCondition condition() {
        return condition;
    }

    /** Sets condition. */
    public PriceAlert condition(PriceAlertCondition value) {
        this.condition = value;
        return this;
    }

    /** Your note, sent with it. */
    public String note() {
        return note;
    }

    /** Sets note. */
    public PriceAlert note(String value) {
        this.note = value;
        return this;
    }

    /** armed: it waits for its level. triggered: it fired (rearm arms it again). disabled: its account or market went away before it fired. */
    public PriceAlertStatus status() {
        return status;
    }

    /** Sets status. */
    public PriceAlert status(PriceAlertStatus value) {
        this.status = value;
        return this;
    }

    /** The price it watches: the market's last trade (the middle of the book until there is one), the price trailing stops follow. */
    public PriceAlertPriceSource priceSource() {
        return priceSource;
    }

    /** Sets price_source. */
    public PriceAlert priceSource(PriceAlertPriceSource value) {
        this.priceSource = value;
        return this;
    }

    /** The price it fired at. */
    public BigDecimal triggerPrice() {
        return triggerPrice;
    }

    /** Sets trigger_price. */
    public PriceAlert triggerPrice(BigDecimal value) {
        this.triggerPrice = value;
        return this;
    }

    /** When it fired. */
    public Long triggeredAt() {
        return triggeredAt;
    }

    /** Sets triggered_at. */
    public PriceAlert triggeredAt(Long value) {
        this.triggeredAt = value;
        return this;
    }

    /** Why it was disabled. */
    public PriceAlertDisabledReason disabledReason() {
        return disabledReason;
    }

    /** Sets disabled_reason. */
    public PriceAlert disabledReason(PriceAlertDisabledReason value) {
        this.disabledReason = value;
        return this;
    }

    /** When it was set. */
    public Long createdAt() {
        return createdAt;
    }

    /** Sets created_at. */
    public PriceAlert createdAt(Long value) {
        this.createdAt = value;
        return this;
    }

    /** Its latest change. */
    public Long updatedAt() {
        return updatedAt;
    }

    /** Sets updated_at. */
    public PriceAlert updatedAt(Long value) {
        this.updatedAt = value;
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
        if (!(other instanceof PriceAlert)) {
            return false;
        }
        PriceAlert o = (PriceAlert) other;
        return Objects.equals(id, o.id)
            && Objects.equals(accountId, o.accountId)
            && Objects.equals(market, o.market)
            && Objects.equals(price, o.price)
            && Objects.equals(condition, o.condition)
            && Objects.equals(note, o.note)
            && Objects.equals(status, o.status)
            && Objects.equals(priceSource, o.priceSource)
            && Objects.equals(triggerPrice, o.triggerPrice)
            && Objects.equals(triggeredAt, o.triggeredAt)
            && Objects.equals(disabledReason, o.disabledReason)
            && Objects.equals(createdAt, o.createdAt)
            && Objects.equals(updatedAt, o.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, market, price, condition, note, status, priceSource, triggerPrice, triggeredAt, disabledReason, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "PriceAlert{" + "id=" + id + ", account_id=" + accountId + ", market=" + market + ", price=" + price + ", condition=" + condition + ", note=" + note + ", status=" + status + ", price_source=" + priceSource + ", trigger_price=" + triggerPrice + ", triggered_at=" + triggeredAt + ", disabled_reason=" + disabledReason + ", created_at=" + createdAt + ", updated_at=" + updatedAt + "}";
    }
}
