package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.ApiConnectionException;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.ApiTimeoutException;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.AuthenticationException;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.ServerException;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.ValidationException;
import com.phoenixperpsfunding.sdk.gen.Meta;
import com.phoenixperpsfunding.sdk.gen.Resources;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Phoenix Perpetuals API (docs/BEHAVIOUR.md): every REST call, the account stream and market data, with retries,
 * idempotency keys, paging and typed errors. Make one and share it: it is safe for concurrent use.
 *
 * <pre>{@code
 * PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();   // PHOENIXPERPS_API_KEY
 * Order order = client.orders().create(accountId, new CreateOrder().market("BTC-USD").side(OrderSide.BUY)
 *     .type(CreateOrderType.LIMIT).size(new BigDecimal("0.01")).limitPrice(new BigDecimal("62000")));
 * }</pre>
 */
public final class PhoenixPerpsClient implements Resources.Access {
    /** This SDK's version (lockstep with every other Phoenix Perpetuals SDK). */
    public static final String SDK_VERSION = "1.0.0";

    private static final Pattern PREFIX = Pattern.compile("^(ppk|ppo)_(live|test)_");
    static final Set<Integer> RETRY_STATUS = Set.of(429, 500, 502, 503, 504);
    private static final Duration RETRY_AFTER_MAX = Duration.ofSeconds(60);

    private final String key;
    private final String environment;
    private final String baseUrl;
    private final Duration timeout;
    private final int maxRetries;
    private final String appInfo;
    private final HttpClient http;
    private final BiConsumer<String, String> logger;
    final String accountStreamUrl;
    final String marketStreamUrl;
    private final Core core;
    private final Resources.All resources;
    private final Set<String> warned = new HashSet<>();
    private final Map<String, RateLimitState> windows = new HashMap<>(); // per budget: an operation's x-pp-scope
    private RateLimitState rateLimit; // the last answer's

    /** What the last answer of a budget said of its rate-limit window. */
    private record RateLimitState(int limit, int remaining, Instant resetAt) {
    }

    private PhoenixPerpsClient(Builder b) {
        String k = b.apiKey != null ? b.apiKey : System.getenv("PHOENIXPERPS_API_KEY");
        // no key: the public calls (markets, platform) work, sent without Authorization; any other answers missing_api_key
        if (k != null && k.isEmpty()) {
            k = null;
        }
        Matcher m = k == null ? null : PREFIX.matcher(k);
        String inferred = m != null && m.find() ? ("live".equals(m.group(2)) ? "live" : "sandbox") : null;
        // the one given, else the key's own, else PHOENIXPERPS_ENVIRONMENT (a variable never turns a key against itself)
        String env = b.environment != null ? b.environment : inferred != null ? inferred : System.getenv("PHOENIXPERPS_ENVIRONMENT");
        env = env == null ? "sandbox" : env.toLowerCase(Locale.ROOT);
        if (!env.equals("live") && !env.equals("sandbox")) {
            throw new ValidationException("invalid_environment", "The environment is live or sandbox.", null);
        }
        if (inferred != null && !inferred.equals(env)) {
            throw new AuthenticationException("wrong_environment", "This key is a " + inferred + " key, not a " + env + " one.", null);
        }
        String base = b.baseUrl != null ? b.baseUrl : System.getenv("PHOENIXPERPS_BASE_URL");
        this.key = k;
        this.environment = env;
        // the host; the REST address as the docs write it (https://api.phoenixperpsfunding.com/v1) is taken as its host
        this.baseUrl = (base != null ? base : Meta.SERVERS.get(env)).replaceAll("(/v1)?/*$", "");
        this.timeout = b.timeout;
        this.maxRetries = b.maxRetries;
        this.appInfo = b.appInfo;
        // HTTP/2 over TLS (ALPN); a plain http host (a local proxy, a test server) gets HTTP/1.1, never an h2c upgrade
        this.http = b.httpClient != null ? b.httpClient : HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10))
                .version(this.baseUrl.startsWith("http://") ? HttpClient.Version.HTTP_1_1 : HttpClient.Version.HTTP_2).build();
        this.logger = b.logger;
        this.accountStreamUrl = b.accountStreamUrl != null ? b.accountStreamUrl : Meta.ACCOUNT_STREAMS.get(env);
        this.marketStreamUrl = b.marketStreamUrl != null ? b.marketStreamUrl : Meta.MARKET_STREAM;
        this.core = new Core();
        this.resources = new Resources.All(core);
    }

    /** A client from the environment (PHOENIXPERPS_API_KEY, PHOENIXPERPS_ENVIRONMENT, PHOENIXPERPS_BASE_URL). */
    public static PhoenixPerpsClient fromEnv() {
        return builder().build();
    }

    /** A builder; a key of the other environment fails at build(), before any request. */
    public static Builder builder() {
        return new Builder();
    }

    /** The options of a client. */
    public static final class Builder {
        private String apiKey;
        private String environment;
        private String baseUrl;
        private Duration timeout = Duration.ofSeconds(30);
        private int maxRetries = 2;
        private String appInfo;
        private HttpClient httpClient;
        private BiConsumer<String, String> logger;
        private String accountStreamUrl;
        private String marketStreamUrl;

        private Builder() {
        }

        /** The key (else PHOENIXPERPS_API_KEY; with neither, only the public calls work: markets, platform). */
        public Builder apiKey(String v) {
            apiKey = v;
            return this;
        }

        /** live or sandbox, in any case (else the key's own, else PHOENIXPERPS_ENVIRONMENT). */
        public Builder environment(String v) {
            environment = v;
            return this;
        }

        /** The API host, or its REST address (.../v1) (else PHOENIXPERPS_BASE_URL, else the environment's). */
        public Builder baseUrl(String v) {
            baseUrl = v;
            return this;
        }

        /** The time one attempt may take (30 s). */
        public Builder timeout(Duration v) {
            timeout = v;
            return this;
        }

        /** How often a safe call is tried again (2). */
        public Builder maxRetries(int v) {
            maxRetries = v;
            return this;
        }

        /** Your app's name and version, added to the User-Agent. */
        public Builder appInfo(String v) {
            appInfo = v;
            return this;
        }

        /** The HttpClient the calls go through. */
        public Builder httpClient(HttpClient v) {
            httpClient = v;
            return this;
        }

        /** Receives (level, message): a line per retry (a call's, or a stream's reconnect, with why) and one per deprecated API version. */
        public Builder logger(BiConsumer<String, String> v) {
            logger = v;
            return this;
        }

        /** The account and market stream URLs (null keeps the default). */
        public Builder streamUrls(String account, String market) {
            accountStreamUrl = account;
            marketStreamUrl = market;
            return this;
        }

        /** The client. */
        public PhoenixPerpsClient build() {
            return new PhoenixPerpsClient(this);
        }
    }

    @Override
    public Resources.All resources() {
        return resources;
    }

    /** live or sandbox. */
    public String environment() {
        return environment;
    }

    /** The API host the calls go to. */
    public String baseUrl() {
        return baseUrl;
    }

    /** The key with its secret hidden (ppk_test_****abcd); null without a key. */
    public String maskedKey() {
        return mask(key);
    }

    /** The requests the last answer said were left in the window (null before the first answer). */
    public synchronized Integer rateLimitRemaining() {
        return rateLimit == null ? null : rateLimit.remaining();
    }

    /** When the rate-limit window starts again (null when unknown). */
    public synchronized Instant rateLimitResetAt() {
        return rateLimit == null ? null : rateLimit.resetAt();
    }

    /** What every request says of the SDK. */
    public String userAgent() {
        return "phoenixperps-java/" + SDK_VERSION + " (java " + System.getProperty("java.version") + "; " + System.getProperty("os.name").split(" ")[0].toLowerCase() + ") spec/" + Meta.SPEC_VERSION + (appInfo == null ? "" : " " + appInfo);
    }

    /** Never shows the key. */
    @Override
    public String toString() {
        return "PhoenixPerpsClient{environment=" + environment + ", baseUrl=" + baseUrl + ", apiKey=" + maskedKey() + "}";
    }

    /** Its JSON (Json.MAPPER or any Jackson mapper) never shows the key either. */
    @JsonValue
    Map<String, String> json() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("environment", environment);
        m.put("baseUrl", baseUrl);
        m.put("apiKey", maskedKey());
        return m;
    }

    /**
     * Any request to the API (a route newer than this SDK) with the client's key, retries and errors. path is the API
     * path (/v1/...); query and body may be null. It is tried again when that is safe: a GET, a route the spec marks
     * retry-safe, or a call with an Idempotency-Key (RequestOptions.withIdempotencyKey). The answer comes as it came
     * (read its body with Json.MAPPER); a failure throws its PhoenixPerpsException.
     */
    public RawResponse request(String method, String path, Map<String, String> query, Object body, RequestOptions options) {
        if (path == null || !path.startsWith("/")) {
            throw new ValidationException("invalid_request", "The path starts with /.", null);
        }
        String m = method.toUpperCase(Locale.ROOT);
        Transport.Operation known = route(m, path);
        boolean retrySafe = "GET".equals(m) || (known != null && known.retrySafe());
        return core.send(new Transport.Operation("request", m, path, options != null && options.idempotencyKey != null, retrySafe, null, known == null ? null : known.scope()), null, query, body, options);
    }

    /** The operation of the spec for this method and path (the most literal segments win), or null. */
    private static Transport.Operation route(String method, String path) {
        String[] segs = path.split("/", -1);
        Transport.Operation best = null;
        int most = -1;
        for (Transport.Operation op : Resources.Ops.ALL) {
            String[] tpl = op.path().split("/", -1);
            if (!op.method().equals(method) || tpl.length != segs.length) {
                continue;
            }
            int literal = 0;
            for (int i = 0; i < tpl.length && literal >= 0; i++) {
                if (tpl[i].startsWith("{") && !segs[i].isEmpty()) {
                    continue;
                }
                literal = tpl[i].equals(segs[i]) ? literal + 1 : -1;
            }
            if (literal > most) {
                best = op;
                most = literal;
            }
        }
        return best;
    }

    /** Connects to the account stream; it runs until closed. */
    public AccountStream openAccountStream(AccountStream.Options options) {
        return new AccountStream(this, options == null ? AccountStream.options() : options);
    }

    /** Connects to the market data stream; it runs until closed. */
    public MarketStream openMarketStream() {
        return new MarketStream(this);
    }

    String key() {
        return key;
    }

    HttpClient http() {
        return http;
    }

    Transport transport() {
        return core;
    }

    static String mask(String key) {
        if (key == null) {
            return null;
        }
        int i = key.indexOf('_', Math.min(4, key.length()));
        return (i >= 0 ? key.substring(0, i + 1) : "") + "****" + (key.length() > 4 ? key.substring(key.length() - 4) : key);
    }

    /** text with the key masked (a failure's message may quote the request). */
    String redact(String text) {
        return key == null || text == null ? text : text.replace(key, maskedKey());
    }

    void log(String level, String message) {
        if (logger != null) {
            logger.accept(level, message);
        }
    }

    static Duration retryAfter(HttpHeaders h) {
        String v = h.firstValue("retry-after").map(String::trim).orElse("");
        if (v.isEmpty()) {
            return null;
        }
        if (v.matches("\\d+(\\.\\d+)?")) {
            // seconds, fractions included (1.5 is 1500 ms, never 0)
            BigDecimal ms = new BigDecimal(v).movePointRight(3).setScale(0, RoundingMode.CEILING);
            return Duration.ofMillis(ms.min(BigDecimal.valueOf(Long.MAX_VALUE)).longValue());
        }
        try {
            Duration d = Duration.between(Instant.now(), ZonedDateTime.parse(v, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant());
            return d.isNegative() ? Duration.ZERO : d;
        } catch (RuntimeException e) {
            return null;
        }
    }

    /** Any answer as an error: the API's envelope, a bare {"error": "text"}, or HTML or text. */
    static PhoenixPerpsException errorFrom(int status, HttpHeaders h, byte[] body, String idempotencyKey) {
        String code = "http_" + status;
        String message = "";
        String type = null;
        String orderId = null;
        String docUrl = null;
        Boolean retryable = null;
        String requestId = h.firstValue("x-request-id").orElse(null);
        JsonNode details = null;
        String text = new String(body, StandardCharsets.UTF_8);
        try {
            JsonNode root = Json.MAPPER.readTree(body);
            JsonNode e = root == null ? null : root.get("error");
            if (e != null && e.isObject()) {
                if (e.hasNonNull("code")) {
                    code = e.get("code").asText();
                }
                message = e.path("message").asText("");
                type = e.hasNonNull("type") ? e.get("type").asText() : null;
                orderId = e.hasNonNull("order_id") ? e.get("order_id").asText() : null;
                docUrl = e.hasNonNull("doc_url") ? e.get("doc_url").asText() : null;
                retryable = e.path("retryable").isBoolean() ? e.get("retryable").asBoolean() : null;
                if (e.hasNonNull("request_id")) {
                    requestId = e.get("request_id").asText();
                }
                details = e.hasNonNull("details") ? e.get("details") : null;
            } else if (e != null && e.isTextual()) {
                message = clip(e.asText());
            } else {
                message = clip(text);
            }
        } catch (IOException | RuntimeException ex) {
            message = clip(text);
        }
        if (message.isEmpty()) {
            message = code;
        }
        PhoenixPerpsException x = PhoenixPerpsException.forStatus(status, code, message);
        x.errorType = type;
        x.requestId = requestId;
        x.details = details;
        x.retryAfter = retryAfter(h);
        // the API's own flag (a 409 operation_in_progress may be asked again, a 503 sandbox_disabled not); else the status
        x.retryable = retryable != null ? retryable : RETRY_STATUS.contains(status);
        x.docUrl = docUrl;
        x.orderId = orderId;
        x.idempotencyKey = idempotencyKey;
        x.headers = h;
        return x;
    }

    /** A 2xx whose body is not JSON (a proxy's page): an error, never an empty answer. */
    static PhoenixPerpsException notJson(RawResponse raw, Exception cause) {
        int status = raw.statusCode();
        PhoenixPerpsException x = new ServerException("http_" + status, "The answer is not JSON: " + clip(new String(raw.body(), StandardCharsets.UTF_8)), status, cause);
        x.requestId = raw.requestId();
        x.idempotencyKey = raw.idempotencyKey();
        x.headers = raw.headers();
        return x;
    }

    static String clip(String s) {
        return s.length() > 200 ? s.substring(0, 200) : s;
    }

    private static String encodePath(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8).replace("+", "%20");
    }

    /** The transport of the resources, kept off the client's own surface. */
    private final class Core implements Transport {
        // the window an operation draws from: its budget (one for every operation whose budget is not known)
        private String budget(Operation op) {
            return op.scope() == null ? "" : op.scope();
        }

        private void preWait(Operation op) {
            RateLimitState r;
            synchronized (PhoenixPerpsClient.this) {
                r = windows.get(budget(op));
            }
            if (r == null || r.remaining() > 0 || r.resetAt() == null) {
                return;
            }
            Duration wait = Duration.between(Instant.now(), r.resetAt());
            if (!wait.isNegative() && wait.compareTo(Duration.ofMinutes(1)) < 0) {
                sleep(wait);
            }
        }

        private void remember(Operation op, HttpHeaders h) {
            // any answer may say its version is deprecated, with or without the rate-limit headers
            String dep = h.firstValue("deprecation").orElse(null);
            boolean first;
            synchronized (PhoenixPerpsClient.this) {
                first = dep != null && warned.add(dep);
            }
            if (first) {
                log("warn", "phoenixperps: this API version is deprecated (Deprecation: " + dep + ")");
            }
            String limit = h.firstValue("x-ratelimit-limit").orElse(null);
            if (limit == null) {
                return;
            }
            Instant resetAt = null;
            try {
                double reset = Double.parseDouble(h.firstValue("x-ratelimit-reset").orElse(""));
                // Reset as epoch seconds (above a year in seconds) or as seconds from now
                resetAt = reset > 31536000 ? Instant.ofEpochMilli((long) (reset * 1000)) : Instant.now().plusMillis((long) (reset * 1000));
            } catch (NumberFormatException e) {
                resetAt = null;
            }
            int remaining = 0;
            try {
                remaining = Integer.parseInt(h.firstValue("x-ratelimit-remaining").orElse("0"));
            } catch (NumberFormatException e) {
                remaining = 0;
            }
            synchronized (PhoenixPerpsClient.this) {
                int lim;
                try {
                    lim = Integer.parseInt(limit);
                } catch (NumberFormatException e) {
                    lim = 0;
                }
                rateLimit = new RateLimitState(lim, remaining, resetAt);
                windows.put(budget(op), rateLimit);
            }
        }

        // the answer within t, its body included (the request's own timeout ends with the headers)
        private HttpResponse<byte[]> exchange(HttpRequest req, Duration t) throws IOException, InterruptedException {
            CompletableFuture<HttpResponse<byte[]>> f = http.sendAsync(req, HttpResponse.BodyHandlers.ofByteArray());
            try {
                return f.get(t.toMillis(), TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                f.cancel(true);
                throw new HttpTimeoutException("no answer within " + t.toMillis() + " ms");
            } catch (InterruptedException e) {
                f.cancel(true);
                throw e;
            } catch (ExecutionException e) {
                if (e.getCause() instanceof IOException io) {
                    throw io;
                }
                if (e.getCause() instanceof RuntimeException r) {
                    throw r;
                }
                throw new IOException(e.getCause());
            }
        }

        private URI uri(Operation op, String[] path, Map<String, String> query) {
            StringBuilder sb = new StringBuilder(baseUrl);
            String p = op.path();
            int n = 0;
            for (int i = 0; i < p.length(); i++) {
                char ch = p.charAt(i);
                if (ch != '{') {
                    sb.append(ch);
                    continue;
                }
                int end = p.indexOf('}', i);
                String v = path != null && n < path.length ? path[n++] : null;
                if (v == null || v.isEmpty()) {
                    throw new ValidationException("invalid_request", p.substring(i + 1, end) + " is required.", null);
                }
                sb.append(encodePath(v));
                i = end;
            }
            if (query != null && !query.isEmpty()) {
                StringBuilder q = new StringBuilder();
                for (Map.Entry<String, String> e : query.entrySet()) {
                    q.append(q.length() == 0 ? '?' : '&').append(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)).append('=').append(URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8));
                }
                sb.append(q);
            }
            return URI.create(sb.toString());
        }

        /** One logical call: its attempts, and its answer. */
        RawResponse send(Operation op, String[] path, Map<String, String> query, Object body, RequestOptions options) {
            RequestOptions o = options == null ? RequestOptions.none() : options;
            String idem = o.idempotencyKey != null ? o.idempotencyKey : op.idempotent() ? UUID.randomUUID().toString() : null;
            // safe to send again: a GET, a retry-safe route, or a call with its Idempotency-Key (an order change given one)
            boolean safe = op.retrySafe() || idem != null;
            int max = o.maxRetries != null ? o.maxRetries : maxRetries;
            Duration t = o.timeout != null ? o.timeout : timeout;
            URI uri = uri(op, path, query);
            byte[] payload;
            try {
                payload = body == null ? null : Json.MAPPER.writeValueAsBytes(body);
            } catch (IOException e) {
                throw new ValidationException("invalid_request", e.getMessage(), null);
            }
            for (int attempt = 0;; attempt++) {
                preWait(op);
                HttpRequest.Builder rb = HttpRequest.newBuilder(uri).timeout(t)
                        .header("Accept", "application/json")
                        .header("User-Agent", userAgent())
                        .method(op.method(), payload == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofByteArray(payload));
                if (key != null) {
                    rb.header("Authorization", "Bearer " + key);
                }
                if (payload != null) {
                    rb.header("Content-Type", "application/json");
                }
                if (idem != null) {
                    rb.header("Idempotency-Key", idem);
                }
                if (o.headers != null) {
                    o.headers.forEach(rb::header);
                }
                HttpResponse<byte[]> res = null;
                PhoenixPerpsException failure = null;
                try {
                    res = exchange(rb.build(), t);
                } catch (HttpTimeoutException e) {
                    failure = new ApiTimeoutException("timeout", "No answer within " + t.toMillis() + " ms.", e);
                    failure.idempotencyKey = idem;
                } catch (IOException e) {
                    failure = new ApiConnectionException("connection_failed", clip(redact(String.valueOf(e.getMessage()))), e);
                    failure.idempotencyKey = idem;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ApiConnectionException("interrupted", "The call was interrupted.", e);
                }
                if (res != null) {
                    remember(op, res.headers());
                    RawResponse raw = new RawResponse(res.statusCode(), res.headers(), res.body(), res.headers().firstValue("x-request-id").orElse(null), idem);
                    if (o.onResponse != null) {
                        o.onResponse.accept(raw);
                    }
                    // a 304 answers the caller's own If-None-Match: nothing changed, no error (and no body)
                    if ((res.statusCode() >= 200 && res.statusCode() < 300) || res.statusCode() == 304) {
                        return raw;
                    }
                    PhoenixPerpsException err = errorFrom(res.statusCode(), res.headers(), res.body(), idem);
                    Duration ra = err.retryAfter();
                    if (!(safe && attempt < max && err.retryable()) || (ra != null && ra.compareTo(RETRY_AFTER_MAX) > 0)) {
                        throw err;
                    }
                    Duration wait = ra != null ? ra : jitter(attempt);
                    log("debug", "phoenixperps: retrying " + op.id() + " after " + res.statusCode() + " " + err.code() + " in " + wait.toMillis() + " ms (attempt " + (attempt + 1) + ")");
                    sleep(wait);
                    continue;
                }
                if (!(safe && attempt < max)) {
                    throw failure;
                }
                Duration wait = jitter(attempt);
                log("debug", "phoenixperps: retrying " + op.id() + " after " + failure.code() + " in " + wait.toMillis() + " ms (attempt " + (attempt + 1) + ")");
                sleep(wait);
            }
        }

        // the body as JSON; a 2xx that is not (a proxy's page, nothing at all) is an error, never an empty answer
        private JsonNode tree(RawResponse raw) {
            JsonNode root;
            try {
                root = Json.MAPPER.readTree(raw.body());
            } catch (IOException e) {
                throw notJson(raw, e);
            }
            if (root == null || root.isMissingNode()) {
                throw notJson(raw, null);
            }
            return root;
        }

        @Override
        public <T> T one(Operation op, String[] path, Map<String, String> query, Object body, Class<T> type, RequestOptions options) {
            RawResponse raw = send(op, path, query, body, options);
            // a 304 (nothing changed since the caller's If-None-Match) answers nothing, a 204 too; its meta: withOnResponse.
            // Any other empty answer is no JSON: http_<status>
            if (raw.statusCode() == 304 || (raw.statusCode() == 204 && raw.body().length == 0)) {
                return null;
            }
            JsonNode root = tree(raw);
            try {
                JsonNode data = root != null && root.isObject() && root.has("data") ? root.get("data") : root;
                return Json.MAPPER.treeToValue(data, type);
            } catch (IOException | RuntimeException e) {
                throw new ServerException("invalid_response", "The answer could not be read: " + e.getMessage(), raw.statusCode(), e);
            }
        }

        @Override
        public <T> Page<T> list(Operation op, String[] path, Map<String, String> query, Class<T> type, RequestOptions options) {
            RawResponse raw = send(op, path, query, null, options);
            if (raw.statusCode() == 304) {
                return null;
            }
            JsonNode root = tree(raw);
            Page<T> page;
            try {
                JavaType jt = Json.MAPPER.getTypeFactory().constructParametricType(Page.class, type);
                page = Json.MAPPER.readerFor(jt).readValue(root);
            } catch (IOException | RuntimeException e) {
                throw new ServerException("invalid_response", "The answer could not be read: " + e.getMessage(), raw.statusCode(), e);
            }
            page.link(raw, op.cursor() == null ? null : cursor -> {
                Map<String, String> q = new LinkedHashMap<>(query == null ? Map.of() : query);
                q.put(op.cursor(), cursor);
                return list(op, path, q, type, options);
            });
            return page;
        }

        @Override
        public <T> Iterable<T> autoPaging(Supplier<Page<T>> first, RequestOptions options) {
            int max = options != null && options.maxItems != null ? options.maxItems : 0;
            return () -> new Iterator<T>() {
                private Page<T> page;
                private int index;
                private int n;
                private boolean started;

                @Override
                public boolean hasNext() {
                    if (max > 0 && n >= max) {
                        return false;
                    }
                    if (!started) {
                        started = true;
                        page = first.get();
                    }
                    while (page != null && index >= page.data().size()) {
                        page = page.nextPage();
                        index = 0;
                    }
                    return page != null;
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    n++;
                    return page.data().get(index++);
                }
            };
        }
    }

    private static Duration jitter(int attempt) {
        return Duration.ofMillis((long) (ThreadLocalRandom.current().nextDouble() * Math.min(8000, 500 * Math.pow(2, attempt))));
    }

    static void sleep(Duration d) {
        try {
            Thread.sleep(d.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiConnectionException("interrupted", "The call was interrupted.", e);
        }
    }
}
