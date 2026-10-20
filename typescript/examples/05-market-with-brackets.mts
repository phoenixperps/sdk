// 05: a market order with its exits: a take profit 1% away and a stop loss 0.5% away, placed by the API the moment
// it fills and kept on its servers. The position is closed at the end.
//   node examples/05-market-with-brackets.mts
import { PhoenixPerps } from '@phoenixperps/sdk';

const client = new PhoenixPerps();
if (client.environment === 'live' && process.env.PHOENIXPERPS_ALLOW_LIVE !== 'yes') {
  console.error('This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).');
  process.exit(1);
}
const [account] = (await client.accounts.list()).data;
const btc = await client.markets.get('BTC-USD');

const order = await client.orders.create(account.id, {
  market: 'BTC-USD',
  side: 'buy',
  type: 'market',
  size: btc.min_size,
  exits: {
    take_profit: [{ percent: '1' }],
    stop_loss: { percent: '0.5' },
  },
});
console.log(`market order ${order.id}: ${order.status} at ${order.average_fill_price ?? '-'}`);

const exits = await client.exits.get(account.id, 'BTC-USD');
for (const tp of exits.take_profit) console.log(`take profit: ${JSON.stringify(tp)}`);
for (const sl of exits.stop_loss) console.log(`stop loss:   ${JSON.stringify(sl)}`);

const close = await client.positions.close(account.id, 'BTC-USD', {});
console.log(`closed: ${close.id} ${close.status} (the exits went with the position)`);
