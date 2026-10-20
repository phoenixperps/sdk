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

/** TradingViewParse */
public final class TradingViewParse {
    @JsonProperty("ok")
    private Boolean ok;

    @JsonProperty("code")
    private String code;

    @JsonProperty("field")
    private String field;

    @JsonProperty("message")
    private String message;

    @JsonProperty("signal")
    private Map<String, Object> signal;

    @JsonProperty("market")
    private String market;

    @JsonProperty("suggestion")
    private String suggestion;

    @JsonProperty("plan")
    private Map<String, Object> plan;

    /** It reads, and names a market we offer. */
    public Boolean ok() {
        return ok;
    }

    /** Sets ok. */
    public TradingViewParse ok(Boolean value) {
        this.ok = value;
        return this;
    }

    /** Why not, when it does not. */
    public String code() {
        return code;
    }

    /** Sets code. */
    public TradingViewParse code(String value) {
        this.code = value;
        return this;
    }

    /** The field at fault. */
    public String field() {
        return field;
    }

    /** Sets field. */
    public TradingViewParse field(String value) {
        this.field = value;
        return this;
    }

    /** What went wrong. */
    public String message() {
        return message;
    }

    /** Sets message. */
    public TradingViewParse message(String value) {
        this.message = value;
        return this;
    }

    /** The message as read: action, side, sizes, prices, exits. */
    public Map<String, Object> signal() {
        return signal;
    }

    /** Sets signal. */
    public TradingViewParse signal(Map<String, Object> value) {
        this.signal = value;
        return this;
    }

    /** The market it names (ALL for every market). */
    public String market() {
        return market;
    }

    /** Sets market. */
    public TradingViewParse market(String value) {
        this.market = value;
        return this;
    }

    /** The market meant, when the ticker names one we offer only scaled. */
    public String suggestion() {
        return suggestion;
    }

    /** Sets suggestion. */
    public TradingViewParse suggestion(String value) {
        this.suggestion = value;
        return this;
    }

    /** With hook_id: what the alert would do now ({ legs } / { skip } / { refuse }). */
    public Map<String, Object> plan() {
        return plan;
    }

    /** Sets plan. */
    public TradingViewParse plan(Map<String, Object> value) {
        this.plan = value;
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
        if (!(other instanceof TradingViewParse)) {
            return false;
        }
        TradingViewParse o = (TradingViewParse) other;
        return Objects.equals(ok, o.ok)
            && Objects.equals(code, o.code)
            && Objects.equals(field, o.field)
            && Objects.equals(message, o.message)
            && Objects.equals(signal, o.signal)
            && Objects.equals(market, o.market)
            && Objects.equals(suggestion, o.suggestion)
            && Objects.equals(plan, o.plan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ok, code, field, message, signal, market, suggestion, plan);
    }

    @Override
    public String toString() {
        return "TradingViewParse{" + "ok=" + ok + ", code=" + code + ", field=" + field + ", message=" + message + ", signal=" + signal + ", market=" + market + ", suggestion=" + suggestion + ", plan=" + plan + "}";
    }
}
