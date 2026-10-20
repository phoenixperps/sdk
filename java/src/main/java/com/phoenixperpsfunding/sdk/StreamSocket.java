package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.net.http.WebSocketHandshakeException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * One WebSocket connection of a stream, read as a queue of whole text frames (the JDK's java.net.http.WebSocket, no
 * dependency): next(timeout) answers a frame, a close (its code), a failure, or null when nothing came in time.
 */
final class StreamSocket {
    /** What the connection gave: a frame, or its end. */
    record Item(JsonNode frame, Integer closeCode, Throwable error) {
    }

    /** The upgrade was refused with this HTTP status (401, 403, 429...), and the server's answer as an error. */
    static final class Refused extends RuntimeException {
        private static final long serialVersionUID = 1L;
        final int status;
        final PhoenixPerpsException error;

        Refused(HttpResponse<?> r) {
            super("refused " + r.statusCode());
            this.status = r.statusCode();
            Object body = r.body();
            byte[] bytes = body instanceof byte[] b ? b : body == null ? new byte[0] : String.valueOf(body).getBytes(StandardCharsets.UTF_8);
            this.error = PhoenixPerpsClient.errorFrom(status, r.headers(), bytes, null);
        }

        /** A refusal a retry may change (429, 5xx), as for a REST call. */
        boolean retryable() {
            return PhoenixPerpsClient.RETRY_STATUS.contains(status);
        }

        /** What the log says of it. */
        String why() {
            return "refused with " + status + " " + error.code() + " (" + error.getMessage() + ")";
        }
    }

    private final BlockingQueue<Item> items = new LinkedBlockingQueue<>();
    private final WebSocket ws;

    StreamSocket(PhoenixPerpsClient c, String url) {
        StringBuilder text = new StringBuilder();
        WebSocket.Listener listener = new WebSocket.Listener() {
            @Override
            public void onOpen(WebSocket w) {
                w.request(1);
            }

            @Override
            public CompletionStage<?> onText(WebSocket w, CharSequence data, boolean last) {
                text.append(data);
                if (last) {
                    try {
                        items.add(new Item(Json.MAPPER.readTree(text.toString()), null, null));
                    } catch (java.io.IOException e) {
                        items.add(new Item(null, null, e));
                    }
                    text.setLength(0);
                }
                w.request(1);
                return null;
            }

            @Override
            public CompletionStage<?> onClose(WebSocket w, int statusCode, String reason) {
                items.add(new Item(null, statusCode, null));
                return null;
            }

            @Override
            public void onError(WebSocket w, Throwable error) {
                items.add(new Item(null, null, error));
            }
        };
        WebSocket.Builder b = c.http().newWebSocketBuilder().header("User-Agent", c.userAgent()).connectTimeout(Duration.ofSeconds(10));
        if (c.key() != null) {
            b.header("Authorization", "Bearer " + c.key());
        }
        try {
            ws = b.buildAsync(URI.create(url), listener).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof WebSocketHandshakeException h) {
                throw new Refused(h.getResponse());
            }
            throw e;
        }
    }

    /**
     * What the log says of a failure: the classes of its causes (a DNS failure has no message: ConnectException:
     * UnresolvedAddressException) and the first message that is not a cause's own name, the key masked.
     */
    static String why(PhoenixPerpsClient c, Throwable e) {
        StringBuilder sb = new StringBuilder();
        String message = null;
        for (Throwable t = e; t != null; t = t.getCause() == t ? null : t.getCause()) {
            if (!(t instanceof CompletionException)) {
                sb.append(sb.length() == 0 ? "" : ": ").append(t.getClass().getSimpleName());
            }
            if (message == null && t.getMessage() != null && (t.getCause() == null || !t.getMessage().equals(t.getCause().toString()))) {
                message = t.getMessage();
            }
        }
        return c.redact(sb + (message == null ? "" : " (" + message + ")"));
    }

    /** The next item, or null when nothing came within the wait. */
    Item next(Duration wait) throws InterruptedException {
        return items.poll(wait.toMillis(), TimeUnit.MILLISECONDS);
    }

    synchronized void send(Object message) {
        try {
            ws.sendText(Json.MAPPER.writeValueAsString(message), true).join();
        } catch (java.io.IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    void abort() {
        ws.abort();
    }

    void close() {
        try {
            ws.sendClose(WebSocket.NORMAL_CLOSURE, "").orTimeout(2, TimeUnit.SECONDS).exceptionally(e -> null).join();
        } catch (RuntimeException e) {
            ws.abort();
        }
        ws.abort();
    }

    /** The server's answer to an ask, waiting at most wait; else what ended the stream, or ApiTimeoutException. */
    static JsonNode answer(CompletableFuture<JsonNode> reply, Duration wait) {
        try {
            return reply.get(wait.toMillis(), TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            throw e.getCause() instanceof RuntimeException r ? r : new IllegalStateException(e.getCause());
        } catch (TimeoutException e) {
            throw new PhoenixPerpsException.ApiTimeoutException("timeout", "No answer within " + wait.toMillis() + " ms.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PhoenixPerpsException.ApiConnectionException("interrupted", "The wait was interrupted.", e);
        }
    }

    static Duration backoff(int n) {
        return Duration.ofMillis((long) (ThreadLocalRandom.current().nextDouble() * Math.min(30000, 500 * Math.pow(2, n))));
    }

    static Duration restart() {
        return Duration.ofMillis((long) (ThreadLocalRandom.current().nextDouble() * 3000));
    }
}
