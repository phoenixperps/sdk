// The starter bot: an EMA cross on one-minute candles, each entry sized so its stop loses PP_RISK_USD, previewed first,
// placed with its stop loss on the API's servers. What it does, exactly: ../README.md. Run it on the sandbox.
//   npm install && npm start
import { existsSync, readFileSync } from 'node:fs';
import { MarketRules, PhoenixPerps } from '@phoenixperps/sdk';

// .env beside it: only what the environment does not set already
if (existsSync('.env')) {
  for (const line of readFileSync('.env', 'utf8').split(/\r?\n/)) {
    const m = /^\s*([A-Z0-9_]+)\s*=\s*(.*?)\s*$/.exec(line);
    if (m && !line.trimStart().startsWith('#') && process.env[m[1]] === undefined) process.env[m[1]] = m[2].replace(/^(['"])(.*)\1$/, '$2');
  }
}
const env = (name: string, fallback = '') => process.env[name] || fallback;
const num = (name: string, fallback: number) => Number(env(name, String(fallback)));

const cfg = {
  market: env('PP_MARKET', 'BTC-USD').toUpperCase(),
  riskUsd: env('PP_RISK_USD', '50'),
  fast: num('PP_FAST', 9),
  slow: num('PP_SLOW', 21),
  atrN: num('PP_ATR', 14),
  atrMult: num('PP_ATR_MULT', 2),
  trailPercent: env('PP_TRAIL_PERCENT'),
  breakEvenPercent: env('PP_BREAK_EVEN_PERCENT'),
  maxTradesPerDay: num('PP_MAX_TRADES_PER_DAY', 10),
  runCandles: num('PP_RUN_CANDLES', 0),
};

const client = new PhoenixPerps(env('PP_STREAM_URL') ? { marketStreamUrl: env('PP_STREAM_URL') } : {});
const live = client.environment === 'live';
if (live && env('PHOENIXPERPS_ALLOW_LIVE') !== 'yes') {
  console.error('This is a live key: the starter trades real money only with PHOENIXPERPS_ALLOW_LIVE=yes. Use a sandbox key to try it.');
  process.exit(1);
}
const log = (msg: string) => console.log(`${new Date().toISOString()} ${live ? 'LIVE ' : ''}${msg}`);

// the account and the market's rules
const accounts = (await client.accounts.list()).data;
const account = env('PP_ACCOUNT') ? accounts.find((a) => a.id === env('PP_ACCOUNT').toUpperCase()) : accounts.find((a) => a.status === 'active');
if (!account) throw new Error(env('PP_ACCOUNT') ? `No account ${env('PP_ACCOUNT')} for this key.` : 'No active account for this key.');
const market = await client.markets.get(cfg.market);
const rules = new MarketRules(market);
const tick = Number(market.tick_size);
log(`${account.id} on ${cfg.market}: EMA ${cfg.fast}/${cfg.slow}, stop ${cfg.atrMult} x ATR(${cfg.atrN}), ${cfg.riskUsd} USD a trade`);

// the indicators (../README.md step 3)
type Candle = { time: number; open: string; high: string; low: string; close: string; final: boolean };
class Ema {
  value: number | null = null;
  seed: number[] = [];
  n: number;
  constructor(n: number) {
    this.n = n;
  }
  add(x: number) {
    if (this.value === null) {
      this.seed.push(x);
      if (this.seed.length === this.n) this.value = this.seed.reduce((s, v) => s + v, 0) / this.n;
    } else this.value = (x - this.value) * (2 / (this.n + 1)) + this.value;
  }
}
class Atr {
  value: number | null = null;
  seed: number[] = [];
  prevClose: number | null = null;
  n: number;
  constructor(n: number) {
    this.n = n;
  }
  add(c: Candle) {
    const [h, l, cl] = [Number(c.high), Number(c.low), Number(c.close)];
    const tr = this.prevClose === null ? h - l : Math.max(h - l, Math.abs(h - this.prevClose), Math.abs(l - this.prevClose));
    this.prevClose = cl;
    if (this.value === null) {
      this.seed.push(tr);
      if (this.seed.length === this.n) this.value = this.seed.reduce((s, v) => s + v, 0) / this.n;
    } else this.value = (this.value * (this.n - 1) + tr) / this.n;
  }
}
const fast = new Ema(cfg.fast);
const slow = new Ema(cfg.slow);
const atr = new Atr(cfg.atrN);
let last = 0;
let trades = { day: '', n: 0 };
let liveCandles = 0;

type Values = { fast: number | null; slow: number | null; atr: number | null };

/** One live candle, with the indicators as of it and the candle before: the position first, then at most one order. */
async function onLive(c: Candle, prev: Values, now: Values) {
  if (now.fast === null || now.slow === null || now.atr === null || prev.fast === null || prev.slow === null) return;
  const up = prev.fast <= prev.slow && now.fast > now.slow;
  const down = prev.fast >= prev.slow && now.fast < now.slow;
  const position = (await client.positions.list(account!.id, { market: cfg.market })).data.find((p) => Number(p.size) !== 0);
  if (position && !position.protected) {
    log(`the ${position.side} position has no stop: closing it`);
    await client.positions.close(account!.id, cfg.market, {});
    return;
  }
  if (!up && !down) return;
  const side = up ? 'buy' : 'sell';
  if (position) {
    if ((position.side === 'long') !== up) {
      log(`cross ${up ? 'up' : 'down'}: closing the ${position.side} position`);
      await client.positions.close(account!.id, cfg.market, {});
    }
    return;
  }
  const day = new Date(c.time).toISOString().slice(0, 10);
  if (trades.day !== day) trades = { day, n: 0 };
  if (trades.n >= cfg.maxTradesPerDay) {
    log(`cross ${up ? 'up' : 'down'}: ${trades.n} entries today already (PP_MAX_TRADES_PER_DAY)`);
    return;
  }
  const ticks = Math.floor((now.atr * cfg.atrMult) / tick + 0.5);
  if (ticks < 1) return;
  const distance = rules.roundPrice(ticks * tick);
  const size = rules.sizeForRisk(cfg.riskUsd, distance);
  if (Number(size) < Number(market.min_size) || Number(size) === 0) {
    log(`cross ${up ? 'up' : 'down'}: ${cfg.riskUsd} USD over a ${distance} stop is below the market's smallest size`);
    return;
  }
  const preview = await client.orders.preview(account!.id, { market: cfg.market, side, type: 'market', size });
  if (preview.result === 'would_reject') {
    log(`cross ${up ? 'up' : 'down'}: the API would refuse ${side} ${size} (${preview.reject?.code})`);
    return;
  }
  const stopLoss: Record<string, unknown> = { distance };
  if (cfg.trailPercent) stopLoss.trail = { percent: cfg.trailPercent };
  if (cfg.breakEvenPercent) stopLoss.break_even = { trigger_percent: cfg.breakEvenPercent, offset_ticks: 1 };
  const order = await client.orders.create(account!.id, {
    market: cfg.market, side, type: 'market', size, client_order_id: `starter-${c.time}`, exits: { stop_loss: stopLoss },
  }, { idempotencyKey: `starter-${cfg.market}-${c.time}` });
  trades.n += 1;
  log(`cross ${up ? 'up' : 'down'}: ${side} ${size} ${cfg.market} (${order.status}), stop ${distance} away`);
}

// candles in, one at a time: history warms the indicators up, live ones trade
const stream = client.marketStream();
let warm = false;
let stopped = false;
let queue = Promise.resolve();
// the end: the stream closed, the process ends once nothing is left to do (never exit() under a closing socket)
const stop = (code: number) => {
  stopped = true;
  process.exitCode = code;
  stream.close();
};
const take = (c: Candle) => {
  if (stopped || !c.final || c.time <= last) return;
  last = c.time;
  const prev = { fast: fast.value, slow: slow.value, atr: atr.value };
  fast.add(Number(c.close));
  slow.add(Number(c.close));
  atr.add(c);
  if (!warm) return;
  const now = { fast: fast.value, slow: slow.value, atr: atr.value }; // the queue may run it after later candles came
  queue = queue.then(() => {
    if (!stopped) return onLive(c, prev, now);
  }).then(() => {
    liveCandles += 1;
    if (cfg.runCandles && liveCandles >= cfg.runCandles) stop(0);
  }).catch((err) => {
    log(`stopped: ${err.message}`);
    stop(1);
  });
};
stream.on('candles', (f: { data: Candle[] }) => f.data.forEach(take));
stream.on('snapshot_end', () => {
  if (!warm) log('warmed up: trading on the next candles');
  warm = true;
});
stream.on('error', (err: Error) => log(`market stream: ${err.message}`));
await stream.subscribe('candles', [cfg.market], { interval: '1m', history: Math.max(3 * cfg.slow, 50) });
