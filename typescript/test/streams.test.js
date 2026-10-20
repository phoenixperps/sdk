'use strict';
// The streams' edges the conformance suite does not script: an upgrade refused (429, 5xx) in a program with no 'error'
// listener, the error frames that answer the account stream's subscribe, risk frames, ping and unsubscribe across a
// reconnect, an event's seq and replayed, a 4008 told as a gap, the reconnect's log line, frames that are not JSON; the
// market stream's list, ping, error details, unsubscribe by interval, a refused upgrade and its resubscribe in chunks.
// Against the conformance mock's scripted WebSocket.
const test = require('node:test');
const assert = require('node:assert/strict');
const { EventEmitter, once } = require('node:events');
const mock = require('../../conformance/mock-server');
const sdk = require('..');

const S = require('../../conformance/scenarios.json');
const ACCOUNT = 'SANDBOX-00042-001';
const OTHER = 'SANDBOX-00042-002';
const hello = { send: { type: 'hello', stream_id: 'str_1', environment: 'sandbox', heartbeat_ms: 15000 } };
const event = (id) => ({ send: { type: 'event', event: { id, type: 'order.created', account_id: ACCOUNT, time: 1791460800000, source: 'api', data: {} } } });
const marketHello = (limits = { subscriptions: 200, symbols_per_subscribe: 50, messages_per_s: 20 }) => ({ send: { type: 'hello', session: 'mds_1', heartbeat_ms: 15000, channels: ['ticker', 'candles'], limits } });
const subscribed = (channel, symbols, more = {}) => ({ reply: { type: 'subscribed', channel, symbols, rejected: [], ...more } });

let server;
test.before(async () => {
  server = await mock.start();
});
test.after(() => server.stop());

const play = (connections) => fetch(`${server.url}/__scenario`, { method: 'POST', body: JSON.stringify({ connections }) });
const requests = async () => (await fetch(`${server.url}/__requests`)).json();
const client = (opts) => {
  const ws = server.url.replace(/^http/, 'ws');
  return new sdk.PhoenixPerps({ apiKey: S.key, baseUrl: server.url, accountStreamUrl: `${ws}/v1/stream`, marketStreamUrl: `${ws}/v1/market-data`, ...opts });
};
/** A message as sent, but its id. */
const sent = ({ id, ...rest }) => rest;
/** What the stream gives next; a stream left waiting fails the test instead of hanging it. */
const within = (p) => Promise.race([p, new Promise((resolve, reject) => setTimeout(() => reject(new Error('the stream is still waiting')), 4000).unref())]);

test('an upgrade refused (429) with no error listener is tried again, never thrown out of the process', { timeout: 20000 }, async () => {
  await play([[{ refuse: 429 }], [hello, { expect: 'subscribe' }, { send: { type: 'snapshot_end' } }, event('evt_1'), { wait_ms: 3000 }]]);
  const s = client().accountStream();
  const reconnects = [];
  s.on('reconnect', (r) => reconnects.push(r));
  const it = s[Symbol.asyncIterator]();
  const first = await within(it.next());
  s.close();
  assert.equal(first.value.id, 'evt_1');
  assert.equal(reconnects.length, 1);
  assert.equal((await requests()).length, 2);
});

test('an upgrade refused and tried again is a reconnect with the API\'s error (details, type), never an \'error\'', async () => {
  const body = JSON.stringify({ error: { type: 'rate_limit', code: 'rate_limited', message: 'Too many requests: slow down.', request_id: 'req_1', retryable: true, details: { scope: 'stream_key' } } });
  // a WebSocket whose upgrade is answered 429 with the API's envelope
  class Refused extends EventEmitter {
    constructor() {
      super();
      setImmediate(() => {
        const res = Object.assign(new EventEmitter(), { statusCode: 429, headers: { 'retry-after': '5' } });
        this.emit('unexpected-response', {}, res);
        res.emit('data', body);
        res.emit('end');
      });
    }
    close() {}
  }
  const s = client().accountStream({ WebSocket: Refused });
  let errors = 0;
  s.on('error', () => {
    errors += 1;
  });
  const r = await new Promise((resolve) => s.on('reconnect', resolve).start());
  s.close();
  const err = r.error;
  assert.deepEqual([errors, r.cause, r.in_ms], [0, 'http 429', 5000], 'events.once(stream, "ready") is not failed by it');
  assert.ok(err instanceof sdk.RateLimitError);
  assert.deepEqual([err.code, err.type, err.details, err.retryAfter, err.requestId], ['rate_limited', 'rate_limit', { scope: 'stream_key' }, 5000, 'req_1']);
});

test('error frame too_many_accounts ends the stream with a ValidationError, never reconnecting', { timeout: 20000 }, async () => {
  await play([[hello, { expect: 'subscribe' }, { send: { type: 'error', code: 'too_many_accounts', details: { max: 50 } } }, { wait_ms: 3000 }], [hello, { wait_ms: 3000 }]]);
  const s = client().accountStream({ accounts: Array.from({ length: 51 }, (_, i) => `SANDBOX-00042-${String(i + 1).padStart(3, '0')}`) });
  const err = await within(s[Symbol.asyncIterator]().next()).catch((e) => e);
  await new Promise((r) => setTimeout(r, 500)); // time enough for a wrong reconnect to show
  s.close();
  assert.ok(err instanceof sdk.ValidationError, String(err));
  assert.equal(err.code, 'too_many_accounts');
  assert.deepEqual(err.details, { max: 50 });
  assert.equal((await requests()).length, 1);
});

test('error frame account_not_found ends the stream with the account named; nothing after it (no ready)', { timeout: 20000 }, async () => {
  await play([[hello, { expect: 'subscribe' }, { send: { type: 'error', code: 'account_not_found', account_id: 'SANDBOX-00042-009' } }, { send: { type: 'snapshot_end', accounts: [] } }, { wait_ms: 3000 }]]);
  const s = client().accountStream({ accounts: ['SANDBOX-00042-009'] });
  let ready = false;
  s.on('ready', () => {
    ready = true;
  });
  const told = new Promise((resolve) => s.on('error', resolve));
  s.start();
  const err = await within(told);
  await new Promise((r) => setTimeout(r, 300));
  s.close();
  assert.ok(err instanceof sdk.ValidationError);
  assert.equal(err.code, 'account_not_found');
  assert.deepEqual(err.details, { account_id: 'SANDBOX-00042-009' });
  assert.equal(ready, false);
});

test('an error frame settles ready: a program awaiting it is told the error', { timeout: 20000 }, async () => {
  await play([[hello, { expect: 'subscribe' }, { send: { type: 'error', code: 'account_not_found', account_id: 'SANDBOX-00042-009' } }, { wait_ms: 1000 }]]);
  const s = client().accountStream({ accounts: ['SANDBOX-00042-009'] });
  const ready = once(s, 'ready');
  s.start();
  await assert.rejects(within(ready), (e) => e instanceof sdk.ValidationError && e.code === 'account_not_found' && e.accountId === 'SANDBOX-00042-009');
  s.close();
});

test('risk frames come only when asked (risk: true), each told as it comes', { timeout: 20000 }, async () => {
  const risk = (n) => ({ send: { type: 'risk', account_id: ACCOUNT, data: { equity: n } } });
  await play([
    [hello, { expect: 'subscribe' }, { send: { type: 'snapshot_end', accounts: [ACCOUNT] } }, risk('1000'), risk('990'), { wait_ms: 1000 }],
    [hello, { expect: 'subscribe' }, { send: { type: 'snapshot_end', accounts: [ACCOUNT] } }, { wait_ms: 1000 }],
  ]);
  const s = client().accountStream({ risk: true });
  const told = [];
  const two = new Promise((resolve) => s.on('risk', (f) => {
    told.push(f.data.equity);
    if (told.length === 2) resolve();
  }));
  s.start();
  await within(two);
  s.close();
  assert.deepEqual(told, ['1000', '990']);
  const plain = client().accountStream();
  const ready = once(plain, 'ready');
  plain.start();
  await within(ready);
  plain.close();
  assert.deepEqual((await requests()).map((r) => r.messages[0]), [{ op: 'subscribe', risk: true }, { op: 'subscribe' }]);
});

test('each event keeps its frame\'s seq and replayed', { timeout: 20000 }, async () => {
  const at = (id, seq, replayed) => ({ send: { ...event(id).send, seq, ...(replayed ? { replayed: true } : {}) } });
  await play([[hello, { expect: 'subscribe' }, at('evt_1', 1, true), { send: { type: 'snapshot_end', accounts: [] } }, at('evt_2', 2, false), { wait_ms: 1000 }]]);
  const s = client().accountStream({ resume: 'evt_0' });
  const it = s[Symbol.asyncIterator]();
  const got = [(await within(it.next())).value, (await within(it.next())).value];
  s.close();
  assert.deepEqual(got.map((e) => [e.id, e.seq, e.replayed]), [['evt_1', 1, true], ['evt_2', 2, false]]);
  assert.equal((await requests())[0].query.resume, 'evt_0');
});

test('ping() is answered; an unsubscribed account stays out after a reconnect', { timeout: 20000 }, async () => {
  await play([
    [hello, { expect: 'subscribe' }, { send: { type: 'snapshot_end', accounts: [ACCOUNT, OTHER] } }, { expect: 'ping' }, { send: { type: 'pong', time: 7 } },
      { expect: 'unsubscribe' }, { send: { type: 'unsubscribed', accounts: [OTHER] } }, { close: 1012 }],
    [hello, { expect: 'subscribe' }, { send: { type: 'snapshot_end', accounts: [ACCOUNT] } }, { wait_ms: 1000 }],
  ]);
  const s = client().accountStream({ accounts: [ACCOUNT, OTHER] });
  const ready = once(s, 'ready');
  s.start();
  await within(ready);
  assert.deepEqual(await within(s.ping()), { type: 'pong', time: 7 });
  const again = once(s, 'ready');
  assert.deepEqual(await within(s.unsubscribe([OTHER.toLowerCase()])), { type: 'unsubscribed', accounts: [OTHER] });
  await within(again);
  s.close();
  assert.deepEqual((await requests()).map((r) => r.messages), [
    [{ op: 'subscribe', accounts: [ACCOUNT, OTHER] }, { op: 'ping' }, { op: 'unsubscribe', accounts: [OTHER.toLowerCase()] }],
    [{ op: 'subscribe', accounts: [ACCOUNT] }],
  ]);
});

test('following every account, one unsubscribed stays out: the subscribe again names the hello\'s accounts less it', { timeout: 20000 }, async () => {
  const helloOf = (accounts) => ({ send: { ...hello.send, accounts } });
  await play([
    [helloOf([ACCOUNT, OTHER]), { expect: 'subscribe' }, { send: { type: 'snapshot_end', accounts: [ACCOUNT, OTHER] } }, { expect: 'unsubscribe' }, { send: { type: 'unsubscribed', accounts: [OTHER] } }, { close: 1012 }],
    [helloOf([ACCOUNT, OTHER, 'SANDBOX-00042-003']), { expect: 'subscribe' }, { send: { type: 'snapshot_end', accounts: [] } }, { wait_ms: 1000 }],
  ]);
  const s = client().accountStream();
  const ready = once(s, 'ready');
  s.start();
  await within(ready);
  const again = once(s, 'ready');
  await within(s.unsubscribe([OTHER]));
  await within(again);
  s.close();
  assert.deepEqual((await requests()).map((r) => r.messages[0]), [{ op: 'subscribe' }, { op: 'subscribe', accounts: [ACCOUNT, 'SANDBOX-00042-003'] }]);
});

test('4008 is a gap told after the last event, and each reconnect is one log line with its cause', { timeout: 20000 }, async () => {
  await play([
    [hello, { expect: 'subscribe' }, { send: { type: 'snapshot_end', accounts: [] } }, event('evt_1'), { close: 4008 }],
    [hello, { expect: 'subscribe' }, { send: { type: 'snapshot_end', accounts: [] } }, event('evt_2'), { wait_ms: 1000 }],
  ]);
  const lines = [];
  const s = client({ logger: { warn: (msg, data) => lines.push([msg, data]) } }).accountStream();
  const gaps = [];
  s.on('gap', (g) => gaps.push(g));
  const it = s[Symbol.asyncIterator]();
  const ids = [(await within(it.next())).value.id, (await within(it.next())).value.id];
  s.close();
  assert.deepEqual(ids, ['evt_1', 'evt_2']);
  assert.deepEqual(gaps, [{ code: 'too_far_behind', after: 'evt_1' }]);
  assert.deepEqual(lines.map(([msg, d]) => [msg, d.cause ?? d.code, d.resume]), [
    ['phoenixperps: the account stream missed events', 'too_far_behind', undefined],
    ['phoenixperps: AccountStream reconnects', 'close 4008', false],
  ]);
  assert.deepEqual((await requests()).map((r) => r.query.resume ?? null), [null, null]);
});

test('a frame that is not JSON is dropped, never thrown out of the socket', async () => {
  // a WebSocket that sends a proxy's page, then a hello
  class Garbled extends EventEmitter {
    constructor() {
      super();
      setImmediate(() => {
        this.emit('message', Buffer.from('<html>bad gateway</html>'));
        this.emit('message', Buffer.from(JSON.stringify({ type: 'hello', session: 'mds_1', heartbeat_ms: 15000 })));
      });
    }
    send() {}
    close() {}
  }
  const s = client().accountStream({ WebSocket: Garbled });
  const m = client().marketStream({ WebSocket: Garbled });
  const hellos = Promise.all([once(s, 'hello'), once(m, 'hello')]);
  s.start();
  m.start();
  await within(hellos);
  s.close();
  m.close();
});

test('market: list() and ping() are answered by id; an error frame keeps its details', { timeout: 20000 }, async () => {
  await play([[marketHello(), { expect: 'subscribe' }, { reply: { type: 'error', code: 'invalid_parameter', details: { field: 'interval', issue: '1m 5m' } } },
    { expect: 'list' }, { reply: { type: 'list', subscriptions: [] } }, { expect: 'ping' }, { reply: { type: 'pong', time: 7 } }, { wait_ms: 1000 }]]);
  const m = client().marketStream();
  await assert.rejects(within(m.subscribe('candles', ['BTC-USD'], { interval: '2m' })), (e) => e instanceof sdk.ValidationError && e.code === 'invalid_parameter' && e.details.field === 'interval');
  assert.deepEqual((await within(m.list())).subscriptions, []);
  assert.equal((await within(m.ping())).type, 'pong');
  m.close();
  assert.deepEqual((await requests())[0].messages.map((x) => x.op), ['subscribe', 'list', 'ping']);
});

test('market: an unsubscribe names the interval or ends every one; what is left is made again, as it was', { timeout: 20000 }, async () => {
  const unsubscribed = (channel, symbols) => ({ reply: { type: 'unsubscribed', channel, symbols, rejected: [] } });
  await play([
    [marketHello(), { expect: 'subscribe' }, subscribed('candles', ['BTC-USD'], { interval: '1m' }), { expect: 'subscribe' }, subscribed('candles', ['BTC-USD'], { interval: '5m' }),
      { expect: 'subscribe' }, subscribed('candles', ['ETH-USD'], { interval: '1m' }), { expect: 'unsubscribe' }, unsubscribed('candles', ['BTC-USD']), { close: 1012 }],
    [marketHello(), { expect: 'subscribe' }, { expect: 'subscribe' }, { expect: 'unsubscribe' }, unsubscribed('candles', ['BTC-USD']), { close: 1012 }],
    [marketHello(), { expect: 'subscribe' }, { wait_ms: 1000 }],
  ]);
  const m = client().marketStream();
  await within(m.subscribe('candles', ['BTC-USD'], { interval: '1m', history: 5 }));
  await within(m.subscribe('candles', ['BTC-USD'], { interval: '5m' }));
  await within(m.subscribe('candles', ['ETH-USD'], { interval: '1m' }));
  const second = once(m, 'hello');
  await within(m.unsubscribe('candles', ['BTC-USD'], { interval: '1m' }));
  await within(second);
  const third = once(m, 'hello');
  await within(m.unsubscribe('candles', ['BTC-USD']));
  await within(third);
  await new Promise((r) => setTimeout(r, 300));
  m.close();
  const got = (await requests()).map((r) => r.messages.map(sent));
  assert.deepEqual(got[0][3], { op: 'unsubscribe', channel: 'candles', symbols: ['BTC-USD'], interval: '1m' });
  assert.deepEqual(got[1], [
    { op: 'subscribe', channel: 'candles', symbols: ['BTC-USD'], interval: '5m' },
    { op: 'subscribe', channel: 'candles', symbols: ['ETH-USD'], interval: '1m' },
    { op: 'unsubscribe', channel: 'candles', symbols: ['BTC-USD'] },
  ]);
  assert.deepEqual(got[2], [{ op: 'subscribe', channel: 'candles', symbols: ['ETH-USD'], interval: '1m' }]);
});

test('market: the subscriptions made again come at most symbols_per_subscribe (the hello\'s limits) a message', { timeout: 20000 }, async () => {
  const one = marketHello({ subscriptions: 200, symbols_per_subscribe: 1, messages_per_s: 20 });
  await play([
    [one, { expect: 'subscribe' }, subscribed('ticker', ['BTC-USD', 'ETH-USD']), { close: 1012 }],
    [one, { expect: 'subscribe' }, { expect: 'subscribe' }, { wait_ms: 1000 }],
  ]);
  const m = client().marketStream();
  const second = new Promise((resolve) => {
    let n = 0;
    m.on('hello', () => (n += 1) === 2 && resolve());
  });
  await within(m.subscribe('ticker', ['BTC-USD', 'ETH-USD'], { interval_ms: 500 }));
  assert.deepEqual(m.limits, { subscriptions: 200, symbols_per_subscribe: 1, messages_per_s: 20 });
  await within(second);
  await new Promise((r) => setTimeout(r, 300));
  m.close();
  assert.deepEqual((await requests())[1].messages.map(sent), [
    { op: 'subscribe', channel: 'ticker', symbols: ['BTC-USD'], interval_ms: 500 },
    { op: 'subscribe', channel: 'ticker', symbols: ['ETH-USD'], interval_ms: 500 },
  ]);
});

test('market: an upgrade refused with 503 reconnects after its Retry-After; with 401 it ends with the API\'s error', { timeout: 20000 }, async () => {
  const refuse = (status, code, retryAfter) => ({ refuse: status, ...(retryAfter !== undefined ? { retry_after: retryAfter } : {}), body: { error: { type: 'x', code, message: 'x', request_id: 'req_1', retryable: status === 503 } } });
  await play([[refuse(503, 'capacity', 0)], [marketHello(), { expect: 'subscribe' }, subscribed('ticker', ['BTC-USD']), { wait_ms: 1000 }]]);
  const m = client().marketStream();
  const reconnects = [];
  m.on('reconnect', (r) => reconnects.push(r));
  await within(m.subscribe('ticker', ['BTC-USD']));
  m.close();
  assert.deepEqual(reconnects.map(({ error, ...r }) => [r, error && error.code]), [[{ in_ms: 0, resume: false, cause: 'http 503' }, 'capacity']]);
  assert.equal((await requests()).length, 2);

  await play([[refuse(401, 'invalid_api_key')]]);
  const denied = client().marketStream();
  await assert.rejects(within(denied.subscribe('ticker', ['BTC-USD'])), (e) => e instanceof sdk.AuthenticationError && e.code === 'invalid_api_key' && e.requestId === 'req_1');
  await new Promise((r) => setTimeout(r, 500)); // time enough for a wrong reconnect to show
  assert.equal((await requests()).length, 1);
});
