// 12: what failures look like: each has a class by family, the API's code, the request id to quote to support, and
// details. A write whose outcome is unknown (a timeout) is repeated with the same Idempotency-Key, never a new one.
//   node examples/12-error-handling.mts
import { randomUUID } from 'node:crypto';
import { NotFoundError, PhoenixPerps, PhoenixPerpsError, RateLimitError, RuleRejectedError, TimeoutError, ValidationError } from '@phoenixperps/sdk';
import type { CreateOrder, Order } from '@phoenixperps/sdk';

const client = new PhoenixPerps();
const [account] = (await client.accounts.list()).data;

function show(err: unknown) {
  if (!(err instanceof PhoenixPerpsError)) throw err;
  console.log(`${err.constructor.name}: ${err.code} (HTTP ${err.status}, ${err.requestId}) ${err.message}`);
  if (err.details) console.log(`  details: ${JSON.stringify(err.details)}`);
  console.log(`  ${err.retryable ? 'may succeed later' : 'will not succeed as it is'}${err.docUrl ? `: ${err.docUrl}` : ''}`);
}

try {
  await client.orders.get(account.id, 'ord_does_not_exist');
} catch (err) {
  if (!(err instanceof NotFoundError)) throw err;
  show(err);
}

try {
  await client.orders.preview(account.id, { market: 'BTC-USD', side: 'buy', type: 'market', size: '0.0000001' });
} catch (err) {
  if (!(err instanceof ValidationError || err instanceof RuleRejectedError)) throw err;
  show(err);
}

// a write that may or may not have happened: the same key again is safe, a new key could place it twice
export async function placeOnce(body: CreateOrder): Promise<Order> {
  const idempotencyKey = randomUUID();
  for (let attempt = 1; ; attempt += 1) {
    try {
      return await client.orders.create(account.id, body, { idempotencyKey });
    } catch (err) {
      if (err instanceof TimeoutError && attempt < 3) continue; // still unknown after the SDK's own retries: same key
      if (err instanceof RateLimitError && err.retryAfter !== null && attempt < 3) {
        await new Promise((r) => setTimeout(r, err.retryAfter ?? 0));
        continue;
      }
      throw err;
    }
  }
}
