// 07: what an order would do now, without placing it: fill or rest, the price, the fee, the margin, the position
// after. Nothing is sent to the market.
//   node examples/07-what-if.mts
import { PhoenixPerps } from '@phoenixperps/sdk';

const client = new PhoenixPerps();
const [account] = (await client.accounts.list()).data;

const w = await client.orders.preview(account.id, { market: 'BTC-USD', side: 'buy', type: 'market', notional_usd: '1000' });
console.log(`result:         ${w.result}${w.reject ? ` (${JSON.stringify(w.reject)})` : ''}`);
console.log(`size:           ${w.size ?? '-'}`);
console.log(`price:          ${w.execution?.expected_price ?? '-'} (worst ${w.execution?.worst_price ?? '-'})`);
console.log(`notional, fee:  ${w.notional ?? '-'}, ${w.fee ?? '-'}`);
console.log(`margin:         ${w.margin?.required ?? '-'} needed, ${w.margin?.available_after ?? '-'} left after`);
console.log(`position after: ${w.position_after?.side ?? 'flat'} ${w.position_after?.size ?? ''}`);
