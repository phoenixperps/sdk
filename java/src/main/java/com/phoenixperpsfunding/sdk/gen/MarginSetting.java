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

/** MarginSetting */
public final class MarginSetting {
    @JsonProperty("market")
    private String market;

    @JsonProperty("leverage")
    private Long leverage;

    @JsonProperty("margin_mode")
    private MarginMode marginMode;

    @JsonProperty("max_leverage")
    private Long maxLeverage;

    @JsonProperty("source")
    private MarginSettingSource source;

    @JsonProperty("locked")
    private Boolean locked;

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public MarginSetting market(String value) {
        this.market = value;
        return this;
    }

    /** The leverage new positions on this market get. */
    public Long leverage() {
        return leverage;
    }

    /** Sets leverage. */
    public MarginSetting leverage(Long value) {
        this.leverage = value;
        return this;
    }

    /** Isolated: each position holds its own margin. Cross: positions share the balance. */
    public MarginMode marginMode() {
        return marginMode;
    }

    /** Sets margin_mode. */
    public MarginSetting marginMode(MarginMode value) {
        this.marginMode = value;
        return this;
    }

    /** The highest this account may use on the market (its trading rule's). null: the market is not available on this account. */
    public Long maxLeverage() {
        return maxLeverage;
    }

    /** Sets max_leverage. */
    public MarginSetting maxLeverage(Long value) {
        this.maxLeverage = value;
        return this;
    }

    /** Where it comes from: the open position, your saved setting, or the market default. */
    public MarginSettingSource source() {
        return source;
    }

    /** Sets source. */
    public MarginSetting source(MarginSettingSource value) {
        this.source = value;
        return this;
    }

    /** A position or a working order on the market: the mode cannot change now. */
    public Boolean locked() {
        return locked;
    }

    /** Sets locked. */
    public MarginSetting locked(Boolean value) {
        this.locked = value;
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
        if (!(other instanceof MarginSetting)) {
            return false;
        }
        MarginSetting o = (MarginSetting) other;
        return Objects.equals(market, o.market)
            && Objects.equals(leverage, o.leverage)
            && Objects.equals(marginMode, o.marginMode)
            && Objects.equals(maxLeverage, o.maxLeverage)
            && Objects.equals(source, o.source)
            && Objects.equals(locked, o.locked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, leverage, marginMode, maxLeverage, source, locked);
    }

    @Override
    public String toString() {
        return "MarginSetting{" + "market=" + market + ", leverage=" + leverage + ", margin_mode=" + marginMode + ", max_leverage=" + maxLeverage + ", source=" + source + ", locked=" + locked + "}";
    }
}
