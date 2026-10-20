package com.phoenixperpsfunding.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.phoenixperpsfunding.sdk.gen.Event;
import com.phoenixperpsfunding.sdk.gen.Market;
import com.phoenixperpsfunding.sdk.gen.ModifyOrder;
import com.phoenixperpsfunding.sdk.gen.Order;
import com.phoenixperpsfunding.sdk.gen.OrderSide;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

/**
 * The conformance suite (conformance/scenarios.json) through the Java SDK against the mock server (node
 * conformance/mock-server.js): every scenario, local check, webhook and rules vector and stream of it, none skipped (an
 * unknown check fails); then this SDK's own on the same mock (stream errors and reconnects, a 304, the raw request, a
 * stalled body, the budgets, what the streams tell as it comes...).
 */
class ConformanceTest {
    private static final HttpClient HTTP = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    private static final Map<String, Class<?>> CLASSES = Map.of(
            "AuthenticationError", PhoenixPerpsException.AuthenticationException.class,
            "PermissionError", PhoenixPerpsException.PermissionException.class,
            "NotFoundError", PhoenixPerpsException.NotFoundException.class,
            "ConflictError", PhoenixPerpsException.ConflictException.class,
            "ValidationError", PhoenixPerpsException.ValidationException.class,
            "RuleRejectedError", PhoenixPerpsException.RuleRejectedException.class,
            "RateLimitError", PhoenixPerpsException.RateLimitException.class,
            "ServerError", PhoenixPerpsException.ServerException.class);
    private static Process mock;
    private static String url;
    private static JsonNode suite;
    private static String key;

    @BeforeAll
    static void start() throws Exception {
        Path root = Path.of(System.getProperty("sdk.root"));
        suite = Json.MAPPER.readTree(Files.readString(root.resolve("conformance").resolve("scenarios.json")));
        key = suite.get("key").asText();
        mock = new ProcessBuilder("node", root.resolve("conformance").resolve("mock-server.js").toString(), "0").start();
        url = new BufferedReader(new InputStreamReader(mock.getInputStream(), StandardCharsets.UTF_8)).readLine().trim();
    }

    @AfterAll
    static void stop() {
        mock.destroy();
    }

    private static void load(Map<String, JsonNode> scenario) throws Exception {
        HTTP.send(HttpRequest.newBuilder(URI.create(url + "/__scenario")).POST(HttpRequest.BodyPublishers.ofString(Json.MAPPER.writeValueAsString(scenario))).build(), HttpResponse.BodyHandlers.discarding());
    }

    private static List<JsonNode> requests() throws Exception {
        JsonNode r = Json.MAPPER.readTree(HTTP.send(HttpRequest.newBuilder(URI.create(url + "/__requests")).build(), HttpResponse.BodyHandlers.ofString()).body());
        List<JsonNode> out = new ArrayList<>();
        r.forEach(out::add);
        return out;
    }

    /** got, keeping only what want names (the expected objects are partial). */
    private static JsonNode pick(JsonNode got, JsonNode want) {
        if (!want.isObject()) {
            return got == null ? Json.MAPPER.nullNode() : got;
        }
        ObjectNode o = Json.MAPPER.createObjectNode();
        want.fieldNames().forEachRemaining(k -> o.set(k, pick(got == null ? null : got.get(k), want.get(k))));
        return o;
    }

    private static PhoenixPerpsClient client() {
        return PhoenixPerpsClient.builder().apiKey(key).baseUrl(url).maxRetries(2).build();
    }

    // what this runner plays (conformance/scenarios.json "vocabulary"): a key it does not know fails, never skipped
    private static final Set<String> REST_EXPECT = Set.of("ok", "requests", "result", "error", "items", "cursors", "first_body", "min_elapsed_ms", "checks", "page_fields", "paths");
    private static final Set<String> STREAM_EXPECT = Set.of("events", "connections", "resume", "first_message", "error", "frames", "resubscribe", "unsubscribe_sent", "limits", "gap");
    private static final Set<String> ERROR_EXPECT = Set.of("class", "code", "status", "retryable", "doc_url", "retry_after_ms", "order_id", "request_id", "account_id");

    private static void known(JsonNode expect, Set<String> keys) {
        expect.fieldNames().forEachRemaining(k -> assertTrue(keys.contains(k), "a key this runner does not play: " + k));
        if (expect.has("error")) {
            expect.get("error").fieldNames().forEachRemaining(k -> assertTrue(ERROR_EXPECT.contains(k), "an error key this runner does not play: " + k));
        }
    }

    /** The error a scenario expects: its class, code, status, retryable, doc_url, Retry-After, order and request ids. */
    private static void expectError(JsonNode xe, PhoenixPerpsException error) {
        assertNotNull(error, "an error");
        if (xe.has("class")) {
            assertEquals(CLASSES.get(xe.get("class").asText()), error.getClass());
        }
        assertEquals(xe.get("code").asText(), error.code());
        if (xe.has("status")) {
            assertEquals(xe.get("status").asInt(), error.status());
        }
        if (xe.has("retryable")) {
            assertEquals(xe.get("retryable").asBoolean(), error.retryable());
        }
        if (xe.has("doc_url")) {
            assertEquals(xe.get("doc_url").asText(), error.docUrl());
        }
        if (xe.has("retry_after_ms")) {
            assertEquals(xe.get("retry_after_ms").asLong(), error.retryAfter().toMillis());
        }
        if (xe.has("order_id")) {
            assertEquals(xe.get("order_id").asText(), error.orderId());
        }
        if (xe.has("request_id")) {
            assertEquals(xe.get("request_id").asText(), error.requestId());
        }
        if (xe.has("account_id")) {
            assertEquals(xe.get("account_id").asText(), error.details().path("account_id").asText(null));
        }
    }

    @TestFactory
    Stream<DynamicTest> rest() {
        List<DynamicTest> tests = new ArrayList<>();
        suite.get("scenarios").forEach(sc -> tests.add(DynamicTest.dynamicTest(sc.get("name").asText(), () -> {
            known(sc.get("expect"), REST_EXPECT);
            load(Map.of("exchanges", sc.get("exchanges")));
            // client.base_url_suffix: the mock's URL plus it as the baseUrl
            PhoenixPerpsClient c = PhoenixPerpsClient.builder().apiKey(key).baseUrl(url + sc.path("client").path("base_url_suffix").asText("")).maxRetries(2).build();
            List<JsonNode> calls = new ArrayList<>();
            if (sc.has("calls")) {
                sc.get("calls").forEach(calls::add);
            } else {
                calls.add(sc.get("call"));
            }
            Result result = null;
            PhoenixPerpsException error = null;
            try {
                for (JsonNode call : calls) {
                    result = Dispatch.run(c, new Call(call), RequestOptions.none());
                }
            } catch (PhoenixPerpsException e) {
                error = e;
            }
            List<JsonNode> got = requests();
            JsonNode x = sc.get("expect");
            assertEquals(x.get("requests").asInt(), got.size(), "requests");
            if (x.has("ok")) {
                assertEquals(x.get("ok").asBoolean(), error == null, "ok");
            }
            if (x.has("error")) {
                expectError(x.get("error"), error);
            } else {
                assertNull(error);
                if (x.has("result")) {
                    // result null: the call answered nothing (a 304)
                    assertEquals(x.get("result"), x.get("result").isNull() ? (result.result() == null ? Json.MAPPER.nullNode() : result.result()) : pick(result.result(), x.get("result")));
                }
                if (x.has("items")) {
                    List<String> want = new ArrayList<>();
                    x.get("items").forEach(i -> want.add(i.asText()));
                    assertEquals(want, result.items());
                }
                if (x.has("page_fields")) {
                    assertEquals(x.get("page_fields"), pick(result.pageFields(), x.get("page_fields")));
                }
            }
            if (x.has("paths")) {
                List<String> want = new ArrayList<>();
                x.get("paths").forEach(p -> want.add(p.asText()));
                assertEquals(want, got.stream().map(r -> r.get("path").asText()).toList());
            }
            if (x.has("cursors")) {
                for (int i = 0; i < x.get("cursors").size(); i++) {
                    JsonNode w = x.get("cursors").get(i);
                    JsonNode q = got.get(i).get("query").get("cursor");
                    assertEquals(w.isNull() ? null : w.asText(), q == null ? null : q.asText());
                }
            }
            if (x.has("first_body")) {
                assertEquals(x.get("first_body"), pick(got.get(0).get("body"), x.get("first_body")));
            }
            if (x.has("min_elapsed_ms")) {
                long elapsed = got.get(got.size() - 1).get("at").asLong() - got.get(0).get("at").asLong();
                assertTrue(elapsed >= x.get("min_elapsed_ms").asLong(), elapsed + " ms");
            }
            if (x.has("checks")) {
                for (JsonNode check : x.get("checks")) {
                    JsonNode h = got.get(0).get("headers");
                    switch (check.asText()) {
                        case "bearer" -> assertEquals("Bearer " + key, h.get("authorization").asText());
                        case "user_agent" -> assertTrue(h.get("user-agent").asText().matches("^phoenixperps-java/\\d+\\.\\d+\\.\\d+ \\(.+\\) spec/.*"), h.get("user-agent").asText());
                        case "same_idempotency_key" -> {
                            Set<String> keys = new HashSet<>();
                            got.forEach(r -> keys.add(r.get("headers").get("idempotency-key").asText()));
                            assertEquals(1, keys.size());
                            assertTrue(keys.iterator().next().matches("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"));
                        }
                        default -> throw new AssertionError("a check this runner does not play: " + check);
                    }
                }
            }
        })));
        return tests.stream();
    }

    @TestFactory
    Stream<DynamicTest> streams() {
        List<DynamicTest> tests = new ArrayList<>();
        suite.get("streams").forEach(sc -> tests.add(DynamicTest.dynamicTest(sc.get("name").asText(), () -> {
            known(sc.get("expect"), STREAM_EXPECT);
            load(Map.of("connections", sc.get("connections")));
            String ws = "ws" + url.substring(4);
            PhoenixPerpsClient c = PhoenixPerpsClient.builder().apiKey(key).baseUrl(url).streamUrls(ws + "/v1/stream", ws + "/v1/market-data").build();
            JsonNode x = sc.get("expect");
            if ("account".equals(sc.get("stream").asText())) {
                List<String> want = new ArrayList<>();
                x.path("events").forEach(e -> want.add(e.asText()));
                List<String> ids = new ArrayList<>();
                List<String> gaps = new CopyOnWriteArrayList<>();
                PhoenixPerpsException error = null;
                try (AccountStream s = c.openAccountStream(AccountStream.options().onGap(gaps::add))) {
                    try {
                        while (ids.size() < want.size()) {
                            var e = s.poll(Duration.ofSeconds(10));
                            assertNotNull(e, "an event in time");
                            ids.add(e.id());
                        }
                        if (x.has("error")) {
                            s.poll(Duration.ofSeconds(10));
                        }
                    } catch (PhoenixPerpsException e) {
                        error = e;
                    }
                    if (x.has("error")) {
                        Thread.sleep(500); // time enough for a wrong reconnect to show
                    }
                }
                assertEquals(want, ids);
                if (x.has("error")) {
                    expectError(x.get("error"), error);
                }
                if (x.has("gap")) {
                    // the gap is told, with the last event id told before it
                    assertEquals(List.of(x.get("gap").asText()), gaps);
                }
            } else {
                List<String> want = new ArrayList<>();
                x.get("frames").forEach(f -> want.add(f.asText()));
                String channel = sc.get("subscribe").get("channel").asText();
                List<String> frames = new ArrayList<>();
                try (MarketStream m = c.openMarketStream()) {
                    // the calls the scenario names, in order, each answered before the next
                    m.subscribe(channel, texts(sc.get("subscribe").get("symbols")), params(sc.get("subscribe").path("params")), Duration.ofSeconds(10));
                    if (sc.has("unsubscribe")) {
                        JsonNode u = sc.get("unsubscribe");
                        m.unsubscribe(u.get("channel").asText(), texts(u.get("symbols")), params(u.path("params")), Duration.ofSeconds(10));
                    }
                    while (frames.size() < want.size()) {
                        MarketStream.Frame f = m.poll(Duration.ofSeconds(10));
                        assertNotNull(f, "a frame in time");
                        if (f.type().equals(channel)) {
                            frames.add(f.data().get("last").asText());
                        }
                    }
                    if (x.has("limits")) {
                        assertEquals(x.get("limits"), m.limits());
                    }
                }
                assertEquals(want, frames);
            }
            List<JsonNode> reqs = requests();
            assertEquals(x.get("connections").asInt(), reqs.size(), "connections");
            if (x.has("resume")) {
                for (int i = 0; i < x.get("resume").size(); i++) {
                    JsonNode w = x.get("resume").get(i);
                    JsonNode q = reqs.get(i).get("query").get("resume");
                    assertEquals(w.isNull() ? null : w.asText(), q == null ? null : q.asText());
                }
            }
            assertEquals("Bearer " + key, reqs.get(0).get("headers").get("authorization").asText());
            if (x.has("first_message")) {
                assertEquals(x.get("first_message"), pick(reqs.get(0).get("messages").get(0), x.get("first_message")));
            }
            if (x.has("resubscribe")) {
                assertEquals(x.get("resubscribe"), pick(reqs.get(reqs.size() - 1).get("messages").get(0), x.get("resubscribe")));
            }
            if (x.has("unsubscribe_sent")) {
                JsonNode sent = reqs.stream().flatMap(r -> Stream.of(Json.MAPPER.convertValue(r.get("messages"), JsonNode[].class)))
                        .filter(msg -> "unsubscribe".equals(msg.path("op").asText())).findFirst().orElse(null);
                assertEquals(x.get("unsubscribe_sent"), pick(sent, x.get("unsubscribe_sent")));
            }
        })));
        return tests.stream();
    }

    private static List<String> texts(JsonNode array) {
        List<String> out = new ArrayList<>();
        array.forEach(v -> out.add(v.asText()));
        return out;
    }

    private static Map<String, Object> params(JsonNode object) {
        Map<String, Object> out = new HashMap<>();
        object.fields().forEachRemaining(e -> out.put(e.getKey(), Json.MAPPER.convertValue(e.getValue(), Object.class)));
        return out;
    }

    /** conformance/scenarios.json local: each by its check (none: a client that fails before any request). */
    @TestFactory
    Stream<DynamicTest> local() {
        List<DynamicTest> tests = new ArrayList<>();
        suite.get("local").forEach(l -> tests.add(DynamicTest.dynamicTest(l.get("name").asText(), () -> {
            switch (l.path("check").asText("")) {
                case "redaction" -> redaction();
                case "webhook_vectors" -> webhookVectors();
                case "keyless" -> keyless(l.get("client"));
                case "" -> {
                    JsonNode cl = l.get("client");
                    PhoenixPerpsException e = assertThrows(PhoenixPerpsException.class, () -> PhoenixPerpsClient.builder().apiKey(cl.get("api_key").asText()).environment(cl.get("environment").asText()).baseUrl("http://127.0.0.1:9").build());
                    assertEquals(l.get("expect").get("error_code").asText(), e.code());
                }
                default -> throw new AssertionError("a check this runner does not play: " + l.get("check"));
            }
        })));
        return tests.stream();
    }

    /** A keyless client in a process of its own (PHOENIXPERPS_API_KEY unset): a public call, then a private one. */
    static final class KeylessProbe {
        public static void main(String[] args) {
            PhoenixPerpsClient c = PhoenixPerpsClient.builder().environment(args[1]).baseUrl(args[0]).maxRetries(0).build();
            System.out.print(c.environment() + " " + c.maskedKey() + " " + c.markets().list().data().size());
            try {
                c.me().get();
            } catch (PhoenixPerpsException e) {
                System.out.print(" " + e.getClass().getSimpleName() + " " + e.code());
            }
        }
    }

    private static void keyless(JsonNode client) throws Exception {
        assertTrue(client.get("api_key").isNull());
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":200,\"body\":{\"data\":[{\"market\":\"BTC-USD\"}],\"has_more\":false,\"next_cursor\":null,\"as_of\":1}}},"
                + "{\"respond\":{\"status\":401,\"body\":{\"error\":{\"type\":\"authentication\",\"code\":\"missing_api_key\",\"message\":\"x\",\"request_id\":\"req_1\",\"retryable\":false}}}}]")));
        ProcessBuilder pb = new ProcessBuilder(Path.of(System.getProperty("java.home"), "bin", "java").toString(), "-cp", System.getProperty("java.class.path"), KeylessProbe.class.getName(), url, client.get("environment").asText());
        pb.environment().remove("PHOENIXPERPS_API_KEY");
        pb.environment().remove("PHOENIXPERPS_ENVIRONMENT");
        Process p = pb.redirectErrorStream(true).start();
        String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
        p.waitFor();
        // made without a key; the public call answered, the other is the API's missing_api_key
        assertEquals("sandbox null 1 AuthenticationException missing_api_key", out);
        List<JsonNode> got = requests();
        assertEquals(List.of("/v1/markets", "/v1/me"), got.stream().map(r -> r.get("path").asText()).toList());
        got.forEach(r -> assertFalse(r.get("headers").has("authorization"), "no Authorization without a key"));
    }

    private static void redaction() throws Exception {
        PhoenixPerpsClient c = PhoenixPerpsClient.builder().apiKey(key).baseUrl("http://127.0.0.1:9").maxRetries(0).timeout(Duration.ofSeconds(2)).build();
        assertFalse(c.toString().contains(key));
        assertFalse(String.valueOf(c).contains(key));
        assertEquals("ppk_test_****" + key.substring(key.length() - 4), c.maskedKey());
        PhoenixPerpsException e = assertThrows(PhoenixPerpsException.class, () -> c.me().get());
        assertFalse(String.valueOf(e).contains(key));
        // its JSON too, through the SDK's mapper or any other (a streaming write keeps what it wrote before a failure)
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Json.MAPPER.writeValue(out, c);
        } catch (java.io.IOException x) {
            // what was written is checked below
        }
        assertFalse(out.toString(StandardCharsets.UTF_8).contains(key), out.toString(StandardCharsets.UTF_8));
        assertEquals("{\"environment\":\"sandbox\",\"baseUrl\":\"http://127.0.0.1:9\",\"apiKey\":\"" + c.maskedKey() + "\"}", new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).writeValueAsString(c));
    }

    private static PhoenixPerpsClient streamClient(List<String> log) {
        String ws = "ws" + url.substring(4);
        return PhoenixPerpsClient.builder().apiKey(key).baseUrl(url).streamUrls(ws + "/v1/stream", ws + "/v1/market-data").logger((level, m) -> log.add(level + " " + m)).build();
    }

    private static void connections(String json) throws Exception {
        load(Map.of("connections", Json.MAPPER.readTree(json)));
    }

    /** Waits (5 s at most) for what the test needs to happen. */
    private static void await(java.util.function.BooleanSupplier done) throws Exception {
        long until = System.nanoTime() + 5_000_000_000L;
        while (!done.getAsBoolean() && System.nanoTime() < until) {
            Thread.sleep(50);
        }
    }

    private static PhoenixPerpsException readyError(AccountStream s) {
        ExecutionException e = assertThrows(ExecutionException.class, () -> s.ready().get(5, TimeUnit.SECONDS));
        return assertInstanceOf(PhoenixPerpsException.class, e.getCause());
    }

    @Test
    void accountStreamErrorFrame() throws Exception {
        // a refused subscribe ends the stream with its error, ready() included, and is not made again
        connections("[[{\"send\":{\"type\":\"hello\",\"heartbeat_ms\":15000}},{\"expect\":\"subscribe\"},{\"send\":{\"type\":\"error\",\"code\":\"too_many_accounts\",\"details\":{\"max\":50}}},{\"wait_ms\":3000}]]");
        try (AccountStream s = streamClient(new ArrayList<>()).openAccountStream(null)) {
            PhoenixPerpsException e = readyError(s);
            assertEquals(PhoenixPerpsException.ValidationException.class, e.getClass());
            assertEquals("too_many_accounts", e.code());
            assertEquals(50, e.details().get("max").asInt());
            assertThrows(PhoenixPerpsException.ValidationException.class, () -> s.poll(Duration.ofSeconds(1)));
            Thread.sleep(500); // time enough for a wrong reconnect to show
        }
        assertEquals(1, requests().size(), "connections");
        // an account it does not know: its snapshot_end follows, the error still ends the stream
        connections("[[{\"send\":{\"type\":\"hello\",\"heartbeat_ms\":15000}},{\"expect\":\"subscribe\"},{\"send\":{\"type\":\"error\",\"code\":\"account_not_found\",\"account_id\":\"SANDBOX-00042-009\"}},{\"send\":{\"type\":\"snapshot_end\",\"accounts\":[]}},{\"wait_ms\":3000}]]");
        try (AccountStream s = streamClient(new ArrayList<>()).openAccountStream(AccountStream.options().accounts(List.of("SANDBOX-00042-009")))) {
            PhoenixPerpsException e = readyError(s);
            assertEquals("account_not_found", e.code());
            assertEquals("SANDBOX-00042-009", e.details().get("account_id").asText());
            assertTrue(e.getMessage().contains("SANDBOX-00042-009"), e.getMessage());
        }
    }

    @Test
    void streamRefusedOrUnreachable() throws Exception {
        // a refusal that a retry cannot change ends the stream with the server's answer
        connections("[[{\"refuse\":404}]]");
        List<String> log = new CopyOnWriteArrayList<>();
        try (AccountStream s = streamClient(log).openAccountStream(null)) {
            assertEquals(PhoenixPerpsException.NotFoundException.class, readyError(s).getClass());
            assertThrows(PhoenixPerpsException.NotFoundException.class, () -> s.poll(Duration.ofSeconds(1)));
            Thread.sleep(500);
        }
        assertEquals(1, requests().size(), "connections");
        connections("[[{\"refuse\":404}]]");
        try (MarketStream m = streamClient(log).openMarketStream()) {
            assertThrows(PhoenixPerpsException.NotFoundException.class, () -> m.subscribe("ticker", List.of("BTC-USD"), Map.of(), Duration.ofSeconds(5)));
        }
        // one it can (429, 5xx) is made again, a line to the logger each time
        connections("[[{\"refuse\":503}],[{\"send\":{\"type\":\"hello\",\"heartbeat_ms\":15000}},{\"expect\":\"subscribe\"},{\"send\":{\"type\":\"snapshot_end\"}},{\"wait_ms\":3000}]]");
        log.clear();
        try (AccountStream s = streamClient(log).openAccountStream(null)) {
            s.ready().get(10, TimeUnit.SECONDS);
        }
        assertEquals(1, log.size(), log.toString());
        assertTrue(log.get(0).matches("warn phoenixperps: account stream: refused with 503 http_503 .*; reconnecting in \\d+ ms \\(attempt 1\\)"), log.get(0));
        // a network failure is made again too, with its cause; close() settles ready()
        log.clear();
        PhoenixPerpsClient c = PhoenixPerpsClient.builder().apiKey(key).baseUrl(url).streamUrls("ws://127.0.0.1:9/v1/stream", "ws://127.0.0.1:9/v1/market-data").logger((level, m) -> log.add(level + " " + m)).build();
        AccountStream s = c.openAccountStream(null);
        await(() -> !log.isEmpty());
        s.close();
        assertTrue(!log.isEmpty() && log.get(0).contains("ConnectException"), log.toString());
        assertFalse(log.get(0).contains(key));
        assertEquals(PhoenixPerpsException.StreamClosedException.class, readyError(s).getClass());
        log.clear();
        MarketStream m = c.openMarketStream();
        await(() -> !log.isEmpty());
        m.close();
        assertTrue(!log.isEmpty() && log.get(0).startsWith("warn phoenixperps: market stream: "), log.toString());
    }

    @Test
    void marketStreamHeartbeat() throws Exception {
        // three heartbeats (as the hello says) without a frame: a dead connection, made again
        String quiet = "[{\"send\":{\"type\":\"hello\",\"session\":\"mds_1\",\"heartbeat_ms\":200,\"channels\":[],\"intervals\":[]}},{\"wait_ms\":5000}]";
        connections("[" + quiet + "," + quiet + "]");
        List<String> log = new CopyOnWriteArrayList<>();
        MarketStream m = streamClient(log).openMarketStream();
        await(() -> {
            try {
                return requests().size() >= 2;
            } catch (Exception e) {
                return false;
            }
        });
        m.close();
        assertTrue(requests().size() >= 2, "a reconnect within 5 s");
        assertTrue(log.get(0).contains("no frame in 600 ms"), log.toString());
    }

    @Test
    void notModified() throws Exception {
        // an If-None-Match that matched: a 304, no error; nothing new to read
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":304,\"headers\":{\"etag\":\"W/\\\"abc\\\"\"}}}]")));
        List<Integer> statuses = new ArrayList<>();
        Market m = client().markets().get("BTC-USD", RequestOptions.none().withHeaders(Map.of("If-None-Match", "W/\"abc\"")).withOnResponse(r -> statuses.add(r.statusCode())));
        assertNull(m);
        assertEquals(List.of(304), statuses);
        assertEquals("W/\"abc\"", requests().get(0).get("headers").get("if-none-match").asText());
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":304}}]")));
        assertNull(client().orders().list("SANDBOX-00042-001", null, RequestOptions.none().withHeaders(Map.of("If-None-Match", "W/\"abc\""))));
        // a page that is not JSON (a proxy's) is an error too, never an empty page
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":200,\"body\":\"<html>proxy</html>\"}}]")));
        PhoenixPerpsException e = assertThrows(PhoenixPerpsException.ServerException.class, () -> client().orders().list("SANDBOX-00042-001", null));
        assertEquals("http_200", e.code());
        assertEquals("text/html", e.headers().firstValue("content-type").orElse("").split(";")[0]);
    }

    @Test
    void restAddressAsBaseUrl() throws Exception {
        // the address the docs give for REST (.../v1) is the host
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":200,\"body\":{\"data\":{}}}}]")));
        PhoenixPerpsClient c = PhoenixPerpsClient.builder().apiKey(key).baseUrl(url + "/v1/").build();
        assertEquals(url, c.baseUrl());
        c.me().get();
        assertEquals("/v1/me", requests().get(0).get("path").asText());
    }

    /** What fromEnv() makes of the environment it runs in: its environment, or its error code. */
    static final class EnvProbe {
        public static void main(String[] args) {
            try {
                System.out.print(PhoenixPerpsClient.fromEnv().environment());
            } catch (PhoenixPerpsException e) {
                System.out.print(e.code());
            }
        }
    }

    private static String fromEnv(String apiKey, String environment) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(Path.of(System.getProperty("java.home"), "bin", "java").toString(), "-cp", System.getProperty("java.class.path"), EnvProbe.class.getName());
        pb.environment().put("PHOENIXPERPS_API_KEY", apiKey);
        pb.environment().put("PHOENIXPERPS_ENVIRONMENT", environment);
        pb.environment().put("PHOENIXPERPS_BASE_URL", "http://127.0.0.1:9");
        Process p = pb.redirectErrorStream(true).start();
        String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
        p.waitFor();
        return out;
    }

    @Test
    void environmentFromTheKeyFirst() throws Exception {
        // the key's own environment, else PHOENIXPERPS_ENVIRONMENT (docs/BEHAVIOUR.md "Configuration")
        assertEquals("sandbox", fromEnv(key, "live"));
        assertEquals("live", fromEnv("pp_unprefixed_key", "live"));
        assertEquals("invalid_environment", fromEnv("pp_unprefixed_key", "staging"));
        // live or sandbox in any case
        assertEquals("live", fromEnv("pp_unprefixed_key", "LIVE"));
        assertEquals("sandbox", PhoenixPerpsClient.builder().apiKey(key).environment("Sandbox").baseUrl("http://127.0.0.1:9").build().environment());
    }

    @Test
    void deprecationWithoutRateHeaders() throws Exception {
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":200,\"headers\":{\"deprecation\":\"@1798761600\"},\"body\":{\"data\":{}}}}]")));
        List<String> log = new ArrayList<>();
        PhoenixPerpsClient.builder().apiKey(key).baseUrl(url).logger((level, m) -> log.add(level + " " + m)).build().me().get();
        assertEquals(List.of("warn phoenixperps: this API version is deprecated (Deprecation: @1798761600)"), log);
    }

    @Test
    void webhookTimestampBeyondAnyClock() {
        for (String ts : List.of("99999999999999999999", "999999999999999999")) {
            Map<String, String> h = Map.of("X-Phoenix-Timestamp", ts, "X-Phoenix-Signature", "sha256=00");
            assertEquals("stale", assertThrows(Webhooks.VerificationException.class, () -> Webhooks.verify("{}", h, "ppwhsec_x")).code(), ts);
        }
    }

    @Test
    void worstPriceSide() throws Exception {
        MarketRules r = new MarketRules(Json.MAPPER.treeToValue(suite.get("rules_vectors").get("market"), Market.class));
        BigDecimal p = new BigDecimal("62000");
        // the BigDecimal an order takes, at the tick's decimals
        assertEquals("62620.0", r.worstPrice(OrderSide.BUY, p, null).toPlainString());
        assertEquals("61380.0", r.worstPrice(OrderSide.SELL, p, null).toPlainString());
        assertEquals("62620.0", r.worstPrice("BUY", p, null).toPlainString());
        assertEquals("61380.0", r.worstPrice("Sell", p, null).toPlainString());
        assertThrows(PhoenixPerpsException.ValidationException.class, () -> r.sizeForNotional(BigDecimal.TEN, BigDecimal.ZERO));
        assertThrows(PhoenixPerpsException.ValidationException.class, () -> r.sizeForRisk(BigDecimal.TEN, new BigDecimal("-0.0")));
        assertThrows(PhoenixPerpsException.ValidationException.class, () -> r.worstPrice("long", p, null));
        assertThrows(PhoenixPerpsException.ValidationException.class, () -> r.worstPrice(OrderSide.of("short"), p, null));
        // the market's slippage bounds (0.05 to 5 %), both ends included
        assertEquals("65100.0", r.worstPrice(OrderSide.BUY, p, new BigDecimal("5")).toPlainString());
        assertEquals("62031.0", r.worstPrice(OrderSide.BUY, p, new BigDecimal("0.05")).toPlainString());
        assertThrows(PhoenixPerpsException.ValidationException.class, () -> r.worstPrice(OrderSide.SELL, p, new BigDecimal("0.01")));
    }

    @Test
    void rawRequest() throws Exception {
        // a route newer than the SDK: the client's key, retries (a GET) and errors
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":503}},{\"respond\":{\"status\":200,\"body\":{\"data\":{\"x\":1}}}}]")));
        RawResponse r = client().request("get", "/v1/something-new", Map.of("q", "1"), null, null);
        assertEquals(200, r.statusCode());
        assertEquals(1, Json.MAPPER.readTree(r.body()).get("data").get("x").asInt());
        List<JsonNode> got = requests();
        assertEquals(2, got.size());
        assertEquals("/v1/something-new", got.get(1).get("path").asText());
        assertEquals("1", got.get(1).get("query").get("q").asText());
        assertEquals("Bearer " + key, got.get(1).get("headers").get("authorization").asText());
        // a write is tried again only with its Idempotency-Key, the same on every attempt
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":503}}]")));
        assertThrows(PhoenixPerpsException.ServerException.class, () -> client().request("POST", "/v1/something-new", null, Map.of("a", 1), null));
        assertEquals(1, requests().size());
        assertEquals(1, requests().get(0).get("body").get("a").asInt());
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":503}},{\"respond\":{\"status\":201,\"body\":{}}}]")));
        assertEquals(201, client().request("POST", "/v1/something-new", null, Map.of(), RequestOptions.none().withIdempotencyKey("k1")).statusCode());
        assertEquals(List.of("k1", "k1"), requests().stream().map(x -> x.get("headers").get("idempotency-key").asText()).toList());
        assertThrows(PhoenixPerpsException.ValidationException.class, () -> client().request("GET", "v1/status", null, null, null));
    }

    @Test
    void streamOperations() throws Exception {
        // the market stream's list and ping, answered by their id
        connections("[[{\"send\":{\"type\":\"hello\",\"session\":\"mds_1\",\"heartbeat_ms\":15000,\"channels\":[],\"intervals\":[]}},{\"expect\":\"list\"},{\"reply\":{\"type\":\"list\",\"subscriptions\":[{\"channel\":\"ticker\",\"symbol\":\"BTC-USD\"}]}},{\"expect\":\"ping\"},{\"reply\":{\"type\":\"pong\",\"time\":1791460800000}},{\"wait_ms\":3000}]]");
        try (MarketStream m = streamClient(new ArrayList<>()).openMarketStream()) {
            assertEquals("BTC-USD", m.list(Duration.ofSeconds(5)).get("subscriptions").get(0).get("symbol").asText());
            assertEquals(1791460800000L, m.ping(Duration.ofSeconds(5)).get("time").asLong());
        }
        // the account stream's ping and unsubscribe; an account unsubscribed is left out after a reconnect
        String hello = "{\"send\":{\"type\":\"hello\",\"session\":\"ses_1\",\"environment\":\"sandbox\",\"heartbeat_ms\":15000,\"accounts\":[\"SANDBOX-00042-001\",\"SANDBOX-00042-002\"]}}";
        connections("[[" + hello + ",{\"expect\":\"subscribe\"},{\"send\":{\"type\":\"snapshot\",\"account_id\":\"SANDBOX-00042-002\",\"orders\":[],\"positions\":[],\"risk\":null}},{\"send\":{\"type\":\"snapshot_end\"}},"
                + "{\"expect\":\"ping\"},{\"send\":{\"type\":\"pong\",\"time\":1791460800000}},{\"expect\":\"unsubscribe\"},{\"send\":{\"type\":\"unsubscribed\",\"accounts\":[\"SANDBOX-00042-002\"]}},{\"close\":1012}],"
                + "[" + hello + ",{\"expect\":\"subscribe\"},{\"send\":{\"type\":\"snapshot_end\"}},{\"wait_ms\":3000}]]");
        try (AccountStream s = streamClient(new ArrayList<>()).openAccountStream(null)) {
            s.ready().get(5, TimeUnit.SECONDS);
            assertEquals(1791460800000L, s.ping(Duration.ofSeconds(5)).get("time").asLong());
            assertEquals("SANDBOX-00042-002", s.unsubscribe(List.of("sandbox-00042-002"), Duration.ofSeconds(5)).get("accounts").get(0).asText());
            assertNull(s.snapshot("SANDBOX-00042-002"));
            await(() -> {
                try {
                    List<JsonNode> r = requests();
                    return r.size() == 2 && r.get(1).get("messages").size() > 0;
                } catch (Exception e) {
                    return false;
                }
            });
        }
        List<JsonNode> got = requests();
        assertEquals(Json.MAPPER.readTree("{\"op\":\"subscribe\"}"), got.get(0).get("messages").get(0));
        assertEquals(Json.MAPPER.readTree("{\"op\":\"subscribe\",\"accounts\":[\"SANDBOX-00042-001\"]}"), got.get(1).get("messages").get(0));
    }

    private static void webhookVectors() {
        JsonNode v = suite.get("webhook_vectors");
        Instant now = Instant.ofEpochMilli(v.get("now_ms").asLong());
        List<String> secret = List.of(v.get("secret").asText());
        java.util.function.BiFunction<String, String, Map<String, String>> at = (ts, sig) -> Map.of("X-Phoenix-Timestamp", ts, "X-Phoenix-Signature", sig);
        String ts = v.get("timestamp").asText();
        String body = v.get("body").asText();
        // the WebhookPayload, as the API sent it
        assertEquals(v.get("want"), pick(Json.MAPPER.valueToTree(Webhooks.verify(body, at.apply(ts, v.get("signature").asText()), secret, null, now)), v.get("want")));
        assertEquals(v.get("want"), pick(Json.MAPPER.valueToTree(Webhooks.verify(body, at.apply(ts, v.get("rotation").asText()), secret, null, now)), v.get("want")));
        assertEquals("bad_signature", assertThrows(Webhooks.VerificationException.class, () -> Webhooks.verify(body, at.apply(ts, v.get("signature").asText()), List.of(v.get("other").asText()), null, now)).code());
        assertEquals("stale", assertThrows(Webhooks.VerificationException.class, () -> Webhooks.verify(body, at.apply(ts, v.get("signature").asText()), secret, null, now.plusSeconds(1000))).code());
        assertEquals("bad_signature", assertThrows(Webhooks.VerificationException.class, () -> Webhooks.verify(body + " ", at.apply(ts, v.get("signature").asText()), secret, null, now)).code());
        // extra: each with its own signature, body and timestamp, verified with the secret at now_ms
        for (JsonNode x : v.get("extra")) {
            Map<String, String> h = at.apply(x.get("timestamp").asText(), x.get("signature").asText());
            if (x.has("error")) {
                // one of the SDK's own errors, never the language's
                PhoenixPerpsException e = assertThrows(Webhooks.VerificationException.class, () -> Webhooks.verify(x.get("body").asText(), h, secret, null, now), x.get("name").asText());
                assertEquals(x.get("error").asText(), e.code(), x.get("name").asText());
            } else {
                assertEquals(x.get("want"), pick(Json.MAPPER.valueToTree(Webhooks.verify(x.get("body").asText(), h, secret, null, now)), x.get("want")), x.get("name").asText());
            }
        }
    }

    @TestFactory
    Stream<DynamicTest> marketRulesVectors() throws Exception {
        JsonNode rv = suite.get("rules_vectors");
        MarketRules r = new MarketRules(Json.MAPPER.treeToValue(rv.get("market"), Market.class));
        List<DynamicTest> tests = new ArrayList<>();
        for (JsonNode c : rv.get("cases")) {
            tests.add(DynamicTest.dynamicTest(c.get("fn").asText() + " " + c.get("args") + " " + c.path("mode").asText(""), () -> {
                // the arguments as the strings the vectors give
                List<String> a = texts(c.get("args"));
                String mode = c.path("mode").asText("nearest");
                org.junit.jupiter.api.function.ThrowingSupplier<BigDecimal> call = () -> switch (c.get("fn").asText()) {
                    case "round_price" -> r.roundPrice(a.get(0), "down".equals(mode) ? MarketRules.Mode.DOWN : "up".equals(mode) ? MarketRules.Mode.UP : MarketRules.Mode.NEAREST);
                    case "round_size" -> r.roundSize(a.get(0));
                    case "size_for_notional" -> r.sizeForNotional(a.get(0), a.get(1));
                    case "size_for_risk" -> r.sizeForRisk(a.get(0), a.get(1));
                    case "worst_price" -> r.worstPrice(a.get(0), a.get(1), a.size() > 2 ? a.get(2) : null);
                    default -> throw new IllegalStateException("a function this runner does not play: " + c.get("fn"));
                };
                if (c.path("error").asBoolean(false)) {
                    // a ValidationException of the SDK's own
                    assertThrows(PhoenixPerpsException.ValidationException.class, call::get);
                } else {
                    assertEquals(c.get("want").asText(), call.get().toPlainString());
                }
            }));
        }
        return tests.stream();
    }

    @Test
    void aStalledBodyIsATimeout() throws Exception {
        // the answer's headers came in time, its body never does: a timeout as any other, within the call's timeout
        try (java.net.ServerSocket server = new java.net.ServerSocket(0)) {
            Thread t = new Thread(() -> {
                try (java.net.Socket s = server.accept()) {
                    s.getInputStream().read(new byte[8192]);
                    s.getOutputStream().write("HTTP/1.1 200 OK\r\ncontent-type: application/json\r\ncontent-length: 100\r\n\r\n{\"da".getBytes(StandardCharsets.UTF_8));
                    s.getOutputStream().flush();
                    Thread.sleep(10000);
                } catch (Exception e) {
                    // the client gave up
                }
            });
            t.setDaemon(true);
            t.start();
            PhoenixPerpsClient c = PhoenixPerpsClient.builder().apiKey(key).baseUrl("http://127.0.0.1:" + server.getLocalPort()).timeout(Duration.ofSeconds(1)).maxRetries(0).build();
            long t0 = System.nanoTime();
            PhoenixPerpsException e = assertThrows(PhoenixPerpsException.ApiTimeoutException.class, () -> c.me().get());
            assertTrue(System.nanoTime() - t0 < 5_000_000_000L, "within the timeout, not when the server gives up");
            assertTrue(e.retryable());
        }
    }

    @Test
    void anOrderChangeWithItsKeyIsSentAgain() throws Exception {
        // a PATCH honours an Idempotency-Key when one is given: with it, it is tried again with the same key
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":503,\"headers\":{\"retry-after\":\"0\"},\"body\":{\"error\":{\"type\":\"engine\",\"code\":\"engine_unavailable\",\"message\":\"x\",\"request_id\":\"req_1\",\"retryable\":true}}}},"
                + "{\"respond\":{\"status\":200,\"body\":{\"data\":{\"id\":\"ord_1\"}}}}]")));
        assertEquals("ord_1", client().orders().modify("SANDBOX-00042-001", "ord_1", new ModifyOrder().limitPrice(new BigDecimal("61900")), RequestOptions.none().withIdempotencyKey("chg-1")).id());
        assertEquals(List.of("chg-1", "chg-1"), requests().stream().map(r -> r.get("headers").get("idempotency-key").asText()).toList());
    }

    @Test
    void aWindowPerBudget() throws Exception {
        // a budget used up (trade) holds back its own next call until its reset, never a call of another budget (read)
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":200,\"headers\":{\"x-ratelimit-limit\":\"10\",\"x-ratelimit-remaining\":\"0\",\"x-ratelimit-reset\":\"2\"},\"body\":{\"data\":{}}}},"
                + "{\"respond\":{\"status\":200,\"body\":{\"data\":{}}}},{\"respond\":{\"status\":200,\"body\":{\"data\":{}}}}]")));
        Transport t = client().transport();
        Transport.Operation trade = new Transport.Operation("trade", "POST", "/v1/trade", false, false, null, "trade");
        Transport.Operation read = new Transport.Operation("read", "GET", "/v1/read", false, true, null, "read");
        t.one(trade, null, null, null, JsonNode.class, null);
        t.one(read, null, null, null, JsonNode.class, null);
        t.one(trade, null, null, null, JsonNode.class, null);
        List<JsonNode> got = requests();
        assertTrue(got.get(1).get("at").asLong() - got.get(0).get("at").asLong() < 1000, "read: not held back");
        assertTrue(got.get(2).get("at").asLong() - got.get(0).get("at").asLong() >= 1800, "trade: held back until its reset");
    }

    @Test
    void accountStreamTellsAsItComes() throws Exception {
        // risk asked: the subscribe says so and each risk frame goes to onRisk; each event keeps its frame's seq and
        // replayed; a 4008 is a gap, told with the last event id, and the next connection does not resume
        String hello = "{\"send\":{\"type\":\"hello\",\"session\":\"ses_1\",\"environment\":\"sandbox\",\"heartbeat_ms\":15000,\"accounts\":[\"SANDBOX-00042-001\"]}}";
        String event = "{\"send\":{\"type\":\"event\",\"seq\":%d,%s\"event\":{\"id\":\"%s\",\"type\":\"order.created\",\"account_id\":\"SANDBOX-00042-001\",\"time\":1791460800000,\"source\":\"api\",\"data\":{}}}}";
        connections("[[" + hello + ",{\"expect\":\"subscribe\"}," + String.format(event, 1, "\"replayed\":true,", "evt_1") + ",{\"send\":{\"type\":\"snapshot_end\",\"accounts\":[\"SANDBOX-00042-001\"]}},"
                + "{\"send\":{\"type\":\"risk\",\"account_id\":\"SANDBOX-00042-001\",\"data\":{\"equity\":\"10000\"}}}," + String.format(event, 2, "", "evt_2") + ",{\"close\":4008}],"
                + "[" + hello + ",{\"expect\":\"subscribe\"},{\"send\":{\"type\":\"snapshot_end\",\"accounts\":[]}}," + String.format(event, 1, "", "evt_3") + ",{\"wait_ms\":3000}]]");
        List<JsonNode> risks = new CopyOnWriteArrayList<>();
        List<String> gaps = new CopyOnWriteArrayList<>();
        List<String> log = new CopyOnWriteArrayList<>();
        List<Event> events = new ArrayList<>();
        try (AccountStream s = streamClient(log).openAccountStream(AccountStream.options().risk(true).onRisk(risks::add).onGap(gaps::add))) {
            while (events.size() < 3) {
                Event e = s.poll(Duration.ofSeconds(10));
                assertNotNull(e, "an event in time");
                events.add(e);
            }
        }
        assertEquals(List.of(1L, 2L, 1L), events.stream().map(e -> e.extra().get("seq")).toList());
        assertEquals(List.of(true, false, false), events.stream().map(e -> e.extra().get("replayed")).toList());
        assertEquals(1, risks.size());
        assertEquals("10000", risks.get(0).path("data").path("equity").asText());
        assertEquals(List.of("evt_2"), gaps);
        assertTrue(log.stream().anyMatch(l -> l.contains("GET /v1/events?after=evt_2")), log.toString());
        List<JsonNode> got = requests();
        assertEquals(Json.MAPPER.readTree("{\"op\":\"subscribe\",\"risk\":true}"), got.get(0).get("messages").get(0));
        assertNull(got.get(1).get("query").get("resume"), "4008: no resume");
    }

    @Test
    void marketSubscriptions() throws Exception {
        // a subscription is a channel, a market and (candles) an interval, with the last subscribe's settings; an
        // unsubscribe names markets (and an interval) only, and without one ends every interval; made again one subscribe
        // per settings, at most the hello's symbols_per_subscribe markets each; an error frame keeps its details
        String hello = "{\"send\":{\"type\":\"hello\",\"session\":\"mds_1\",\"heartbeat_ms\":15000,\"channels\":[],\"intervals\":[],\"limits\":{\"subscriptions\":200,\"symbols_per_subscribe\":2,\"messages_per_s\":20}}}";
        String took = "{\"expect\":\"subscribe\"},{\"reply\":{\"type\":\"subscribed\",\"channel\":\"%s\",\"symbols\":%s,\"rejected\":[]}}";
        connections("[[" + hello + "," + String.format(took, "ticker", "[\"BTC-USD\",\"ETH-USD\",\"SOL-USD\",\"ADA-USD\"]") + "," + String.format(took, "ticker", "[\"ETH-USD\"]") + ","
                + String.format(took, "candles", "[\"BTC-USD\"]") + "," + String.format(took, "candles", "[\"BTC-USD\"]") + "," + String.format(took, "candles", "[\"ETH-USD\"]") + ","
                + "{\"expect\":\"unsubscribe\"},{\"reply\":{\"type\":\"unsubscribed\",\"channel\":\"candles\",\"symbols\":[\"BTC-USD\"],\"rejected\":[]}},"
                + "{\"expect\":\"subscribe\"},{\"reply\":{\"type\":\"error\",\"code\":\"invalid_parameter\",\"details\":{\"field\":\"symbols\",\"issue\":\"1 to 50\"}}},{\"close\":1012}],"
                + "[" + hello + ",{\"wait_ms\":3000}]]");
        Duration w = Duration.ofSeconds(5);
        try (MarketStream m = streamClient(new ArrayList<>()).openMarketStream()) {
            m.subscribe("ticker", List.of("BTC-USD", "ETH-USD", "SOL-USD", "ADA-USD"), Map.of("interval_ms", 250), w);
            m.subscribe("ticker", List.of("ETH-USD"), Map.of("interval_ms", 500), w);
            m.subscribe("candles", List.of("BTC-USD"), Map.of("interval", "1m", "history", 5), w);
            m.subscribe("candles", List.of("BTC-USD"), Map.of("interval", "5m"), w);
            m.subscribe("candles", List.of("ETH-USD"), Map.of("interval", "1m"), w);
            m.unsubscribe("candles", List.of("BTC-USD"), Map.of("history", 5), w);
            PhoenixPerpsException e = assertThrows(PhoenixPerpsException.ValidationException.class, () -> m.subscribe("book", List.of(), Map.of(), w));
            assertEquals("invalid_parameter", e.code());
            assertEquals("symbols", e.details().path("field").asText());
            await(() -> {
                try {
                    List<JsonNode> r = requests();
                    return r.size() == 2 && r.get(1).get("messages").size() >= 4;
                } catch (Exception x) {
                    return false;
                }
            });
        }
        List<JsonNode> got = requests();
        assertEquals(Json.MAPPER.readTree("{\"op\":\"unsubscribe\",\"channel\":\"candles\",\"symbols\":[\"BTC-USD\"]}"), ((ObjectNode) got.get(0).get("messages").get(5).deepCopy()).without("id"));
        List<JsonNode> again = new ArrayList<>();
        got.get(1).get("messages").forEach(x -> again.add(((ObjectNode) x.deepCopy()).without("id")));
        assertEquals(Json.MAPPER.readTree("[{\"interval_ms\":250,\"op\":\"subscribe\",\"channel\":\"ticker\",\"symbols\":[\"BTC-USD\",\"SOL-USD\"]},"
                + "{\"interval_ms\":250,\"op\":\"subscribe\",\"channel\":\"ticker\",\"symbols\":[\"ADA-USD\"]},{\"interval_ms\":500,\"op\":\"subscribe\",\"channel\":\"ticker\",\"symbols\":[\"ETH-USD\"]},"
                + "{\"interval\":\"1m\",\"op\":\"subscribe\",\"channel\":\"candles\",\"symbols\":[\"ETH-USD\"]}]"), Json.MAPPER.valueToTree(again));
    }

    @Test
    void openEnumAndUnknownField() throws Exception {
        load(Map.of("exchanges", Json.MAPPER.readTree("[{\"respond\":{\"status\":200,\"body\":{\"data\":{\"id\":\"ord_1\",\"status\":\"a_status_from_the_future\",\"a_new_field\":1}}}}]")));
        Order o = client().orders().get("SANDBOX-00042-001", "ord_1");
        assertEquals("a_status_from_the_future", o.status().value());
        assertEquals(1, o.extra().get("a_new_field"));
        Iterator<String> names = Json.MAPPER.valueToTree(o).fieldNames();
        List<String> fields = new ArrayList<>();
        names.forEachRemaining(fields::add);
        assertTrue(fields.contains("a_new_field"), "kept when written again");
    }
}
