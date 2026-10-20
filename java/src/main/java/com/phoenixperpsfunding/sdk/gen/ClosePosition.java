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

/** What to close. */
public final class ClosePosition {
    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("client_order_id")
    private String clientOrderId;

    /** How much to close; absent: the whole position. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public ClosePosition size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** Your own id for the close order (a partial close). */
    public String clientOrderId() {
        return clientOrderId;
    }

    /** Sets client_order_id. */
    public ClosePosition clientOrderId(String value) {
        this.clientOrderId = value;
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
        if (!(other instanceof ClosePosition)) {
            return false;
        }
        ClosePosition o = (ClosePosition) other;
        return Objects.equals(size, o.size)
            && Objects.equals(clientOrderId, o.clientOrderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, clientOrderId);
    }

    @Override
    public String toString() {
        return "ClosePosition{" + "size=" + size + ", client_order_id=" + clientOrderId + "}";
    }
}
