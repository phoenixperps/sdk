// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** read: look only. trade: also place and cancel. Open: a value from a newer API version is kept as is. */
public final class MeCredentialAccess {
    /** read */
    public static final MeCredentialAccess READ = new MeCredentialAccess("read");

    /** trade */
    public static final MeCredentialAccess TRADE = new MeCredentialAccess("trade");

    private final String value;

    private MeCredentialAccess(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static MeCredentialAccess of(String value) {
        switch (value) {
            case "read":
                return READ;
            case "trade":
                return TRADE;
            default:
                return new MeCredentialAccess(value);
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
        return o instanceof MeCredentialAccess && ((MeCredentialAccess) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
