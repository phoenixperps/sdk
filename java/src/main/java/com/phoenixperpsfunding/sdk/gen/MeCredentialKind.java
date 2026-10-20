// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** What you called with: an API key, or an app you signed in to. Open: a value from a newer API version is kept as is. */
public final class MeCredentialKind {
    /** key */
    public static final MeCredentialKind KEY = new MeCredentialKind("key");

    /** app */
    public static final MeCredentialKind APP = new MeCredentialKind("app");

    private final String value;

    private MeCredentialKind(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static MeCredentialKind of(String value) {
        switch (value) {
            case "key":
                return KEY;
            case "app":
                return APP;
            default:
                return new MeCredentialKind(value);
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
        return o instanceof MeCredentialKind && ((MeCredentialKind) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
