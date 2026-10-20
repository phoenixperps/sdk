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

/** ExitsBatchExpectedExit */
public final class ExitsBatchExpectedExit {
    @JsonProperty("id")
    private String id;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("price")
    private BigDecimal price;

    /** A leg. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public ExitsBatchExpectedExit id(String value) {
        this.id = value;
        return this;
    }

    /** Its size. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public ExitsBatchExpectedExit size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** Its price; null for a stop-loss that waits (as GET exits shows it). */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public ExitsBatchExpectedExit price(BigDecimal value) {
        this.price = value;
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
        if (!(other instanceof ExitsBatchExpectedExit)) {
            return false;
        }
        ExitsBatchExpectedExit o = (ExitsBatchExpectedExit) other;
        return Objects.equals(id, o.id)
            && Objects.equals(size, o.size)
            && Objects.equals(price, o.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size, price);
    }

    @Override
    public String toString() {
        return "ExitsBatchExpectedExit{" + "id=" + id + ", size=" + size + ", price=" + price + "}";
    }
}
