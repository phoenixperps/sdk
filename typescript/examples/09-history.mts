// 09: an account's history: the equity curve by day, the result of each trading day, closed positions and fills.
//   node examples/09-history.mts
import { PhoenixPerps } from '@phoenixperps/sdk';

const client = new PhoenixPerps();
const [account] = (await client.accounts.list()).data;

const equity = await client.history.equity(account.id, { resolution: 'day' });
console.log(`equity: ${equity.points.length} points`, equity.points.slice(-3));

for (const d of (await client.history.daily(account.id)).data) {
  console.log(`${d.date}: ${d.trades} trades, P&L ${d.pnl}, fees ${d.fees}`);
}
for (const p of (await client.positions.listClosed(account.id, { limit: 10 })).data) {
  console.log(`closed ${p.market} ${p.side} ${p.size}: ${p.entry_price} -> ${p.exit_price}, P&L ${p.pnl}`);
}
let n = 0;
for await (const f of client.fills.list(account.id, {}, { maxItems: 100 })) {
  n += 1;
  if (n <= 5) console.log(`fill ${f.market} ${f.side} ${f.size} @ ${f.price}`);
}
console.log(`${n} fills read (every page, at most 100)`);
