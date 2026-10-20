'use strict';
const { EventEmitter } = require('node:events');
const E = require('./errors');

/*
 * The two read-only streams (docs/BEHAVIOUR.md "Streams"; orders always go through REST).
 *
 * AccountStream: wss://<api host>/v1/stream with the key on the upgrade. hello, then one snapshot per account and
 * snapshot_end ('ready'), then events (each once: replays are told apart by their event id; each keeps the frame's seq
 * and replayed). It reconnects by itself with ?resume=<last event id> (the events missed come before the new snapshot),
 * waiting min(30 s, 0.5 s x 2^n) with jitter; after 1012 (the server restarts) 0 to 3 s. A resume the server cannot
 * cover (notice resume_gap) and 4008 (too far behind: reconnects without resume) are a gap, told as 'gap' { code,
 * after: the last event id told, to read GET /v1/events?after= from }. Close 4001 (the key ended) stops it with an
 * AuthenticationError, an error frame (its subscribe refused: too_many_accounts, account_not_found, invalid_json) with a
 * ValidationError; 4013 (too many streams) waits longer. ping(); unsubscribe(accounts) keeps them out after a reconnect.
 * Events: hello, snapshot (its `error` set when the account could not be read now), ready, event, <event type>, risk
 * (only with risk: true), gap, notice, reconnect, error, close. Also an async iterable over its events.
 *
 * MarketStream: wss://stream.<domain>/v1/market-data. subscribe(channel, symbols, params) answers once the server
 * took it (subscribed: the symbols and the ones it refused). A subscription is one channel, one market and (candles)
 * one interval, its other params the last subscribe's; every one the server took is made again after a reconnect, its
 * history asked again (trades and candles already told can come again: a trade's id, a candle's time tell them apart).
 * unsubscribe(channel, symbols, { interval }) ends them (without an interval, every interval). list(), ping(); limits:
 * the hello's. Events: <channel> (ticker, book, trades, candles, stats, status) with { symbol, data }, snapshot_end,
 * reconnect, error, close.
 *
 * Both: three heartbeats (heartbeat_ms) without a frame mean a dead connection. An upgrade refused with 429 or 5xx, or a
 * network failure, reconnects after its Retry-After (else the backoff); any other refusal ends the stream with the API's
 * own error. Each reconnect is told ('reconnect' { in_ms, resume, cause }) and logged once with its cause; an 'error'
 * listener is told, and optional.
 */
const LRU = 2048;
const backoff = (n) => Math.random() * Math.min(30000, 500 * 2 ** n);
const closed = () => new E.StreamClosedError({ code: 'stream_closed', message: 'The stream is closed.' });

function socketFactory(opts) {
  return opts.WebSocket || require('ws');
}

/** What an upgrade carries: the key (no Authorization without one) and the User-Agent. */
const upgradeHeaders = (c) => ({ ...(c._key ? { authorization: `Bearer ${c._key}` } : {}), 'user-agent': c.userAgent });

/** What both streams do alike: frames read, the silence watchdog, a refused upgrade, a reconnect told with its cause. */
class Stream extends EventEmitter {
  _read(raw) {
    let f;
    try {
      f = JSON.parse(String(raw));
    } catch {
      return; // not a frame: nothing to tell, nothing thrown out of the socket's callback
    }
    this._frame(f);
  }

  _alive(ms) {
    clearTimeout(this._watch);
    this._watch = setTimeout(() => {
      this._cause = 'silence';
      if (this.ws) this.ws.terminate();
    }, ms * 3);
    this._watch.unref?.();
  }

  _refused(res) {
    let body = '';
    res.on('data', (d) => {
      body += d;
    });
    res.on('end', () => {
      if (this._closed) return;
      const err = E.fromText({ status: res.statusCode, headers: new Headers(res.headers) }, body, null);
      if (res.statusCode !== 429 && res.statusCode < 500) return this._fail(err);
      // tried again: told on 'reconnect' with its error, never as an 'error' (which ends a stream: events.once(stream,
      // 'ready') would give up on a stream that recovers by itself)
      return this._again(err.retryAfter, `http ${res.statusCode}`, err);
    });
  }

  _again(wait, cause, error = null) {
    if (this._closed) return;
    const ms = wait ?? backoff(this._attempt);
    this._attempt += 1;
    const r = { in_ms: Math.round(ms), resume: Boolean(this._resume), cause };
    this.emit('reconnect', error ? { ...r, error } : r);
    this._client._log('warn', `phoenixperps: ${this.constructor.name} reconnects`, r);
    this._retry = setTimeout(() => this._connect(), ms);
  }
}

class AccountStream extends Stream {
  constructor(client, { accounts = null, risk = false, resume = null, WebSocket } = {}) {
    super();
    Object.defineProperty(this, '_client', { value: client, enumerable: false });
    this._WS = socketFactory({ WebSocket });
    this.accounts = accounts;
    this.risk = risk;
    this.lastEventId = resume;
    this.ws = null;
    this._seen = new Set();
    this._order = [];
    this._attempt = 0;
    this._closed = false;
    this._resume = true;
    this._open = false;
    this._dropped = new Set(); // unsubscribed while following every account
    this._asks = []; // pings and unsubscribes waiting for their answer, in order
    this._queue = [];
    this._waiters = [];
    this._watch = null;
  }

  start() {
    this._closed = false;
    this._connect();
    return this;
  }

  close() {
    this._closed = true;
    clearTimeout(this._watch);
    clearTimeout(this._retry);
    if (this.ws) this.ws.close(1000);
    for (const a of this._asks.splice(0)) a.reject(closed());
    this._drain(null);
  }

  /** The server's pong (asked again on the next connection when this one ends first). */
  ping() {
    return this._ask({ op: 'ping' }, 'pong');
  }

  /** Stops following `accounts`: they stay out after a reconnect (the subscribe sent again names what is left). */
  unsubscribe(accounts) {
    const gone = new Set(accounts.map((a) => String(a).toUpperCase()));
    if (this.accounts) this.accounts = this.accounts.filter((a) => !gone.has(String(a).toUpperCase()));
    else for (const a of gone) this._dropped.add(a);
    return this._ask({ op: 'unsubscribe', accounts }, 'unsubscribed');
  }

  _ask(msg, answer) {
    if (this._closed) return Promise.reject(closed());
    return new Promise((resolve, reject) => {
      this._asks.push({ msg, answer, resolve, reject });
      if (this._open) this.ws.send(JSON.stringify(msg));
    });
  }

  _connect() {
    const c = this._client;
    // after 4008 without resume, until a connection is made
    const resume = this._resume && this.lastEventId;
    const url = `${c.streamUrls.account}${resume ? `?resume=${encodeURIComponent(resume)}` : ''}`;
    const ws = new this._WS(url, { headers: upgradeHeaders(c) });
    this.ws = ws;
    this._cause = null;
    ws.on('unexpected-response', (req, res) => this._refused(res));
    ws.on('message', (raw) => this._read(raw));
    ws.on('close', (code) => this._closedBy(code));
    // a network failure is a reconnect's cause (the close that follows tries again), never an 'error': that ends a stream
    ws.on('error', (err) => {
      this._cause = `network: ${String(err.message || err).slice(0, 200)}`;
    });
  }

  _frame(f) {
    if (this._closed) return; // a frame still in flight after the stream ended
    if (this._hb) this._alive(this._hb);
    if (f.type === 'hello') {
      this._attempt = 0;
      this._open = true;
      this._resume = true;
      this._hb = f.heartbeat_ms || 15000;
      this._alive(this._hb);
      this.emit('hello', f);
      // the accounts asked less those unsubscribed (following every account: the hello's, less those)
      const accounts = this.accounts ?? (this._dropped.size ? (f.accounts || []).filter((a) => !this._dropped.has(String(a).toUpperCase())) : null);
      this.ws.send(JSON.stringify({ op: 'subscribe', ...(accounts ? { accounts } : {}), ...(this.risk ? { risk: true } : {}) }));
      for (const a of this._asks) this.ws.send(JSON.stringify(a.msg));
    } else if (f.type === 'snapshot') this.emit('snapshot', f);
    else if (f.type === 'snapshot_end') this.emit('ready');
    else if (f.type === 'event') {
      const e = f.event;
      if (this._seen.has(e.id)) return;
      this._seen.add(e.id);
      this._order.push(e.id);
      if (this._order.length > LRU) this._seen.delete(this._order.shift());
      // the frame's own: its place in this connection's events, and whether a resume replayed it
      Object.assign(e, { seq: f.seq ?? null, replayed: f.replayed === true });
      this.lastEventId = e.id;
      this.emit('event', e);
      this.emit(e.type, e);
      this._push(e);
    } else if (f.type === 'risk') this.emit('risk', f);
    else if (f.type === 'notice' && f.code === 'resume_gap') this._gap('resume_gap');
    else if (f.type === 'notice') this.emit('notice', f);
    else if (f.type === 'pong' || f.type === 'unsubscribed') {
      const i = this._asks.findIndex((a) => a.answer === f.type);
      if (i >= 0) this._asks.splice(i, 1)[0].resolve(f);
    } else if (f.type === 'error') {
      // the subscribe refused (too_many_accounts, account_not_found, invalid_json): it is sent again as it is on every
      // reconnect, so the refusal ends the stream, as 4001 does
      const details = { ...(f.details || {}), ...(f.account_id ? { account_id: f.account_id } : {}) };
      this._fail(new E.ValidationError({ code: f.code, message: f.message || f.code, details, accountId: f.account_id ?? null }));
      this.ws.close(1000);
    }
  }

  /** Events the stream could not tell: told, with the last event id told (GET /v1/events?after= reads them). */
  _gap(code) {
    const gap = { code, after: this.lastEventId };
    this.emit('gap', gap);
    this._client._log('warn', 'phoenixperps: the account stream missed events', gap);
  }

  _closedBy(code) {
    clearTimeout(this._watch);
    this._open = false;
    this.emit('close', code);
    if (this._closed) return;
    const cause = this._cause || `close ${code}`;
    if (code === 4001) return this._fail(new E.AuthenticationError({ code: 'stream_credential_ended', message: 'The key or session of this stream ended.' }));
    if (code === 4008) {
      this._resume = false;
      this._gap('too_far_behind');
    }
    if (code === 4013) return this._again(30000 + backoff(this._attempt), cause);
    if (code === 1012) return this._again(Math.random() * 3000, cause);
    return this._again(null, cause);
  }

  _fail(err) {
    this._closed = true;
    clearTimeout(this._watch);
    this._error = err; // told to the next reader if none is waiting now
    if (this.listenerCount('error')) this.emit('error', err);
    if (this._waiters.length) this._error = null;
    for (const a of this._asks.splice(0)) a.reject(err);
    this._drain(err);
  }

  _push(e) {
    const w = this._waiters.shift();
    if (w) w.resolve({ value: e, done: false });
    else this._queue.push(e);
  }
  _drain(err) {
    for (const w of this._waiters.splice(0)) (err ? w.reject(err) : w.resolve({ value: undefined, done: true }));
  }
  [Symbol.asyncIterator]() {
    if (!this.ws && !this._closed) this.start();
    return {
      next: () => {
        if (this._queue.length) return Promise.resolve({ value: this._queue.shift(), done: false });
        if (this._error) {
          const err = this._error;
          this._error = null;
          return Promise.reject(err);
        }
        if (this._closed) return Promise.resolve({ value: undefined, done: true });
        return new Promise((resolve, reject) => this._waiters.push({ resolve, reject }));
      },
      return: () => {
        this.close();
        return Promise.resolve({ value: undefined, done: true });
      },
    };
  }
}

class MarketStream extends Stream {
  constructor(client, { WebSocket } = {}) {
    super();
    Object.defineProperty(this, '_client', { value: client, enumerable: false });
    this._WS = socketFactory({ WebSocket });
    this._subs = new Map(); // "<channel>|<symbol>|<interval>" -> { channel, symbol, interval, params, live }
    this._pending = new Map(); // id -> { resolve, reject, msg }
    this._id = 0;
    this._attempt = 0;
    this._closed = false;
    this.ws = null;
    this.limits = null;
  }

  start() {
    this._closed = false;
    this._connect();
    return this;
  }

  close() {
    this._closed = true;
    clearTimeout(this._retry);
    clearTimeout(this._watch);
    for (const p of this._pending.values()) p.reject(closed());
    this._pending.clear();
    if (this.ws) this.ws.close(1000);
  }

  _connect() {
    const c = this._client;
    const ws = new this._WS(c.streamUrls.market, { headers: upgradeHeaders(c) });
    this.ws = ws;
    this._open = false;
    this._cause = null;
    ws.on('unexpected-response', (req, res) => this._refused(res));
    ws.on('message', (raw) => this._read(raw));
    ws.on('close', (code) => {
      clearTimeout(this._watch);
      this._open = false;
      this.emit('close', code);
      if (this._closed) return;
      if (code === 4001) return this._fail(new E.AuthenticationError({ code: 'stream_credential_ended', message: 'The key of this stream ended.' }));
      this._again(code === 1012 ? Math.random() * 3000 : null, this._cause || `close ${code}`);
    });
    ws.on('error', (err) => {
      this._cause = `network: ${String(err.message || err).slice(0, 200)}`;
    });
  }

  _send(msg) {
    if (this._open) this.ws.send(JSON.stringify(msg));
  }

  _frame(f) {
    if (this._closed) return;
    if (this._hb) this._alive(this._hb);
    if (f.type === 'hello') {
      this._open = true;
      this._attempt = 0;
      this.limits = f.limits || null;
      this._hb = f.heartbeat_ms || 15000;
      this._alive(this._hb);
      this.emit('hello', f);
      // every subscription the server took, again, by channel and params (at most symbols_per_subscribe a message);
      // then the asks still waiting for their answer
      const per = (this.limits && this.limits.symbols_per_subscribe) || 50;
      const groups = new Map();
      for (const s of this._subs.values()) {
        if (!s.live) continue;
        const k = `${s.channel}|${JSON.stringify(s.params)}`;
        if (!groups.has(k)) groups.set(k, { channel: s.channel, params: s.params, symbols: [] });
        groups.get(k).symbols.push(s.symbol);
      }
      for (const g of groups.values()) {
        for (let i = 0; i < g.symbols.length; i += per) this._send({ op: 'subscribe', id: ++this._id, channel: g.channel, ...(g.channel === 'status' ? {} : { symbols: g.symbols.slice(i, i + per) }), ...g.params });
      }
      for (const p of this._pending.values()) this._send(p.msg);
      return;
    }
    if (f.type === 'subscribed' || f.type === 'unsubscribed' || f.type === 'error' || f.type === 'list' || f.type === 'pong') {
      const p = this._pending.get(f.id);
      const err = f.type === 'error' ? new E.ValidationError({ code: f.code || 'stream_refused', message: f.message || f.code, details: f.details ?? null }) : null;
      if (p) {
        this._pending.delete(f.id);
        if (err) p.reject(err);
        else p.resolve(f);
      }
      if (err && !p && this.listenerCount('error')) this.emit('error', err);
      return;
    }
    if (f.type === 'heartbeat') return;
    this.emit(f.type, f);
  }

  // an ask waits for its answer across reconnects: it is sent again on each new connection until answered
  _ask(msg) {
    if (this._closed) return Promise.reject(closed());
    if (!this.ws) this.start();
    const id = ++this._id;
    return new Promise((resolve, reject) => {
      const m = { ...msg, id };
      this._pending.set(id, { resolve, reject, msg: m });
      this._send(m);
    });
  }

  _fail(err) {
    this._closed = true;
    clearTimeout(this._watch);
    for (const p of this._pending.values()) p.reject(err);
    this._pending.clear();
    if (this.listenerCount('error')) this.emit('error', err);
  }

  /** Follows `symbols` on a channel (ticker, book, trades, candles, stats, status); params: the settings it takes. */
  subscribe(channel, symbols = [], params = {}) {
    const interval = channel === 'candles' ? params.interval ?? null : null;
    const names = channel === 'status' ? ['*'] : symbols.map((x) => String(x).toUpperCase());
    const key = (name) => `${channel}|${name}|${interval ?? ''}`;
    for (const name of names) this._subs.set(key(name), { ...(this._subs.get(key(name)) || { channel, symbol: name, interval, live: false }), params });
    // what the server took is made again after a reconnect; what it refused is forgotten
    const settle = (took) => {
      for (const name of names) {
        const s = this._subs.get(key(name));
        if (s && took.has(name)) s.live = true;
        else if (s && !s.live) this._subs.delete(key(name));
      }
    };
    return this._ask({ op: 'subscribe', channel, ...(channel === 'status' ? {} : { symbols }), ...params }).then((reply) => {
      settle(new Set((reply.symbols || names).map((x) => String(x).toUpperCase())));
      return reply;
    }, (err) => {
      settle(new Set());
      throw err;
    });
  }

  /** Ends `symbols` on a channel (none named: every one); on candles params.interval, else every interval. */
  unsubscribe(channel, symbols = [], params = {}) {
    const interval = params.interval ?? null;
    const names = new Set(symbols.map((x) => String(x).toUpperCase()));
    for (const [k, s] of this._subs) {
      if (s.channel === channel && (!names.size || names.has(s.symbol)) && (interval === null || s.interval === interval)) this._subs.delete(k);
    }
    return this._ask({ op: 'unsubscribe', channel, symbols, ...(interval ? { interval } : {}) });
  }

  /** The subscriptions the server holds for this connection: { subscriptions: [{ channel, symbol, interval? }] }. */
  list() {
    return this._ask({ op: 'list' });
  }

  /** The server's pong. */
  ping() {
    return this._ask({ op: 'ping' });
  }
}

module.exports = { AccountStream, MarketStream };
