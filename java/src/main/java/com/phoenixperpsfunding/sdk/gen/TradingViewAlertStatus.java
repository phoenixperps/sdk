// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** What it did. Open: a value from a newer API version is kept as is. */
public final class TradingViewAlertStatus {
    /** processing */
    public static final TradingViewAlertStatus PROCESSING = new TradingViewAlertStatus("processing");

    /** placed */
    public static final TradingViewAlertStatus PLACED = new TradingViewAlertStatus("placed");

    /** partial */
    public static final TradingViewAlertStatus PARTIAL = new TradingViewAlertStatus("partial");

    /** dry_run */
    public static final TradingViewAlertStatus DRY_RUN = new TradingViewAlertStatus("dry_run");

    /** duplicate */
    public static final TradingViewAlertStatus DUPLICATE = new TradingViewAlertStatus("duplicate");

    /** skipped */
    public static final TradingViewAlertStatus SKIPPED = new TradingViewAlertStatus("skipped");

    /** refused */
    public static final TradingViewAlertStatus REFUSED = new TradingViewAlertStatus("refused");

    /** failed */
    public static final TradingViewAlertStatus FAILED = new TradingViewAlertStatus("failed");

    /** rate_limited */
    public static final TradingViewAlertStatus RATE_LIMITED = new TradingViewAlertStatus("rate_limited");

    private final String value;

    private TradingViewAlertStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static TradingViewAlertStatus of(String value) {
        switch (value) {
            case "processing":
                return PROCESSING;
            case "placed":
                return PLACED;
            case "partial":
                return PARTIAL;
            case "dry_run":
                return DRY_RUN;
            case "duplicate":
                return DUPLICATE;
            case "skipped":
                return SKIPPED;
            case "refused":
                return REFUSED;
            case "failed":
                return FAILED;
            case "rate_limited":
                return RATE_LIMITED;
            default:
                return new TradingViewAlertStatus(value);
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
        return o instanceof TradingViewAlertStatus && ((TradingViewAlertStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
