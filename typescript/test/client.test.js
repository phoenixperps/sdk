'use strict';
// The transport's edges the conformance suite does not script: the API's own `retryable`, a success's body inside the
// attempt, path parameters checked before any request, every page asked as the first, a list's other fields, an
// error's headers, the environment's case, a change's Idempotency-Key, a 304's meta, the raw request, the rate limit
// per budget, and webhook signatures that are not ASCII. Against the conformance mock where an answer is needed.
const test = require('node:test');
const assert = require('node:assert/strict');
const crypto = require('node:crypto');
const util = require('node:util');
const mock = require('../../conformance/mock-server');
const sdk = require('..');

const S = require('../../conformance/scenarios.json');
const ACCOUNT = 'SANDBOX-00042-001';
const ORDER = { id: 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W', status: 'working' };
const envelope = (code, retryable) => ({ error: { type: 'x', code, message: 'x', request_id: 'req_1', retryable } });

let server;
test.before(async () => {
  server = await mock.start();
});
test.after(() => server.stop());

/** The mock answers these, in order; the requests it got come back from requests(). */
const script = (...respond) => fetch(`${server.url}/__scenario`, { method: 'POST', body: JSON.stringify({ exchanges: respond.map((r) => ({ respond: r })) }) });
const requests = async () => (await fetch(`${server.url}/__requests`)).json();
const client = (opts) => new sdk.PhoenixPerps({ apiKey: S.key, baseUrl: server.url, ...opts });

test('409 operation_in_progress (retryable) is asked again with the same key: the order, not a ConflictError', async () => {
  await script({ status: 409, headers: { 'retry-after': '0' }, body: envelope('operation_in_progress', true) }, { status: 201, body: { data: ORDER } });
  const order = await client().orders.create(ACCOUNT, { market: 'BTC-USD', side: 'buy', type: 'market', size: '0.01' });
  const got = await requests();
  assert.equal(order.id, ORDER.id);
  assert.equal(got.length, 2);
  assert.equal(new Set(got.map((r) => r.headers['idempotency-key'])).size, 1);
});

test('a 503 the API marks not retryable (sandbox_disabled) is not asked again', async () => {
  await script({ status: 503, headers: { 'retry-after': '0' }, body: envelope('sandbox_disabled', false) }, { status: 200, body: { data: {} } });
  await assert.rejects(client().me.get(), (e) => e instanceof sdk.ServerError && e.code === 'sandbox_disabled');
  assert.equal((await requests()).length, 1);
});

test('an empty path parameter is a ValidationError before any request, never retried', async () => {
  let sent = 0;
  const c = client({ fetch: async () => { sent += 1; throw new Error('never'); } });
  const t0 = Date.now();
  await assert.rejects(c.orders.get(ACCOUNT, ''), (e) => e instanceof sdk.ValidationError && e.code === 'invalid_request');
  assert.equal(sent, 0);
  assert.ok(Date.now() - t0 < 200, 'no backoff');
});

test('a success whose body stalls past the timeout is a TimeoutError, and tried again as any timeout', async () => {
  let sent = 0;
  // as fetch does: the call's signal ends the body too
  const stalls = async (url, init) => {
    sent += 1;
    return new Response(new ReadableStream({
      start(c) {
        c.enqueue(new TextEncoder().encode('{"data":'));
        init.signal.addEventListener('abort', () => c.error(init.signal.reason));
      },
    }), { status: 200 });
  };
  await assert.rejects(client({ fetch: stalls }).me.get({ timeout: 50, maxRetries: 1 }), (e) => e instanceof sdk.TimeoutError && e.code === 'timeout' && e.retryable === true);
  assert.equal(sent, 2);
});

test('a number goes out in its decimal form, never with an exponent', async () => {
  let sent = null;
  const c = client({ fetch: async (url, init) => { sent = init.body; return new Response(JSON.stringify({ data: ORDER }), { status: 201 }); } });
  await c.orders.create(ACCOUNT, { market: 'BTC-USD', side: 'buy', type: 'limit', size: 1e-7, limit_price: 62000.5, notional_usd: -2.5e-8, client_order_id: 'n', max_value: 1.5e21 });
  assert.equal(sent, '{"market":"BTC-USD","side":"buy","type":"limit","size":0.0000001,"limit_price":62000.5,"notional_usd":-0.000000025,"client_order_id":"n","max_value":1500000000000000000000}');
});

test('a network failure is a ConnectionError marked retryable', async () => {
  const c = client({ fetch: async () => { throw new TypeError('fetch failed'); } });
  await assert.rejects(c.me.get({ maxRetries: 0 }), (e) => e instanceof sdk.ConnectionError && e.retryable === true);
});

test('environment is read in any case, and the key\'s own comes before PHOENIXPERPS_ENVIRONMENT', () => {
  const saved = process.env.PHOENIXPERPS_ENVIRONMENT;
  process.env.PHOENIXPERPS_ENVIRONMENT = 'LIVE';
  try {
    assert.equal(new sdk.PhoenixPerps({ apiKey: S.key }).environment, 'sandbox');
    assert.equal(new sdk.PhoenixPerps({ apiKey: S.key, environment: 'Sandbox' }).environment, 'sandbox');
    assert.equal(new sdk.PhoenixPerps({ apiKey: '' }).environment, 'live');
    assert.throws(() => new sdk.PhoenixPerps({ apiKey: S.key, environment: 'LIVE' }), (e) => e.code === 'wrong_environment');
  } finally {
    if (saved === undefined) delete process.env.PHOENIXPERPS_ENVIRONMENT;
    else process.env.PHOENIXPERPS_ENVIRONMENT = saved;
  }
});

test('a change sent with an Idempotency-Key is tried again under it (PATCH honours one)', async () => {
  await script({ status: 503, headers: { 'retry-after': '0' }, body: envelope('engine_unavailable', true) }, { status: 200, body: { data: ORDER } });
  const order = await client().orders.modify(ACCOUNT, ORDER.id, { limit_price: '61900' }, { idempotencyKey: 'change-1' });
  assert.equal(order.meta.idempotencyKey, 'change-1');
  assert.deepEqual((await requests()).map((r) => r.headers['idempotency-key']), ['change-1', 'change-1']);
});

test('a 304 answers null, its meta (status, ETag) told to onMeta; a list answers null, never an empty page', async () => {
  const metas = [];
  const onMeta = (m) => metas.push(m);
  await script({ status: 304, headers: { etag: '"v1-abc"' } }, { status: 304, headers: { etag: '"v2-def"' } }, { status: 304 });
  assert.equal(await client().me.get({ headers: { 'if-none-match': '"v1-abc"' }, onMeta }), null);
  assert.equal(await client().orders.list(ACCOUNT, {}, { headers: { 'if-none-match': '"v2-def"' }, onMeta }), null);
  const items = [];
  for await (const o of client().orders.list(ACCOUNT, {}, { headers: { 'if-none-match': '"v3"' } })) items.push(o);
  assert.deepEqual(items, []);
  assert.deepEqual(metas.map((m) => [m.status, m.headers.get('etag')]), [[304, '"v1-abc"'], [304, '"v2-def"']]);
  assert.deepEqual((await requests()).map((r) => r.headers['if-none-match']), ['"v1-abc"', '"v2-def"', '"v3"']);
});

test('the raw request: the answer as it came, with the transport\'s key, retries and errors', async () => {
  const c = client();
  // a GET: tried again
  await script({ status: 503, headers: { 'retry-after': '0' }, body: envelope('engine_unavailable', true) }, { status: 200, headers: { etag: '"e"' }, body: { data: { ok: true } } });
  const r = await c.request('get', '/v1/me', { fields: 'credential' });
  assert.deepEqual([r.status, r.headers.get('etag'), r.body, r.requestId], [200, '"e"', { data: { ok: true } }, 'req_conformance']);
  let got = await requests();
  assert.deepEqual(got.map((x) => [x.method, x.path, x.query.fields, x.headers.authorization]), [['GET', '/v1/me', 'credential', `Bearer ${S.key}`], ['GET', '/v1/me', 'credential', `Bearer ${S.key}`]]);
  // a route the spec marks retry-safe (an order's preview): tried again; a placed order: its own Idempotency-Key
  await script({ status: 503, headers: { 'retry-after': '0' }, body: envelope('engine_unavailable', true) }, { status: 200, body: { data: {} } }, { status: 201, body: { data: ORDER } });
  await c.request('POST', `/v1/accounts/${ACCOUNT}/orders/test`, null, { market: 'BTC-USD', side: 'buy', type: 'market', size: '0.01' });
  const placed = await c.request('POST', `/v1/accounts/${ACCOUNT}/orders`, null, { market: 'BTC-USD', side: 'buy', type: 'market', size: '0.01' });
  got = await requests();
  assert.equal(got.length, 3);
  assert.deepEqual(got[2].body, { market: 'BTC-USD', side: 'buy', type: 'market', size: '0.01' });
  assert.match(placed.idempotencyKey, /^[0-9a-f-]{36}$/);
  assert.equal(got[2].headers['idempotency-key'], placed.idempotencyKey);
  // a write the spec does not mark safe: sent once, its error the same class as a typed call's
  await script({ status: 503, headers: { 'retry-after': '0' }, body: envelope('engine_unavailable', true) });
  await assert.rejects(c.request('POST', '/v1/webhook-endpoints/we_1/test'), (e) => e instanceof sdk.ServerError && e.code === 'engine_unavailable');
  assert.equal((await requests()).length, 1);
  // ... unless it carries an Idempotency-Key
  await script({ status: 503, headers: { 'retry-after': '0' }, body: envelope('engine_unavailable', true) }, { status: 200, body: { data: {} } });
  await c.request('POST', '/v1/webhook-endpoints/we_1/test', null, null, { idempotencyKey: 'raw-1' });
  assert.deepEqual((await requests()).map((x) => x.headers['idempotency-key']), ['raw-1', 'raw-1']);
});

test('the rate limit is kept per budget (the operation\'s x-pp-scope): one used up does not hold the other', async () => {
  const { OPS } = require('../src/gen/resources');
  const c = client();
  const read = { ...OPS.getMe, scope: 'read' };
  const trade = { ...OPS.getMe, scope: 'trade' };
  const spent = { 'x-ratelimit-limit': '10', 'x-ratelimit-remaining': '0', 'x-ratelimit-reset': '1' };
  await script({ status: 200, headers: spent, body: { data: {} } }, { status: 200, body: { data: {} } }, { status: 200, body: { data: {} } });
  await c._one(trade, {});
  const t0 = Date.now();
  await c._one(read, {});
  assert.ok(Date.now() - t0 < 500, 'the read budget is not the trade one');
  await c._one(trade, {});
  assert.ok(Date.now() - t0 >= 800, 'the trade budget waited for its reset');
});

test('a success whose body is not JSON (a proxy page) is a PhoenixPerpsError http_<status>', async () => {
  await script({ status: 200, body: '<html>maintenance</html>' });
  await assert.rejects(client().me.get(), (e) => e instanceof sdk.PhoenixPerpsError && e.code === 'http_200' && e.status === 200 && e.message === '<html>maintenance</html>');
});

test('every page is asked with the first call\'s options (headers here)', async () => {
  await script(
    { status: 200, body: { data: [{ id: 'o1' }, { id: 'o2' }], has_more: true, next_cursor: 'c1', as_of: 1 } },
    { status: 200, body: { data: [{ id: 'o3' }], has_more: false, next_cursor: null, as_of: 1 } },
  );
  const ids = [];
  for await (const o of client().orders.list(ACCOUNT, { limit: 2 }, { headers: { 'x-client-request-id': 'paging' } })) ids.push(o.id);
  assert.deepEqual(ids, ['o1', 'o2', 'o3']);
  assert.deepEqual((await requests()).map((r) => r.headers['x-client-request-id']), ['paging', 'paging']);
});

test('a page keeps the list\'s other fields (history_complete as historyComplete)', async () => {
  await script({ status: 200, body: { data: [], has_more: false, next_cursor: null, as_of: 7, history_complete: false } });
  const page = await client().orders.list(ACCOUNT, { status: 'all' });
  assert.equal(page.historyComplete, false);
  assert.deepEqual(Object.keys(page), ['data', 'hasMore', 'nextCursor', 'asOf', 'historyComplete']);
  // its meta as a single result's: status, headers, request id and Idempotency-Key (none on a read)
  assert.deepEqual([page.meta.status, page.meta.requestId, page.meta.idempotencyKey], [200, 'req_conformance', null]);
});

test('an error carries the answer\'s headers (Idempotent-Replayed), not enumerable', async () => {
  await script({ status: 422, headers: { 'idempotent-replayed': 'true' }, body: { error: { type: 'rule_rejection', code: 'insufficient_margin', message: 'x', request_id: 'req_9', retryable: false, order_id: ORDER.id } } });
  const err = await client().orders.create(ACCOUNT, { market: 'BTC-USD', side: 'buy', type: 'market', size: '5' }).catch((e) => e);
  assert.ok(err instanceof sdk.RuleRejectedError);
  assert.equal(err.headers.get('idempotent-replayed'), 'true');
  assert.ok(!Object.keys(err).includes('headers'));
  assert.ok(!util.inspect(err).includes('idempotent-replayed'));
});

test('a signature with a non-ASCII character is bad_signature, never a RangeError; a good one after it still checks', () => {
  const v = S.webhook_vectors;
  const at = (sig) => ({ 'X-Phoenix-Timestamp': v.timestamp, 'X-Phoenix-Signature': sig });
  const good = `sha256=${crypto.createHmac('sha256', v.secret).update(`${v.timestamp}.${v.body}`).digest('hex')}`;
  // as many characters as a real signature, more bytes (Node reads a header's bytes as latin1)
  for (const forged of [`${good.slice(0, -1)}é`, `${good.slice(0, -2)}Ã©`, `${good.slice(0, -1)}\ud800`]) {
    assert.equal(forged.length, good.length);
    assert.throws(() => sdk.Webhooks.verify(v.body, at(forged), v.secret, 300, v.now_ms), (e) => e instanceof sdk.WebhookVerificationError && e.code === 'bad_signature');
    assert.equal(sdk.Webhooks.verify(v.body, at(`${forged}, ${good}`), v.secret, 300, v.now_ms).type, v.want.type);
  }
});
