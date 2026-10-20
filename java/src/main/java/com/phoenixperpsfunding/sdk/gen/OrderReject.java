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

/** OrderReject */
public final class OrderReject {
    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    /** The error code. */
    public String code() {
        return code;
    }

    /** Sets code. */
    public OrderReject code(String value) {
        this.code = value;
        return this;
    }

    /** What happened. */
    public String message() {
        return message;
    }

    /** Sets message. */
    public OrderReject message(String value) {
        this.message = value;
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
        if (!(other instanceof OrderReject)) {
            return false;
        }
        OrderReject o = (OrderReject) other;
        return Objects.equals(code, o.code)
            && Objects.equals(message, o.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }

    @Override
    public String toString() {
        return "OrderReject{" + "code=" + code + ", message=" + message + "}";
    }
}
