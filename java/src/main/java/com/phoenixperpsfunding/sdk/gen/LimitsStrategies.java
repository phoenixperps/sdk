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

/** LimitsStrategies */
public final class LimitsStrategies {
    @JsonProperty("per_account")
    private Long perAccount;

    @JsonProperty("programs_per_account")
    private Long programsPerAccount;

    @JsonProperty("legs_per_account")
    private Long legsPerAccount;

    @JsonProperty("per_customer")
    private Long perCustomer;

    @JsonProperty("take_profits_per_position")
    private Long takeProfitsPerPosition;

    @JsonProperty("stop_losses_per_position")
    private Long stopLossesPerPosition;

    @JsonProperty("twap_min_minutes")
    private Long twapMinMinutes;

    @JsonProperty("twap_max_minutes")
    private Long twapMaxMinutes;

    @JsonProperty("slice_min_seconds")
    private Long sliceMinSeconds;

    @JsonProperty("scaled_max_rungs")
    private Long scaledMaxRungs;

    @JsonProperty("chase_enabled")
    private Boolean chaseEnabled;

    @JsonProperty("chase_max_minutes")
    private Long chaseMaxMinutes;

    /** Running strategies an account may hold. */
    public Long perAccount() {
        return perAccount;
    }

    /** Sets per_account. */
    public LimitsStrategies perAccount(Long value) {
        this.perAccount = value;
        return this;
    }

    /** Of those, programs (oco, twap, vwap, scaled, chase). */
    public Long programsPerAccount() {
        return programsPerAccount;
    }

    /** Sets programs_per_account. */
    public LimitsStrategies programsPerAccount(Long value) {
        this.programsPerAccount = value;
        return this;
    }

    /** Working orders of strategies on one account. */
    public Long legsPerAccount() {
        return legsPerAccount;
    }

    /** Sets legs_per_account. */
    public LimitsStrategies legsPerAccount(Long value) {
        this.legsPerAccount = value;
        return this;
    }

    /** Running strategies across your accounts. */
    public Long perCustomer() {
        return perCustomer;
    }

    /** Sets per_customer. */
    public LimitsStrategies perCustomer(Long value) {
        this.perCustomer = value;
        return this;
    }

    /** Take-profit legs on one position. */
    public Long takeProfitsPerPosition() {
        return takeProfitsPerPosition;
    }

    /** Sets take_profits_per_position. */
    public LimitsStrategies takeProfitsPerPosition(Long value) {
        this.takeProfitsPerPosition = value;
        return this;
    }

    /** Stop-loss legs on one position. */
    public Long stopLossesPerPosition() {
        return stopLossesPerPosition;
    }

    /** Sets stop_losses_per_position. */
    public LimitsStrategies stopLossesPerPosition(Long value) {
        this.stopLossesPerPosition = value;
        return this;
    }

    /** The shortest twap or vwap, in minutes. */
    public Long twapMinMinutes() {
        return twapMinMinutes;
    }

    /** Sets twap_min_minutes. */
    public LimitsStrategies twapMinMinutes(Long value) {
        this.twapMinMinutes = value;
        return this;
    }

    /** The longest twap or vwap, in minutes. */
    public Long twapMaxMinutes() {
        return twapMaxMinutes;
    }

    /** Sets twap_max_minutes. */
    public LimitsStrategies twapMaxMinutes(Long value) {
        this.twapMaxMinutes = value;
        return this;
    }

    /** The shortest time between slices, in seconds. */
    public Long sliceMinSeconds() {
        return sliceMinSeconds;
    }

    /** Sets slice_min_seconds. */
    public LimitsStrategies sliceMinSeconds(Long value) {
        this.sliceMinSeconds = value;
        return this;
    }

    /** The most limits in a scaled ladder. */
    public Long scaledMaxRungs() {
        return scaledMaxRungs;
    }

    /** Sets scaled_max_rungs. */
    public LimitsStrategies scaledMaxRungs(Long value) {
        this.scaledMaxRungs = value;
        return this;
    }

    /** Whether chase strategies may start. */
    public Boolean chaseEnabled() {
        return chaseEnabled;
    }

    /** Sets chase_enabled. */
    public LimitsStrategies chaseEnabled(Boolean value) {
        this.chaseEnabled = value;
        return this;
    }

    /** The longest a chase may run, in minutes. */
    public Long chaseMaxMinutes() {
        return chaseMaxMinutes;
    }

    /** Sets chase_max_minutes. */
    public LimitsStrategies chaseMaxMinutes(Long value) {
        this.chaseMaxMinutes = value;
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
        if (!(other instanceof LimitsStrategies)) {
            return false;
        }
        LimitsStrategies o = (LimitsStrategies) other;
        return Objects.equals(perAccount, o.perAccount)
            && Objects.equals(programsPerAccount, o.programsPerAccount)
            && Objects.equals(legsPerAccount, o.legsPerAccount)
            && Objects.equals(perCustomer, o.perCustomer)
            && Objects.equals(takeProfitsPerPosition, o.takeProfitsPerPosition)
            && Objects.equals(stopLossesPerPosition, o.stopLossesPerPosition)
            && Objects.equals(twapMinMinutes, o.twapMinMinutes)
            && Objects.equals(twapMaxMinutes, o.twapMaxMinutes)
            && Objects.equals(sliceMinSeconds, o.sliceMinSeconds)
            && Objects.equals(scaledMaxRungs, o.scaledMaxRungs)
            && Objects.equals(chaseEnabled, o.chaseEnabled)
            && Objects.equals(chaseMaxMinutes, o.chaseMaxMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(perAccount, programsPerAccount, legsPerAccount, perCustomer, takeProfitsPerPosition, stopLossesPerPosition, twapMinMinutes, twapMaxMinutes, sliceMinSeconds, scaledMaxRungs, chaseEnabled, chaseMaxMinutes);
    }

    @Override
    public String toString() {
        return "LimitsStrategies{" + "per_account=" + perAccount + ", programs_per_account=" + programsPerAccount + ", legs_per_account=" + legsPerAccount + ", per_customer=" + perCustomer + ", take_profits_per_position=" + takeProfitsPerPosition + ", stop_losses_per_position=" + stopLossesPerPosition + ", twap_min_minutes=" + twapMinMinutes + ", twap_max_minutes=" + twapMaxMinutes + ", slice_min_seconds=" + sliceMinSeconds + ", scaled_max_rungs=" + scaledMaxRungs + ", chase_enabled=" + chaseEnabled + ", chase_max_minutes=" + chaseMaxMinutes + "}";
    }
}
