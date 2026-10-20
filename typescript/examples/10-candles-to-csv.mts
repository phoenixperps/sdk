// 10: the last 100 closed one-minute candles of a market and the one forming (final false), written to candles.csv.
//   node examples/10-candles-to-csv.mts [BTC-USD]
import { writeFileSync } from 'node:fs';
import { PhoenixPerps } from '@phoenixperps/sdk';

type Candle = { time: number; open: string; high: string; low: string; close: string; volume: string; final: boolean };
const client = new PhoenixPerps();
const symbol = (process.argv[2] ?? 'BTC-USD').toUpperCase();
const candles = new Map<number, Candle>();

const market = client.marketStream();
const history = new Promise<void>((resolve) => {
  market.on('candles', (f: { data: Candle[] }) => {
    for (const c of f.data) candles.set(c.time, c);
  });
  market.once('snapshot_end', () => resolve());
});
await market.subscribe('candles', [symbol], { interval: '1m', history: 100 });
await history;
market.close();

const rows = [...candles.values()].sort((a, b) => a.time - b.time);
const lines = rows.map((c) => [new Date(c.time).toISOString(), c.open, c.high, c.low, c.close, c.volume, c.final].join(','));
writeFileSync('candles.csv', `time,open,high,low,close,volume,final\n${lines.join('\n')}\n`);
console.log(`${rows.length} candles of ${symbol} in candles.csv`);
