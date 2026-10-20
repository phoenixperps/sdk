// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** from_alert: the size the alert gives. qty, usd, pct_balance: the link's own. Open: a value from a newer API version is kept as is. */
public final class TradingViewHookSizingMode {
    /** from_alert */
    public static final TradingViewHookSizingMode FROM_ALERT = new TradingViewHookSizingMode("from_alert");

    /** qty */
    public static final TradingViewHookSizingMode QTY = new TradingViewHookSizingMode("qty");

    /** usd */
    public static final TradingViewHookSizingMode USD = new TradingViewHookSizingMode("usd");

    /** pct_balance */
    public static final TradingViewHookSizingMode PCT_BALANCE = new TradingViewHookSizingMode("pct_balance");

    private final String value;

    private TradingViewHookSizingMode(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static TradingViewHookSizingMode of(String value) {
        switch (value) {
            case "from_alert":
                return FROM_ALERT;
            case "qty":
                return QTY;
            case "usd":
                return USD;
            case "pct_balance":
                return PCT_BALANCE;
            default:
                return new TradingViewHookSizingMode(value);
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
        return o instanceof TradingViewHookSizingMode && ((TradingViewHookSizingMode) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
