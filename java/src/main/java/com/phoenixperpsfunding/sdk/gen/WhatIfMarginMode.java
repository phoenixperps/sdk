// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** The margin mode the position would use. Open: a value from a newer API version is kept as is. */
public final class WhatIfMarginMode {
    /** isolated */
    public static final WhatIfMarginMode ISOLATED = new WhatIfMarginMode("isolated");

    /** cross */
    public static final WhatIfMarginMode CROSS = new WhatIfMarginMode("cross");

    private final String value;

    private WhatIfMarginMode(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static WhatIfMarginMode of(String value) {
        switch (value) {
            case "isolated":
                return ISOLATED;
            case "cross":
                return CROSS;
            default:
                return new WhatIfMarginMode(value);
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
        return o instanceof WhatIfMarginMode && ((WhatIfMarginMode) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
