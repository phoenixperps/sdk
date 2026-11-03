// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** The price it watches: the market's last trade (the middle of the book until there is one), the price trailing stops follow. Open: a value from a newer API version is kept as is. */
public final class PriceAlertPriceSource {
    /** last */
    public static final PriceAlertPriceSource LAST = new PriceAlertPriceSource("last");

    private final String value;

    private PriceAlertPriceSource(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static PriceAlertPriceSource of(String value) {
        switch (value) {
            case "last":
                return LAST;
            default:
                return new PriceAlertPriceSource(value);
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
        return o instanceof PriceAlertPriceSource && ((PriceAlertPriceSource) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
