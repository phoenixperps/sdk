'use strict';

/*
 * The spec read into what every printer needs (docs/api/design/14-sdks.md section 3), language-free:
 *   operations: [{ id, resource, method, http, path, pathParams (with example), query, body, bodyExample, response, list,
 *                  paging, idempotent, retrySafe, scope, summary, description }]
 *   resources:  [{ name, operations }] in the spec's order
 *   schemas:    { Name: JSON Schema } as the spec has them
 *   servers:    { live, sandbox }
 *   errors:     [{ code, type, status, retryable }]
 * The names come from x-pp-resource / x-pp-method only (never the paths), so a path may move without an SDK change.
 */
function build(spec) {
  const servers = Object.fromEntries((spec.servers || []).map((s) => [s['x-pp-environment'], s.url]));
  const operations = [];
  for (const [path, item] of Object.entries(spec.paths)) {
    for (const [http, o] of Object.entries(item)) {
      if (!o['x-pp-resource'] || !o['x-pp-method']) throw new Error(`${http} ${path}: no x-pp-resource / x-pp-method`);
      const params = o.parameters || [];
      const ok = o.responses['200'] || o.responses['201'] || o.responses['202'];
      const schema = ok && ok.content && ok.content['application/json'] && ok.content['application/json'].schema;
      const list = Boolean(schema && schema.properties && schema.properties.data && schema.properties.data.type === 'array');
      const itemRef = list ? schema.properties.data.items.$ref : schema && schema.properties && schema.properties.data && schema.properties.data.$ref;
      operations.push({
        id: o.operationId,
        resource: o['x-pp-resource'],
        method: o['x-pp-method'],
        http: http.toUpperCase(),
        path,
        pathParams: params.filter((p) => p.in === 'path').map((p) => ({ name: p.name, description: p.description || '', example: p.schema && p.schema.example !== undefined ? p.schema.example : p.example })),
        query: params.filter((p) => p.in === 'query').map((p) => ({ name: p.name, description: p.description || '', schema: p.schema || {} })),
        body: o.requestBody ? refName(o.requestBody.content['application/json'].schema.$ref) : null,
        bodyRequired: Boolean(o.requestBody && o.requestBody.required),
        bodyExample: o.requestBody ? o.requestBody.content['application/json'].example : undefined,
        response: itemRef ? refName(itemRef) : null,
        list,
        paging: o['x-pp-pagination'] || null,
        idempotent: Boolean(o['x-pp-idempotent']),
        retrySafe: Boolean(o['x-pp-retry-safe']) || http === 'get',
        scope: o['x-pp-scope'],
        summary: o.summary || '',
        description: o.description || '',
      });
    }
  }
  const resources = [];
  for (const op of operations) {
    let r = resources.find((x) => x.name === op.resource);
    if (!r) resources.push((r = { name: op.resource, operations: [] }));
    r.operations.push(op);
  }
  const errors = (spec.components['x-pp-error-codes'] || []).map((e) => ({ code: e.code, type: e.type, status: e.status, retryable: Boolean(e.retryable) }));
  return { version: spec.info.version, servers, operations, resources, schemas: spec.components.schemas, errors };
}

const refName = (ref) => (ref ? String(ref).split('/').pop() : null);

module.exports = { build, refName };
