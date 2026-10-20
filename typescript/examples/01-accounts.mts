// 01: who you are (the key, its access and limits) and your accounts.
//   PHOENIXPERPS_API_KEY=ppk_test_... node examples/01-accounts.mts
import { PhoenixPerps } from '@phoenixperps/sdk';

const client = new PhoenixPerps(); // the key: PHOENIXPERPS_API_KEY (a sandbox key: the sandbox)

const me = await client.me.get();
console.log(`${me.credential.kind} "${me.credential.name}" on ${me.credential.environment}: ${me.credential.access} access`);
console.log(`limits: ${me.budgets.read_per_min} reads and ${me.budgets.trade_per_min} trading calls a minute`);

const { data } = await client.accounts.list();
for (const account of data) {
  console.log(`${account.id}  ${account.stage}  ${account.status}  balance ${account.balance ?? '-'}`);
}
