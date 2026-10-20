'use strict';

/*
 * The Postman collection (postman/): one folder per resource, one request per operation, the key as a Bearer token from
 * the environment ({{apiKey}}, never written in), an Idempotency-Key made per send on the calls that take one, and the
 * spec's example as each body. Two environments, live and sandbox, with the hosts and an empty key.
 */
const camel = (s) => s.replace(/_([a-z])/g, (m, c) => c.toUpperCase());

function example(schema, schemas, depth = 0) {
  if (!schema || depth > 4) return null;
  if (schema.$ref) return example(schemas[schema.$ref.split('/').pop()], schemas, depth + 1);
  if (schema.example !== undefined) return schema.example;
  if (schema.properties) {
    const req = new Set(schema.required || []);
    return Object.fromEntries(Object.entries(schema.properties).filter(([k]) => req.has(k)).map(([k, v]) => [k, example(v, schemas, depth + 1)]));
  }
  return null;
}

function print(ir) {
  const folders = ir.resources.map((r) => ({
    name: r.name,
    item: r.operations.map((o) => {
      const path = o.path.split('/').filter(Boolean).map((seg) => seg.replace(/^\{(.+)\}$/, (m, n) => `:${n}`));
      const body = o.body ? example(ir.schemas[o.body], ir.schemas) : null;
      return {
        name: o.summary,
        request: {
          method: o.http,
          header: [
            ...(body ? [{ key: 'Content-Type', value: 'application/json' }] : []),
            ...(o.idempotent ? [{ key: 'Idempotency-Key', value: '{{idempotencyKey}}' }] : []),
          ],
          url: {
            raw: `{{baseUrl}}/${path.join('/')}`,
            host: ['{{baseUrl}}'],
            path,
            variable: o.pathParams.map((p) => ({ key: p.name, value: `{{${camel(p.name)}}}`, description: p.description })),
            query: o.query.map((q) => ({ key: q.name, value: '', description: q.description, disabled: true })),
          },
          ...(body ? { body: { mode: 'raw', raw: JSON.stringify(body, null, 2), options: { raw: { language: 'json' } } } } : {}),
          description: o.description || o.summary,
          ...(o.scope === 'public' ? { auth: { type: 'noauth' } } : {}),
        },
      };
    }),
  }));
  const collection = {
    info: { name: 'Phoenix Perpetuals API', description: `The REST API, spec ${ir.version}. Pick the live or sandbox environment and set apiKey in it.`, schema: 'https://schema.getpostman.com/json/collection/v2.1.0/collection.json' },
    auth: { type: 'bearer', bearer: [{ key: 'token', value: '{{apiKey}}', type: 'string' }] },
    event: [{ listen: 'prerequest', script: { type: 'text/javascript', exec: ['// one Idempotency-Key per send: a retry of the same request in the same run reuses it', "pm.variables.set('idempotencyKey', pm.variables.replaceIn('{{$guid}}'));"] } }],
    variable: [{ key: 'accountId', value: '' }, { key: 'market', value: 'BTC-USD' }, { key: 'orderId', value: '' }, { key: 'strategyId', value: '' }, { key: 'endpointId', value: '' }, { key: 'deliveryId', value: '' }, { key: 'hookId', value: '' }, { key: 'alertId', value: '' }, { key: 'eventId', value: '' }],
    item: folders,
  };
  const env = (name, url) => ({ name: `Phoenix Perpetuals ${name}`, values: [{ key: 'baseUrl', value: url, enabled: true }, { key: 'apiKey', value: '', type: 'secret', enabled: true }] });
  return {
    'postman/phoenixperps.postman_collection.json': `${JSON.stringify(collection, null, 2)}\n`,
    'postman/live.postman_environment.json': `${JSON.stringify(env('live', ir.servers.live), null, 2)}\n`,
    'postman/sandbox.postman_environment.json': `${JSON.stringify(env('sandbox', ir.servers.sandbox), null, 2)}\n`,
  };
}

module.exports = { print };
