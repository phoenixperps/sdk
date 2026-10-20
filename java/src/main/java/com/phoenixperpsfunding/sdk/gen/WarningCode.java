// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** What it is about. Open: a value from a newer API version is kept as is. */
public final class WarningCode {
    /** daily_loss_used */
    public static final WarningCode DAILY_LOSS_USED = new WarningCode("daily_loss_used");

    /** max_drawdown_used */
    public static final WarningCode MAX_DRAWDOWN_USED = new WarningCode("max_drawdown_used");

    /** liquidation_near */
    public static final WarningCode LIQUIDATION_NEAR = new WarningCode("liquidation_near");

    /** no_stop_loss */
    public static final WarningCode NO_STOP_LOSS = new WarningCode("no_stop_loss");

    /** margin_used */
    public static final WarningCode MARGIN_USED = new WarningCode("margin_used");

    /** order_without_position */
    public static final WarningCode ORDER_WITHOUT_POSITION = new WarningCode("order_without_position");

    private final String value;

    private WarningCode(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static WarningCode of(String value) {
        switch (value) {
            case "daily_loss_used":
                return DAILY_LOSS_USED;
            case "max_drawdown_used":
                return MAX_DRAWDOWN_USED;
            case "liquidation_near":
                return LIQUIDATION_NEAR;
            case "no_stop_loss":
                return NO_STOP_LOSS;
            case "margin_used":
                return MARGIN_USED;
            case "order_without_position":
                return ORDER_WITHOUT_POSITION;
            default:
                return new WarningCode(value);
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
        return o instanceof WarningCode && ((WarningCode) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
