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

/** Account */
public final class Account {
    @JsonProperty("id")
    private String id;

    @JsonProperty("environment")
    private PlatformEnvironment environment;

    @JsonProperty("stage")
    private AccountStage stage;

    @JsonProperty("status")
    private AccountStatus status;

    @JsonProperty("product")
    private String product;

    @JsonProperty("tier")
    private String tier;

    @JsonProperty("start_balance")
    private BigDecimal startBalance;

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("rule")
    private Rule rule;

    @JsonProperty("payout_eligible")
    private Boolean payoutEligible;

    @JsonProperty("parked")
    private Boolean parked;

    @JsonProperty("created_at")
    private Long createdAt;

    /** The account number. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public Account id(String value) {
        this.id = value;
        return this;
    }

    /** The environment the account lives in. */
    public PlatformEnvironment environment() {
        return environment;
    }

    /** Sets environment. */
    public Account environment(PlatformEnvironment value) {
        this.environment = value;
        return this;
    }

    /** Where the account is in the programme. */
    public AccountStage stage() {
        return stage;
    }

    /** Sets stage. */
    public Account stage(AccountStage value) {
        this.stage = value;
        return this;
    }

    /** pending: being opened; active: trading. */
    public AccountStatus status() {
        return status;
    }

    /** Sets status. */
    public Account status(AccountStatus value) {
        this.status = value;
        return this;
    }

    /** The product it was bought as. */
    public String product() {
        return product;
    }

    /** Sets product. */
    public Account product(String value) {
        this.product = value;
        return this;
    }

    /** Its size as sold. */
    public String tier() {
        return tier;
    }

    /** Sets tier. */
    public Account tier(String value) {
        this.tier = value;
        return this;
    }

    /** The balance it started with. */
    public BigDecimal startBalance() {
        return startBalance;
    }

    /** Sets start_balance. */
    public Account startBalance(BigDecimal value) {
        this.startBalance = value;
        return this;
    }

    /** The balance: closed trades and fees only. */
    public BigDecimal balance() {
        return balance;
    }

    /** Sets balance. */
    public Account balance(BigDecimal value) {
        this.balance = value;
        return this;
    }

    /** The rule. */
    public Rule rule() {
        return rule;
    }

    /** Sets rule. */
    public Account rule(Rule value) {
        this.rule = value;
        return this;
    }

    /** A payout may be requested now. */
    public Boolean payoutEligible() {
        return payoutEligible;
    }

    /** Sets payout_eligible. */
    public Account payoutEligible(Boolean value) {
        this.payoutEligible = value;
        return this;
    }

    /** Sandbox only: parked after a while unused; it wakes the moment you use it. */
    public Boolean parked() {
        return parked;
    }

    /** Sets parked. */
    public Account parked(Boolean value) {
        this.parked = value;
        return this;
    }

    /** When the account was made. */
    public Long createdAt() {
        return createdAt;
    }

    /** Sets created_at. */
    public Account createdAt(Long value) {
        this.createdAt = value;
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
        if (!(other instanceof Account)) {
            return false;
        }
        Account o = (Account) other;
        return Objects.equals(id, o.id)
            && Objects.equals(environment, o.environment)
            && Objects.equals(stage, o.stage)
            && Objects.equals(status, o.status)
            && Objects.equals(product, o.product)
            && Objects.equals(tier, o.tier)
            && Objects.equals(startBalance, o.startBalance)
            && Objects.equals(balance, o.balance)
            && Objects.equals(rule, o.rule)
            && Objects.equals(payoutEligible, o.payoutEligible)
            && Objects.equals(parked, o.parked)
            && Objects.equals(createdAt, o.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, environment, stage, status, product, tier, startBalance, balance, rule, payoutEligible, parked, createdAt);
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", environment=" + environment + ", stage=" + stage + ", status=" + status + ", product=" + product + ", tier=" + tier + ", start_balance=" + startBalance + ", balance=" + balance + ", rule=" + rule + ", payout_eligible=" + payoutEligible + ", parked=" + parked + ", created_at=" + createdAt + "}";
    }
}
