// Compile-only, as an ES module (nodenext): the README's default import is the client, as src/index.mjs exports it.
import PhoenixPerps, { PhoenixPerps as Named, ValidationError, type Order } from '@phoenixperps/sdk';

const client: Named = new PhoenixPerps({ apiKey: 'ppk_test_x' });
const order: Promise<Order> = client.orders.get('SANDBOX-00042-001', 'ord_x');
void [order, ValidationError];
