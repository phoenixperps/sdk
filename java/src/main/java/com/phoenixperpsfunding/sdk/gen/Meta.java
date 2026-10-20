// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import java.util.Map;

/** The spec this SDK was generated from: its version, hosts and stream URLs. */
public final class Meta {
    private Meta() {
    }

    /** The version of the API spec. */
    public static final String SPEC_VERSION = "1.0.0";

    /** The API host of each environment. */
    public static final Map<String, String> SERVERS = Map.of("live", "https://api.phoenixperpsfunding.com", "sandbox", "https://sandbox-api.phoenixperpsfunding.com");

    /** The account stream URL of each environment. */
    public static final Map<String, String> ACCOUNT_STREAMS = Map.of("live", "wss://api.phoenixperpsfunding.com/v1/stream", "sandbox", "wss://sandbox-api.phoenixperpsfunding.com/v1/stream");

    /** The market stream URL. */
    public static final String MARKET_STREAM = "wss://stream.phoenixperpsfunding.com/v1/market-data";
}
