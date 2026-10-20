'use strict';
const { model, typeName } = require('../typed');
const { snake } = require('./python');
const GO = require('./go');
const CS = require('./csharp');
const JAVA = require('./java');
const RUST = require('./rust');
const PHP = require('./php');

/*
 * The call of every operation in the seven SDKs (docs/api/design/14-sdks.md section 11: every reference page shows
 * curl and the SDKs), sending the spec's own examples (path parameters and request body):
 *   snippets/snippets.json   { operationId: [{ lang, source }] } for the docs (curl is the docs' own)
 * and, so a snippet can never drift from its SDK, the same calls as code each SDK's checks compile:
 *   typescript/test/snippets.ts (tsc), python/tests/snippets_check.py (compiled), go/snippets_test.go (go vet),
 *   csharp/tests/.../Generated/Snippets.cs, java/src/test/.../Snippets.java, rust/tests/snippets.rs,
 *   php/tests/Generated/Snippets.php (php -l)
 * A body is the typed request in each language (a struct, an object initializer, fluent setters); PHP and the dynamic
 * languages send it as the API writes it.
 */
const LANGS = ['python', 'typescript', 'go', 'csharp', 'java', 'rust', 'php'];
const SHOWN = ['id', 'market', 'account_id', 'status', 'name'];
// the C# words a variable cannot be named (written @word)
const CS_KEYWORDS = new Set(['base', 'bool', 'byte', 'char', 'checked', 'class', 'decimal', 'default', 'delegate', 'double', 'event', 'fixed', 'float', 'in', 'int', 'internal', 'is', 'lock', 'long', 'namespace', 'new', 'null', 'object', 'operator', 'out', 'override', 'params', 'private', 'public', 'readonly', 'ref', 'return', 'short', 'static', 'string', 'struct', 'switch', 'this', 'throw', 'typeof', 'using', 'virtual', 'void', 'while']);
const lowerFirst = (s) => s.charAt(0).toLowerCase() + s.slice(1);
const indentAll = (text, pad) => text.split('\n').map((l) => (l ? pad + l : l)).join('\n');
const jsKey = (k) => (/^[A-Za-z_$][\w$]*$/.test(k) ? k : `'${k}'`);
const sq = (s) => `'${String(s).replace(/\\/g, '\\\\').replace(/'/g, "\\'")}'`;
const dq = (s) => JSON.stringify(String(s));

function print(ir) {
  const m = model(ir);
  const struct = (name) => m.structs.find((s) => s.name === name);
  const files = {};
  const out = {};

  // values in each language, typed by the model where the language is typed
  const js = (v, pad = '') => {
    if (Array.isArray(v)) return v.length ? `[\n${v.map((x) => `${pad}  ${js(x, `${pad}  `)},`).join('\n')}\n${pad}]` : '[]';
    if (v && typeof v === 'object') return `{\n${Object.entries(v).map(([k, x]) => `${pad}  ${jsKey(k)}: ${js(x, `${pad}  `)},`).join('\n')}\n${pad}}`;
    return typeof v === 'string' ? sq(v) : String(v);
  };
  const py = (v, pad = '') => {
    if (Array.isArray(v)) return v.length ? `[\n${v.map((x) => `${pad}    ${py(x, `${pad}    `)},`).join('\n')}\n${pad}]` : '[]';
    if (v && typeof v === 'object') return `{\n${Object.entries(v).map(([k, x]) => `${pad}    ${dq(k)}: ${py(x, `${pad}    `)},`).join('\n')}\n${pad}}`;
    if (v === true) return 'True';
    if (v === false) return 'False';
    if (v === null) return 'None';
    return typeof v === 'string' ? dq(v) : String(v);
  };
  const php = (v, pad = '') => {
    if (Array.isArray(v)) return v.length ? `[\n${v.map((x) => `${pad}    ${php(x, `${pad}    `)},`).join('\n')}\n${pad}]` : '[]';
    if (v && typeof v === 'object') return `[\n${Object.entries(v).map(([k, x]) => `${pad}    ${sq(k)} => ${php(x, `${pad}    `)},`).join('\n')}\n${pad}]`;
    if (v === null) return 'null';
    return typeof v === 'string' ? sq(v) : String(v);
  };

  // Go: a composite literal, in gofmt's layout (key: value pairs of consecutive lines aligned)
  const goVal = (t, v, optional, pad) => {
    const ptr = (t.nullable || optional);
    const P = (x) => `phoenixperps.Ptr(${x})`;
    switch (t.kind) {
      case 'string': return ptr ? P(dq(v)) : dq(v);
      case 'int': return ptr ? P(`int64(${v})`) : String(v);
      case 'bool': return ptr ? P(String(v)) : String(v);
      case 'number': return ptr ? P(`float64(${v})`) : String(v);
      case 'decimal': case 'decimalIn': return ptr ? `phoenixperps.Dec(${dq(v)})` : dq(v);
      case 'enum': { const c = `phoenixperps.${t.name}${GO.goName(v)}`; return ptr ? P(c) : c; }
      case 'ref': return `${ptr ? '&' : ''}phoenixperps.${t.name}${goStruct(t.name, v, pad)}`;
      case 'array': {
        if (t.of.kind === 'ref') return `[]phoenixperps.${t.of.name}{\n${v.map((x) => `${pad}\t${goStruct(t.of.name, x, `${pad}\t`)},`).join('\n')}\n${pad}}`;
        return `[]${t.of.kind === 'int' ? 'int64' : 'string'}{${v.map((x) => (typeof x === 'string' ? dq(x) : String(x))).join(', ')}}`;
      }
      default: return 'nil';
    }
  };
  const goStruct = (name, v, pad) => {
    const s = struct(name);
    const rows = [];
    for (const [k, x] of Object.entries(v)) {
      const f = s.fields.find((y) => y.wire === k);
      const value = goVal(f.type, x, !f.required, `${pad}\t`);
      if (value.includes('\n')) rows.push(`${pad}\t${GO.goName(k)}: ${value},`); // a multi-line value ends an aligned run
      else rows.push(GO.row(`${pad}\t`, `${GO.goName(k)}:`, `${value},`));
    }
    return `{\n${GO.aligned(rows)}\n${pad}}`;
  };

  // C#: an object initializer
  const csEnum = (t, v) => {
    let n = CS.pascal(v);
    if (n === t.name || n === 'Value') n = `${n}Value`;
    return `${t.name}.${n}`;
  };
  const csVal = (t, v, pad) => {
    switch (t.kind) {
      case 'string': return dq(v);
      case 'int': case 'number': return String(v);
      case 'bool': return String(v);
      case 'decimal': case 'decimalIn': return `${v}m`;
      case 'enum': return csEnum(t, v);
      case 'ref': return csNew(t.name, v, pad);
      case 'array': return `new()\n${pad}{\n${v.map((x) => `${pad}    ${csVal(t.of, x, `${pad}    `)},`).join('\n')}\n${pad}}`;
      default: return 'null';
    }
  };
  const csNew = (name, v, pad) => {
    const s = struct(name);
    return `new ${name}\n${pad}{\n${Object.entries(v).map(([k, x]) => {
      const f = s.fields.find((y) => y.wire === k);
      return `${pad}    ${CS.propName(name, k)} = ${csVal(f.type, x, `${pad}    `)},`;
    }).join('\n')}\n${pad}}`;
  };

  // Java: fluent setters
  const javaVal = (t, v, pad) => {
    switch (t.kind) {
      case 'string': return dq(v);
      case 'int': return `${v}L`;
      case 'number': return `${v}d`;
      case 'bool': return String(v);
      case 'decimal': case 'decimalIn': return `new BigDecimal(${dq(v)})`;
      case 'enum': return `${t.name}.${JAVA.constant(v)}`;
      case 'ref': return javaNew(t.name, v, pad);
      case 'array': return `List.of(${v.map((x) => javaVal(t.of, x, pad)).join(', ')})`;
      default: return 'null';
    }
  };
  const javaNew = (name, v, pad) => {
    const s = struct(name);
    const calls = Object.entries(v).map(([k, x]) => `${pad}        .${JAVA.camel(k)}(${javaVal(s.fields.find((y) => y.wire === k).type, x, `${pad}        `)})`);
    return `new ${name}()${calls.length ? `\n${calls.join('\n')}` : ''}`;
  };

  // Rust: a struct literal, the rest Default
  const rustEnum = (t, v) => {
    const n = RUST.pascal(v);
    return `${t.name}::${n === 'Other' ? 'OtherValue' : n}`;
  };
  const rustVal = (t, v, optional, pad) => {
    const wrap = (x) => (t.nullable || optional ? `Some(${x})` : x);
    switch (t.kind) {
      case 'string': return wrap(`${dq(v)}.into()`);
      case 'int': case 'number': return wrap(String(v));
      case 'bool': return wrap(String(v));
      case 'decimal': case 'decimalIn': return wrap(`dec(${dq(v)})`);
      case 'enum': return wrap(rustEnum(t, v));
      case 'ref': return wrap(rustStruct(t.name, v, pad));
      case 'array': return wrap(`vec![${v.map((x) => rustVal(t.of, x, false, pad)).join(', ')}]`);
      default: return 'None';
    }
  };
  const rustStruct = (name, v, pad) => {
    const s = struct(name);
    const lines = Object.entries(v).map(([k, x]) => {
      const f = s.fields.find((y) => y.wire === k);
      return `${pad}    ${RUST.field(k)}: ${rustVal(f.type, x, !f.required, `${pad}    `)},`;
    });
    return `${name} {\n${lines.join('\n')}\n${pad}    ..Default::default()\n${pad}}`;
  };

  const compile = { typescript: [], python: [], go: [], csharp: [], java: [], rust: [], php: [] };
  for (const o of ir.operations) {
    const resp = o.response ? struct(typeName(o.response)) : null;
    const shown = resp && SHOWN.find((k) => resp.fields.some((f) => f.wire === k && ['string', 'enum'].includes(f.type.kind)));
    const body = o.body ? o.bodyExample : undefined;
    const pathVals = o.pathParams.map((p) => String(p.example ?? p.name));
    const bodyName = o.body && typeName(o.body);
    const item = lowerFirst(o.response || 'result');
    const v = o.list ? item : lowerFirst(o.response || 'result');
    const snippet = {};

    // Python
    {
      const args = [...pathVals.map(dq), ...(body !== undefined ? [py(body)] : [])].join(', ');
      const call = `client.${snake(o.resource)}.${snake(o.method)}(${args})`;
      const lines = o.list
        ? (o.paging ? [`for ${snake(item)} in ${call}.auto_paging_iter():`, `    print(${snake(item)}${shown ? `[${dq(shown)}]` : ''})`] : [`for ${snake(item)} in ${call}.data:`, `    print(${snake(item)}${shown ? `[${dq(shown)}]` : ''})`])
        : [`${snake(v)} = ${call}`, `print(${snake(v)})`];
      snippet.python = `from phoenixperps import PhoenixPerps\n\nclient = PhoenixPerps()  # the key: PHOENIXPERPS_API_KEY\n${lines.join('\n')}\n`;
      compile.python.push(`def ${snake(o.id)}(client: PhoenixPerps) -> None:\n${indentAll(lines.join('\n'), '    ')}\n`);
    }
    // TypeScript
    {
      const args = [...pathVals.map(sq), ...(body !== undefined ? [js(body)] : [])].join(', ');
      const call = `client.${o.resource}.${o.method}(${args})`;
      const lines = o.list
        ? (o.paging ? [`for await (const ${item} of ${call}) {`, `  console.log(${item}${shown ? `.${shown}` : ''});`, '}'] : [`const { data } = await ${call};`, `for (const ${item} of data) console.log(${item}${shown ? `.${shown}` : ''});`])
        : [`const ${v} = await ${call};`, `console.log(${v});`];
      snippet.typescript = `import { PhoenixPerps } from '@phoenixperps/sdk';\n\nconst client = new PhoenixPerps(); // the key: PHOENIXPERPS_API_KEY\n${lines.join('\n')}\n`;
      compile.typescript.push(`export async function ${o.id}(client: PhoenixPerps): Promise<void> {\n${indentAll(lines.join('\n'), '  ')}\n}\n`);
    }
    // Go
    {
      const goArgs = ['ctx', ...pathVals.map(dq)];
      if (bodyName) goArgs.push(`${o.bodyRequired ? '' : '&'}phoenixperps.${bodyName}${goStruct(bodyName, body || {}, '')}`);
      if (m.params[o.id]) goArgs.push('nil');
      const call = `client.${GO.resName(o.resource)}.${GO.methodName(o.method)}(${goArgs.join(', ')})`;
      const label = (x) => (shown ? `${x}.${GO.goName(shown)}` : x);
      const lines = o.list
        ? (o.paging
          ? [`for ${item}, err := range ${call.replace(`.${GO.methodName(o.method)}(`, `.${GO.methodName(o.method)}AutoPaging(`)} {`, '\tif err != nil {', '\t\treturn err', '\t}', `\tfmt.Println(${label(item)})`, '}', 'return nil']
          : [`page, err := ${call}`, 'if err != nil {', '\treturn err', '}', `for _, ${item} := range page.Data {`, `\tfmt.Println(${label(item)})`, '}', 'return nil'])
        : [`${v}, err := ${call}`, 'if err != nil {', '\treturn err', '}', `fmt.Printf("%+v\\n", ${v})`, 'return nil'];
      const fn = (name) => `func ${name}(ctx context.Context, client *phoenixperps.Client) error {\n${indentAll(lines.join('\n'), '\t')}\n}\n`;
      snippet.go = `package main\n\nimport (\n\t"context"\n\t"fmt"\n\t"log"\n\n\tphoenixperps "github.com/phoenixperps/sdk/go"\n)\n\nfunc main() {\n\tclient, err := phoenixperps.NewClient() // the key: PHOENIXPERPS_API_KEY\n\tif err != nil {\n\t\tlog.Fatal(err)\n\t}\n\tif err := run(context.Background(), client); err != nil {\n\t\tlog.Fatal(err)\n\t}\n}\n\n${fn('run')}`;
      compile.go.push(fn(`snippet${GO.goName(o.id)}`).replace(/phoenixperps\./g, ''));
    }
    // C#
    {
      const args = [...pathVals.map(dq), ...(bodyName ? [csNew(bodyName, body || {}, '')] : [])].join(', ');
      const name = `${CS.methodPascal(o.method)}Async`;
      const call = `client.${CS.resName(o.resource)}.${o.list && o.paging ? name.replace('Async', 'AutoPagingAsync') : name}(${args})`;
      const label = (x) => (shown ? `${x}.${CS.propName(typeName(o.response), shown)}` : x);
      const ci = CS_KEYWORDS.has(item) ? `@${item}` : item;
      const cv = CS_KEYWORDS.has(v) ? `@${v}` : v;
      const lines = o.list
        ? (o.paging ? [`await foreach (var ${ci} in ${call})`, `    Console.WriteLine(${label(ci)});`] : [`var page = await ${call};`, `foreach (var ${ci} in page.Data)`, `    Console.WriteLine(${label(ci)});`])
        : [`var ${cv} = await ${call};`, `Console.WriteLine(${resp && shown ? label(cv) : cv});`];
      snippet.csharp = `using PhoenixPerps;\n\nusing var client = new PhoenixPerpsClient(); // the key: PHOENIXPERPS_API_KEY\n${lines.join('\n')}\n`;
      compile.csharp.push(`    internal static async Task ${CS.pascal(o.id)}(PhoenixPerpsClient client)\n    {\n${indentAll(lines.join('\n'), '        ')}\n    }\n`);
    }
    // Java
    {
      const args = [...pathVals.map(dq), ...(bodyName ? [javaNew(bodyName, body || {}, '')] : []), ...(m.params[o.id] ? ['null'] : [])].join(', ');
      const call = `client.${JAVA.resAccessor(o.resource)}().${o.list && o.paging ? `${o.method}AutoPaging` : o.method}(${args})`;
      const T = o.response ? typeName(o.response) : 'JsonNode';
      const label = (x) => (shown ? `${x}.${JAVA.camel(shown)}()` : x);
      const lines = o.list
        ? (o.paging ? [`for (${T} ${item} : ${call}) {`, `    System.out.println(${label(item)});`, '}'] : [`for (${T} ${item} : ${call}.data()) {`, `    System.out.println(${label(item)});`, '}'])
        : [`${T} ${v} = ${call};`, `System.out.println(${v});`];
      snippet.java = `${T === 'JsonNode' ? 'import com.fasterxml.jackson.databind.JsonNode;\n' : ''}import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;\nimport com.phoenixperpsfunding.sdk.gen.*;\nimport java.math.BigDecimal;\nimport java.util.List;\n\nPhoenixPerpsClient client = PhoenixPerpsClient.fromEnv(); // the key: PHOENIXPERPS_API_KEY\n${lines.join('\n')}\n`;
      compile.java.push(`    static void ${o.id}(PhoenixPerpsClient client) {\n${indentAll(lines.join('\n'), '        ')}\n    }\n`);
    }
    // Rust
    {
      const args = [...pathVals.map(dq), ...(bodyName ? [`${o.bodyRequired ? '&' : 'Some(&'}${rustStruct(bodyName, body || {}, '')}${o.bodyRequired ? '' : ')'}`] : []), ...(m.params[o.id] ? ['None'] : [])];
      const name = RUST.snake(o.method);
      const ri = snake(item);
      const show = shown ? `println!("{}", ${ri}.${RUST.field(shown)});` : `println!("{${ri}:?}");`;
      const lines = o.list
        ? (o.paging
          ? ['let options = RequestOptions::default();', `let mut items = client.${RUST.snake(o.resource)}().${name}_auto_paging(${[...args, '&options'].join(', ')});`, 'while let Some(item) = items.next().await {', `    let ${ri} = item?;`, `    ${show}`, '}']
          : [`let page = client.${RUST.snake(o.resource)}().${name}(${args.join(', ')}).await?;`, `for ${ri} in &page.data {`, `    ${show}`, '}'])
        : [`let ${snake(v)} = client.${RUST.snake(o.resource)}().${name}(${args.join(', ')}).await?;`, `println!("{${snake(v)}:?}");`];
      // StreamExt only where the sample iterates (items.next()), so no sample warns of an unused import
      snippet.rust = `${o.list && o.paging ? 'use futures_util::StreamExt;\n' : ''}use phoenixperps::*;\n\n#[tokio::main]\nasync fn main() -> Result<()> {\n    let client = Client::from_env()?; // the key: PHOENIXPERPS_API_KEY\n${indentAll(lines.join('\n'), '    ')}\n    Ok(())\n}\n`;
      compile.rust.push(`pub async fn ${snake(o.id)}(client: &Client) -> Result<()> {\n${indentAll(lines.join('\n'), '    ')}\n    Ok(())\n}\n`);
    }
    // PHP
    {
      const args = [...pathVals.map(sq), ...(body !== undefined ? [php(body)] : [])].join(', ');
      const call = `$client->${PHP.resProp(o.resource)}->${o.list && o.paging ? `${o.method}AutoPaging` : o.method}(${args})`;
      const label = (x) => (shown ? `$${x}->${PHP.camel(shown)}` : `json_encode($${x})`);
      const lines = o.list
        ? [`foreach (${call}${o.paging ? '' : '->data'} as $${item}) {`, `    echo ${label(item)}, "\\n";`, '}']
        : [`$${v} = ${call};`, `print_r(${o.response ? `$${v}->toArray()` : `$${v}`});`];
      snippet.php = `<?php\n\nrequire 'vendor/autoload.php';\n\n$client = new PhoenixPerps\\Client(); // the key: PHOENIXPERPS_API_KEY\n${lines.join('\n')}\n`;
      compile.php.push(`    public static function ${o.id}(Client $client): void\n    {\n${indentAll(lines.join('\n'), '        ')}\n    }\n`);
    }
    out[o.id] = LANGS.map((lang) => ({ lang, source: snippet[lang] }));
  }

  const HEAD = 'Generated from the API spec by generator/printers/snippets.js. Do not edit: run `node generator`.';
  files['snippets/snippets.json'] = `${JSON.stringify(out, null, 2)}\n`;
  files['typescript/test/snippets.ts'] = `// ${HEAD}\n// Every docs snippet, type-checked (npm run typecheck); never run.\nimport { PhoenixPerps } from '..';\n\n${compile.typescript.join('\n')}`;
  files['python/tests/snippets_check.py'] = `# ${HEAD}\n# Every docs snippet, compiled with the suite; never run.\nfrom phoenixperps import PhoenixPerps\n\n\n${compile.python.join('\n\n')}`;
  files['go/snippets_test.go'] = `// ${HEAD}\n\npackage phoenixperps\n\n// Every docs snippet, compiled with the tests (go vet); never run.\n\nimport (\n\t"context"\n\t"fmt"\n)\n\n${compile.go.join('\n')}`;
  files['csharp/tests/PhoenixPerps.Sdk.Tests/Generated/Snippets.cs'] = `// <auto-generated>\n// ${HEAD}\n// </auto-generated>\n// Every docs snippet, compiled with the tests; never run.\n#nullable enable\nusing System;\nusing System.Threading.Tasks;\n\nnamespace PhoenixPerps.Tests;\n\ninternal static class Snippets\n{\n${compile.csharp.join('\n')}}\n`;
  files['java/src/test/java/com/phoenixperpsfunding/sdk/Snippets.java'] = `// ${HEAD}\npackage com.phoenixperpsfunding.sdk;\n\nimport com.fasterxml.jackson.databind.JsonNode;\nimport com.phoenixperpsfunding.sdk.gen.*;\nimport java.math.BigDecimal;\nimport java.util.List;\n\n/** Every docs snippet, compiled with the tests; never run. */\n@SuppressWarnings("unused")\nfinal class Snippets {\n    private Snippets() {\n    }\n\n${compile.java.join('\n')}}\n`;
  files['rust/tests/snippets.rs'] = `// ${HEAD}\n// Every docs snippet, compiled with the tests; never run.\n#![allow(dead_code, unused_imports)]\n\nuse futures_util::StreamExt;\nuse phoenixperps::*;\n\n${compile.rust.join('\n')}`;
  files['php/tests/Generated/Snippets.php'] = `<?php\n\n// ${HEAD}\n// Every docs snippet, linted with the suite; never run.\n\ndeclare(strict_types=1);\n\nnamespace PhoenixPerps\\Tests\\Generated;\n\nuse PhoenixPerps\\Client;\n\nfinal class Snippets\n{\n${compile.php.join('\n')}}\n`;
  return files;
}

module.exports = { print, LANGS };
