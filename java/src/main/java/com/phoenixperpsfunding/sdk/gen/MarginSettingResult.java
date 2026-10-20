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

/** MarginSettingResult */
public final class MarginSettingResult {
    @JsonProperty("market")
    private String market;

    @JsonProperty("margin_mode")
    private MarginMode marginMode;

    @JsonProperty("leverage")
    private Long leverage;

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public MarginSettingResult market(String value) {
        this.market = value;
        return this;
    }

    /** The margin mode. */
    public MarginMode marginMode() {
        return marginMode;
    }

    /** Sets margin_mode. */
    public MarginSettingResult marginMode(MarginMode value) {
        this.marginMode = value;
        return this;
    }

    /** The leverage. */
    public Long leverage() {
        return leverage;
    }

    /** Sets leverage. */
    public MarginSettingResult leverage(Long value) {
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
        if (!(other instanceof MarginSettingResult)) {
            return false;
        }
        MarginSettingResult o = (MarginSettingResult) other;
        return Objects.equals(market, o.market)
            && Objects.equals(marginMode, o.marginMode)
            && Objects.equals(leverage, o.leverage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, marginMode, leverage);
    }

    @Override
    public String toString() {
        return "MarginSettingResult{" + "market=" + market + ", margin_mode=" + marginMode + ", leverage=" + leverage + "}";
    }
}
