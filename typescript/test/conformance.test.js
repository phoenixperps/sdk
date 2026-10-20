'use strict';
// The conformance suite (conformance/scenarios.json) through the TypeScript SDK against the mock server: retries,
// idempotency keys, Retry-After, errors, paging, open enums, decimals, the rate-limit wait, 304, the base URL; the local
// checks (wrong environment, the key never printed, webhook signatures, the keyless client); MarketRules; the streams.
// Every key of the suite's "vocabulary" is checked here, none skipped.
const test = require('node:test');
const assert = require('node:assert/strict');
const util = require('node:util');
const mock = require('../../conformance/mock-server');
const sdk = require('..');
const { resources } = require('../src/gen/resources');

const S = require('../../conformance/scenarios.json');
let server;
test.before(async () => {
  server = await mock.start();
});
test.after(() => server.stop());

/** The SDK call a scenario names, its arguments in the generated method's order (path parameters, body, query, options). */
function invoke(client, call) {
  const seen = {};
  const recorder = { _one: (o, c) => Object.assign(seen, { o, c }), _page: (o, c) => Object.assign(seen, { o, c }) };
  resources(recorder)[call.resource][call.method]();
  const args = [...seen.o.path.matchAll(/{([^}]+)}/g)].map((m) => call.path[m[1]]);
  if ('body' in seen.c) args.push(call.body);
  if ('query' in seen.c) args.push(call.query);
  args.push(call.max_items !== undefined ? { maxItems: call.max_items } : undefined);
  return client[call.resource][call.method](...args);
}

const client = () => new sdk.PhoenixPerps({ apiKey: S.key, baseUrl: server.url, maxRetries: 2 });
const requests = async () => (await fetch(`${server.url}/__requests`)).json();
const pick = (obj, want) => Object.fromEntries(Object.keys(want).map((k) => [k, obj[k] && typeof want[k] === 'object' && !Array.isArray(want[k]) ? pick(obj[k], want[k]) : obj[k]]));
// the suite's keys in this SDK's naming (doc_url: docUrl, retry_after_ms: retryAfter, in ms)
const camel = (s) => (s === 'retry_after_ms' ? 'retryAfter' : s.replace(/_([a-z])/g, (m, c) => c.toUpperCase()));
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

for (const sc of S.scenarios) {
  test(sc.name, async () => {
    await fetch(`${server.url}/__scenario`, { method: 'POST', body: JSON.stringify({ exchanges: sc.exchanges }) });
    const c = new sdk.PhoenixPerps({ apiKey: S.key, baseUrl: `${server.url}${(sc.client && sc.client.base_url_suffix) || ''}`, maxRetries: 2 });
    let result;
    let error = null;
    let items = null;
    try {
      for (const call of sc.calls || [sc.call]) {
        if (call.iterate) {
          items = [];
          for await (const x of invoke(c, call)) items.push(x.id);
        } else result = await invoke(c, call);
      }
    } catch (err) {
      error = err;
    }
    const got = await requests();
    const x = sc.expect;
    assert.equal(got.length, x.requests, 'requests sent');
    if (x.ok !== undefined) assert.equal(error === null, x.ok, error ? error.message : 'no error');
    if (x.error) {
      assert.ok(error, 'an error');
      if (x.error.class) assert.equal(error.constructor.name, x.error.class);
      for (const [k, v] of Object.entries(x.error)) if (k !== 'class') assert.equal(error[camel(k)], v, k);
    } else {
      assert.equal(error, null, error && error.message);
      if (x.result === null) assert.equal(result, null, 'the call answered nothing');
      else if (x.result) assert.deepEqual(pick(result, x.result), x.result);
      if (x.items) assert.deepEqual(items, x.items);
      for (const [k, v] of Object.entries(x.page_fields || {})) assert.deepEqual(result[camel(k)], v, k);
    }
    if (x.paths) assert.deepEqual(got.map((r) => r.path), x.paths);
    if (x.cursors) assert.deepEqual(got.map((r) => r.query.cursor ?? null), x.cursors);
    if (x.first_body) assert.deepEqual(pick(got[0].body, x.first_body), x.first_body);
    if (x.min_elapsed_ms) assert.ok(got.at(-1).at - got[0].at >= x.min_elapsed_ms, `waited ${got.at(-1).at - got[0].at} ms`);
    for (const check of x.checks || []) {
      if (check === 'bearer') assert.equal(got[0].headers.authorization, `Bearer ${S.key}`);
      if (check === 'user_agent') assert.match(got[0].headers['user-agent'], /^phoenixperps-typescript\/\d+\.\d+\.\d+ \(.+\) spec\//);
      if (check === 'same_idempotency_key') {
        const keys = new Set(got.map((r) => r.headers['idempotency-key']));
        assert.equal(keys.size, 1);
        assert.match([...keys][0], /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/);
      }
    }
  });
}

/** The local checks: by their `check`, or (none) a client the SDK refuses before any request. */
const LOCAL = {
  refused(l) {
    assert.throws(() => new sdk.PhoenixPerps({ apiKey: l.client.api_key, environment: l.client.environment, baseUrl: 'http://127.0.0.1:9' }), (e) => e.code === l.expect.error_code);
  },
  redaction() {
    const c = client();
    for (const text of [util.inspect(c), JSON.stringify(c), String(c), Object.keys(c).join(',')]) assert.ok(!text.includes(S.key), text);
    assert.equal(c.maskedKey, `ppk_test_****${S.key.slice(-4)}`);
  },
  webhook_vectors() {
    const v = S.webhook_vectors;
    const at = (sig, ts = v.timestamp) => ({ 'X-Phoenix-Timestamp': ts, 'X-Phoenix-Signature': sig });
    const e = sdk.Webhooks.verify(v.body, at(v.signature), v.secret, 300, v.now_ms);
    assert.deepEqual(pick(e, v.want), v.want, 'a real delivery reads as the event');
    assert.equal(sdk.Webhooks.verify(v.body, at(v.rotation), [v.secret], 300, v.now_ms).id, 'evt_01JAB7Q8XK4M2N5P7R9S3T5V7W');
    const refused = (code) => (err) => err instanceof sdk.WebhookVerificationError && err instanceof sdk.PhoenixPerpsError && err.code === code;
    assert.throws(() => sdk.Webhooks.verify(v.body, at(v.signature), v.other, 300, v.now_ms), refused('bad_signature'));
    assert.throws(() => sdk.Webhooks.verify(v.body, at(v.signature), v.secret, 300, v.now_ms + 1000000), refused('stale'));
    assert.throws(() => sdk.Webhooks.verify(`${v.body} `, at(v.signature), v.secret, 300, v.now_ms), refused('bad_signature'));
    assert.throws(() => sdk.Webhooks.verify(v.body, {}, v.secret, 300, v.now_ms), refused('missing_headers'));
    for (const x of v.extra) {
      const verify = () => sdk.Webhooks.verify(x.body, at(x.signature, x.timestamp), v.secret, 300, v.now_ms);
      if (x.error) assert.throws(verify, refused(x.error), x.name);
      else assert.deepEqual(pick(verify(), x.want), x.want, x.name);
    }
  },
  async keyless(l) {
    // api_key null: no key at all (one in this process's environment would be taken)
    const saved = process.env.PHOENIXPERPS_API_KEY;
    delete process.env.PHOENIXPERPS_API_KEY;
    try {
      const c = new sdk.PhoenixPerps({ apiKey: l.client.api_key, environment: l.client.environment, baseUrl: server.url });
      await fetch(`${server.url}/__scenario`, { method: 'POST', body: JSON.stringify({ exchanges: [{ respond: { status: 200, body: { data: [{ market: 'BTC-USD' }], has_more: false, next_cursor: null } } }] }) });
      const page = await c.markets.list();
      assert.equal(page.data[0].market, 'BTC-USD');
      const got = await requests();
      assert.equal(got.length, 1);
      assert.equal(got[0].headers.authorization, undefined, 'no Authorization without a key');
      assert.equal(c.maskedKey, null);
    } finally {
      if (saved !== undefined) process.env.PHOENIXPERPS_API_KEY = saved;
    }
  },
};
for (const l of S.local) test(`local: ${l.name}`, () => LOCAL[l.check || 'refused'](l));

test('local: MarketRules rounds exactly, as every SDK does', () => {
  const R = S.rules_vectors;
  const r = new sdk.MarketRules(R.market);
  const fn = { round_price: (a, m) => r.roundPrice(a[0], m), round_size: (a) => r.roundSize(a[0]), size_for_notional: (a) => r.sizeForNotional(a[0], a[1]), size_for_risk: (a) => r.sizeForRisk(a[0], a[1]), worst_price: (a) => r.worstPrice(...a) };
  for (const c of R.cases) {
    const label = `${c.fn}(${c.args.join(', ')}${c.mode ? `, ${c.mode}` : ''})`;
    // error: a ValidationError of the SDK's own, never the language's
    if (c.error) assert.throws(() => fn[c.fn](c.args, c.mode), (e) => e instanceof sdk.ValidationError, label);
    else assert.equal(fn[c.fn](c.args, c.mode), c.want, label);
  }
  assert.equal(r.roundPrice(62000.06), '62000.1');
  assert.equal(r.roundSize(1e-7), '0.000');
  // an exponent is read exactly, never through a float
  assert.equal(r.roundSize('2.3e-2'), '0.023');
  assert.equal(r.roundSize('1.1E-2'), '0.011');
  assert.equal(r.roundSize(1e21), '1000000000000000000000.000');
  assert.equal(r.roundPrice('6.20005e4', 'up'), '62000.5');
  assert.throws(() => r.roundSize('1e-99999'), (e) => e instanceof sdk.ValidationError);
});

for (const sc of S.streams) {
  test(`stream: ${sc.name}`, { timeout: 20000 }, async () => {
    await fetch(`${server.url}/__scenario`, { method: 'POST', body: JSON.stringify({ connections: sc.connections }) });
    const ws = server.url.replace(/^http/, 'ws');
    const c = new sdk.PhoenixPerps({ apiKey: S.key, baseUrl: server.url, accountStreamUrl: `${ws}/v1/stream`, marketStreamUrl: `${ws}/v1/market-data` });
    const x = sc.expect;
    let error = null;
    if (sc.stream === 'account') {
      // read as a program would, with no 'error' listener: an error ends the iteration, nothing escapes
      const s = c.accountStream();
      let gap = null;
      s.on('gap', (g) => {
        gap = g.after;
      });
      const it = s[Symbol.asyncIterator]();
      const ids = [];
      try {
        while (ids.length < (x.events || []).length) {
          const r = await it.next();
          if (r.done) break;
          ids.push(r.value.id);
        }
        if (x.error) await it.next();
      } catch (e) {
        error = e;
      }
      if (x.error) await sleep(500); // time enough for a wrong reconnect to show
      s.close();
      if (x.events) assert.deepEqual(ids, x.events);
      if (x.gap !== undefined) assert.equal(gap, x.gap, 'the gap told, after the last event');
    } else {
      const m = c.marketStream();
      const frames = [];
      const got = new Promise((resolve) => m.on(sc.subscribe.channel, (f) => {
        frames.push(f.data.last);
        if (frames.length === x.frames.length) resolve();
      }));
      await m.subscribe(sc.subscribe.channel, sc.subscribe.symbols, sc.subscribe.params);
      if (sc.unsubscribe) await m.unsubscribe(sc.unsubscribe.channel, sc.unsubscribe.symbols, sc.unsubscribe.params);
      await got;
      m.close();
      assert.deepEqual(frames, x.frames);
      if (x.limits) assert.deepEqual(m.limits, x.limits);
    }
    if (x.error) {
      assert.ok(error, 'an error ends it');
      if (x.error.class) assert.equal(error.constructor.name, x.error.class);
      for (const [k, v] of Object.entries(x.error)) if (k !== 'class') assert.equal(error[camel(k)], v, k);
    }
    const reqs = await requests();
    assert.equal(reqs.length, x.connections);
    (x.resume || []).forEach((want, i) => assert.equal(reqs[i].query.resume ?? null, want));
    assert.equal(reqs[0].headers.authorization, `Bearer ${S.key}`);
    const shallow = (o, w) => Object.fromEntries(Object.keys(w).map((k) => [k, o && o[k]]));
    // a message as the SDK sent it, but its id: nothing more than the scenario names
    const sent = (msg) => {
      const { id, ...rest } = msg || {};
      return rest;
    };
    if (x.first_message) assert.deepEqual(shallow(reqs[0].messages[0], x.first_message), x.first_message);
    if (x.resubscribe) assert.deepEqual(sent(reqs.at(-1).messages[0]), x.resubscribe);
    if (x.unsubscribe_sent) assert.deepEqual(sent(reqs.flatMap((r) => r.messages).find((msg) => msg.op === 'unsubscribe')), x.unsubscribe_sent);
  });
}
