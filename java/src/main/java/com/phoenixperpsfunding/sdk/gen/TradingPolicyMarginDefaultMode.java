// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** The margin mode of a market you never set. Open: a value from a newer API version is kept as is. */
public final class TradingPolicyMarginDefaultMode {
    /** isolated */
    public static final TradingPolicyMarginDefaultMode ISOLATED = new TradingPolicyMarginDefaultMode("isolated");

    /** cross */
    public static final TradingPolicyMarginDefaultMode CROSS = new TradingPolicyMarginDefaultMode("cross");

    private final String value;

    private TradingPolicyMarginDefaultMode(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static TradingPolicyMarginDefaultMode of(String value) {
        switch (value) {
            case "isolated":
                return ISOLATED;
            case "cross":
                return CROSS;
            default:
                return new TradingPolicyMarginDefaultMode(value);
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
        return o instanceof TradingPolicyMarginDefaultMode && ((TradingPolicyMarginDefaultMode) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
