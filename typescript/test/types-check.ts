// Compile-only: the declarations describe the client as it is used (npm run typecheck).
import PhoenixPerps, { RuleRejectedError, MarketRules, Webhooks, WebhookVerificationError, PhoenixPerpsError, type Order, type Market, type StreamGap, type MarketLimits } from '..';

async function main(): Promise<void> {
  const client = new PhoenixPerps({ apiKey: 'ppk_test_x', appInfo: 'types-check/1.0' });
  const keyless = new PhoenixPerps({ environment: 'SANDBOX' });
  const masked: string | null = keyless.maskedKey;
  const raw = await client.request<{ data: Order }>('GET', '/v1/accounts/SANDBOX-00042-001/orders/ord_x', null, undefined, { onMeta: (m) => void m.status });
  const rawStatus: number = raw.status;
  void raw.body?.data.id;
  const notModified = await client.me.get({ headers: { 'if-none-match': '"v1"' }, onMeta: (m) => void m.headers.get('etag') });
  const order: Order = await client.orders.create('SANDBOX-00042-001', { market: 'BTC-USD', side: 'buy', type: 'limit', size: '0.01', limit_price: 62000 }, { idempotencyKey: 'k-1' });
  const status: string = order.status;
  const page = await client.orders.list('SANDBOX-00042-001', { status: 'working', limit: 50 });
  for (const o of page.data) void o.id;
  for await (const fill of client.fills.list('SANDBOX-00042-001', undefined, { maxItems: 100 })) void fill.price;
  const market: Market = await client.markets.get('BTC-USD');
  const rules = new MarketRules(market);
  const worst: string = rules.worstPrice('buy', '62000');
  void rules.worstPrice('SELL', '62000', '0.5');
  const exits = await client.exits.set('SANDBOX-00042-001', 'BTC-USD', { expected_position: { side: 'long', size: '0.01' }, expected_exits: [], operations: [{ op: 'place', group: 'stop_loss', price: '61000', trail: { distance: '500' } }] });
  void exits.stop_loss;
  const stream = client.accountStream({ risk: true });
  stream.on('ready', () => undefined);
  stream.on('gap', (g: StreamGap) => void client.events.list({ after: g.after ?? undefined }));
  const pong: number = (await stream.ping()).time;
  void stream.unsubscribe(['SANDBOX-00042-002']);
  for await (const e of stream) void [e.type, e.seq, e.replayed];
  const data = client.marketStream();
  const limits: MarketLimits | null = data.limits;
  const ended = await data.unsubscribe('candles', ['BTC-USD'], { interval: '1m' });
  void [ended.symbols, ended.rejected, (await data.list()).subscriptions, (await data.ping()).time, limits?.symbols_per_subscribe];
  try {
    await client.orders.cancel('SANDBOX-00042-001', 'ord_x');
  } catch (err) {
    if (err instanceof RuleRejectedError) void [err.code, err.retryable, err.docUrl];
  }
  const event = Webhooks.verify('{}', { 'x-phoenix-timestamp': '1', 'x-phoenix-signature': 'x' }, 'ppwhsec_x');
  const refused = new WebhookVerificationError('bad_body', 'x');
  const family: PhoenixPerpsError = refused;
  void [status, worst, event.id, event.test, event.environment, masked, rawStatus, notModified, pong, family.code];
}
void main;
