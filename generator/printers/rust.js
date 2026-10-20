'use strict';
const { model, typeName } = require('../typed');

/*
 * The Rust SDK's generated surface (rust/src/generated, re-exported at the crate root):
 *   meta.rs       the spec version, hosts, stream URLs; the error codes (codes::*)
 *   enums.rs      open enums: the known values as variants plus Other(String) (a value from a newer API is kept as is)
 *   types.rs      every schema as a struct (inline objects named after their parent), wire names kept (snake_case),
 *                 unknown fields kept in `extra`, decimals as rust_decimal::Decimal (strings on the wire); the query
 *                 parameters of each list
 *   resources.rs  one resource per resource (client.orders()), one async method per operation and its _with
 *                 (RequestOptions) twin; a paged list also gets <method>_auto_paging, a Stream over every item
 * and rust/tests/common/dispatch.rs, the conformance runner's table.
 */
const HEAD = '// Generated from the API spec by generator/printers/rust.js. Do not edit: run `node generator`.\n';
const KEYWORDS = new Set(['as', 'break', 'const', 'continue', 'crate', 'else', 'enum', 'extern', 'false', 'fn', 'for', 'if', 'impl', 'in', 'let', 'loop', 'match', 'mod', 'move', 'mut', 'pub', 'ref', 'return', 'self', 'Self', 'static', 'struct', 'super', 'trait', 'true', 'type', 'unsafe', 'use', 'where', 'while', 'async', 'await', 'dyn', 'abstract', 'become', 'box', 'do', 'final', 'macro', 'override', 'priv', 'typeof', 'unsized', 'virtual', 'yield', 'try', 'gen']);
const words = (s) => String(s).replace(/([a-z0-9])([A-Z])/g, '$1_$2').split(/[_\s.-]+/).filter(Boolean);
const snake = (s) => words(s).map((w) => w.toLowerCase()).join('_');
const field = (s) => {
  const n = snake(s).replace(/^(\d)/, 'n$1');
  if (n === 'extra') return 'extra_field';
  return KEYWORDS.has(n) ? `r#${n}` : n;
};
const pascal = (s) => words(s).map((w) => w.charAt(0).toUpperCase() + w.slice(1)).join('').replace(/^(\d)/, 'V$1');
const upper = (s) => words(s).join('_').toUpperCase();
const resName = (r) => `${r === 'tradingview' ? 'TradingView' : pascal(r)}Resource`;
const doc = (text, indent) => {
  const t = String(text || '').replace(/\s+/g, ' ').trim();
  return t ? `${indent}/// ${t.replace(/\[/g, '\\[').replace(/\]/g, '\\]').replace(/</g, '\\<').replace(/>/g, '\\>')}\n` : '';
};
const str = (s) => JSON.stringify(s);

function rType(t, optional) {
  let base;
  switch (t.kind) {
    case 'string': base = 'String'; break;
    case 'int': base = 'i64'; break;
    case 'bool': base = 'bool'; break;
    case 'number': base = 'f64'; break;
    case 'decimal': case 'decimalIn': base = 'Decimal'; break;
    case 'enum': case 'ref': base = t.name; break;
    case 'array': base = `Vec<${rType(t.of, false)}>`; break;
    case 'map': base = 'Map<String, Value>'; break;
    default: return 'Option<Value>';
  }
  return t.nullable || optional ? `Option<${base}>` : base;
}

function print(ir) {
  const m = model(ir);
  const files = {};
  files['rust/src/generated/meta.rs'] = `${HEAD}
/// The version of the API spec this SDK was generated from.
pub const SPEC_VERSION: &str = ${str(ir.version)};

pub(crate) fn server(environment: &str) -> &'static str {
    match environment {
${Object.entries(ir.servers).map(([k, v]) => `        ${str(k)} => ${str(v)},`).join('\n')}
        _ => ${str(ir.servers.sandbox)},
    }
}

pub(crate) fn account_stream(environment: &str) -> &'static str {
    match environment {
${Object.entries(ir.streams.account).map(([k, v]) => `        ${str(k)} => ${str(v)},`).join('\n')}
        _ => ${str(ir.streams.account.sandbox)},
    }
}

pub(crate) const MARKET_STREAM: &str = ${str(ir.streams.market)};

/// The error codes the API answers (\`Error::code\`).
pub mod codes {
${ir.errors.map((e) => `    /// ${e.status} ${e.type}${e.retryable ? ', worth trying again' : ''}.\n    pub const ${upper(e.code)}: &str = ${str(e.code)};`).join('\n')}
}
`;

  files['rust/src/generated/enums.rs'] = `${HEAD}
use serde::{Deserialize, Deserializer, Serialize, Serializer};
${m.enums.map((e) => {
    const vs = e.values.map((v) => ({ v, n: pascal(v) === 'Other' ? 'OtherValue' : pascal(v) }));
    return `
${doc(`${e.description || e.name} Open: a value from a newer API version is kept in \`Other\`.`, '')}#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum ${e.name} {
${vs.map(({ v, n }) => `    /// \`${v}\`\n    ${n},`).join('\n')}
    /// A value this SDK version does not know yet.
    Other(String),
}

impl ${e.name} {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
${vs.map(({ v, n }) => `            Self::${n} => ${str(v)},`).join('\n')}
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for ${e.name} {
    fn from(s: &str) -> Self {
        match s {
${vs.map(({ v, n }) => `            ${str(v)} => Self::${n},`).join('\n')}
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for ${e.name} {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for ${e.name} {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for ${e.name} {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for ${e.name} {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}
`;
  }).join('')}`;

  const structs = m.structs.map((s) => {
    const fields = s.fields.map((f) => {
      const t = rType(f.type, !f.required);
      const opt = t.startsWith('Option<');
      const name = field(f.wire);
      const attrs = [];
      if (name.replace(/^r#/, '') !== f.wire) attrs.push(`rename = ${str(f.wire)}`);
      if (opt) attrs.push('skip_serializing_if = "Option::is_none"');
      return `${doc(f.description || `The ${f.wire.replace(/_/g, ' ')}.`, '    ')}${attrs.length ? `    #[serde(${attrs.join(', ')})]\n` : ''}    pub ${name}: ${t},`;
    });
    if (s.query) {
      const adds = s.fields.map((f) => {
        const n = field(f.wire);
        const v = f.type.kind === 'enum' ? 'v.as_str().to_string()' : 'v.to_string()';
        return `        if let Some(v) = &self.${n} {\n            q.push((${str(f.wire)}.to_string(), ${v}));\n        }`;
      });
      return `
${doc(s.description, '')}#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ${s.name} {
${fields.join('\n')}
}

impl ${s.name} {
    pub(crate) fn to_query(&self) -> Vec<(String, String)> {
        let mut q = Vec::new();
${adds.join('\n')}
        q
    }
}
`;
    }
    return `
${doc(s.description || s.name, '')}#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ${s.name} {
${fields.join('\n')}
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}
`;
  });
  files['rust/src/generated/types.rs'] = `${HEAD}
use super::enums::*;
use rust_decimal::Decimal;
use serde::{Deserialize, Serialize};
use serde_json::{Map, Value};
${structs.join('')}`;

  // resources
  const ops = ir.operations.map((o) => `pub(crate) const ${upper(o.id)}: Operation = Operation { id: ${str(o.id)}, method: ${str(o.http)}, path: ${str(o.path)}, idempotent: ${o.idempotent}, retry_safe: ${o.retrySafe}, cursor: ${o.paging ? `Some(${str(o.paging.param)})` : 'None'}, scope: ${str(o.scope)} };`);
  const resources = ir.resources.map((r) => {
    const cls = resName(r.name);
    const methods = [];
    for (const o of r.operations) {
      const name = snake(o.method);
      const body = o.body && typeName(o.body);
      const response = o.response ? typeName(o.response) : 'Value';
      const params = m.params[o.id];
      // the handle by value (it is Copy: a reference to the client): a call's future borrows nothing temporary, so
      // futures can be kept, joined and collected
      const args = ['self', ...o.pathParams.map((p) => `${field(p.name)}: &str`)];
      if (body) args.push(`body: ${o.bodyRequired ? '' : 'Option<'}&${body}${o.bodyRequired ? '' : '>'}`);
      if (params) args.push(`params: Option<&${params}>`);
      const names = [...o.pathParams.map((p) => field(p.name)), ...(body ? ['body'] : []), ...(params ? ['params'] : [])];
      const path = `&[${o.pathParams.map((p) => field(p.name)).join(', ')}]`;
      const q = params ? 'params.map(|p| p.to_query())' : 'None';
      const b = body ? (o.bodyRequired ? 'Some(body)' : 'body') : 'None::<&()>';
      const op = `&ops::${upper(o.id)}`;
      const ret = o.list ? `Page<${response}>` : response;
      const call = (opts) => (o.list ? `self.c.list(${op}, ${path}, ${q}, ${opts}).await` : `self.c.one(${op}, ${path}, ${q}, ${b}, ${opts}).await`);
      if (o.http === 'GET') {
        // a read given an If-None-Match may answer 304 Not Modified: its _with call answers None then
        methods.push(`${doc(`${o.summary}.`, '    ')}    pub async fn ${name}(${args.join(', ')}) -> Result<${ret}> {
        ${call('&RequestOptions::default()')}
    }

${doc(`${o.summary}, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).`, '    ')}    pub async fn ${name}_with(${[...args, 'options: &RequestOptions'].join(', ')}) -> Result<Option<${ret}>> {
        ${o.list ? `self.c.list_or_none(${op}, ${path}, ${q}, options).await` : call('options')}
    }`);
      } else {
        methods.push(`${doc(`${o.summary}.`, '    ')}    pub async fn ${name}(${args.join(', ')}) -> Result<${ret}> {
        self.${name}_with(${[...names, '&RequestOptions::default()'].join(', ')}).await
    }

${doc(`${o.summary}, with options.`, '    ')}    pub async fn ${name}_with(${[...args, 'options: &RequestOptions'].join(', ')}) -> Result<${ret}> {
        ${call('options')}
    }`);
      }
      if (o.list && o.paging) {
        // the stream keeps the client, not this resource: `client.orders().list_auto_paging(..)` may be bound with let
        methods.push(`${doc(`Every item of ${name}, page after page (RequestOptions::max_items caps it).`, '    ')}    pub fn ${name}_auto_paging<'s>(${[...args.map((a) => a.replace(/: &str$/, ": &'s str").replace(/: (Option<)?&(\w+)/, ": $1&'s $2")), "options: &'s RequestOptions"].join(', ')}) -> impl Stream<Item = Result<${response}>> + 's
    where
        'a: 's,
    {
        let c = self.c;
        auto_paging(async move { c.list(${op}, ${path}, ${q}, options).await }, options)
    }`);
      }
    }
    return `
/// The ${r.name} calls (\`client.${snake(r.name)}()\`).
#[derive(Clone, Copy)]
pub struct ${cls}<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> ${cls}<'a> {
${methods.join('\n\n')}
}
`;
  });
  files['rust/src/generated/resources.rs'] = `${HEAD}
use super::types::*;
use crate::client::{auto_paging, Client};
use crate::{Page, RequestOptions, Result};
use futures_util::Stream;
use serde_json::Value;

pub(crate) mod ops {
    use crate::client::Operation;

${ops.map((l) => `    ${l}`).join('\n')}

    /// Every operation, for the raw request (its route's retry-safety and budget).
    pub(crate) const ALL: &[&Operation] = &[${ir.operations.map((o) => `&${upper(o.id)}`).join(', ')}];
}

impl Client {
${ir.resources.map((r) => `    /// The ${r.name} calls.\n    pub fn ${snake(r.name)}(&self) -> ${resName(r.name)}<'_> {\n        ${resName(r.name)} { c: self }\n    }`).join('\n\n')}
}
${resources.join('')}`;

  // conformance dispatch
  const cases = ir.operations.map((o) => {
    const call = `c.${snake(o.resource)}().${snake(o.method)}`;
    const args = o.pathParams.map((p) => `&call.path(${str(p.name)})`);
    const pre = [];
    if (o.body) {
      pre.push(`let body: ${typeName(o.body)} = call.body();`);
      args.push(o.bodyRequired ? '&body' : 'Some(&body)');
    }
    const params = m.params[o.id];
    if (params) {
      pre.push(`let params: Option<${params}> = call.query();`);
      args.push('params.as_ref()');
    }
    const lines = [`        ${str(`${o.resource}.${o.method}`)} => {`, ...pre.map((l) => `            ${l}`)];
    if (o.list && o.paging) {
      lines.push('            if call.iterate {', '                let opts = RequestOptions { max_items: call.max_items, ..Default::default() };', `                return Ok(Answer::Items(ids(${call}_auto_paging(${[...args, '&opts'].join(', ')})).await?));`, '            }');
    }
    if (o.http === 'GET') {
      // through the _with call, so a 304 answers nothing (null)
      lines.push(`            let r = ${call}_with(${[...args, '&RequestOptions::default()'].join(', ')}).await?;`, `            Ok(Answer::Value(serde_json::to_value(${o.list ? 'r.map(|p| p.data)' : '&r'}).unwrap()))`, '        }');
    } else {
      lines.push(`            let r = ${call}(${args.join(', ')}).await?;`, `            Ok(Answer::Value(serde_json::to_value(${o.list ? '&r.data' : '&r'}).unwrap()))`, '        }');
    }
    return lines.join('\n');
  });
  files['rust/tests/common/dispatch.rs'] = `${HEAD}
use super::{ids, Answer, Call};
use phoenixperps::*;

/// Makes the call a conformance scenario names (resource + method) through the typed client.
pub async fn dispatch(c: &Client, call: &Call) -> Result<Answer> {
    match format!("{}.{}", call.resource, call.method).as_str() {
${cases.join('\n')}
        other => panic!("no operation {other}"),
    }
}
`;
  return files;
}

module.exports = { print, field, snake, pascal, resName };
