// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Why it was disabled. Open: a value from a newer API version is kept as is. */
public final class PriceAlertDisabledReason {
    /** account_removed */
    public static final PriceAlertDisabledReason ACCOUNT_REMOVED = new PriceAlertDisabledReason("account_removed");

    /** market_removed */
    public static final PriceAlertDisabledReason MARKET_REMOVED = new PriceAlertDisabledReason("market_removed");

    private final String value;

    private PriceAlertDisabledReason(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static PriceAlertDisabledReason of(String value) {
        switch (value) {
            case "account_removed":
                return ACCOUNT_REMOVED;
            case "market_removed":
                return MARKET_REMOVED;
            default:
                return new PriceAlertDisabledReason(value);
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
        return o instanceof PriceAlertDisabledReason && ((PriceAlertDisabledReason) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
