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

/** A new price alert. Its condition (above or below) is set from the price now. */
public final class CreatePriceAlert {
    @JsonProperty("market")
    private String market;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("note")
    private String note;

    /** A market id: the asset and USD; an option adds its venue or hours after a dot. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public CreatePriceAlert market(String value) {
        this.market = value;
        return this;
    }

    /** The level; not the price now. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public CreatePriceAlert price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** Your note (at most 280 characters). */
    public String note() {
        return note;
    }

    /** Sets note. */
    public CreatePriceAlert note(String value) {
        this.note = value;
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
        if (!(other instanceof CreatePriceAlert)) {
            return false;
        }
        CreatePriceAlert o = (CreatePriceAlert) other;
        return Objects.equals(market, o.market)
            && Objects.equals(price, o.price)
            && Objects.equals(note, o.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, price, note);
    }

    @Override
    public String toString() {
        return "CreatePriceAlert{" + "market=" + market + ", price=" + price + ", note=" + note + "}";
    }
}
