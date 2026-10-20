'use strict';

/*
 * The Python SDK's generated surface (python/src/phoenixperps/_gen.py and types.py): the hosts, streams, spec version
 * and error codes; one class per resource in a sync and an async flavour, a method per operation (snake_case) typed with
 * what it answers; every schema as a TypedDict of phoenixperps.types (decimals are str: the API sends them as strings;
 * inputs also take int, float or Decimal).
 */
const HEAD = '# Generated from the API spec by generator/printers/python.js. Do not edit: run `node generator`.\n';
const snake = (s) => s.replace(/([a-z0-9])([A-Z])/g, '$1_$2').toLowerCase();
const pascal = (s) => s.charAt(0).toUpperCase() + s.slice(1).replace(/_([a-z])/g, (m, c) => c.toUpperCase());
const KEYWORDS = new Set(['from', 'class', 'def', 'return', 'import', 'global', 'lambda', 'type', 'id']);
const arg = (name) => (KEYWORDS.has(name) ? `${name}_` : name);
// what a TypedDict field cannot be named (Python's own keywords); id and type are fine there, they shadow nothing
const PY_KEYWORDS = new Set(['False', 'None', 'True', 'and', 'as', 'assert', 'async', 'await', 'break', 'class', 'continue', 'def', 'del', 'elif', 'else',
  'except', 'finally', 'for', 'from', 'global', 'if', 'import', 'in', 'is', 'lambda', 'nonlocal', 'not', 'or', 'pass', 'raise', 'return', 'try', 'while', 'with', 'yield']);
const pyDoc = (text, indent) => (text ? `${indent}"""${String(text).replace(/"""/g, '\'\'\'').replace(/\\/g, '\\\\').replace(/\s+/g, ' ').trim()}"""\n` : '');
const pyStr = (v) => JSON.stringify(v);
const pyLiteral = (v) => JSON.stringify(v).replace(/\btrue\b/g, 'True').replace(/\bfalse\b/g, 'False').replace(/\bnull\b/g, 'None');

function pyType(schema) {
  if (!schema) return 'Any';
  if (schema.$ref) return `"${schema.$ref.split('/').pop()}"`;
  const types = Array.isArray(schema.type) ? schema.type : schema.type ? [schema.type] : [];
  const nullable = types.includes('null');
  const base = types.filter((t) => t !== 'null');
  let t;
  if (schema.enum) t = 'str';
  else if (base.includes('string') && base.includes('number')) t = 'Union[str, int, float, Decimal]';
  else if (base[0] === 'string') t = 'str';
  else if (base[0] === 'integer') t = 'int';
  else if (base[0] === 'number') t = 'float';
  else if (base[0] === 'boolean') t = 'bool';
  else if (base[0] === 'array') t = `List[${pyType(schema.items)}]`;
  else t = 'Dict[str, Any]';
  return nullable ? `Optional[${t}]` : t;
}

function print(ir) {
  const files = {};
  const ops = ir.operations.map((o) => `    ${pyStr(o.id)}: ${pyLiteral({ id: o.id, http: o.http, path: o.path, idempotent: o.idempotent, retry_safe: o.retrySafe, scope: o.scope, paging: o.paging })},`);
  const typedNames = new Set(Object.entries(ir.schemas).filter(([, s]) => s.properties).map(([name]) => name));
  const classes = [];
  for (const asyncFlavour of [false, true]) {
    for (const r of ir.resources) {
      const name = `${asyncFlavour ? 'Async' : ''}${pascal(r.name)}Resource`;
      const methods = r.operations.map((o) => {
        // a body is any mapping in the API's words: a plain dict, or the TypedDict of phoenixperps.types
        const params = ['self', ...o.pathParams.map((p) => `${arg(p.name)}: str`), ...(o.body ? [`body: ${o.bodyRequired ? '' : 'Optional['}Mapping[str, Any]${o.bodyRequired ? '' : '] = None'}`] : []), ...(o.query.length ? ['query: Optional[Dict[str, Any]] = None'] : []), '**opts: Any'];
        const call = [`OPS[${pyStr(o.id)}]`, o.pathParams.length ? `path={${o.pathParams.map((p) => `${pyStr(p.name)}: ${arg(p.name)}`).join(', ')}}` : null, o.body ? 'body=body' : null, o.query.length ? 'query=query' : null, 'opts=opts'].filter(Boolean).join(', ');
        const kind = o.list ? '_page' : '_one';
        // the answer's TypedDict (phoenixperps.types); an async list answers its call (awaited: the AsyncPage; async for:
        // every item). None comes only on a 304 Not Modified, to an If-None-Match the caller put in headers: typed as
        // the answer, as every SDK types it (the plain call sends none)
        const item = o.response && typedNames.has(o.response) ? `types.${o.response}` : 'Any';
        const ret = o.list ? (asyncFlavour ? `_AsyncPageCall[${item}]` : `Page[${item}]`) : item;
        if (asyncFlavour && !o.list) return `    async def ${snake(o.method)}(${params.join(', ')}) -> ${ret}:\n${pyDoc(o.summary, '        ')}        return await self._c.${kind}(${call})\n`;
        return `    def ${snake(o.method)}(${params.join(', ')}) -> ${ret}:\n${pyDoc(o.summary, '        ')}        return self._c.${kind}(${call})\n`;
      });
      classes.push(`class ${name}:\n    def __init__(self, client: Any) -> None:\n        self._c = client\n\n${methods.join('\n')}`);
    }
  }
  const registry = ir.resources.map((r) => `    ${pyStr(snake(r.name))}: (${pascal(r.name)}Resource, Async${pascal(r.name)}Resource),`);
  // the clients' resources as type checkers see them (each client sets them at run time, from RESOURCES)
  const declared = (prefix) => ir.resources.map((r) => `    ${snake(r.name)}: ${prefix}${pascal(r.name)}Resource`).join('\n');
  files['python/src/phoenixperps/_gen.py'] = `${HEAD}from __future__ import annotations

from typing import TYPE_CHECKING, Any, Dict, Mapping, Optional

if TYPE_CHECKING:
    from . import types
    from ._core import Page, _AsyncPageCall

SPEC_VERSION = ${pyStr(ir.version)}
SERVERS = ${pyLiteral(ir.servers)}
STREAMS: Dict[str, Any] = ${pyLiteral(ir.streams)}
ERROR_CODES = ${pyLiteral(Object.fromEntries(ir.errors.map((e) => [e.code, { type: e.type, status: e.status, retryable: e.retryable }])))}

OPS: Dict[str, Dict[str, Any]] = {
${ops.join('\n')}
}


${classes.join('\n\n')}

RESOURCES = {
${registry.join('\n')}
}


class Resources:
${declared('')}


class AsyncResources:
${declared('Async')}
`;
  const typed = Object.entries(ir.schemas).filter(([, s]) => s.properties).map(([name, s]) => {
    const fields = Object.entries(s.properties).filter(([k]) => /^[a-z_][a-z0-9_]*$/i.test(k) && !PY_KEYWORDS.has(k)).map(([k, v]) => `    ${k}: ${pyType(v)}`);
    return `class ${name}(TypedDict, total=False):\n${pyDoc(s.description, '    ')}${fields.length ? fields.join('\n') : '    pass'}\n`;
  });
  // public: from phoenixperps.types import Order (what each call answers, and the bodies it takes)
  files['python/src/phoenixperps/types.py'] = `${HEAD}from __future__ import annotations

from decimal import Decimal
from typing import Any, Dict, List, Optional, TypedDict, Union

__all__ = [${[...typedNames].map(pyStr).join(', ')}]


${typed.join('\n\n')}`;
  return files;
}

module.exports = { print, snake };
