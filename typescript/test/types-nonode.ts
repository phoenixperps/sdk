// Q94: the declarations need nothing installed but this package (no @types/node, no DOM lib): the docs' first call
// type-checks after `npm install @phoenixperps/sdk` alone.
import { PhoenixPerps, Webhooks } from '..';

const client = new PhoenixPerps({ apiKey: 'ppk_test_x' });
export async function first(): Promise<string> {
  const me = await client.me.get();
  const s = client.accountStream();
  s.on('ready', () => undefined);
  const e = Webhooks.verify('{}', { 'x-phoenix-timestamp': '1', 'x-phoenix-signature': 'sha256=0' }, 'ppwhsec_x');
  return `${String(me)} ${e.id}`;
}
