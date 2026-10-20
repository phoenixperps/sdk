// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** What happened; webhook.ping is a test. Open: a value from a newer API version is kept as is. */
public final class WebhookPayloadType {
    /** order.created */
    public static final WebhookPayloadType ORDER_CREATED = new WebhookPayloadType("order.created");

    /** order.edited */
    public static final WebhookPayloadType ORDER_EDITED = new WebhookPayloadType("order.edited");

    /** order.filled */
    public static final WebhookPayloadType ORDER_FILLED = new WebhookPayloadType("order.filled");

    /** order.canceled */
    public static final WebhookPayloadType ORDER_CANCELED = new WebhookPayloadType("order.canceled");

    /** order.rejected */
    public static final WebhookPayloadType ORDER_REJECTED = new WebhookPayloadType("order.rejected");

    /** order.expired */
    public static final WebhookPayloadType ORDER_EXPIRED = new WebhookPayloadType("order.expired");

    /** fill.created */
    public static final WebhookPayloadType FILL_CREATED = new WebhookPayloadType("fill.created");

    /** position.opened */
    public static final WebhookPayloadType POSITION_OPENED = new WebhookPayloadType("position.opened");

    /** position.changed */
    public static final WebhookPayloadType POSITION_CHANGED = new WebhookPayloadType("position.changed");

    /** position.closed */
    public static final WebhookPayloadType POSITION_CLOSED = new WebhookPayloadType("position.closed");

    /** strategy.created */
    public static final WebhookPayloadType STRATEGY_CREATED = new WebhookPayloadType("strategy.created");

    /** strategy.leg_placed */
    public static final WebhookPayloadType STRATEGY_LEG_PLACED = new WebhookPayloadType("strategy.leg_placed");

    /** strategy.leg_modified */
    public static final WebhookPayloadType STRATEGY_LEG_MODIFIED = new WebhookPayloadType("strategy.leg_modified");

    /** strategy.leg_filled */
    public static final WebhookPayloadType STRATEGY_LEG_FILLED = new WebhookPayloadType("strategy.leg_filled");

    /** strategy.leg_canceled */
    public static final WebhookPayloadType STRATEGY_LEG_CANCELED = new WebhookPayloadType("strategy.leg_canceled");

    /** strategy.leg_rejected */
    public static final WebhookPayloadType STRATEGY_LEG_REJECTED = new WebhookPayloadType("strategy.leg_rejected");

    /** strategy.orphan_repaired */
    public static final WebhookPayloadType STRATEGY_ORPHAN_REPAIRED = new WebhookPayloadType("strategy.orphan_repaired");

    /** strategy.slice */
    public static final WebhookPayloadType STRATEGY_SLICE = new WebhookPayloadType("strategy.slice");

    /** strategy.ended */
    public static final WebhookPayloadType STRATEGY_ENDED = new WebhookPayloadType("strategy.ended");

    /** tradingview.alert_executed */
    public static final WebhookPayloadType TRADINGVIEW_ALERT_EXECUTED = new WebhookPayloadType("tradingview.alert_executed");

    /** tradingview.alert_refused */
    public static final WebhookPayloadType TRADINGVIEW_ALERT_REFUSED = new WebhookPayloadType("tradingview.alert_refused");

    /** account.updated */
    public static final WebhookPayloadType ACCOUNT_UPDATED = new WebhookPayloadType("account.updated");

    /** account.passed */
    public static final WebhookPayloadType ACCOUNT_PASSED = new WebhookPayloadType("account.passed");

    /** account.failed */
    public static final WebhookPayloadType ACCOUNT_FAILED = new WebhookPayloadType("account.failed");

    /** account.payout_eligible */
    public static final WebhookPayloadType ACCOUNT_PAYOUT_ELIGIBLE = new WebhookPayloadType("account.payout_eligible");

    /** account.disabled */
    public static final WebhookPayloadType ACCOUNT_DISABLED = new WebhookPayloadType("account.disabled");

    /** account.removed */
    public static final WebhookPayloadType ACCOUNT_REMOVED = new WebhookPayloadType("account.removed");

    /** key.loss_stop */
    public static final WebhookPayloadType KEY_LOSS_STOP = new WebhookPayloadType("key.loss_stop");

    /** key.expiring */
    public static final WebhookPayloadType KEY_EXPIRING = new WebhookPayloadType("key.expiring");

    /** system.restarted */
    public static final WebhookPayloadType SYSTEM_RESTARTED = new WebhookPayloadType("system.restarted");

    /** risk.warning */
    public static final WebhookPayloadType RISK_WARNING = new WebhookPayloadType("risk.warning");

    /** risk.cleared */
    public static final WebhookPayloadType RISK_CLEARED = new WebhookPayloadType("risk.cleared");

    /** webhook.ping */
    public static final WebhookPayloadType WEBHOOK_PING = new WebhookPayloadType("webhook.ping");

    private final String value;

    private WebhookPayloadType(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static WebhookPayloadType of(String value) {
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
            case "webhook.ping":
                return WEBHOOK_PING;
            default:
                return new WebhookPayloadType(value);
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
        return o instanceof WebhookPayloadType && ((WebhookPayloadType) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
