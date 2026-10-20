// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** What happened (GET /v1/webhook-event-types says what each type means). Open: a value from a newer API version is kept as is. */
public final class EventType {
    /** order.created */
    public static final EventType ORDER_CREATED = new EventType("order.created");

    /** order.edited */
    public static final EventType ORDER_EDITED = new EventType("order.edited");

    /** order.filled */
    public static final EventType ORDER_FILLED = new EventType("order.filled");

    /** order.canceled */
    public static final EventType ORDER_CANCELED = new EventType("order.canceled");

    /** order.rejected */
    public static final EventType ORDER_REJECTED = new EventType("order.rejected");

    /** order.expired */
    public static final EventType ORDER_EXPIRED = new EventType("order.expired");

    /** fill.created */
    public static final EventType FILL_CREATED = new EventType("fill.created");

    /** position.opened */
    public static final EventType POSITION_OPENED = new EventType("position.opened");

    /** position.changed */
    public static final EventType POSITION_CHANGED = new EventType("position.changed");

    /** position.closed */
    public static final EventType POSITION_CLOSED = new EventType("position.closed");

    /** strategy.created */
    public static final EventType STRATEGY_CREATED = new EventType("strategy.created");

    /** strategy.leg_placed */
    public static final EventType STRATEGY_LEG_PLACED = new EventType("strategy.leg_placed");

    /** strategy.leg_modified */
    public static final EventType STRATEGY_LEG_MODIFIED = new EventType("strategy.leg_modified");

    /** strategy.leg_filled */
    public static final EventType STRATEGY_LEG_FILLED = new EventType("strategy.leg_filled");

    /** strategy.leg_canceled */
    public static final EventType STRATEGY_LEG_CANCELED = new EventType("strategy.leg_canceled");

    /** strategy.leg_rejected */
    public static final EventType STRATEGY_LEG_REJECTED = new EventType("strategy.leg_rejected");

    /** strategy.orphan_repaired */
    public static final EventType STRATEGY_ORPHAN_REPAIRED = new EventType("strategy.orphan_repaired");

    /** strategy.slice */
    public static final EventType STRATEGY_SLICE = new EventType("strategy.slice");

    /** strategy.ended */
    public static final EventType STRATEGY_ENDED = new EventType("strategy.ended");

    /** tradingview.alert_executed */
    public static final EventType TRADINGVIEW_ALERT_EXECUTED = new EventType("tradingview.alert_executed");

    /** tradingview.alert_refused */
    public static final EventType TRADINGVIEW_ALERT_REFUSED = new EventType("tradingview.alert_refused");

    /** account.updated */
    public static final EventType ACCOUNT_UPDATED = new EventType("account.updated");

    /** account.passed */
    public static final EventType ACCOUNT_PASSED = new EventType("account.passed");

    /** account.failed */
    public static final EventType ACCOUNT_FAILED = new EventType("account.failed");

    /** account.payout_eligible */
    public static final EventType ACCOUNT_PAYOUT_ELIGIBLE = new EventType("account.payout_eligible");

    /** account.disabled */
    public static final EventType ACCOUNT_DISABLED = new EventType("account.disabled");

    /** account.removed */
    public static final EventType ACCOUNT_REMOVED = new EventType("account.removed");

    /** key.loss_stop */
    public static final EventType KEY_LOSS_STOP = new EventType("key.loss_stop");

    /** key.expiring */
    public static final EventType KEY_EXPIRING = new EventType("key.expiring");

    /** system.restarted */
    public static final EventType SYSTEM_RESTARTED = new EventType("system.restarted");

    /** risk.warning */
    public static final EventType RISK_WARNING = new EventType("risk.warning");

    /** risk.cleared */
    public static final EventType RISK_CLEARED = new EventType("risk.cleared");

    private final String value;

    private EventType(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static EventType of(String value) {
        switch (value) {
            case "order.created":
                return ORDER_CREATED;
            case "order.edited":
                return ORDER_EDITED;
            case "order.filled":
                return ORDER_FILLED;
            case "order.canceled":
                return ORDER_CANCELED;
            case "order.rejected":
                return ORDER_REJECTED;
            case "order.expired":
                return ORDER_EXPIRED;
            case "fill.created":
                return FILL_CREATED;
            case "position.opened":
                return POSITION_OPENED;
            case "position.changed":
                return POSITION_CHANGED;
            case "position.closed":
                return POSITION_CLOSED;
            case "strategy.created":
                return STRATEGY_CREATED;
            case "strategy.leg_placed":
                return STRATEGY_LEG_PLACED;
            case "strategy.leg_modified":
                return STRATEGY_LEG_MODIFIED;
            case "strategy.leg_filled":
                return STRATEGY_LEG_FILLED;
            case "strategy.leg_canceled":
                return STRATEGY_LEG_CANCELED;
            case "strategy.leg_rejected":
                return STRATEGY_LEG_REJECTED;
            case "strategy.orphan_repaired":
                return STRATEGY_ORPHAN_REPAIRED;
            case "strategy.slice":
                return STRATEGY_SLICE;
            case "strategy.ended":
                return STRATEGY_ENDED;
            case "tradingview.alert_executed":
                return TRADINGVIEW_ALERT_EXECUTED;
            case "tradingview.alert_refused":
                return TRADINGVIEW_ALERT_REFUSED;
            case "account.updated":
                return ACCOUNT_UPDATED;
            case "account.passed":
                return ACCOUNT_PASSED;
            case "account.failed":
                return ACCOUNT_FAILED;
            case "account.payout_eligible":
                return ACCOUNT_PAYOUT_ELIGIBLE;
            case "account.disabled":
                return ACCOUNT_DISABLED;
            case "account.removed":
                return ACCOUNT_REMOVED;
            case "key.loss_stop":
                return KEY_LOSS_STOP;
            case "key.expiring":
                return KEY_EXPIRING;
            case "system.restarted":
                return SYSTEM_RESTARTED;
            case "risk.warning":
                return RISK_WARNING;
            case "risk.cleared":
                return RISK_CLEARED;
            default:
                return new EventType(value);
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
        return o instanceof EventType && ((EventType) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
