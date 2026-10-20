// 08: a live copy of your accounts' positions and working orders: the snapshot first, then every event applied once.
// After a reconnect the SDK resumes from the last event; events it could not resume are told as a gap, and the
// snapshot that follows rebuilds the copy. Ctrl+C to stop.
//   node examples/08-account-stream-mirror.mts
import { PhoenixPerps } from '@phoenixperps/sdk';

type Row = { id?: string; market?: string; status?: string; size?: string; side?: string };
const FINAL = new Set(['filled', 'canceled', 'rejected', 'expired']);
const mirror = new Map<string, { positions: Map<string, Row>; orders: Map<string, Row> }>();

const client = new PhoenixPerps();
const stream = client.accountStream();
stream.on('snapshot', (f: { account_id: string; positions: Row[]; orders: Row[]; error?: string }) => {
  // an account that could not be read now comes empty with the reason: not a flat account, so not mirrored
  if (f.error) {
    mirror.delete(f.account_id);
    console.log(`[snapshot] ${f.account_id}: not readable now (${f.error}); read it again by REST`);
    return;
  }
  mirror.set(f.account_id, {
    positions: new Map(f.positions.map((p) => [String(p.market), p])),
    orders: new Map(f.orders.map((o) => [String(o.id), o])),
  });
});
stream.on('ready', () => print('snapshot'));
stream.on('reconnect', ({ in_ms, cause }: { in_ms: number; cause: string }) => console.log(`reconnecting in ${in_ms} ms (${cause})`));
// the events missed are in GET /v1/events?after=; the copy itself comes back with the next snapshot
stream.on('gap', ({ after }: { after: string | null }) => console.log(`[gap] events after ${after ?? 'the start'} were not told: client.events.list({ after })`));

function print(why: string) {
  for (const [id, m] of mirror) {
    const pos = [...m.positions.values()].map((p) => `${p.market} ${p.side} ${p.size}`).join(', ') || 'none';
    console.log(`[${why}] ${id}: positions ${pos}; ${m.orders.size} working orders`);
  }
}

for await (const e of stream) {
  const m = mirror.get(e.account_id);
  if (!m) continue;
  const data = e.data as { order?: Row; position?: Row };
  if (data.order?.id) {
    if (FINAL.has(String(data.order.status))) m.orders.delete(data.order.id);
    else m.orders.set(data.order.id, data.order);
  }
  if (data.position?.market) {
    if (e.type === 'position.closed') m.positions.delete(data.position.market);
    else m.positions.set(data.position.market, data.position);
  }
  print(e.type);
}
