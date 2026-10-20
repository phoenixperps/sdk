// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** TradingViewAlert */
public final class TradingViewAlert {
    @JsonProperty("id")
    private String id;

    @JsonProperty("hook_id")
    private String hookId;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("status")
    private TradingViewAlertStatus status;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("reason_text")
    private String reasonText;

    @JsonProperty("format")
    private String format;

    @JsonProperty("market")
    private String market;

    @JsonProperty("action")
    private String action;

    @JsonProperty("message")
    private String message;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("plan")
    private Map<String, Object> plan;

    @JsonProperty("results")
    private List<Map<String, Object>> results;

    @JsonProperty("details")
    private Map<String, Object> details;

    @JsonProperty("duplicate_of")
    private String duplicateOf;

    @JsonProperty("repeat_count")
    private Long repeatCount;

    @JsonProperty("received_at")
    private Long receivedAt;

    @JsonProperty("finished_at")
    private Long finishedAt;

    /** The alert. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public TradingViewAlert id(String value) {
        this.id = value;
        return this;
    }

    /** Its link. */
    public String hookId() {
        return hookId;
    }

    /** Sets hook_id. */
    public TradingViewAlert hookId(String value) {
        this.hookId = value;
        return this;
    }

    /** The account number. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public TradingViewAlert accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** What it did. */
    public TradingViewAlertStatus status() {
        return status;
    }

    /** Sets status. */
    public TradingViewAlert status(TradingViewAlertStatus value) {
        this.status = value;
        return this;
    }

    /** Why it placed nothing (or not all): a reason code. */
    public String reason() {
        return reason;
    }

    /** Sets reason. */
    public TradingViewAlert reason(String value) {
        this.reason = value;
        return this;
    }

    /** That reason in words. */
    public String reasonText() {
        return reasonText;
    }

    /** Sets reason_text. */
    public TradingViewAlert reasonText(String value) {
        this.reasonText = value;
        return this;
    }

    /** How the message was read: universal, pickmytrade, traderspost, pineconnector, text. */
    public String format() {
        return format;
    }

    /** Sets format. */
    public TradingViewAlert format(String value) {
        this.format = value;
        return this;
    }

    /** The market it named. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public TradingViewAlert market(String value) {
        this.market = value;
        return this;
    }

    /** Its action. */
    public String action() {
        return action;
    }

    /** Sets action. */
    public TradingViewAlert action(String value) {
        this.action = value;
        return this;
    }

    /** The message as kept (credentials masked). */
    public String message() {
        return message;
    }

    /** Sets message. */
    public TradingViewAlert message(String value) {
        this.message = value;
        return this;
    }

    /** The sender's address, its end masked. */
    public String sender() {
        return sender;
    }

    /** Sets sender. */
    public TradingViewAlert sender(String value) {
        this.sender = value;
        return this;
    }

    /** What it was to do: { legs } or { skip } or { refuse }. */
    public Map<String, Object> plan() {
        return plan;
    }

    /** Sets plan. */
    public TradingViewAlert plan(Map<String, Object> value) {
        this.plan = value;
        return this;
    }

    /** What each leg did. */
    public List<Map<String, Object>> results() {
        return results;
    }

    /** Sets results. */
    public TradingViewAlert results(List<Map<String, Object>> value) {
        this.results = value;
        return this;
    }

    /** More about a refusal. */
    public Map<String, Object> details() {
        return details;
    }

    /** Sets details. */
    public TradingViewAlert details(Map<String, Object> value) {
        this.details = value;
        return this;
    }

    /** The alert this one repeats. */
    public String duplicateOf() {
        return duplicateOf;
    }

    /** Sets duplicate_of. */
    public TradingViewAlert duplicateOf(String value) {
        this.duplicateOf = value;
        return this;
    }

    /** Refusals folded into this row (rate limits). */
    public Long repeatCount() {
        return repeatCount;
    }

    /** Sets repeat_count. */
    public TradingViewAlert repeatCount(Long value) {
        this.repeatCount = value;
        return this;
    }

    /** When it came. */
    public Long receivedAt() {
        return receivedAt;
    }

    /** Sets received_at. */
    public TradingViewAlert receivedAt(Long value) {
        this.receivedAt = value;
        return this;
    }

    /** When it was done. */
    public Long finishedAt() {
        return finishedAt;
    }

    /** Sets finished_at. */
    public TradingViewAlert finishedAt(Long value) {
        this.finishedAt = value;
        return this;
    }

    @JsonIgnore
    private final Map<String, Object> extra = new LinkedHashMap<>();

    /** Fields this SDK version does not know yet (a newer API's), kept as they came. */
    @JsonAnyGetter
    public Map<String, Object> extra() {
        return extra;
    }

    @JsonAnySetter
    void putExtra(String key, Object value) {
        extra.put(key, value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TradingViewAlert)) {
            return false;
        }
        TradingViewAlert o = (TradingViewAlert) other;
        return Objects.equals(id, o.id)
            && Objects.equals(hookId, o.hookId)
            && Objects.equals(accountId, o.accountId)
            && Objects.equals(status, o.status)
            && Objects.equals(reason, o.reason)
            && Objects.equals(reasonText, o.reasonText)
            && Objects.equals(format, o.format)
            && Objects.equals(market, o.market)
            && Objects.equals(action, o.action)
            && Objects.equals(message, o.message)
            && Objects.equals(sender, o.sender)
            && Objects.equals(plan, o.plan)
            && Objects.equals(results, o.results)
            && Objects.equals(details, o.details)
            && Objects.equals(duplicateOf, o.duplicateOf)
            && Objects.equals(repeatCount, o.repeatCount)
            && Objects.equals(receivedAt, o.receivedAt)
            && Objects.equals(finishedAt, o.finishedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hookId, accountId, status, reason, reasonText, format, market, action, message, sender, plan, results, details, duplicateOf, repeatCount, receivedAt, finishedAt);
    }

    @Override
    public String toString() {
        return "TradingViewAlert{" + "id=" + id + ", hook_id=" + hookId + ", account_id=" + accountId + ", status=" + status + ", reason=" + reason + ", reason_text=" + reasonText + ", format=" + format + ", market=" + market + ", action=" + action + ", message=" + message + ", sender=" + sender + ", plan=" + plan + ", results=" + results + ", details=" + details + ", duplicate_of=" + duplicateOf + ", repeat_count=" + repeatCount + ", received_at=" + receivedAt + ", finished_at=" + finishedAt + "}";
    }
}
