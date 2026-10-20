// 04: a limit order far from the market, found again by your own id, then canceled. The Idempotency-Key makes a
// retried request return the first order instead of placing a second one.
//   node examples/04-place-cancel-limit.mts
import { randomUUID } from 'node:crypto';
import { MarketRules, PhoenixPerps } from '@phoenixperps/sdk';

const client = new PhoenixPerps();
if (client.environment === 'live' && process.env.PHOENIXPERPS_ALLOW_LIVE !== 'yes') {
  console.error('This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).');
  process.exit(1);
}
const [account] = (await client.accounts.list()).data;
const rules = new MarketRules(await client.markets.get('BTC-USD'));

const market = client.marketStream();
const tick = new Promise<{ bid: string }>((resolve) => market.once('ticker', (f) => resolve(f.data)));
await market.subscribe('ticker', ['BTC-USD']);
const { bid } = await tick;
market.close();

const clientOrderId = `example-04-${Date.now()}`;
const order = await client.orders.create(account.id, {
  market: 'BTC-USD',
  side: 'buy',
  type: 'limit',
  size: rules.sizeForNotional('100', bid), // about 100 USD
  limit_price: rules.roundPrice(Number(bid) * 0.9, 'down'), // 10% below: it rests
  client_order_id: clientOrderId,
}, { idempotencyKey: randomUUID() });
console.log(`placed ${order.id}: ${order.status}, ${order.size} at ${order.limit_price}`);

const found = (await client.orders.list(account.id, { client_order_id: clientOrderId })).data[0];
console.log(`found by client_order_id: ${found.id}`);

const canceled = await client.orders.cancel(account.id, order.id);
console.log(`canceled: ${canceled.status} (${canceled.cancel_reason})`);
