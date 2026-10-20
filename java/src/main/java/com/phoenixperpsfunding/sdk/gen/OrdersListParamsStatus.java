// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** OrdersListParamsStatus Open: a value from a newer API version is kept as is. */
public final class OrdersListParamsStatus {
    /** all */
    public static final OrdersListParamsStatus ALL = new OrdersListParamsStatus("all");

    /** working */
    public static final OrdersListParamsStatus WORKING = new OrdersListParamsStatus("working");

    /** history */
    public static final OrdersListParamsStatus HISTORY = new OrdersListParamsStatus("history");

    private final String value;

    private OrdersListParamsStatus(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static OrdersListParamsStatus of(String value) {
        switch (value) {
            case "all":
                return ALL;
            case "working":
                return WORKING;
            case "history":
                return HISTORY;
            default:
                return new OrdersListParamsStatus(value);
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
        return o instanceof OrdersListParamsStatus && ((OrdersListParamsStatus) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
