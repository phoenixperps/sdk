// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Where it comes from: the open position, your saved setting, or the market default. Open: a value from a newer API version is kept as is. */
public final class MarginSettingSource {
    /** position */
    public static final MarginSettingSource POSITION = new MarginSettingSource("position");

    /** saved */
    public static final MarginSettingSource SAVED = new MarginSettingSource("saved");

    /** default */
    public static final MarginSettingSource DEFAULT = new MarginSettingSource("default");

    private final String value;

    private MarginSettingSource(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static MarginSettingSource of(String value) {
        switch (value) {
            case "position":
                return POSITION;
            case "saved":
                return SAVED;
            case "default":
                return DEFAULT;
            default:
                return new MarginSettingSource(value);
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
        return o instanceof MarginSettingSource && ((MarginSettingSource) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
