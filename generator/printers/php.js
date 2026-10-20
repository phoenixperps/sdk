'use strict';
const { model, typeName } = require('../typed');

/*
 * The PHP SDK's generated surface (php/src, namespace PhoenixPerps):
 *   Meta.php, ErrorCodes.php   the spec version, hosts, stream URLs; the error codes
 *   Enum/<Name>.php            open enums: the known values as constants (a value from a newer API is kept as is: the
 *                              fields are strings)
 *   Model/<Name>.php           every schema as a final class with readonly promoted properties (camelCase, named
 *                              arguments), fromArray / toArray (wire names), $extra for fields a newer API adds; decimals
 *                              are strings; the query parameters of each list
 *   Resource/<Name>Resource.php, BaseClient.php   one resource per resource ($client->orders), one method per
 *                              operation; a paged list also gets <method>AutoPaging, a Generator over every item
 * and php/tests/Generated/Dispatch.php, the conformance runner's table.
 */
const HEAD = '<?php\n\n// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.\n\ndeclare(strict_types=1);\n';
const words = (s) => String(s).replace(/([a-z0-9])([A-Z])/g, '$1_$2').split(/[_\s.-]+/).filter(Boolean);
const camel = (s) => {
  const c = words(s).map((w, i) => (i ? w.charAt(0).toUpperCase() + w.slice(1) : w.toLowerCase())).join('').replace(/^(\d)/, 'n$1');
  return c === 'extra' ? 'extraField' : c;
};
const pascal = (s) => words(s).map((w) => w.charAt(0).toUpperCase() + w.slice(1)).join('');
const constant = (v) => {
  const c = words(v).join('_').toUpperCase().replace(/[^A-Z0-9_]/g, '_');
  return /^\d/.test(c) ? `V_${c}` : c;
};
const RESOURCE_NAMES = { tradingview: 'TradingView' };
const resClass = (r) => `${RESOURCE_NAMES[r] || pascal(r)}Resource`;
const resProp = (r) => (RESOURCE_NAMES[r] ? 'tradingView' : camel(r));
const q = (s) => `'${String(s).replace(/\\/g, '\\\\').replace(/'/g, "\\'")}'`;
const doc = (text, indent) => {
  const t = String(text || '').replace(/\s+/g, ' ').replace(/\*\//g, '* /').trim();
  return t ? `${indent}/** ${t} */\n` : '';
};
const phpArray = (obj, indent) => `[\n${Object.entries(obj).map(([k, v]) => `${indent}    ${q(k)} => ${typeof v === 'string' ? q(v) : v === null ? 'null' : typeof v === 'boolean' ? String(v) : typeof v === 'number' ? String(v) : phpArray(v, `${indent}    `)},`).join('\n')}\n${indent}]`;

function phpType(t) {
  switch (t.kind) {
    case 'string': case 'decimal': case 'enum': return '?string';
    case 'decimalIn': return 'string|int|float|null';
    case 'int': return '?int';
    case 'bool': return '?bool';
    case 'number': return '?float';
    case 'ref': return `?${t.name}`;
    case 'array': case 'map': return '?array';
    default: return 'mixed';
  }
}
const docType = (t) => {
  if (t.kind === 'array') return `list<${t.of.kind === 'ref' ? t.of.name : t.of.kind === 'int' ? 'int' : t.of.kind === 'bool' ? 'bool' : t.of.kind === 'map' ? 'array<string, mixed>' : 'string'}>|null`;
  if (t.kind === 'map') return 'array<string, mixed>|null';
  return null;
};
const fromExpr = (t, v) => {
  if (t.kind === 'ref') return `isset(${v}) && is_array(${v}) ? ${t.name}::fromArray(${v}) : null`;
  if (t.kind === 'array' && t.of.kind === 'ref') return `isset(${v}) && is_array(${v}) ? array_map(fn ($x) => ${t.of.name}::fromArray($x), ${v}) : null`;
  if (t.kind === 'decimal') return `isset(${v}) ? (string) ${v} : null`;
  return `${v} ?? null`;
};
const toExpr = (t, v) => {
  if (t.kind === 'ref') return `${v}->toArray()`;
  if (t.kind === 'array' && t.of.kind === 'ref') return `array_map(fn ($x) => $x->toArray(), ${v})`;
  if (t.kind === 'decimalIn') return `Decimal::of(${v})`;
  return v;
};

function print(ir) {
  const m = model(ir);
  const files = {};
  files['php/src/Meta.php'] = `${HEAD}
namespace PhoenixPerps;

/** The spec this SDK was generated from: its version, hosts and stream URLs. */
final class Meta
{
    /** The version of the API spec. */
    public const SPEC_VERSION = ${q(ir.version)};

    /** The API host of each environment. */
    public const SERVERS = ${phpArray(ir.servers, '    ')};

    /** The account stream URL of each environment. */
    public const ACCOUNT_STREAMS = ${phpArray(ir.streams.account, '    ')};

    /** The market stream URL. */
    public const MARKET_STREAM = ${q(ir.streams.market)};
}
`;
  files['php/src/ErrorCodes.php'] = `${HEAD}
namespace PhoenixPerps;

/** The error codes the API answers (PhoenixPerpsException::$code). */
final class ErrorCodes
{
${ir.errors.map((e) => `    /** ${e.status} ${e.type}${e.retryable ? ', worth trying again' : ''}. */\n    public const ${constant(e.code)} = ${q(e.code)};`).join('\n\n')}
}
`;
  for (const e of m.enums) {
    files[`php/src/Enum/${e.name}.php`] = `${HEAD}
namespace PhoenixPerps\\Enum;

${doc(`${e.description || e.name} Open: the field is a string, and a value from a newer API version is kept as is.`, '')}final class ${e.name}
{
${e.values.map((v) => `    public const ${constant(v)} = ${q(v)};`).join('\n')}

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [${e.values.map((v) => `self::${constant(v)}`).join(', ')}];
}
`;
  }
  for (const s of m.structs) {
    const params = s.fields.map((f) => {
      const dt = docType(f.type);
      return `${doc(f.description || `The ${f.wire.replace(/_/g, ' ')}.`, '        ')}${dt ? `        /** @var ${dt} */\n` : ''}        public readonly ${phpType(f.type)} $${camel(f.wire)} = null,`;
    });
    const wires = s.fields.map((f) => `${q(f.wire)} => true`).join(', ');
    const from = s.fields.map((f) => `            ${camel(f.wire)}: ${fromExpr(f.type, `$d[${q(f.wire)}]`)},`);
    const to = s.fields.map((f) => `        if ($this->${camel(f.wire)} !== null) {\n            $out[${q(f.wire)}] = ${toExpr(f.type, `$this->${camel(f.wire)}`)};\n        }`);
    const extra = s.query ? '' : `
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],`;
    const queryMethod = s.query ? `

    /** @return array<string, string> the query string parameters */
    public function toQuery(): array
    {
        $out = [];
        foreach ($this->toArray() as $k => $v) {
            $out[$k] = is_bool($v) ? ($v ? 'true' : 'false') : (string) $v;
        }
        return $out;
    }` : '';
    files[`php/src/Model/${s.name}.php`] = `${HEAD}
namespace PhoenixPerps\\Model;

use PhoenixPerps\\Decimal;

${doc(s.description || s.name, '')}final class ${s.name} implements \\JsonSerializable
{
    private const WIRE = [${wires}];

    public function __construct(
${params.join('\n')}${extra}
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
${from.join('\n')}${s.query ? '' : '\n            extra: array_diff_key($d, self::WIRE),'}
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
${to.join('\n')}
${s.query ? '        return $out;' : '        return $out + $this->extra;'}
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }${queryMethod}
}
`.replace(/private const WIRE = \[\];/, 'private const WIRE = [];');
  }

  // resources
  files['php/src/Ops.php'] = `${HEAD}
namespace PhoenixPerps;

/** The operations of the API. */
final class Ops
{
${ir.operations.map((o) => `    public const ${constant(o.id)} = ['id' => ${q(o.id)}, 'method' => ${q(o.http)}, 'path' => ${q(o.path)}, 'idempotent' => ${o.idempotent}, 'retrySafe' => ${o.retrySafe}, 'cursor' => ${o.paging ? q(o.paging.param) : 'null'}, 'scope' => ${q(o.scope)}];`).join('\n')}
}
`;
  for (const r of ir.resources) {
    const cls = resClass(r.name);
    const uses = new Set();
    const methods = [];
    for (const o of r.operations) {
      const body = o.body && typeName(o.body);
      const response = o.response && typeName(o.response);
      const params = m.params[o.id];
      if (body) uses.add(body);
      if (response) uses.add(response);
      if (params) uses.add(params);
      const args = o.pathParams.map((p) => `string $${camel(p.name)}`);
      if (body) args.push(`${body}|array${o.bodyRequired ? '' : '|null'} $body${o.bodyRequired ? '' : ' = null'}`);
      if (params) args.push(`${params}|array|null $params = null`);
      args.push('?RequestOptions $options = null');
      const path = `[${o.pathParams.map((p) => `$${camel(p.name)}`).join(', ')}]`;
      const query = params ? '$params' : 'null';
      const op = `Ops::${constant(o.id)}`;
      // a read given an If-None-Match may answer 304 Not Modified: it answers null then
      const read = o.http === 'GET';
      const one = `$this->c->one(${op}, ${path}, ${query}, ${body ? '$body' : 'null'}, $options)`;
      if (o.list) {
        methods.push(`${doc(`${o.summary}.`, '    ')}    /** @return Page<${response}>|null null on a 304 Not Modified */
    public function ${o.method}(${args.join(', ')}): ?Page
    {
        return $this->c->list(${op}, ${path}, ${query}, [${response}::class, 'fromArray'], $options);
    }`);
        if (o.paging) {
          const call = [...o.pathParams.map((p) => `$${camel(p.name)}`), ...(params ? ['$params'] : []), '$options'].join(', ');
          methods.push(`${doc(`Every item of ${o.method}, page after page (RequestOptions::$maxItems caps it).`, '    ')}    /** @return \\Generator<int, ${response}> */
    public function ${o.method}AutoPaging(${args.join(', ')}): \\Generator
    {
        return $this->${o.method}(${call})?->autoPagingIterator($options?->maxItems) ?? (static fn (): \\Generator => yield from [])();
    }`);
        }
      } else if (response && read) {
        methods.push(`${doc(`${o.summary}.`, '    ')}    /** @return ${response}|null null on a 304 Not Modified */
    public function ${o.method}(${args.join(', ')}): ?${response}
    {
        $d = ${one};
        return $d === null ? null : ${response}::fromArray($d);
    }`);
      } else if (response) {
        methods.push(`${doc(`${o.summary}.`, '    ')}    public function ${o.method}(${args.join(', ')}): ${response}
    {
        return ${response}::fromArray(${one} ?? []);
    }`);
      } else {
        methods.push(`${doc(`${o.summary}.`, '    ')}    /** @return array<string, mixed>${read ? '|null null on a 304 Not Modified' : ''} */
    public function ${o.method}(${args.join(', ')}): ${read ? '?array' : 'array'}
    {
        return ${one}${read ? '' : ' ?? []'};
    }`);
      }
    }
    files[`php/src/Resource/${cls}.php`] = `${HEAD}
namespace PhoenixPerps\\Resource;

use PhoenixPerps\\Client;
${[...uses].sort().map((u) => `use PhoenixPerps\\Model\\${u};`).join('\n')}
use PhoenixPerps\\Ops;
use PhoenixPerps\\Page;
use PhoenixPerps\\RequestOptions;

/** The ${r.name} calls ($client->${resProp(r.name)}). */
final class ${cls}
{
    public function __construct(private readonly Client $c)
    {
    }

${methods.join('\n\n')}
}
`;
  }
  files['php/src/BaseClient.php'] = `${HEAD}
namespace PhoenixPerps;

${ir.resources.map((r) => `use PhoenixPerps\\Resource\\${resClass(r.name)};`).join('\n')}

/** The resources of a client, one per resource of the API. */
abstract class BaseClient
{
${ir.resources.map((r) => `    /** The ${r.name} calls. */\n    public readonly ${resClass(r.name)} $${resProp(r.name)};`).join('\n\n')}

    protected function initResources(Client $c): void
    {
${ir.resources.map((r) => `        $this->${resProp(r.name)} = new ${resClass(r.name)}($c);`).join('\n')}
    }
}
`;

  // conformance dispatch
  const cases = ir.operations.map((o) => {
    const call = `$c->${resProp(o.resource)}->${o.method}`;
    const args = o.pathParams.map((p) => `$call['path'][${q(p.name)}]`);
    if (o.body) args.push(`$call['body'] ?? ${o.bodyRequired ? '[]' : 'null'}`);
    if (m.params[o.id]) args.push(`$call['query'] ?? null`);
    const lines = [`            case ${q(`${o.resource}.${o.method}`)}:`];
    if (o.list && o.paging) {
      lines.push(`                if (!empty($call['iterate'])) {`, `                    return ['items' => self::ids(${call}AutoPaging(${[...args, '$options'].join(', ')}))];`, '                }');
    }
    if (o.list) lines.push(`                $page = ${call}(${[...args, '$options'].join(', ')});`, `                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];`);
    else if (o.response) lines.push(`                return ['result' => ${call}(${[...args, '$options'].join(', ')})${o.http === 'GET' ? '?->' : '->'}toArray()];`);
    else lines.push(`                return ['result' => ${call}(${[...args, '$options'].join(', ')})];`);
    return lines.join('\n');
  });
  files['php/tests/Generated/Dispatch.php'] = `${HEAD}
namespace PhoenixPerps\\Tests\\Generated;

use PhoenixPerps\\Client;
use PhoenixPerps\\RequestOptions;

/** Makes the call a conformance scenario names (resource + method) through the typed client. */
final class Dispatch
{
    /**
     * @param array<string, mixed> $call
     * @return array{result?: mixed, items?: list<string>}
     */
    public static function run(Client $c, array $call, RequestOptions $options): array
    {
        switch ($call['resource'] . '.' . $call['method']) {
${cases.join('\n')}
        }
        throw new \\InvalidArgumentException('no operation ' . $call['resource'] . '.' . $call['method']);
    }

    /** @return list<string> */
    private static function ids(\\Generator $items): array
    {
        $ids = [];
        foreach ($items as $item) {
            $ids[] = (string) ($item->toArray()['id'] ?? '');
        }
        return $ids;
    }
}
`;
  return files;
}

module.exports = { print, camel, resProp };
