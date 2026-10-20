// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** scaled: equal sizes, growing toward end_price, or shrinking. Open: a value from a newer API version is kept as is. */
public final class CreateStrategyDistribution {
    /** flat */
    public static final CreateStrategyDistribution FLAT = new CreateStrategyDistribution("flat");

    /** ascending */
    public static final CreateStrategyDistribution ASCENDING = new CreateStrategyDistribution("ascending");

    /** descending */
    public static final CreateStrategyDistribution DESCENDING = new CreateStrategyDistribution("descending");

    private final String value;

    private CreateStrategyDistribution(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static CreateStrategyDistribution of(String value) {
        switch (value) {
            case "flat":
                return FLAT;
            case "ascending":
                return ASCENDING;
            case "descending":
                return DESCENDING;
            default:
                return new CreateStrategyDistribution(value);
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
        return o instanceof CreateStrategyDistribution && ((CreateStrategyDistribution) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
