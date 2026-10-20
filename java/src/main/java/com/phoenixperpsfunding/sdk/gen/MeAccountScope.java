// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** all: every account of the environment, new ones too. selected: only those listed. Open: a value from a newer API version is kept as is. */
public final class MeAccountScope {
    /** all */
    public static final MeAccountScope ALL = new MeAccountScope("all");

    /** selected */
    public static final MeAccountScope SELECTED = new MeAccountScope("selected");

    private final String value;

    private MeAccountScope(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static MeAccountScope of(String value) {
        switch (value) {
            case "all":
                return ALL;
            case "selected":
                return SELECTED;
            default:
                return new MeAccountScope(value);
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
        return o instanceof MeAccountScope && ((MeAccountScope) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
