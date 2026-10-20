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

/** Through the MCP server (an AI assistant): how its trades are confirmed and its budget. Null for any other call. */
public final class MeAssistant {
    @JsonProperty("confirm_trades")
    private Boolean confirmTrades;

    @JsonProperty("confirm_seconds")
    private Long confirmSeconds;

    @JsonProperty("budget")
    private MeAssistantBudget budget;

    /** Each trade is shown first and placed only when repeated with its confirmation (preview, then confirm). */
    public Boolean confirmTrades() {
        return confirmTrades;
    }

    /** Sets confirm_trades. */
    public MeAssistant confirmTrades(Boolean value) {
        this.confirmTrades = value;
        return this;
    }

    /** How long a preview's confirmation holds, in seconds. */
    public Long confirmSeconds() {
        return confirmSeconds;
    }

    /** Sets confirm_seconds. */
    public MeAssistant confirmSeconds(Long value) {
        this.confirmSeconds = value;
        return this;
    }

    /** The AI assistant's own budget, on top of the others. */
    public MeAssistantBudget budget() {
        return budget;
    }

    /** Sets budget. */
    public MeAssistant budget(MeAssistantBudget value) {
        this.budget = value;
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
        if (!(other instanceof MeAssistant)) {
            return false;
        }
        MeAssistant o = (MeAssistant) other;
        return Objects.equals(confirmTrades, o.confirmTrades)
            && Objects.equals(confirmSeconds, o.confirmSeconds)
            && Objects.equals(budget, o.budget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmTrades, confirmSeconds, budget);
    }

    @Override
    public String toString() {
        return "MeAssistant{" + "confirm_trades=" + confirmTrades + ", confirm_seconds=" + confirmSeconds + ", budget=" + budget + "}";
    }
}
