'use strict';
const crypto = require('node:crypto');
const os = require('node:os');
const util = require('node:util');
const E = require('./errors');
const { SERVERS, STREAMS, SPEC_VERSION } = require('./gen/meta');
const { OPS } = require('./gen/resources');

const SDK_VERSION = require('../package.json').version;

/*
 * The transport (docs/BEHAVIOUR.md): one place every call goes through.
 *   config    apiKey (PHOENIXPERPS_API_KEY; none: the public calls work, no Authorization is sent), environment (else
 *             the key's prefix, else PHOENIXPERPS_ENVIRONMENT; in any case; a mismatch fails here, before any request),
 *             baseUrl (PHOENIXPERPS_BASE_URL, else the spec's host; a trailing /v1 dropped), timeout 30 s per
 *             attempt, maxRetries 2, appInfo (added to the User-Agent), logger, fetch (injectable)
 *   the key   never printed: not enumerable, masked in inspect / toJSON / errors (ppk_test_****abcd)
 *   retries   only a GET, a call with an Idempotency-Key or one the spec marks retry-safe; on a network failure, a
 *             timeout (the body's too), an answer the API marks retryable (an answer without its envelope: 429, 500,
 *             502, 503, 504); after Retry-After when given, else full jitter over min(8 s, 0.5 s x 2^n);
 *             a Retry-After over 60 s is not slept: RateLimitError with retryAfter
 *   keys      one Idempotency-Key per logical call (UUID v4 unless given), the same on every attempt, on the result
 *   decimals  as given: strings as they are, numbers in their decimal form (never an exponent)
 *   limits    X-RateLimit-Limit / Remaining / Reset kept per budget (the operation's scope, its x-pp-scope; operations
 *             without one share a single budget; client.rateLimit: the last seen); with nothing left and the reset
 *             under 60 s away, the budget's next call waits for it instead of drawing a 429
 *   answers   a 304 (the caller's If-None-Match matched) answers null, a list too; opts.onMeta(meta) is told every
 *             answer's status, headers, request id and Idempotency-Key
 *   paging    a list answers a Page (every field of the list kept); the call itself is also an async iterable over every
 *             item (maxItems caps it), each page asked with the call's options
 *   raw       client.request(method, path, query, body, opts): any route, the same transport, the answer as it came
 */
const PREFIX = /^(ppk|ppo)_(live|test)_/;
const RETRY_AFTER_MAX_MS = 60000;
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));
const mask = (key) => (key ? `${String(key).slice(0, String(key).indexOf('_', 4) + 1)}****${String(key).slice(-4)}` : null);
/** A number as JavaScript writes it, its exponent spelled out (1e-7: 0.0000001), its digits unchanged. */
function plain(n) {
  const m = /^(-?)(\d+)(?:\.(\d+))?e([+-]\d+)$/.exec(String(n));
  if (!m) return String(n);
  const digits = `${m[2]}${m[3] || ''}`;
  const point = m[2].length + Number(m[4]); // where the decimal point falls in the digits
  if (point <= 0) return `${m[1]}0.${'0'.repeat(-point)}${digits}`;
  if (point >= digits.length) return `${m[1]}${digits}${'0'.repeat(point - digits.length)}`;
  return `${m[1]}${digits.slice(0, point)}.${digits.slice(point)}`;
}
// a body's numbers go out in their decimal form, never an exponent; a BigInt as its digits
const decimals = (k, v) => {
  if (typeof v === 'bigint') return v.toString();
  if (typeof v === 'number' && /e/.test(String(v))) return typeof JSON.rawJSON === 'function' ? JSON.rawJSON(plain(v)) : plain(v);
  return v;
};
// the spec's routes, for the raw call: a known one keeps its retry-safety, Idempotency-Key and budget
const escape = (s) => s.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
const ROUTES = Object.values(OPS).map((op) => ({ op, re: new RegExp(`^${op.path.split(/\{[^}]+\}/).map(escape).join('[^/]+')}$`) }));

class Page {
  constructor(client, op, call, body, meta, opts) {
    this.data = body.data || [];
    this.hasMore = Boolean(body.has_more);
    this.nextCursor = body.next_cursor ?? null;
    this.asOf = body.as_of ?? null;
    Object.defineProperty(this, '_next', { value: { client, op, call, opts }, enumerable: false });
    Object.defineProperty(this, 'meta', { value: meta, enumerable: false });
    // the list's other fields, named as these are (history_complete: historyComplete): an unknown field is kept
    for (const [k, v] of Object.entries(body)) {
      const name = k.replace(/_([a-z])/g, (m, c) => c.toUpperCase());
      if (!(name in this)) this[name] = v;
    }
  }
  /** The next page, or null on the last (asked with the first page's options: headers, timeout, signal...). */
  async next() {
    if (!this.hasMore || !this.nextCursor) return null;
    const { client, op, call, opts } = this._next;
    return client._list(op, { ...call, query: { ...(call.query || {}), [op.paging.param]: this.nextCursor } }, opts);
  }
}

/** A list call: awaited, its first Page; iterated (for await), every item across pages. */
class PagePromise {
  constructor(first, maxItems) {
    this._load = first;
    this._max = maxItems;
    this._p = null;
  }
  // the first page is asked for once, however often the call is awaited or iterated
  _first() {
    this._p ??= this._load();
    return this._p;
  }
  then(a, b) {
    return this._first().then(a, b);
  }
  catch(b) {
    return this._first().catch(b);
  }
  finally(f) {
    return this._first().finally(f);
  }
  async *[Symbol.asyncIterator]() {
    let page = await this._first();
    let n = 0;
    while (page) {
      for (const item of page.data) {
        if (this._max !== undefined && n >= this._max) return;
        n += 1;
        yield item;
      }
      // the cap reached at a page's end: no further page is asked
      if (this._max !== undefined && n >= this._max) return;
      page = await page.next();
    }
  }
}

class Core {
  constructor(opts = {}) {
    const env = (typeof process !== 'undefined' && process.env) || {};
    // no key: a client of the public calls (markets, status); any other answers the API's missing_api_key
    const apiKey = opts.apiKey ?? env.PHOENIXPERPS_API_KEY ?? null;
    const m = apiKey ? PREFIX.exec(apiKey) : null;
    const inferred = m ? (m[2] === 'live' ? 'live' : 'sandbox') : null;
    const environment = String(opts.environment ?? inferred ?? env.PHOENIXPERPS_ENVIRONMENT ?? 'sandbox').toLowerCase();
    if (environment !== 'live' && environment !== 'sandbox') throw new E.ValidationError({ code: 'invalid_environment', message: 'environment is live or sandbox.' });
    if (inferred && inferred !== environment) throw new E.AuthenticationError({ code: 'wrong_environment', message: `This key is a ${inferred} key, not a ${environment} one.`, details: { key_environment: inferred } });
    Object.defineProperty(this, '_key', { value: apiKey || null, enumerable: false });
    this.environment = environment;
    // the REST host: the REST address as the docs write it (.../v1) is taken as its host
    this.baseUrl = String(opts.baseUrl ?? env.PHOENIXPERPS_BASE_URL ?? SERVERS[environment]).replace(/\/+$/, '').replace(/\/v1$/, '');
    this.timeout = opts.timeout ?? 30000;
    this.maxRetries = opts.maxRetries ?? 2;
    this.appInfo = opts.appInfo ?? null;
    this.rateLimit = null;
    Object.defineProperty(this, '_budgets', { value: new Map(), enumerable: false }); // x-pp-scope -> the last RateLimit
    Object.defineProperty(this, '_fetch', { value: opts.fetch ?? globalThis.fetch, enumerable: false });
    Object.defineProperty(this, '_logger', { value: opts.logger ?? null, enumerable: false });
    Object.defineProperty(this, '_warned', { value: new Set(), enumerable: false });
    Object.defineProperty(this, 'streamUrls', { value: { account: opts.accountStreamUrl ?? STREAMS.account[environment], market: opts.marketStreamUrl ?? STREAMS.market }, enumerable: false });
  }

  get maskedKey() {
    return mask(this._key);
  }
  toJSON() {
    return { environment: this.environment, baseUrl: this.baseUrl, apiKey: this.maskedKey };
  }
  [util.inspect.custom]() {
    return `PhoenixPerps { environment: '${this.environment}', baseUrl: '${this.baseUrl}', apiKey: ${this._key ? `'${this.maskedKey}'` : 'null'} }`;
  }

  get userAgent() {
    const runtime = typeof process !== 'undefined' ? `node ${process.versions.node}` : 'unknown';
    return `phoenixperps-typescript/${SDK_VERSION} (${runtime}; ${os.platform()}) spec/${SPEC_VERSION}${this.appInfo ? ` ${this.appInfo}` : ''}`;
  }

  _log(level, msg, data) {
    if (this._logger && typeof this._logger[level] === 'function') this._logger[level](msg, data);
  }

  /** Waits out the budget's exhausted rate-limit window when it ends soon (its last answer said none were left). */
  async _preWait(op) {
    const r = this._budgets.get(op.scope ?? null);
    if (!r || r.remaining > 0 || r.resetAt === null) return;
    const wait = r.resetAt - Date.now();
    if (wait > 0 && wait < 60000) await sleep(wait);
  }

  _remember(headers, op) {
    const limit = headers.get('x-ratelimit-limit');
    if (limit === null) return;
    const reset = Number(headers.get('x-ratelimit-reset'));
    // Reset as epoch seconds (above a year in seconds) or as seconds from now
    const resetAt = Number.isFinite(reset) ? (reset > 31536000 ? reset * 1000 : Date.now() + reset * 1000) : null;
    this.rateLimit = { limit: Number(limit), remaining: Number(headers.get('x-ratelimit-remaining')), resetAt };
    // per budget: an operation's x-pp-scope (read, trade, public); one budget for an operation without it
    this._budgets.set(op.scope ?? null, this.rateLimit);
    const dep = headers.get('deprecation');
    if (dep && !this._warned.has(dep)) {
      this._warned.add(dep);
      this._log('warn', 'phoenixperps: this API version is deprecated', { deprecation: dep, sunset: headers.get('sunset') });
    }
  }

  _url(op, call) {
    const path = op.path.replace(/\{([^}]+)\}/g, (m, name) => {
      const v = call.path && call.path[name];
      if (v === undefined || v === null || v === '') throw new E.ValidationError({ code: 'invalid_request', message: `${name} is required.` });
      return encodeURIComponent(String(v));
    });
    const q = new URLSearchParams();
    for (const [k, v] of Object.entries(call.query || {})) if (v !== undefined && v !== null) q.set(k, String(v));
    const qs = q.toString();
    return `${this.baseUrl}${path}${qs ? `?${qs}` : ''}`;
  }

  /** One logical call: its attempts, its result { data, status, headers, requestId, idempotencyKey }. */
  async _send(op, call = {}, opts = {}) {
    const key = op.idempotent ? (opts.idempotencyKey ?? crypto.randomUUID()) : opts.idempotencyKey ?? null;
    const retryable = op.retrySafe || op.idempotent || key !== null; // sent with an Idempotency-Key: done once however often
    const max = opts.maxRetries ?? this.maxRetries;
    const url = this._url(op, call); // a missing path parameter fails here, before any request
    const body = call.body === undefined ? undefined : JSON.stringify(call.body, decimals);
    for (let attempt = 0; ; attempt += 1) {
      await this._preWait(op);
      let res = null;
      let text = null;
      let failure = null;
      const ctrl = AbortSignal.timeout(opts.timeout ?? this.timeout);
      const signal = opts.signal ? AbortSignal.any([ctrl, opts.signal]) : ctrl;
      try {
        res = await this._fetch(url, {
          method: op.http,
          headers: {
            ...(this._key ? { authorization: `Bearer ${this._key}` } : {}),
            accept: 'application/json',
            'user-agent': this.userAgent,
            ...(body !== undefined ? { 'content-type': 'application/json' } : {}),
            ...(key ? { 'idempotency-key': key } : {}),
            ...(opts.headers || {}),
          },
          body,
          signal,
        });
        // a success's body comes within the attempt too: cut or past the timeout, the attempt failed
        if (res.ok || res.status === 304) text = await res.text();
      } catch (err) {
        if (opts.signal && opts.signal.aborted) throw err;
        res = null;
        failure = err && err.name === 'TimeoutError'
          ? new E.TimeoutError({ code: 'timeout', message: `No answer within ${opts.timeout ?? this.timeout} ms.`, retryable: true, idempotencyKey: key })
          : new E.ConnectionError({ code: 'connection_failed', message: String((err && err.message) || err).slice(0, 200), retryable: true, idempotencyKey: key });
      }
      if (res) this._remember(res.headers, op);
      // a 304 (the caller's If-None-Match matched) is no error: nothing changed, nothing to answer
      if (res && (res.ok || res.status === 304)) {
        let data;
        try {
          // nothing at all is no JSON either (JSON.parse('') throws), but for a 304 or a 204, which carry no body
          data = !text && (res.status === 304 || res.status === 204) ? null : JSON.parse(text);
        } catch {
          throw E.fromText(res, text, key); // not JSON (a proxy's page, nothing): an error, http_<status>
        }
        const meta = { status: res.status, headers: res.headers, requestId: res.headers.get('x-request-id'), idempotencyKey: key };
        if (opts.onMeta) opts.onMeta(meta);
        return { data, ...meta };
      }
      const err = failure || (await E.fromResponse(res, key));
      // tried again only when safe to send twice, and only after what the API marks retryable
      if (!retryable || attempt >= max || !err.retryable) throw err;
      if (err.retryAfter !== null && err.retryAfter > RETRY_AFTER_MAX_MS) throw err;
      const wait = err.retryAfter ?? Math.random() * Math.min(8000, 500 * 2 ** attempt);
      this._log('debug', 'phoenixperps: retrying', { operation: op.id, attempt: attempt + 1, wait_ms: Math.round(wait), status: res ? res.status : null });
      await sleep(wait);
    }
  }

  /**
   * Any route through the same transport (the key, User-Agent, retries, errors): the answer as it came, { status,
   * headers, body, requestId, idempotencyKey } (body: the JSON, or null). A route of the spec keeps its retry-safety,
   * its Idempotency-Key and its budget; any other is tried again only as a GET or with opts.idempotencyKey.
   */
  async request(method, path, query, body, opts = {}) {
    const http = String(method).toUpperCase();
    const known = ROUTES.find((r) => r.op.http === http && r.re.test(String(path).split('?')[0]));
    const op = { ...(known ? known.op : { id: `${http} ${path}`, idempotent: false, retrySafe: http === 'GET', paging: null }), http, path: String(path) };
    const r = await this._send(op, { query: query ?? undefined, body: body ?? undefined }, opts);
    return { status: r.status, headers: r.headers, body: r.data, requestId: r.requestId, idempotencyKey: r.idempotencyKey };
  }

  /** A single-object call: its data (with .meta not enumerable: status, headers, requestId, idempotencyKey). */
  async _one(op, call, opts) {
    const r = await this._send(op, call, opts);
    const data = r.data && Object.prototype.hasOwnProperty.call(r.data, 'data') ? r.data.data : r.data;
    if (data && typeof data === 'object') Object.defineProperty(data, 'meta', { value: { status: r.status, headers: r.headers, requestId: r.requestId, idempotencyKey: r.idempotencyKey }, enumerable: false });
    return data;
  }

  async _list(op, call, opts) {
    const r = await this._send(op, call, opts);
    if (r.status === 304) return null; // not modified: nothing, never an empty page read as an empty list
    return new Page(this, op, call, r.data || {}, { status: r.status, headers: r.headers, requestId: r.requestId, idempotencyKey: r.idempotencyKey }, opts);
  }

  _page(op, call, opts = {}) {
    return new PagePromise(() => this._list(op, call, opts), opts.maxItems);
  }
}

module.exports = { Core, Page, PagePromise, SDK_VERSION, mask };
