'use strict';

/*
 * The TypeScript SDK's generated surface (typescript/src/gen): meta.js (hosts, streams, spec version, error codes),
 * resources.js (one method per operation, grouped by resource) and types.d.ts (every schema as an interface, open
 * enums, decimals as strings; inputs also take numbers).
 */
const HEAD = '// Generated from the API spec by generator/printers/typescript.js. Do not edit: run `node generator`.\n';
const camel = (s) => s.replace(/_([a-z])/g, (m, c) => c.toUpperCase());
const pascal = (s) => s.charAt(0).toUpperCase() + camel(s).slice(1);
const quote = (s) => JSON.stringify(s);
const doc = (text, indent) => (text ? `${indent}/** ${String(text).replace(/\*\//g, '*\\/').replace(/\s+/g, ' ').trim()} */\n` : '');

function tsType(schema, schemas, { input = false } = {}) {
  if (!schema) return 'unknown';
  if (schema.$ref) return schema.$ref.split('/').pop();
  const types = Array.isArray(schema.type) ? schema.type : schema.type ? [schema.type] : [];
  const nullable = types.includes('null');
  const base = types.filter((t) => t !== 'null');
  let t;
  if (schema.enum) {
    const values = schema.enum.filter((v) => v !== null).map(quote);
    t = values.length ? `${values.join(' | ')} | (string & {})` : 'string';
  } else if (base.includes('string') && base.includes('number')) t = 'string | number';
  else if (base.length > 1) t = base.map((b) => tsType({ ...schema, type: b, enum: undefined }, schemas, { input })).join(' | ');
  else if (base[0] === 'string') t = 'string';
  else if (base[0] === 'integer' || base[0] === 'number') t = 'number';
  else if (base[0] === 'boolean') t = 'boolean';
  else if (base[0] === 'array') t = `Array<${tsType(schema.items, schemas, { input })}>`;
  else if (base[0] === 'object' || schema.properties) t = schema.properties ? objectType(schema, schemas, { input, indent: '  ' }) : 'Record<string, unknown>';
  else t = 'unknown';
  return nullable ? `${t} | null` : t;
}

function objectType(schema, schemas, { input, indent }) {
  const req = new Set(schema.required || []);
  const lines = Object.entries(schema.properties || {}).map(([k, v]) => `${indent}  ${doc(v.description, '').trim() ? `${doc(v.description, '').trim()} ` : ''}${/^[a-z_][a-z0-9_]*$/i.test(k) ? k : quote(k)}${input || !req.has(k) ? '?' : ''}: ${tsType(v, schemas, { input })};`);
  return `{\n${lines.join('\n')}\n${indent}}`;
}

function print(ir) {
  const files = {};
  // meta.js
  files['typescript/src/gen/meta.js'] = `${HEAD}'use strict';

module.exports = {
  SPEC_VERSION: ${quote(ir.version)},
  SERVERS: ${JSON.stringify(ir.servers)},
  STREAMS: ${JSON.stringify(ir.streams)},
  ERROR_CODES: ${JSON.stringify(Object.fromEntries(ir.errors.map((e) => [e.code, { type: e.type, status: e.status, retryable: e.retryable }])))},
};
`;
  // resources.js
  const opLines = ir.operations.map((o) => `  ${o.id}: ${JSON.stringify({ id: o.id, http: o.http, path: o.path, idempotent: o.idempotent, retrySafe: o.retrySafe, scope: o.scope, paging: o.paging })},`);
  const resLines = ir.resources.map((r) => {
    const methods = r.operations.map((o) => {
      const args = [...o.pathParams.map((p) => camel(p.name)), ...(o.body ? ['body'] : []), ...(o.query.length ? ['query'] : []), 'opts'];
      const parts = [o.pathParams.length ? `path: { ${o.pathParams.map((p) => `${p.name}: ${camel(p.name)}`).join(', ')} }` : null, o.body ? 'body' : null, o.query.length ? 'query' : null].filter(Boolean);
      const call = parts.length ? `{ ${parts.join(', ')} }` : '{}';
      return `${doc(o.summary, '      ')}      ${o.method}: (${args.join(', ')}) => c.${o.list ? '_page' : '_one'}(OPS.${o.id}, ${call}, opts),`;
    });
    return `    ${r.name}: {\n${methods.join('\n')}\n    },`;
  });
  files['typescript/src/gen/resources.js'] = `${HEAD}'use strict';

const OPS = {
${opLines.join('\n')}
};

/** The resources of a client: each method sends its operation through the client's transport. */
function resources(c) {
  return {
${resLines.join('\n')}
  };
}

module.exports = { OPS, resources };
`;
  // types.d.ts: a request body's decimals are `string | number` in the spec already (inputs take numbers)
  const ifaces = Object.entries(ir.schemas).map(([name, s]) => ((s.type === 'object' || s.properties) && !Array.isArray(s.type)
    ? `${doc(s.description, '')}export interface ${name} ${objectType(s, ir.schemas, { input: false, indent: '' })}`
    : `export type ${name} = ${tsType(s, ir.schemas)};`));
  const queries = ir.operations.filter((o) => o.query.length).map((o) => `export interface ${pascal(o.id)}Query {\n${o.query.map((q) => `  ${doc(q.description, '').trim()} ${q.name}?: ${tsType(q.schema, ir.schemas)};`).join('\n')}\n}`);
  const resIfaces = ir.resources.map((r) => `export interface ${pascal(r.name)}Resource {\n${r.operations.map((o) => {
    const args = [...o.pathParams.map((p) => `${camel(p.name)}: string`), ...(o.body ? [`body${o.bodyRequired ? '' : '?'}: ${o.body}`] : []), ...(o.query.length ? [`query?: ${pascal(o.id)}Query`] : []), `opts?: ${o.list ? 'ListOptions' : 'RequestOptions'}`];
    const ret = o.list ? `PagePromise<${o.response || 'unknown'}>` : `Promise<${o.response || 'unknown'}>`;
    return `${doc(o.summary, '  ')}  ${o.method}(${args.join(', ')}): ${ret};`;
  }).join('\n')}\n}`);
  files['typescript/src/gen/types.d.ts'] = `${HEAD}import type { RequestOptions, ListOptions, PagePromise } from '../core';

${ifaces.join('\n\n')}

${queries.join('\n\n')}

${resIfaces.join('\n\n')}

export interface Resources {
${ir.resources.map((r) => `  ${r.name}: ${pascal(r.name)}Resource;`).join('\n')}
}
`;
  return files;
}

module.exports = { print, camel, pascal };
