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

/** MeCredential */
public final class MeCredential {
    @JsonProperty("kind")
    private MeCredentialKind kind;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("environment")
    private PlatformEnvironment environment;

    @JsonProperty("access")
    private MeCredentialAccess access;

    @JsonProperty("expires_at")
    private Long expiresAt;

    @JsonProperty("created_at")
    private Long createdAt;

    /** What you called with: an API key, or an app you signed in to. */
    public MeCredentialKind kind() {
        return kind;
    }

    /** Sets kind. */
    public MeCredential kind(MeCredentialKind value) {
        this.kind = value;
        return this;
    }

    /** The key's id (the part after ppk_test_ or ppk_live_), or the app access's id. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public MeCredential id(String value) {
        this.id = value;
        return this;
    }

    /** Its name (an app: the app's name). */
    public String name() {
        return name;
    }

    /** Sets name. */
    public MeCredential name(String value) {
        this.name = value;
        return this;
    }

    /** Its environment. */
    public PlatformEnvironment environment() {
        return environment;
    }

    /** Sets environment. */
    public MeCredential environment(PlatformEnvironment value) {
        this.environment = value;
        return this;
    }

    /** read: look only. trade: also place and cancel. */
    public MeCredentialAccess access() {
        return access;
    }

    /** Sets access. */
    public MeCredential access(MeCredentialAccess value) {
        this.access = value;
        return this;
    }

    /** When it expires; null: never. */
    public Long expiresAt() {
        return expiresAt;
    }

    /** Sets expires_at. */
    public MeCredential expiresAt(Long value) {
        this.expiresAt = value;
        return this;
    }

    /** When it was made. */
    public Long createdAt() {
        return createdAt;
    }

    /** Sets created_at. */
    public MeCredential createdAt(Long value) {
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
        if (!(other instanceof MeCredential)) {
            return false;
        }
        MeCredential o = (MeCredential) other;
        return Objects.equals(kind, o.kind)
            && Objects.equals(id, o.id)
            && Objects.equals(name, o.name)
            && Objects.equals(environment, o.environment)
            && Objects.equals(access, o.access)
            && Objects.equals(expiresAt, o.expiresAt)
            && Objects.equals(createdAt, o.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, id, name, environment, access, expiresAt, createdAt);
    }

    @Override
    public String toString() {
        return "MeCredential{" + "kind=" + kind + ", id=" + id + ", name=" + name + ", environment=" + environment + ", access=" + access + ", expires_at=" + expiresAt + ", created_at=" + createdAt + "}";
    }
}
