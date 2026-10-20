package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.AuthenticationException;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.StreamClosedException;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException.ValidationException;
import com.phoenixperpsfunding.sdk.gen.Event;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * The account stream (docs/BEHAVIOUR.md "Streams"; read only, orders go through REST): the key on the upgrade; hello,
 * one snapshot per account, snapshot_end (ready()), then events, each once (replays are told apart by their event id),
 * each keeping its frame's seq and replayed in extra(). It reconnects by itself with ?resume=&lt;last event id&gt;,
 * waiting min(30 s, 0.5 s x 2^n) with jitter (0 to 3 s after 1012, the server restarting; a refusal's Retry-After), each
 * time a line to the client's logger. A resume the server cannot cover (its resume_gap notice) and 4008 (too far behind:
 * reconnects without resume) are gaps, told to onGap and the logger with the last event id told. 4001 (the key ended)
 * ends it with an AuthenticationException; an upgrade refused for good (401, 403, 404...: not 429 or 5xx) with the
 * server's error; an error frame (its subscribe refused: too_many_accounts, account_not_found) with a
 * ValidationException. 4013 waits longer. Three heartbeats without a frame mean a dead connection. Risk frames (with
 * risk set) go to onRisk as they come. ping() and unsubscribe(accounts) ask the server; an account unsubscribed stays
 * out of the subscribe of every later connection.
 */
public final class AccountStream implements AutoCloseable, Iterable<Event> {
    private static final int LRU = 2048;

    /** What an account stream follows. */
    public static final class Options {
        List<String> accounts;
        boolean risk;
        String resume;
        Consumer<JsonNode> onRisk;
        Consumer<String> onGap;

        /** The accounts to follow (none: every account of the key). */
        public Options accounts(List<String> v) {
            accounts = v;
            return this;
        }

        /** Live risk frames too (the server sends them only when asked). */
        public Options risk(boolean v) {
            risk = v;
            return this;
        }

        /** Start after this event id. */
        public Options resume(String v) {
            resume = v;
            return this;
        }

        /** Each risk frame as it comes ({type: risk, account_id, data}, with risk set), on the stream's thread. */
        public Options onRisk(Consumer<JsonNode> v) {
            onRisk = v;
            return this;
        }

        /**
         * A gap, as it is found: events after this id (the last one told; null: none yet) may be missing, the server
         * could not resume from it. Read GET /v1/events?after=&lt;id&gt; for them. On the stream's thread.
         */
        public Options onGap(Consumer<String> v) {
            onGap = v;
            return this;
        }
    }

    /** Options with nothing set. */
    public static Options options() {
        return new Options();
    }

    private final PhoenixPerpsClient c;
    private final Options o;
    private final BlockingQueue<Object> events = new LinkedBlockingQueue<>(1024);
    private final CompletableFuture<Void> ready = new CompletableFuture<>();
    private final Map<String, JsonNode> snapshots = new ConcurrentHashMap<>();
    private final Map<String, JsonNode> risks = new ConcurrentHashMap<>();
    private final Set<String> seen = new HashSet<>();
    private final ArrayDeque<String> order = new ArrayDeque<>();
    private final Set<String> dropped = ConcurrentHashMap.newKeySet();
    private final List<Ask> asks = new ArrayList<>();
    private final Thread thread;
    private volatile boolean closed;
    private volatile String lastEventId;
    private volatile StreamSocket socket;
    private StreamSocket live; // the connection that took the subscribe (guarded by this)
    private int attempt;
    private static final Object END = new Object();

    /** A message still waiting for its answer (the account stream's answers carry no id: the first of its type). */
    private record Ask(String reply, CompletableFuture<JsonNode> answer, Map<String, Object> message) {
    }

    AccountStream(PhoenixPerpsClient c, Options o) {
        this.c = c;
        this.o = o;
        this.lastEventId = o.resume;
        this.thread = new Thread(this::run, "phoenixperps-account-stream");
        this.thread.setDaemon(true);
        this.thread.start();
    }

    /** Done once the first snapshots are in; failed with what ended the stream before (StreamClosedException after close()). */
    public CompletableFuture<Void> ready() {
        return ready;
    }

    /** The last snapshot frame of an account (null before it came). */
    public JsonNode snapshot(String accountId) {
        return snapshots.get(accountId);
    }

    /** The last risk frame of an account (with risk set). */
    public JsonNode risk(String accountId) {
        return risks.get(accountId);
    }

    /** The id of the last event told (the resume point). */
    public String lastEventId() {
        return lastEventId;
    }

    /** The server's pong (its time), once it answered. */
    public JsonNode ping(Duration wait) {
        return ask("pong", Map.of("op", "ping"), wait);
    }

    /** Stops following these accounts, now and after every reconnect; the server's answer (the accounts it dropped). */
    public JsonNode unsubscribe(List<String> accounts, Duration wait) {
        for (String a : accounts) {
            dropped.add(a.toUpperCase(Locale.ROOT));
            snapshots.remove(a.toUpperCase(Locale.ROOT));
            risks.remove(a.toUpperCase(Locale.ROOT));
        }
        return ask("unsubscribed", Map.of("op", "unsubscribe", "accounts", accounts), wait);
    }

    // an ask still unanswered goes again on the next connection, after its subscribe
    private JsonNode ask(String reply, Map<String, Object> message, Duration wait) {
        CompletableFuture<JsonNode> answer = new CompletableFuture<>();
        StreamSocket s;
        synchronized (this) {
            if (closed) {
                throw new StreamClosedException("stream_closed", "The stream is closed.");
            }
            asks.add(new Ask(reply, answer, message));
            s = live;
        }
        if (s != null) {
            try {
                s.send(message);
            } catch (RuntimeException e) {
                // the connection dropped: the message goes again on the next one
            }
        }
        return StreamSocket.answer(answer, wait);
    }

    private void end(RuntimeException e) {
        List<Ask> waiting;
        synchronized (this) {
            waiting = new ArrayList<>(asks);
            asks.clear();
        }
        waiting.forEach(a -> a.answer().completeExceptionally(e));
    }

    /** The next event, waiting at most wait (null when none came). Throws what ended the stream. */
    public Event poll(Duration wait) throws InterruptedException {
        Object x = events.poll(wait.toMillis(), TimeUnit.MILLISECONDS);
        return take(x);
    }

    /** The next event, waiting as long as it takes. Throws what ended the stream (StreamClosedException after close()). */
    public Event next() throws InterruptedException {
        return take(events.take());
    }

    private Event take(Object x) {
        if (x == null) {
            return null;
        }
        if (x == END) {
            events.offer(END);
            throw new StreamClosedException("stream_closed", "The stream is closed.");
        }
        if (x instanceof RuntimeException e) {
            events.offer(END);
            throw e;
        }
        return (Event) x;
    }

    /** Every event until the stream ends (an error that ended it is thrown). */
    @Override
    public Iterator<Event> iterator() {
        return new Iterator<Event>() {
            private Event next;

            @Override
            public boolean hasNext() {
                if (next != null) {
                    return true;
                }
                try {
                    next = AccountStream.this.next();
                    return true;
                } catch (StreamClosedException e) {
                    return false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }

            @Override
            public Event next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Event e = next;
                next = null;
                return e;
            }
        };
    }

    /** Ends the stream. */
    @Override
    public void close() {
        closed = true;
        StreamSocket s = socket;
        if (s != null) {
            s.close();
        }
        thread.interrupt();
        try {
            thread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        StreamClosedException e = new StreamClosedException("stream_closed", "The stream is closed.");
        ready.completeExceptionally(e);
        end(e);
        events.clear();
        events.offer(END);
    }

    // a handler's failure is the caller's own: a line to the logger, never the end of the stream
    private <T> void tell(Consumer<T> handler, T value) {
        if (handler == null) {
            return;
        }
        try {
            handler.accept(value);
        } catch (RuntimeException e) {
            c.log("warn", "phoenixperps: account stream: a handler failed (" + StreamSocket.why(c, e) + ")");
        }
    }

    // a gap is never hidden: a line to the logger, and onGap with the last event id told
    private void gap(String after, String why) {
        c.log("warn", "phoenixperps: account stream: events" + (after == null ? "" : " after " + after) + " may be missing (" + why + "): read GET /v1/events" + (after == null ? "" : "?after=" + after));
        tell(o.onGap, after);
    }

    // the error comes after the events already in (the reader sees those first); ready() fails with it if still waiting
    private void fail(RuntimeException e) {
        closed = true;
        ready.completeExceptionally(e);
        end(e);
        if (!events.offer(e)) {
            events.clear();
            events.offer(e);
        }
    }

    private void run() {
        boolean resume = true;
        while (!closed) {
            String url = c.accountStreamUrl + (resume && lastEventId != null ? "?resume=" + URLEncoder.encode(lastEventId, StandardCharsets.UTF_8) : "");
            resume = true;
            Duration wait;
            String why;
            try {
                StreamSocket s = new StreamSocket(c, url);
                socket = s;
                Object[] r = read(s);
                socket = null;
                synchronized (this) {
                    live = null;
                }
                if (r == null) {
                    return;
                }
                wait = (Duration) r[0];
                resume = (Boolean) r[1];
                why = (String) r[2];
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
            c.log("warn", "phoenixperps: account stream: " + why + "; reconnecting in " + wait.toMillis() + " ms (attempt " + attempt + ")");
            try {
                Thread.sleep(wait.toMillis());
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /** An error frame as the error ending the stream: its code, and the account and details it gives. */
    private static ValidationException refusal(JsonNode f) {
        String code = f.path("code").asText("stream_refused");
        String account = f.hasNonNull("account_id") ? f.get("account_id").asText() : null;
        ValidationException e = new ValidationException(code, f.path("message").asText(account == null ? code : code + ": " + account), null);
        if (f.path("details").isObject() || account != null) {
            ObjectNode d = f.path("details").isObject() ? f.get("details").deepCopy() : Json.MAPPER.createObjectNode();
            if (account != null) {
                d.put("account_id", account);
            }
            e.details = d;
        }
        return e;
    }

    /** The subscribe of a connection: the accounts named (else, once some were dropped, the ones its hello names), less the dropped ones. */
    private Map<String, Object> subscribe(JsonNode hello) {
        Map<String, Object> msg = new LinkedHashMap<>();
        msg.put("op", "subscribe");
        List<String> accounts = o.accounts != null && !o.accounts.isEmpty() ? o.accounts : null;
        if (accounts == null && !dropped.isEmpty()) {
            accounts = new ArrayList<>();
            for (JsonNode a : hello.path("accounts")) {
                accounts.add(a.asText());
            }
        }
        if (accounts != null) {
            msg.put("accounts", accounts.stream().filter(a -> !dropped.contains(a.toUpperCase(Locale.ROOT))).toList());
        }
        if (o.risk) {
            msg.put("risk", true);
        }
        return msg;
    }

    /** Follows one connection: the wait before the next, whether to resume and why, or null to stop. */
    private Object[] read(StreamSocket s) throws InterruptedException {
        Duration heartbeat = Duration.ofSeconds(15);
        while (true) {
            StreamSocket.Item it = s.next(heartbeat.multipliedBy(3));
            if (closed) {
                return null;
            }
            if (it == null || it.error() != null) {
                s.abort(); // dead or broken
                return new Object[] {StreamSocket.backoff(attempt), true, it == null ? "no frame in " + heartbeat.multipliedBy(3).toMillis() + " ms" : "connection lost (" + StreamSocket.why(c, it.error()) + ")"};
            }
            if (it.closeCode() != null) {
                int code = it.closeCode();
                String why = "closed with " + code;
                if (code == 4001) {
                    fail(new AuthenticationException("stream_credential_ended", "The key or session of this stream ended.", null));
                    return null;
                }
                if (code == 4008) {
                    gap(lastEventId, "closed with 4008, too far behind");
                    return new Object[] {StreamSocket.backoff(attempt), false, why};
                }
                if (code == 4013) {
                    return new Object[] {Duration.ofSeconds(30).plus(StreamSocket.backoff(attempt)), true, why};
                }
                if (code == 1012) {
                    return new Object[] {StreamSocket.restart(), true, why};
                }
                return new Object[] {StreamSocket.backoff(attempt), true, why};
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
                    synchronized (this) {
                        // the subscribe, then every ask still unanswered: an unsubscribe never goes before its subscribe
                        s.send(subscribe(f));
                        asks.forEach(a -> s.send(a.message()));
                        live = s;
                    }
                }
                case "snapshot" -> snapshots.put(f.path("account_id").asText(), f);
                case "snapshot_end" -> ready.complete(null);
                case "risk" -> {
                    risks.put(f.path("account_id").asText(), f);
                    tell(o.onRisk, f);
                }
                case "notice" -> {
                    if ("resume_gap".equals(f.path("code").asText())) {
                        gap(f.hasNonNull("after") ? f.get("after").asText() : lastEventId, "resume_gap: the server cannot resume from it");
                    }
                }
                case "pong", "unsubscribed" -> {
                    Ask a;
                    synchronized (this) {
                        a = asks.stream().filter(x -> x.reply().equals(type)).findFirst().orElse(null);
                        asks.remove(a);
                    }
                    if (a != null) {
                        a.answer().complete(f);
                    }
                }
                case "error" -> {
                    // the subscribe refused: the same one goes again on every reconnect, so it ends the stream as 4001 does
                    fail(refusal(f));
                    s.close();
                    return null;
                }
                case "event" -> {
                    Event e;
                    try {
                        e = Json.MAPPER.treeToValue(f.get("event"), Event.class);
                    } catch (java.io.IOException ex) {
                        continue;
                    }
                    if (!seen.add(e.id())) {
                        continue;
                    }
                    // the frame's place on its connection (gapless) and whether a resume replayed it, kept on the event
                    if (f.has("seq")) {
                        e.extra().put("seq", f.get("seq").asLong());
                    }
                    e.extra().put("replayed", f.path("replayed").asBoolean(false));
                    order.add(e.id());
                    if (order.size() > LRU) {
                        seen.remove(order.removeFirst());
                    }
                    lastEventId = e.id();
                    events.put(e);
                }
                default -> {
                }
            }
        }
    }
}
