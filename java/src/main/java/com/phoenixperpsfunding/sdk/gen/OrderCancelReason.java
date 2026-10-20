// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Why it was canceled. Open: a value from a newer API version is kept as is. */
public final class OrderCancelReason {
    /** user */
    public static final OrderCancelReason USER = new OrderCancelReason("user");

    /** cancel_all */
    public static final OrderCancelReason CANCEL_ALL = new OrderCancelReason("cancel_all");

    /** flatten */
    public static final OrderCancelReason FLATTEN = new OrderCancelReason("flatten");

    /** close_all */
    public static final OrderCancelReason CLOSE_ALL = new OrderCancelReason("close_all");

    /** position_closed */
    public static final OrderCancelReason POSITION_CLOSED = new OrderCancelReason("position_closed");

    /** position_reversed */
    public static final OrderCancelReason POSITION_REVERSED = new OrderCancelReason("position_reversed");

    /** close_assist */
    public static final OrderCancelReason CLOSE_ASSIST = new OrderCancelReason("close_assist");

    /** oco */
    public static final OrderCancelReason OCO = new OrderCancelReason("oco");

    /** replaced */
    public static final OrderCancelReason REPLACED = new OrderCancelReason("replaced");

    /** strategy */
    public static final OrderCancelReason STRATEGY = new OrderCancelReason("strategy");

    /** key_loss_stop */
    public static final OrderCancelReason KEY_LOSS_STOP = new OrderCancelReason("key_loss_stop");

    /** risk_rule */
    public static final OrderCancelReason RISK_RULE = new OrderCancelReason("risk_rule");

    /** system_restart */
    public static final OrderCancelReason SYSTEM_RESTART = new OrderCancelReason("system_restart");

    /** external */
    public static final OrderCancelReason EXTERNAL = new OrderCancelReason("external");

    private final String value;

    private OrderCancelReason(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static OrderCancelReason of(String value) {
        switch (value) {
            case "user":
                return USER;
            case "cancel_all":
                return CANCEL_ALL;
            case "flatten":
                return FLATTEN;
            case "close_all":
                return CLOSE_ALL;
            case "position_closed":
                return POSITION_CLOSED;
            case "position_reversed":
                return POSITION_REVERSED;
            case "close_assist":
                return CLOSE_ASSIST;
            case "oco":
                return OCO;
            case "replaced":
                return REPLACED;
            case "strategy":
                return STRATEGY;
            case "key_loss_stop":
                return KEY_LOSS_STOP;
            case "risk_rule":
                return RISK_RULE;
            case "system_restart":
                return SYSTEM_RESTART;
            case "external":
                return EXTERNAL;
            default:
                return new OrderCancelReason(value);
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
        return o instanceof OrderCancelReason && ((OrderCancelReason) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
