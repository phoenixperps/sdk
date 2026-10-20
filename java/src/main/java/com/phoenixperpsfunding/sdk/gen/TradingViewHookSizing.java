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

/** TradingViewHookSizing */
public final class TradingViewHookSizing {
    @JsonProperty("mode")
    private TradingViewHookSizingMode mode;

    @JsonProperty("value")
    private BigDecimal value;

    /** from_alert: the size the alert gives. qty, usd, pct_balance: the link's own. */
    public TradingViewHookSizingMode mode() {
        return mode;
    }

    /** Sets mode. */
    public TradingViewHookSizing mode(TradingViewHookSizingMode value) {
        this.mode = value;
        return this;
    }

    /** The link's size, in its mode's unit. */
    public BigDecimal value() {
        return value;
    }

    /** Sets value. */
    public TradingViewHookSizing value(BigDecimal value) {
        this.value = value;
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
        if (!(other instanceof TradingViewHookSizing)) {
            return false;
        }
        TradingViewHookSizing o = (TradingViewHookSizing) other;
        return Objects.equals(mode, o.mode)
            && Objects.equals(value, o.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, value);
    }

    @Override
    public String toString() {
        return "TradingViewHookSizing{" + "mode=" + mode + ", value=" + value + "}";
    }
}
