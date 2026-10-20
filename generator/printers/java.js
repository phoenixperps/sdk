'use strict';
const { model, typeName } = require('../typed');

/*
 * The Java SDK's generated surface (java/src/main/java/com/phoenixperpsfunding/sdk/gen, package ...sdk.gen):
 *   Meta.java          the spec version, hosts, stream URLs; ErrorCodes.java the error codes
 *   <Enum>.java        open enums: a final class over the wire string, its known values as constants (a value from a
 *                      newer API is kept as is)
 *   <Model>.java       every schema as a class: record-style getters (order.status()), fluent setters
 *                      (new CreateOrder().market("BTC-USD")), extra() for fields a newer API adds; decimals as BigDecimal
 *                      (strings on the wire); the query parameters of each list
 *   Resources.java     one resource class per resource, one method per operation (with and without RequestOptions); a
 *                      paged list also gets <method>AutoPaging, an Iterable over every item
 * and java/src/test/java/.../Dispatch.java, the conformance runner's table.
 */
const HEAD = '// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.\n';
const PKG = 'com.phoenixperpsfunding.sdk';
const DIR = 'java/src/main/java/com/phoenixperpsfunding/sdk/gen';
const KEYWORDS = new Set(['abstract', 'assert', 'boolean', 'break', 'byte', 'case', 'catch', 'char', 'class', 'const', 'continue', 'default', 'do', 'double', 'else', 'enum', 'extends', 'final', 'finally', 'float', 'for', 'goto', 'if', 'implements', 'import', 'instanceof', 'int', 'interface', 'long', 'native', 'new', 'package', 'private', 'protected', 'public', 'return', 'short', 'static', 'strictfp', 'super', 'switch', 'synchronized', 'this', 'throw', 'throws', 'transient', 'try', 'void', 'volatile', 'while', 'var', 'record', 'yield']);
const words = (s) => String(s).replace(/([a-z0-9])([A-Z])/g, '$1_$2').split(/[_\s.-]+/).filter(Boolean);
const camel = (s) => {
  const w = words(s);
  const c = w.map((x, i) => (i ? x.charAt(0).toUpperCase() + x.slice(1) : x.toLowerCase())).join('').replace(/^(\d)/, 'v$1');
  return KEYWORDS.has(c) || c === 'extra' || c === 'toString' || c === 'hashCode' || c === 'equals' || c === 'getClass' ? `${c}Value` : c;
};
const constant = (v) => {
  const c = words(v).join('_').toUpperCase().replace(/[^A-Z0-9_]/g, '_');
  return /^\d/.test(c) ? `V_${c}` : c;
};
const pascal = (s) => words(s).map((x) => x.charAt(0).toUpperCase() + x.slice(1)).join('');
const RESOURCE_NAMES = { tradingview: 'TradingView' };
const resClass = (r) => RESOURCE_NAMES[r] || pascal(r);
const resAccessor = (r) => (RESOURCE_NAMES[r] ? 'tradingView' : camel(r));
const esc = (s) => String(s || '').replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/\*\//g, '*&#47;').replace(/\s+/g, ' ').trim();
const doc = (text, indent) => (text ? `${indent}/** ${esc(text)} */\n` : '');
const str = (s) => JSON.stringify(s);

function jType(t) {
  switch (t.kind) {
    case 'string': return 'String';
    case 'int': return 'Long';
    case 'bool': return 'Boolean';
    case 'number': return 'Double';
    case 'decimal': case 'decimalIn': return 'BigDecimal';
    case 'enum': case 'ref': return t.name;
    case 'array': return `List<${jType(t.of)}>`;
    case 'map': return 'Map<String, Object>';
    default: return 'JsonNode';
  }
}

const IMPORTS = `import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
`;

function print(ir) {
  const m = model(ir);
  const files = {};
  files[`${DIR}/Meta.java`] = `${HEAD}package ${PKG}.gen;

import java.util.Map;

/** The spec this SDK was generated from: its version, hosts and stream URLs. */
public final class Meta {
    private Meta() {
    }

    /** The version of the API spec. */
    public static final String SPEC_VERSION = ${str(ir.version)};

    /** The API host of each environment. */
    public static final Map<String, String> SERVERS = Map.of(${Object.entries(ir.servers).map(([k, v]) => `${str(k)}, ${str(v)}`).join(', ')});

    /** The account stream URL of each environment. */
    public static final Map<String, String> ACCOUNT_STREAMS = Map.of(${Object.entries(ir.streams.account).map(([k, v]) => `${str(k)}, ${str(v)}`).join(', ')});

    /** The market stream URL. */
    public static final String MARKET_STREAM = ${str(ir.streams.market)};
}
`;
  files[`${DIR}/ErrorCodes.java`] = `${HEAD}package ${PKG}.gen;

/** The error codes the API answers (PhoenixPerpsException.code()). */
public final class ErrorCodes {
    private ErrorCodes() {
    }

${ir.errors.map((e) => `    /** ${e.status} ${esc(e.type)}${e.retryable ? ', worth trying again' : ''}. */\n    public static final String ${constant(e.code)} = ${str(e.code)};`).join('\n\n')}
}
`;

  for (const e of m.enums) {
    const consts = e.values.map((v) => `    /** ${esc(v)} */\n    public static final ${e.name} ${constant(v)} = new ${e.name}(${str(v)});`);
    files[`${DIR}/${e.name}.java`] = `${HEAD}package ${PKG}.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** ${esc(e.description || e.name)} Open: a value from a newer API version is kept as is. */
public final class ${e.name} {
${consts.join('\n\n')}

    private final String value;

    private ${e.name}(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static ${e.name} of(String value) {
        switch (value) {
${e.values.map((v) => `            case ${str(v)}:\n                return ${constant(v)};`).join('\n')}
            default:
                return new ${e.name}(value);
        }
    }

    /** The value as the API writes it. */
    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ${e.name} && ((${e.name}) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
`;
  }

  for (const s of m.structs) {
    const fields = s.fields.map((f) => `    @JsonProperty(${str(f.wire)})\n    private ${jType(f.type)} ${camel(f.wire)};`);
    const accessors = s.fields.map((f) => {
      const n = camel(f.wire);
      const t = jType(f.type);
      return `${doc(f.description || `The ${f.wire.replace(/_/g, ' ')}.`, '    ')}    public ${t} ${n}() {
        return ${n};
    }

    /** Sets ${esc(f.wire)}. */
    public ${s.name} ${n}(${t} value) {
        this.${n} = value;
        return this;
    }`;
    });
    let tail;
    if (s.query) {
      const adds = s.fields.map((f) => `        if (${camel(f.wire)} != null) {\n            q.put(${str(f.wire)}, ${f.type.kind === 'decimal' || f.type.kind === 'decimalIn' ? `${camel(f.wire)}.toPlainString()` : `String.valueOf(${camel(f.wire)})`});\n        }`);
      tail = `

    /** The query string parameters. */
    public Map<String, String> toQuery() {
        Map<String, String> q = new LinkedHashMap<>();
${adds.join('\n')}
        return q;
    }`;
    } else {
      tail = `

    @JsonIgnore
    private final Map<String, Object> extra = new LinkedHashMap<>();

    /** Fields this SDK version does not know yet (a newer API's), kept as they came. */
    @JsonAnyGetter
    public Map<String, Object> extra() {
        return extra;
    }

    @JsonAnySetter
    void putExtra(String key, Object value) {
        extra.put(key, value);
    }`;
    }
    const eq = s.fields.map((f) => `Objects.equals(${camel(f.wire)}, o.${camel(f.wire)})`);
    files[`${DIR}/${s.name}.java`] = `${HEAD}package ${PKG}.gen;

${IMPORTS}
${doc(s.description || s.name, '')}public final class ${s.name} {
${fields.join('\n\n')}

${accessors.join('\n\n')}${tail}

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ${s.name})) {
            return false;
        }
        ${s.name} o = (${s.name}) other;
        return ${eq.length ? eq.join('\n            && ') : 'true'};
    }

    @Override
    public int hashCode() {
        return Objects.hash(${s.fields.map((f) => camel(f.wire)).join(', ')});
    }

    @Override
    public String toString() {
        return "${s.name}{"${s.fields.map((f, i) => ` + "${i ? ', ' : ''}${f.wire}=" + ${camel(f.wire)}`).join('')} + "}";
    }
}
`;
  }

  // resources
  const ops = ir.operations.map((o) => `    static final Operation ${constant(o.id)} = new Operation(${str(o.id)}, ${str(o.http)}, ${str(o.path)}, ${o.idempotent}, ${o.retrySafe}, ${o.paging ? str(o.paging.param) : 'null'}, ${str(o.scope)});`);
  const classes = ir.resources.map((r) => {
    const cls = `${resClass(r.name)}Resource`;
    const methods = [];
    for (const o of r.operations) {
      const name = o.method;
      const body = o.body && typeName(o.body);
      const response = o.response ? typeName(o.response) : 'JsonNode';
      const params = m.params[o.id];
      const args = [...o.pathParams.map((p) => `String ${camel(p.name)}`)];
      if (body) args.push(`${body} body`);
      if (params) args.push(`${params} params`);
      const names = [...o.pathParams.map((p) => camel(p.name)), ...(body ? ['body'] : []), ...(params ? ['params'] : [])];
      const path = o.pathParams.length ? `new String[] {${o.pathParams.map((p) => camel(p.name)).join(', ')}}` : 'null';
      const q = params ? '(params == null ? null : params.toQuery())' : 'null';
      const b = body ? 'body' : 'null';
      const op = `Ops.${constant(o.id)}`;
      const ret = o.list ? `Page<${response}>` : response;
      const impl = o.list ? `c.list(${op}, ${path}, ${q}, ${response}.class, options)` : `c.one(${op}, ${path}, ${q}, ${b}, ${response}.class, options)`;
      methods.push(`${doc(`${o.summary}.`, '    ')}    public ${ret} ${name}(${[...args, 'RequestOptions options'].join(', ')}) {
        return ${impl};
    }

${doc(`${o.summary}.`, '    ')}    public ${ret} ${name}(${args.join(', ')}) {
        return ${name}(${[...names, 'null'].join(', ')});
    }`);
      if (o.list && o.paging) {
        methods.push(`${doc(`Every item of ${name}, page after page (RequestOptions.maxItems caps it).`, '    ')}    public Iterable<${response}> ${name}AutoPaging(${[...args, 'RequestOptions options'].join(', ')}) {
        return c.autoPaging(() -> ${name}(${[...names, 'options'].join(', ')}), options);
    }

${doc(`Every item of ${name}, page after page.`, '    ')}    public Iterable<${response}> ${name}AutoPaging(${args.join(', ')}) {
        return ${name}AutoPaging(${[...names, 'null'].join(', ')});
    }`);
      }
    }
    return `
    /** The ${esc(r.name)} calls (client.${resAccessor(r.name)}()). */
    public static final class ${cls} {
        private final Transport c;

        ${cls}(Transport c) {
            this.c = c;
        }

${methods.join('\n\n').replace(/^/gm, '    ').replace(/^ +$/gm, '')}
    }
`;
  });
  files[`${DIR}/Resources.java`] = `${HEAD}package ${PKG}.gen;

import ${PKG}.Page;
import ${PKG}.RequestOptions;
import ${PKG}.Transport;
import ${PKG}.Transport.Operation;
import com.fasterxml.jackson.databind.JsonNode;

/** The resources of a client, one per resource of the API. */
public final class Resources {
    private Resources() {
    }

    /** The operations of the API. */
    public static final class Ops {
        private Ops() {
        }

${ops.join('\n').replace(/^/gm, '    ')}

        /** Every operation, for the raw request (its route's retry-safety and budget). */
        public static final java.util.List<Operation> ALL = java.util.List.of(${ir.operations.map((o) => constant(o.id)).join(', ')});
    }

    /** Every resource of a client. */
    public static final class All {
${ir.resources.map((r) => `        /** The ${esc(r.name)} calls. */\n        public final ${resClass(r.name)}Resource ${resAccessor(r.name)};`).join('\n\n')}

        /** The resources over a transport. */
        public All(Transport c) {
${ir.resources.map((r) => `            ${resAccessor(r.name)} = new ${resClass(r.name)}Resource(c);`).join('\n')}
        }
    }

    /** The resources as methods of the client (client.orders()). */
    public interface Access {
        /** Every resource. */
        All resources();
${ir.resources.map((r) => `
        /** The ${esc(r.name)} calls. */
        default ${resClass(r.name)}Resource ${resAccessor(r.name)}() {
            return resources().${resAccessor(r.name)};
        }`).join('\n')}
    }
${classes.join('')}}
`;

  // conformance dispatch
  const cases = ir.operations.map((o) => {
    const call = `c.${resAccessor(o.resource)}().${o.method}`;
    const args = o.pathParams.map((p) => `call.path(${str(p.name)})`);
    const lines = [`            case ${str(`${o.resource}.${o.method}`)}: {`];
    if (o.body) args.push(`call.body(${typeName(o.body)}.class)`);
    const params = m.params[o.id];
    if (params) args.push(`call.query(${params}.class)`);
    if (o.list && o.paging) lines.push(`                if (call.iterate()) {\n                    return Result.items(ids(${call}AutoPaging(${[...args, 'options.withMaxItems(call.maxItems())'].join(', ')})));\n                }`);
    lines.push(`                return Result.of(${call}(${[...args, 'options'].join(', ')}));`, '            }');
    return lines.join('\n');
  });
  files['java/src/test/java/com/phoenixperpsfunding/sdk/Dispatch.java'] = `${HEAD}package ${PKG};

import ${PKG}.gen.*;

/** Makes the call a conformance scenario names (resource + method) through the typed client. */
final class Dispatch {
    private Dispatch() {
    }

    static Result run(PhoenixPerpsClient c, Call call, RequestOptions options) {
        switch (call.resource() + "." + call.method()) {
${cases.join('\n')}
            default:
                throw new IllegalArgumentException("no operation " + call.resource() + "." + call.method());
        }
    }

    static java.util.List<String> ids(Iterable<?> items) {
        java.util.List<String> ids = new java.util.ArrayList<>();
        for (Object item : items) {
            ids.add(Json.MAPPER.valueToTree(item).path("id").asText());
        }
        return ids;
    }
}
`;
  return files;
}

module.exports = { print, camel, constant, resAccessor };
