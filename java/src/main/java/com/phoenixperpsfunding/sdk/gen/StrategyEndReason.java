// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** Why it stopped. Open: a value from a newer API version is kept as is. */
public final class StrategyEndReason {
    /** take_profit */
    public static final StrategyEndReason TAKE_PROFIT = new StrategyEndReason("take_profit");

    /** stop_loss */
    public static final StrategyEndReason STOP_LOSS = new StrategyEndReason("stop_loss");

    /** filled */
    public static final StrategyEndReason FILLED = new StrategyEndReason("filled");

    /** position_closed */
    public static final StrategyEndReason POSITION_CLOSED = new StrategyEndReason("position_closed");

    /** position_reversed */
    public static final StrategyEndReason POSITION_REVERSED = new StrategyEndReason("position_reversed");

    /** canceled_by_user */
    public static final StrategyEndReason CANCELED_BY_USER = new StrategyEndReason("canceled_by_user");

    /** external */
    public static final StrategyEndReason EXTERNAL = new StrategyEndReason("external");

    /** key_revoked */
    public static final StrategyEndReason KEY_REVOKED = new StrategyEndReason("key_revoked");

    /** account_closed */
    public static final StrategyEndReason ACCOUNT_CLOSED = new StrategyEndReason("account_closed");

    /** engine_restart */
    public static final StrategyEndReason ENGINE_RESTART = new StrategyEndReason("engine_restart");

    /** child_rejected */
    public static final StrategyEndReason CHILD_REJECTED = new StrategyEndReason("child_rejected");

    /** price_bound */
    public static final StrategyEndReason PRICE_BOUND = new StrategyEndReason("price_bound");

    /** expired */
    public static final StrategyEndReason EXPIRED = new StrategyEndReason("expired");

    private final String value;

    private StrategyEndReason(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static StrategyEndReason of(String value) {
        switch (value) {
            case "take_profit":
                return TAKE_PROFIT;
            case "stop_loss":
                return STOP_LOSS;
            case "filled":
                return FILLED;
            case "position_closed":
                return POSITION_CLOSED;
            case "position_reversed":
                return POSITION_REVERSED;
            case "canceled_by_user":
                return CANCELED_BY_USER;
            case "external":
                return EXTERNAL;
            case "key_revoked":
                return KEY_REVOKED;
            case "account_closed":
                return ACCOUNT_CLOSED;
            case "engine_restart":
                return ENGINE_RESTART;
            case "child_rejected":
                return CHILD_REJECTED;
            case "price_bound":
                return PRICE_BOUND;
            case "expired":
                return EXPIRED;
            default:
                return new StrategyEndReason(value);
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
        return o instanceof StrategyEndReason && ((StrategyEndReason) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
