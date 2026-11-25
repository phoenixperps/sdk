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

/** What to change on an armed alert: at least one field. */
public final class ChangePriceAlert {
    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("note")
    private String note;

    /** A new level; its condition is set again from the price now. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public ChangePriceAlert price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** A new note; null removes it. */
    public String note() {
        return note;
    }

    /** Sets note. */
    public ChangePriceAlert note(String value) {
        this.note = value;
        if (value != null) {
            extra.remove("note");
        }
        return this;
    }

    /** Sends note as null (the API: null removes it). */
    public ChangePriceAlert clearNote() {
        this.note = null;
        // a NullNode, written as null (the mapper skips a Java null)
        extra.put("note", com.fasterxml.jackson.databind.node.NullNode.getInstance());
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
        if (!(other instanceof ChangePriceAlert)) {
            return false;
        }
        ChangePriceAlert o = (ChangePriceAlert) other;
        return Objects.equals(price, o.price)
            && Objects.equals(note, o.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, note);
    }

    @Override
    public String toString() {
        return "ChangePriceAlert{" + "price=" + price + ", note=" + note + "}";
    }
}
