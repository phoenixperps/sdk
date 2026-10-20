'use strict';
const { model, typeName } = require('../typed');

/*
 * The Go SDK's generated surface (go/gen_*.go, package phoenixperps), already in gofmt's layout:
 *   gen_meta.go       the spec version, hosts, stream URLs, the error codes (ErrCode* constants)
 *   gen_types.go      every schema as a struct (inline objects named after their parent), open enums (a string type and
 *                     its known values: a value from a newer API is kept as is), the query parameters of each list
 *   gen_resources.go  one service per resource, one method per operation (context first, CallOptions last); a paged
 *                     list also gets <Method>AutoPaging, an iter.Seq2 over every item
 *   gen_dispatch_test.go  the conformance runner's table: resource + method names to the typed calls
 * Optional and nullable fields are pointers (Ptr, Dec help build them); decimals are Decimal (a string on the wire).
 */
const HEAD = '// Code generated from the API spec by generator/printers/go.js. DO NOT EDIT: run `node generator`.\n\n';
const INITIALISMS = { id: 'ID', ids: 'IDs', url: 'URL', api: 'API', usd: 'USD', ip: 'IP', ips: 'IPs', http: 'HTTP', json: 'JSON', tv: 'TV', pnl: 'PnL', twap: 'TWAP', vwap: 'VWAP', oco: 'OCO' };
const word = (w) => INITIALISMS[w] || w.charAt(0).toUpperCase() + w.slice(1);
const goName = (s) => String(s).split(/[_\s.-]+/).filter(Boolean).map(word).join('').replace(/^(\d)/, 'N$1');
const lowerFirst = (s) => s.charAt(0).toLowerCase() + s.slice(1);
const argName = (s) => {
  const n = lowerFirst(goName(s));
  return ['type', 'func', 'map', 'range', 'select', 'go', 'default'].includes(n) ? `${n}_` : n;
};
const RESOURCE_NAMES = { tradingview: 'TradingView', me: 'Me' };
const resName = (r) => RESOURCE_NAMES[r] || goName(r.replace(/([a-z0-9])([A-Z])/g, '$1_$2').toLowerCase());
const methodName = (m) => goName(m.replace(/([a-z0-9])([A-Z])/g, '$1_$2').toLowerCase());

/** A comment wrapped at about 110 columns, as lines. */
function commentLines(text, indent = '') {
  const t = String(text || '').replace(/\s+/g, ' ').trim();
  if (!t) return [];
  const lines = [];
  let line = '';
  for (const w of t.split(' ')) {
    if (line && line.length + w.length + 1 > 110) {
      lines.push(line);
      line = w;
    } else line = line ? `${line} ${w}` : w;
  }
  if (line) lines.push(line);
  return lines.map((l) => `${indent}// ${l}`);
}
const comment = (text, indent = '') => commentLines(text, indent).map((l) => `${l}\n`).join('');

/** gofmt's alignment: a run of consecutive rows of cells (a verbatim line ends it) pads every cell but each row's last. */
function aligned(rows) {
  const out = [];
  let run = [];
  const flush = () => {
    const widths = [];
    for (const r of run) r.cells.forEach((c, i) => {
      if (i < r.cells.length - 1) widths[i] = Math.max(widths[i] || 0, c.length);
    });
    for (const r of run) out.push(r.indent + r.cells.map((c, i) => (i < r.cells.length - 1 ? c.padEnd(widths[i]) : c)).join(' '));
    run = [];
  };
  for (const r of rows) {
    if (typeof r === 'string') {
      flush();
      out.push(r);
    } else run.push(r);
  }
  flush();
  return out.join('\n');
}
const row = (indent, ...cells) => ({ indent, cells });

const POINTABLE = new Set(['string', 'int', 'bool', 'number', 'decimal', 'decimalIn', 'enum', 'ref']);
function goType(t, optional) {
  let base;
  switch (t.kind) {
    case 'string': base = 'string'; break;
    case 'int': base = 'int64'; break;
    case 'bool': base = 'bool'; break;
    case 'number': base = 'float64'; break;
    case 'decimal': case 'decimalIn': base = 'Decimal'; break;
    case 'enum': case 'ref': base = t.name; break;
    case 'array': return `[]${goType(t.of, false)}`;
    case 'map': return 'map[string]any';
    default: return 'json.RawMessage';
  }
  return (t.nullable || optional) && POINTABLE.has(t.kind) ? `*${base}` : base;
}

function print(ir) {
  const m = model(ir);
  const files = {};
  const kv = (obj) => aligned(Object.entries(obj).map(([k, v]) => row('\t', `${JSON.stringify(k)}:`, `${JSON.stringify(v)},`)));
  files['go/gen_meta.go'] = `${HEAD}package phoenixperps

// SpecVersion is the version of the API spec this SDK was generated from.
const SpecVersion = ${JSON.stringify(ir.version)}

var servers = map[string]string{
${kv(ir.servers)}
}

var accountStreamURLs = map[string]string{
${kv(ir.streams.account)}
}

const marketStreamURL = ${JSON.stringify(ir.streams.market)}

// The error codes the API answers (Error.Code).
const (
${aligned(ir.errors.map((e) => row('\t', `ErrCode${goName(e.code)}`, `= ${JSON.stringify(e.code)}`)))}
)

// ErrorCodeInfo is what the spec says of an error code.
type ErrorCodeInfo struct {
\tType      string
\tStatus    int
\tRetryable bool
}

// ErrorCodes describes every code the API answers.
var ErrorCodes = map[string]ErrorCodeInfo{
${aligned(ir.errors.map((e) => row('\t', `${JSON.stringify(e.code)}:`, `{${JSON.stringify(e.type)}, ${e.status}, ${e.retryable}},`)))}
}
`;

  // types
  const out = [];
  for (const e of m.enums) {
    out.push(`${comment(`${e.name}: ${e.description || 'one of the values below.'} Open: a value from a newer API version is kept as is.`)}type ${e.name} string

const (
${aligned(e.values.map((v) => row('\t', `${e.name}${goName(v)}`, e.name, `= ${JSON.stringify(v)}`)))}
)
`);
  }
  for (const s of m.structs) {
    const rows = [];
    for (const f of s.fields) {
      rows.push(...commentLines(f.description, '\t'));
      rows.push(row('\t', goName(f.wire), goType(f.type, !f.required), `\`json:"${f.wire}${f.required ? '' : ',omitempty'}"\``));
    }
    out.push(`${comment(`${s.name}: ${s.description || 'see the API reference.'}`)}type ${s.name} struct {
${aligned(rows)}
}
`);
    // a required list left nil goes out as [] (the API wants a list there, never null)
    const lists = s.fields.filter((f) => f.required && f.type.kind === 'array' && !f.type.nullable);
    if (lists.length) {
      out.push(`// MarshalJSON sends a required list left nil as [], never null.
func (x ${s.name}) MarshalJSON() ([]byte, error) {
\ttype plain ${s.name}
\tp := plain(x)
${lists.map((f) => `\tif p.${goName(f.wire)} == nil {\n\t\tp.${goName(f.wire)} = ${goType(f.type, false)}{}\n\t}`).join('\n')}
\treturn json.Marshal(p)
}
`);
    }
    if (s.query) {
      const sets = s.fields.map((f) => {
        const n = goName(f.wire);
        const v = f.type.kind === 'int' ? `strconv.FormatInt(*p.${n}, 10)` : f.type.kind === 'bool' ? `strconv.FormatBool(*p.${n})` : f.type.kind === 'number' ? `strconv.FormatFloat(*p.${n}, 'f', -1, 64)` : `string(*p.${n})`;
        return `\tif p.${n} != nil {\n\t\tv.Set(${JSON.stringify(f.wire)}, ${v})\n\t}`;
      });
      out.push(`func (p *${s.name}) values() url.Values {
\tv := url.Values{}
\tif p == nil {
\t\treturn v
\t}
${sets.join('\n')}
\treturn v
}
`);
    }
  }
  files['go/gen_types.go'] = `${HEAD}package phoenixperps

import (
\t"encoding/json"
\t"net/url"
\t"strconv"
)

var _ json.RawMessage

${out.join('\n')}`;

  // resources
  const opRows = ir.operations.map((o) => row('\t', `op${goName(o.id)}`, `= &operation{id: ${JSON.stringify(o.id)}, method: ${JSON.stringify(o.http)}, path: ${JSON.stringify(o.path)}, idempotent: ${o.idempotent}, retrySafe: ${o.retrySafe}, cursor: ${JSON.stringify(o.paging ? o.paging.param : '')}, scope: ${JSON.stringify(o.scope)}}`));
  const services = [];
  for (const r of ir.resources) {
    const svc = `${resName(r.name)}Service`;
    const methods = [];
    for (const o of r.operations) {
      const name = methodName(o.method);
      const body = o.body && typeName(o.body);
      const response = o.response && typeName(o.response);
      const pathArgs = o.pathParams.map((p) => argName(p.name));
      const args = ['ctx context.Context', ...pathArgs.map((a) => `${a} string`)];
      if (body) args.push(`body ${o.bodyRequired ? '' : '*'}${body}`);
      const params = m.params[o.id];
      if (params) args.push(`params *${params}`);
      args.push('opts ...CallOption');
      const pathList = pathArgs.length ? `[]string{${pathArgs.join(', ')}}` : 'nil';
      const query = params ? 'params.values()' : 'nil';
      const bodyArg = body ? (o.bodyRequired ? 'body' : 'optionalBody(body)') : 'nil';
      const doc = comment(`${name}: ${o.summary}.`);
      if (o.list) {
        methods.push(`${doc}func (r *${svc}) ${name}(${args.join(', ')}) (*Page[${response}], error) {
\treturn listPage[${response}](ctx, r.c, op${goName(o.id)}, ${pathList}, ${query}, opts)
}
`);
        if (o.paging) {
          const call = ['ctx', ...pathArgs, ...(params ? ['params'] : []), 'opts...'].join(', ');
          methods.push(`${comment(`${name}AutoPaging: every item of ${name}, page after page (WithMaxItems caps it).`)}func (r *${svc}) ${name}AutoPaging(${args.join(', ')}) iter.Seq2[${response}, error] {
\treturn autoPaging(ctx, func() (*Page[${response}], error) { return r.${name}(${call}) }, opts)
}
`);
        }
      } else if (response) {
        methods.push(`${doc}func (r *${svc}) ${name}(${args.join(', ')}) (*${response}, error) {
\treturn oneOf[${response}](ctx, r.c, op${goName(o.id)}, ${pathList}, ${query}, ${bodyArg}, opts)
}
`);
      } else {
        methods.push(`${doc}func (r *${svc}) ${name}(${args.join(', ')}) (map[string]any, error) {
\tvar out map[string]any
\tif err := r.c.one(ctx, op${goName(o.id)}, ${pathList}, ${query}, ${bodyArg}, &out, opts); err != nil {
\t\treturn nil, err
\t}
\treturn out, nil
}
`);
      }
    }
    services.push(`${comment(`${svc}: the ${r.name} calls (client.${resName(r.name)}).`)}type ${svc} struct{ c *Client }

${methods.join('\n')}`);
  }
  files['go/gen_resources.go'] = `${HEAD}package phoenixperps

import (
\t"context"
\t"iter"
)

var (
${aligned(opRows)}
)

// the operations, for Client.Do (its route's retry-safety and budget)
func init() {
\toperations = []*operation{${ir.operations.map((o) => `op${goName(o.id)}`).join(', ')}}
}

// resources are the services of a Client, one per resource of the API.
type resources struct {
${aligned(ir.resources.map((r) => row('\t', resName(r.name), `*${resName(r.name)}Service`)))}
}

func (c *Client) initResources() {
${ir.resources.map((r) => `\tc.${resName(r.name)} = &${resName(r.name)}Service{c}`).join('\n')}
}

${services.join('\n')}`;

  // conformance dispatch
  const cases = [];
  for (const o of ir.operations) {
    const call = `c.${resName(o.resource)}.${methodName(o.method)}`;
    const lines = [`\tcase ${JSON.stringify(`${o.resource}.${o.method}`)}:`];
    const args = ['ctx', ...o.pathParams.map((p) => `call.Path[${JSON.stringify(p.name)}]`)];
    if (o.body) {
      lines.push(`\t\tbody := new(${typeName(o.body)})`, '\t\tif err := into(call.Body, body); err != nil {', '\t\t\treturn nil, nil, err', '\t\t}');
      args.push(o.bodyRequired ? '*body' : 'body');
    }
    const params = m.params[o.id];
    if (params) {
      lines.push(`\t\tvar params *${params}`, '\t\tif call.Query != nil {', `\t\t\tparams = new(${params})`, '\t\t\tif err := into(call.Query, params); err != nil {', '\t\t\t\treturn nil, nil, err', '\t\t\t}', '\t\t}');
      args.push('params');
    }
    if (o.list && o.paging) {
      lines.push('\t\tif call.Iterate {', `\t\t\tids, err := collect(${call}AutoPaging(${[...args, 'append(opts, WithMaxItems(call.MaxItems))...'].join(', ')}))`, '\t\t\treturn &raw, ids, err', '\t\t}');
    }
    lines.push(`\t\t_, err := ${call}(${[...args, 'opts...'].join(', ')})`, '\t\treturn &raw, nil, err');
    cases.push(lines.join('\n'));
  }
  files['go/gen_dispatch_test.go'] = `${HEAD}package phoenixperps

import (
\t"context"
\t"fmt"
)

// dispatch makes the call a conformance scenario names (resource + method) through the typed client.
func dispatch(ctx context.Context, c *Client, call confCall) (*RawResponse, []string, error) {
\tvar raw RawResponse
\topts := []CallOption{WithRawResponse(&raw)}
\tswitch call.Resource + "." + call.Method {
${cases.join('\n')}
\t}
\treturn nil, nil, fmt.Errorf("no operation %s.%s", call.Resource, call.Method)
}
`;
  return files;
}

module.exports = { print, goName, aligned, row, commentLines, methodName, resName };
