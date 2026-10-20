// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert. Open: a value from a newer API version is kept as is. */
public final class EventSource {
    /** api */
    public static final EventSource API = new EventSource("api");

    /** mcp */
    public static final EventSource MCP = new EventSource("mcp");

    /** platform */
    public static final EventSource PLATFORM = new EventSource("platform");

    /** engine */
    public static final EventSource ENGINE = new EventSource("engine");

    /** strategy */
    public static final EventSource STRATEGY = new EventSource("strategy");

    /** tradingview */
    public static final EventSource TRADINGVIEW = new EventSource("tradingview");

    private final String value;

    private EventSource(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static EventSource of(String value) {
        switch (value) {
            case "api":
                return API;
            case "mcp":
                return MCP;
            case "platform":
                return PLATFORM;
            case "engine":
                return ENGINE;
            case "strategy":
                return STRATEGY;
            case "tradingview":
                return TRADINGVIEW;
            default:
                return new EventSource(value);
        }
    }

    /** The value as the API writes it. */
    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EventSource && ((EventSource) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
