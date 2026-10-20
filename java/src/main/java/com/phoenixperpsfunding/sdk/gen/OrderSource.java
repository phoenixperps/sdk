// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Where it was placed: this API, an AI assistant (the MCP server), a trading platform, a strategy, a TradingView alert, or the system (a liquidation). Open: a value from a newer API version is kept as is. */
public final class OrderSource {
    /** api */
    public static final OrderSource API = new OrderSource("api");

    /** mcp */
    public static final OrderSource MCP = new OrderSource("mcp");

    /** platform */
    public static final OrderSource PLATFORM = new OrderSource("platform");

    /** strategy */
    public static final OrderSource STRATEGY = new OrderSource("strategy");

    /** tradingview */
    public static final OrderSource TRADINGVIEW = new OrderSource("tradingview");

    /** system */
    public static final OrderSource SYSTEM = new OrderSource("system");

    private final String value;

    private OrderSource(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static OrderSource of(String value) {
        switch (value) {
            case "api":
                return API;
            case "mcp":
                return MCP;
            case "platform":
                return PLATFORM;
            case "strategy":
                return STRATEGY;
            case "tradingview":
                return TRADINGVIEW;
            case "system":
                return SYSTEM;
            default:
                return new OrderSource(value);
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
        return o instanceof OrderSource && ((OrderSource) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
