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

/** The market's setting. The mode cannot change while a position or a working order is open on the market. */
public final class MarginSettingInput {
    @JsonProperty("margin_mode")
    private MarginMode marginMode;

    @JsonProperty("leverage")
    private Long leverage;

    /** isolated: the position has its own margin. cross: positions share the account's balance. */
    public MarginMode marginMode() {
        return marginMode;
    }

    /** Sets margin_mode. */
    public MarginSettingInput marginMode(MarginMode value) {
        this.marginMode = value;
        return this;
    }

    /** A whole number from 1 to the market's max_leverage. */
    public Long leverage() {
        return leverage;
    }

    /** Sets leverage. */
    public MarginSettingInput leverage(Long value) {
        this.leverage = value;
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
        if (!(other instanceof MarginSettingInput)) {
            return false;
        }
        MarginSettingInput o = (MarginSettingInput) other;
        return Objects.equals(marginMode, o.marginMode)
            && Objects.equals(leverage, o.leverage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marginMode, leverage);
    }

    @Override
    public String toString() {
        return "MarginSettingInput{" + "margin_mode=" + marginMode + ", leverage=" + leverage + "}";
    }
}
