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

/** Warning */
public final class Warning {
    @JsonProperty("code")
    private WarningCode code;

    @JsonProperty("level")
    private WarningLevel level;

    @JsonProperty("market")
    private String market;

    @JsonProperty("value")
    private BigDecimal value;

    /** What it is about. */
    public WarningCode code() {
        return code;
    }

    /** Sets code. */
    public Warning code(WarningCode value) {
        this.code = value;
        return this;
    }

    /** How serious. */
    public WarningLevel level() {
        return level;
    }

    /** Sets level. */
    public Warning level(WarningLevel value) {
        this.level = value;
        return this;
    }

    /** The market it is about. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public Warning market(String value) {
        this.market = value;
        return this;
    }

    /** The figure behind it, in percent. */
    public BigDecimal value() {
        return value;
    }

    /** Sets value. */
    public Warning value(BigDecimal value) {
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
        if (!(other instanceof Warning)) {
            return false;
        }
        Warning o = (Warning) other;
        return Objects.equals(code, o.code)
            && Objects.equals(level, o.level)
            && Objects.equals(market, o.market)
            && Objects.equals(value, o.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, level, market, value);
    }

    @Override
    public String toString() {
        return "Warning{" + "code=" + code + ", level=" + level + ", market=" + market + ", value=" + value + "}";
    }
}
