// 11: receive webhooks: every delivery is checked (its signature and its time) before it is trusted.
//   PHOENIXPERPS_WEBHOOK_SECRET=ppwhsec_... node examples/11-webhook-receiver.mts
// Then point a webhook endpoint (dashboard, Developers, Webhooks) at https://<this machine>/webhooks (port 8080 here).
import { createServer } from 'node:http';
import { WebhookVerificationError, Webhooks } from '@phoenixperps/sdk';

const secret = process.env.PHOENIXPERPS_WEBHOOK_SECRET;
if (!secret) throw new Error("Set PHOENIXPERPS_WEBHOOK_SECRET to the endpoint's signing secret.");

createServer((req, res) => {
  if (req.method !== 'POST' || req.url !== '/webhooks') {
    res.writeHead(404).end();
    return;
  }
  const chunks: Buffer[] = [];
  req.on('data', (c: Buffer) => chunks.push(c));
  req.on('end', () => {
    try {
      const event = Webhooks.verify(Buffer.concat(chunks), req.headers, secret); // the body exactly as received
      if (event.type === 'webhook.ping') console.log(`a test delivery (${event.environment})`);
      else console.log(`${event.type} on ${event.account_id}: ${JSON.stringify(event.data)}`);
      res.writeHead(200).end();
    } catch (err) {
      if (!(err instanceof WebhookVerificationError)) throw err;
      console.log(`refused a delivery: ${err.code}`);
      res.writeHead(400).end();
    }
  });
}).listen(8080, () => console.log('listening on :8080/webhooks'));
