// 02: the markets and their rules, then a quote (bid, ask, last) from the market stream.
//   node examples/02-markets.mts [BTC-USD]
import { PhoenixPerps } from '@phoenixperps/sdk';

const client = new PhoenixPerps();
const symbol = (process.argv[2] ?? 'BTC-USD').toUpperCase();

const { data } = await client.markets.list();
for (const m of data) console.log(`${m.market.padEnd(10)} tick ${m.tick_size}  step ${m.size_step}  min ${m.min_size}  up to ${m.max_leverage}x  ${m.status}`);

const market = client.marketStream();
const quote = new Promise<{ bid: string; ask: string; last: string }>((resolve) => market.once('ticker', (f) => resolve(f.data)));
const reply = await market.subscribe('ticker', [symbol], { interval_ms: 250 });
if (reply.rejected.length) throw new Error(`${symbol}: ${reply.rejected[0].code}`);
const q = await quote;
console.log(`${symbol}: bid ${q.bid}  ask ${q.ask}  last ${q.last}`);
market.close();
