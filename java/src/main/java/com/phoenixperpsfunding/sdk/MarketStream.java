package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.AuthenticationException;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.StreamClosedException;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.ValidationException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * The market data stream (docs/BEHAVIOUR.md "Streams"): subscribe("ticker", List.of("BTC-USD"), Map.of("interval_ms",
 * 250)) answers once the server took it. A subscription is one channel, one market and (candles) one interval; its other
 * parameters are settings the last subscribe sets. Every subscription the server took is made again after a reconnect
 * (its history asked again: a trade's id, a candle's time tell a repeat apart), and an ask still unanswered goes again on
 * the next connection. A slow reader loses the oldest frames, never the connection. Three heartbeats (the hello's
 * heartbeat_ms) without a frame mean a dead connection; each reconnect is a line to the client's logger, and an upgrade
 * refused for good (401, 403, 404...: not 429 or 5xx) ends the stream with the server's error.
 */
public final class MarketStream implements AutoCloseable, Iterable<MarketStream.Frame> {
    /**
     * One frame: its type (the channel: ticker, book, trades, candles, stats, status; or snapshot_end), symbol and data.
     *
     * @param type the channel
     * @param symbol the market
     * @param data the data
     * @param raw the frame as it came
     */
    public record Frame(String type, String symbol, JsonNode data, JsonNode raw) {
    }

    /** A subscription the server took: a channel, a market and (candles) an interval, with the last subscribe's settings. */
    private record Sub(String channel, String symbol, Object interval, Map<String, Object> settings) {
    }

    /** An ask still waiting for its answer, and what its answer changes (null: nothing). */
    private record Pending(CompletableFuture<JsonNode> reply, Map<String, Object> message, Consumer<JsonNode> answered) {
    }

    private static final Object END = new Object();
    private final PhoenixPerpsClient c;
    private final BlockingQueue<Object> frames = new LinkedBlockingQueue<>(1024);
    private final Map<String, Sub> subs = new LinkedHashMap<>(); // channel|symbol|interval, the last subscribed last
    private final Map<Long, Pending> pending = new LinkedHashMap<>();
    private final Thread thread;
    private volatile boolean closed;
    private volatile JsonNode limits;
    private StreamSocket socket;
    private RuntimeException error;
    private long id;
    private int attempt;

    MarketStream(PhoenixPerpsClient c) {
        this.c = c;
        this.thread = new Thread(this::run, "phoenixperps-market-stream");
        this.thread.setDaemon(true);
        this.thread.start();
    }

    /** What the server's hello says a connection may do: subscriptions, symbols_per_subscribe, messages_per_s (null before it). */
    public JsonNode limits() {
        return limits;
    }

    private static String key(String channel, Object symbol, Object interval) {
        return channel + "|" + symbol + "|" + (interval == null ? "" : interval);
    }

    private static Map<String, Object> message(String op, String channel, List<String> symbols, Map<String, Object> params) {
        Map<String, Object> m = new LinkedHashMap<>(params);
        m.put("op", op);
        m.put("channel", channel);
        if (!"status".equals(channel)) {
            m.put("symbols", symbols);
        }
        return m;
    }

    /**
     * Follows symbols on a channel (candles: params' interval), its other params the settings of those subscriptions;
     * the server's reply (the symbols taken and the ones it refused).
     */
    public JsonNode subscribe(String channel, List<String> symbols, Map<String, Object> params, Duration wait) {
        Map<String, Object> p = params == null ? Map.of() : new LinkedHashMap<>(params);
        // what the server took (its reply's symbols) is made again after a reconnect, as this subscribe set it
        return ask(message("subscribe", channel, symbols == null ? List.of() : symbols, p), reply -> {
            for (JsonNode s : reply.path("symbols")) {
                String k = key(channel, s.asText(), p.get("interval"));
                subs.remove(k); // the last subscribed goes last
                subs.put(k, new Sub(channel, s.asText(), p.get("interval"), p));
            }
        }, wait);
    }

    /**
     * Ends the subscriptions of these markets on a channel (none: every one of the channel), on candles params' interval
     * or else every interval (params' other entries are not an unsubscribe's); the server's reply (symbols: the ones
     * ended; rejected: the ones not followed).
     */
    public JsonNode unsubscribe(String channel, List<String> symbols, Map<String, Object> params, Duration wait) {
        List<String> syms = symbols == null ? List.of() : symbols;
        Object interval = params == null ? null : params.get("interval");
        Map<String, Object> message = message("unsubscribe", channel, syms, interval == null ? Map.of() : Map.of("interval", interval));
        Set<String> named = new HashSet<>();
        syms.forEach(x -> named.add(x.toUpperCase(Locale.ROOT)));
        // left out of every later connection now; the server's answer ends what it ended (a subscribe answered meanwhile)
        synchronized (this) {
            drop(channel, named, interval);
        }
        return ask(message, reply -> {
            Set<String> ended = new HashSet<>();
            reply.path("symbols").forEach(x -> ended.add(x.asText()));
            drop(channel, ended, interval);
        }, wait);
    }

    // guarded by this
    private void drop(String channel, Set<String> symbols, Object interval) {
        subs.values().removeIf(s -> s.channel().equals(channel) && (symbols.isEmpty() || symbols.contains(s.symbol())) && (interval == null || interval.equals(s.interval())));
    }

    /** Your subscriptions as the server holds them: its list answer (subscriptions: channel, symbol, interval). */
    public JsonNode list(Duration wait) {
        return ask(new LinkedHashMap<>(Map.of("op", "list")), null, wait);
    }

    /** The server's pong (its time), once it answered. */
    public JsonNode ping(Duration wait) {
        return ask(new LinkedHashMap<>(Map.of("op", "ping")), null, wait);
    }

    private JsonNode ask(Map<String, Object> message, Consumer<JsonNode> answered, Duration wait) {
        CompletableFuture<JsonNode> reply = new CompletableFuture<>();
        StreamSocket s;
        synchronized (this) {
            if (error != null) {
                throw error;
            }
            if (closed) {
                throw new StreamClosedException("stream_closed", "The stream is closed.");
            }
            long n = ++id;
            message.put("id", n);
            pending.put(n, new Pending(reply, message, answered));
            s = socket;
        }
        if (s != null) {
            try {
                s.send(message);
            } catch (RuntimeException e) {
                // the connection dropped: the message goes again on the next one
            }
        }
        return StreamSocket.answer(reply, wait);
    }

    /** The next frame, waiting at most wait (null when none came). Throws what ended the stream. */
    public Frame poll(Duration wait) throws InterruptedException {
        return take(frames.poll(wait.toMillis(), TimeUnit.MILLISECONDS));
    }

    /** The next frame, waiting as long as it takes. Throws what ended the stream. */
    public Frame next() throws InterruptedException {
        return take(frames.take());
    }

    private Frame take(Object x) {
        if (x == null) {
            return null;
        }
        if (x == END) {
            frames.offer(END);
            throw new StreamClosedException("stream_closed", "The stream is closed.");
        }
        if (x instanceof RuntimeException e) {
            frames.offer(END);
            throw e;
        }
        return (Frame) x;
    }

    /** Every frame until the stream ends (an error that ended it is thrown). */
    @Override
    public Iterator<Frame> iterator() {
        return new Iterator<Frame>() {
            private Frame next;

            @Override
            public boolean hasNext() {
                if (next != null) {
                    return true;
                }
                try {
                    next = MarketStream.this.next();
                    return true;
                } catch (StreamClosedException e) {
                    return false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }

            @Override
            public Frame next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Frame f = next;
                next = null;
                return f;
            }
        };
    }

    /** Ends the stream. */
    @Override
    public void close() {
        StreamSocket s;
        synchronized (this) {
            closed = true;
            s = socket;
        }
        if (s != null) {
            s.close();
        }
        thread.interrupt();
        try {
            thread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        end(new StreamClosedException("stream_closed", "The stream is closed."));
        frames.clear();
        frames.offer(END);
    }

    private void end(RuntimeException e) {
        List<Pending> waiting;
        synchronized (this) {
            if (error == null && !(e instanceof StreamClosedException)) {
                error = e;
            }
            waiting = new ArrayList<>(pending.values());
            pending.clear();
        }
        waiting.forEach(p -> p.reply().completeExceptionally(e));
    }

    private void fail(RuntimeException e) {
        closed = true;
        end(e);
        if (!frames.offer(e)) {
            frames.clear();
            frames.offer(e);
        }
    }

    private void put(Frame f) {
        while (!frames.offer(f)) {
            frames.poll(); // a slow reader loses the oldest frame, never the connection
        }
    }

    private void run() {
        while (!closed) {
            Duration wait;
            String why;
            try {
                StreamSocket s = new StreamSocket(c, c.marketStreamUrl);
                Object[] r = read(s);
                synchronized (this) {
                    socket = null;
                }
                if (r == null) {
                    return;
                }
                wait = (Duration) r[0];
                why = (String) r[1];
            } catch (StreamSocket.Refused e) {
                if (!e.retryable()) {
                    fail(e.error); // the server's own answer (401, 403, a wrong path...), as a REST call would throw it
                    return;
                }
                wait = e.error.retryAfter() != null ? e.error.retryAfter() : StreamSocket.backoff(attempt);
                why = e.why();
            } catch (InterruptedException e) {
                return;
            } catch (RuntimeException e) {
                if (closed) {
                    return;
                }
                wait = StreamSocket.backoff(attempt);
                why = "connection failed (" + StreamSocket.why(c, e) + ")";
            }
            attempt++;
            c.log("warn", "phoenixperps: market stream: " + why + "; reconnecting in " + wait.toMillis() + " ms (attempt " + attempt + ")");
            try {
                Thread.sleep(wait.toMillis());
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /** Follows one connection: the wait before the next and why, or null to stop. */
    private Object[] read(StreamSocket s) throws InterruptedException {
        Duration heartbeat = Duration.ofSeconds(15);
        while (true) {
            StreamSocket.Item it = s.next(heartbeat.multipliedBy(3)); // three heartbeats without a frame: dead
            if (closed) {
                return null;
            }
            if (it == null || it.error() != null) {
                s.abort();
                return new Object[] {StreamSocket.backoff(attempt), it == null ? "no frame in " + heartbeat.multipliedBy(3).toMillis() + " ms" : "connection lost (" + StreamSocket.why(c, it.error()) + ")"};
            }
            if (it.closeCode() != null) {
                if (it.closeCode() == 4001) {
                    fail(new AuthenticationException("stream_credential_ended", "The key of this stream ended.", null));
                    return null;
                }
                return new Object[] {it.closeCode() == 1012 ? StreamSocket.restart() : StreamSocket.backoff(attempt), "closed with " + it.closeCode()};
            }
            JsonNode f = it.frame();
            String type = f.path("type").asText();
            switch (type) {
                case "hello" -> {
                    attempt = 0;
                    long hb = f.path("heartbeat_ms").asLong(0);
                    if (hb > 0) {
                        heartbeat = Duration.ofMillis(hb);
                    }
                    limits = f.get("limits");
                    int most = Math.max(1, f.path("limits").path("symbols_per_subscribe").asInt(50));
                    List<Map<String, Object>> again = new ArrayList<>();
                    synchronized (this) {
                        // every subscription the server took, again with its settings: one subscribe per channel and
                        // settings, at most symbols_per_subscribe markets each; then the asks still unanswered
                        Map<String, List<Sub>> groups = new LinkedHashMap<>();
                        subs.values().forEach(x -> groups.computeIfAbsent(x.channel() + "|" + new TreeMap<>(x.settings()), k -> new ArrayList<>()).add(x));
                        for (List<Sub> g : groups.values()) {
                            for (int i = 0; i < g.size(); i += most) {
                                List<String> symbols = g.subList(i, Math.min(g.size(), i + most)).stream().map(Sub::symbol).toList();
                                Map<String, Object> m = message("subscribe", g.get(0).channel(), symbols, g.get(0).settings());
                                m.put("id", ++id);
                                again.add(m);
                            }
                        }
                        pending.values().forEach(p -> again.add(p.message()));
                        socket = s;
                    }
                    again.forEach(s::send);
                }
                case "subscribed", "unsubscribed", "error", "list", "pong" -> {
                    Pending p;
                    synchronized (this) {
                        p = f.has("id") ? pending.remove(f.get("id").asLong()) : null;
                        if (p != null && p.answered() != null && !"error".equals(type)) {
                            p.answered().accept(f);
                        }
                    }
                    if (p == null) {
                        continue;
                    }
                    if ("error".equals(type)) {
                        // the ask refused: its code, and the details that say why (the field, the market)
                        String code = f.path("code").asText("stream_refused");
                        ValidationException e = new ValidationException(code, f.path("message").asText(code), null);
                        e.details = f.hasNonNull("details") ? f.get("details") : null;
                        p.reply().completeExceptionally(e);
                    } else {
                        p.reply().complete(f);
                    }
                }
                case "heartbeat" -> {
                }
                default -> put(new Frame(type, f.hasNonNull("symbol") ? f.get("symbol").asText() : null, f.get("data"), f));
            }
        }
    }
}
