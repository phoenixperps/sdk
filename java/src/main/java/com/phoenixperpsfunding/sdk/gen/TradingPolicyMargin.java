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

/** TradingPolicyMargin */
public final class TradingPolicyMargin {
    @JsonProperty("default_mode")
    private TradingPolicyMarginDefaultMode defaultMode;

    @JsonProperty("maintenance_margin_pct")
    private BigDecimal maintenanceMarginPct;

    /** The margin mode of a market you never set. */
    public TradingPolicyMarginDefaultMode defaultMode() {
        return defaultMode;
    }

    /** Sets default_mode. */
    public TradingPolicyMargin defaultMode(TradingPolicyMarginDefaultMode value) {
        this.defaultMode = value;
        return this;
    }

    /** The share of a position's margin kept as maintenance, in percent. */
    public BigDecimal maintenanceMarginPct() {
        return maintenanceMarginPct;
    }

    /** Sets maintenance_margin_pct. */
    public TradingPolicyMargin maintenanceMarginPct(BigDecimal value) {
        this.maintenanceMarginPct = value;
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
        if (!(other instanceof TradingPolicyMargin)) {
            return false;
        }
        TradingPolicyMargin o = (TradingPolicyMargin) other;
        return Objects.equals(defaultMode, o.defaultMode)
            && Objects.equals(maintenanceMarginPct, o.maintenanceMarginPct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultMode, maintenanceMarginPct);
    }

    @Override
    public String toString() {
        return "TradingPolicyMargin{" + "default_mode=" + defaultMode + ", maintenance_margin_pct=" + maintenanceMarginPct + "}";
    }
}
