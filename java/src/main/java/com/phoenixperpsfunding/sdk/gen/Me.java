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

/** Me */
public final class Me {
    @JsonProperty("credential")
    private MeCredential credential;

    @JsonProperty("accounts")
    private List<String> accounts;

    @JsonProperty("account_scope")
    private MeAccountScope accountScope;

    @JsonProperty("controls")
    private MeControls controls;

    @JsonProperty("budgets")
    private MeBudgets budgets;

    @JsonProperty("assistant")
    private MeAssistant assistant;

    /** The credential. */
    public MeCredential credential() {
        return credential;
    }

    /** Sets credential. */
    public Me credential(MeCredential value) {
        this.credential = value;
        return this;
    }

    /** The accounts it reaches. */
    public List<String> accounts() {
        return accounts;
    }

    /** Sets accounts. */
    public Me accounts(List<String> value) {
        this.accounts = value;
        return this;
    }

    /** all: every account of the environment, new ones too. selected: only those listed. */
    public MeAccountScope accountScope() {
        return accountScope;
    }

    /** Sets account_scope. */
    public Me accountScope(MeAccountScope value) {
        this.accountScope = value;
        return this;
    }

    /** The limits you set on the key. */
    public MeControls controls() {
        return controls;
    }

    /** Sets controls. */
    public Me controls(MeControls value) {
        this.controls = value;
        return this;
    }

    /** The budgets. */
    public MeBudgets budgets() {
        return budgets;
    }

    /** Sets budgets. */
    public Me budgets(MeBudgets value) {
        this.budgets = value;
        return this;
    }

    /** Through the MCP server (an AI assistant): how its trades are confirmed and its budget. Null for any other call. */
    public MeAssistant assistant() {
        return assistant;
    }

    /** Sets assistant. */
    public Me assistant(MeAssistant value) {
        this.assistant = value;
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
        if (!(other instanceof Me)) {
            return false;
        }
        Me o = (Me) other;
        return Objects.equals(credential, o.credential)
            && Objects.equals(accounts, o.accounts)
            && Objects.equals(accountScope, o.accountScope)
            && Objects.equals(controls, o.controls)
            && Objects.equals(budgets, o.budgets)
            && Objects.equals(assistant, o.assistant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credential, accounts, accountScope, controls, budgets, assistant);
    }

    @Override
    public String toString() {
        return "Me{" + "credential=" + credential + ", accounts=" + accounts + ", account_scope=" + accountScope + ", controls=" + controls + ", budgets=" + budgets + ", assistant=" + assistant + "}";
    }
}
