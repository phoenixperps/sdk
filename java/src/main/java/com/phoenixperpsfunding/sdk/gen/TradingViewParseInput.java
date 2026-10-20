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

/** A message to read, sent nowhere. */
public final class TradingViewParseInput {
    @JsonProperty("message")
    private String message;

    @JsonProperty("hook_id")
    private String hookId;

    /** The alert's message, as TradingView would send it. */
    public String message() {
        return message;
    }

    /** Sets message. */
    public TradingViewParseInput message(String value) {
        this.message = value;
        return this;
    }

    /** A link: the plan is then worked out on its account and settings, as of now. */
    public String hookId() {
        return hookId;
    }

    /** Sets hook_id. */
    public TradingViewParseInput hookId(String value) {
        this.hookId = value;
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
        if (!(other instanceof TradingViewParseInput)) {
            return false;
        }
        TradingViewParseInput o = (TradingViewParseInput) other;
        return Objects.equals(message, o.message)
            && Objects.equals(hookId, o.hookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, hookId);
    }

    @Override
    public String toString() {
        return "TradingViewParseInput{" + "message=" + message + ", hook_id=" + hookId + "}";
    }
}
