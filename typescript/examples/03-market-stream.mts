// 03: live market data: prices for two markets and the trades of one, until 20 frames came.
//   node examples/03-market-stream.mts
import { PhoenixPerps } from '@phoenixperps/sdk';

const client = new PhoenixPerps();
const market = client.marketStream();
let n = 0;
const done = new Promise<void>((resolve) => {
  const count = () => ++n >= 20 && resolve();
  market.on('ticker', (f) => { console.log(`ticker ${f.symbol}: bid ${f.data.bid} ask ${f.data.ask}`); count(); });
  market.on('trades', (f) => { for (const t of f.data) console.log(`trade  ${f.symbol}: ${t.side} ${t.size} @ ${t.price}`); count(); });
});
market.on('reconnect', ({ in_ms, cause }) => console.log(`reconnecting in ${in_ms} ms after ${cause} (subscriptions are made again)`));
await market.subscribe('ticker', ['BTC-USD', 'ETH-USD'], { interval_ms: 500 });
await market.subscribe('trades', ['BTC-USD'], { history: 5 });
await done;
market.close();
