// 06: a position whose stop loss trails the price by 0.5% and moves to break-even once the price has gone 0.5% your
// way, all on the API's servers. Follows the account stream for 30 s to watch the stop move, then closes.
//   node examples/06-trailing-breakeven.mts
import { once } from 'node:events';
import { PhoenixPerps } from '@phoenixperps/sdk';

const client = new PhoenixPerps();
if (client.environment === 'live' && process.env.PHOENIXPERPS_ALLOW_LIVE !== 'yes') {
  console.error('This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).');
  process.exit(1);
}
const [account] = (await client.accounts.list()).data;
const btc = await client.markets.get('BTC-USD');

// the stream first, so every event of the stop is told, from its placing on
const stream = client.accountStream({ accounts: [account.id] });
stream.on('event', (e) => {
  if (e.type.startsWith('strategy.')) console.log(`${new Date(e.time).toISOString()} ${e.type} ${JSON.stringify(e.data)}`);
});
stream.start();
await once(stream, 'ready');

const order = await client.orders.create(account.id, {
  market: 'BTC-USD',
  side: 'buy',
  type: 'market',
  size: btc.min_size,
  exits: {
    stop_loss: {
      percent: '1',
      trail: { percent: '0.5' },
      break_even: { trigger_percent: '0.5', offset_ticks: 2 },
    },
  },
});
console.log(`bought ${order.size} BTC at ${order.average_fill_price ?? '-'}; watching the stop for 30 s`);

await new Promise((r) => setTimeout(r, 30_000));
stream.close();

const close = await client.positions.close(account.id, 'BTC-USD', {});
console.log(`closed: ${close.status}`);
