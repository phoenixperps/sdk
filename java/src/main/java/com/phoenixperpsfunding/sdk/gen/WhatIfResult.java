// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** What the order would do now: fill at once, rest, or be refused (reject says why). Open: a value from a newer API version is kept as is. */
public final class WhatIfResult {
    /** would_fill */
    public static final WhatIfResult WOULD_FILL = new WhatIfResult("would_fill");

    /** would_rest */
    public static final WhatIfResult WOULD_REST = new WhatIfResult("would_rest");

    /** would_reject */
    public static final WhatIfResult WOULD_REJECT = new WhatIfResult("would_reject");

    private final String value;

    private WhatIfResult(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static WhatIfResult of(String value) {
        switch (value) {
            case "would_fill":
                return WOULD_FILL;
            case "would_rest":
                return WOULD_REST;
            case "would_reject":
                return WOULD_REJECT;
            default:
                return new WhatIfResult(value);
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
        return o instanceof WhatIfResult && ((WhatIfResult) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
