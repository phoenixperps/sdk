'use strict';
const fs = require('node:fs');
const path = require('node:path');
const { spawnSync } = require('node:child_process');

/*
 * The docs guides' code samples against these SDKs (the site's seeds/devdocs/**.md): every TypeScript, Go, C#, Java
 * and Rust sample compiled with its SDK, every Python and PHP sample parsed (pyflakes, php -l) with the client calls it
 * makes checked to exist, every JSON request body checked against the API's spec. Run by hand after a guide changes:
 *   node scripts/check-guides.js <path to app/seeds/devdocs> [--only=typescript,go,...]
 * Each sample runs in its own function, given what the guides take as known (client, the raw body and headers of a
 * webhook, ...) unless the sample makes it itself. Install one-liners are skipped. The files it writes are removed.
 * Needs the toolchains of the SDKs (node, go, dotnet, gradle, cargo, php, the Python venv with pyflakes): PATH, or
 * %LOCALAPPDATA%\pp-sdk on the build PC.
 */
const ROOT = path.join(__dirname, '..');
const GUIDES = path.resolve(process.argv[2] || path.join(ROOT, '..', 'app', 'seeds', 'devdocs'));
const ONLY = (process.argv.find((a) => a.startsWith('--only=')) || '').slice(7).split(',').filter(Boolean);
const LANGS = ['typescript', 'python', 'go', 'csharp', 'java', 'rust', 'php', 'json'];
const INSTALL = /^(pip install|npm install|dotnet add|go get|implementation\(|cargo add|composer require)/;

const tools = process.env.LOCALAPPDATA ? path.join(process.env.LOCALAPPDATA, 'pp-sdk') : null;
const env = { ...process.env };
if (tools) {
  env.PATH = [path.join(tools, 'tools', 'go', 'bin'), path.join(tools, 'tools', 'dotnet'), path.join(tools, 'tools', 'cargo', 'bin'), path.join(tools, 'tools', 'php'), path.join(tools, 'venv', 'Scripts'), env.PATH].join(path.delimiter);
  env.CARGO_TARGET_DIR = env.CARGO_TARGET_DIR || path.join(tools, 'build', 'cargo-guides');
  env.RUSTUP_HOME = env.RUSTUP_HOME || path.join(tools, 'tools', 'rustup');
  env.CARGO_HOME = env.CARGO_HOME || path.join(tools, 'tools', 'cargo');
  env.GOPATH = env.GOPATH || path.join(tools, 'gopath');
  env.GOCACHE = env.GOCACHE || path.join(tools, 'gocache');
}

/** Every sample: { lang, file, n, code } (n counts the samples of that language in that file, from 1). */
function samples() {
  const out = [];
  const walk = (dir) => {
    for (const e of fs.readdirSync(dir, { withFileTypes: true })) {
      const p = path.join(dir, e.name);
      // the cookbook is the examples themselves (scripts/cookbook.js), compiled by CI as whole programs
      if (e.isDirectory()) {
        if (e.name !== 'cookbook') walk(p);
      } else if (e.name.endsWith('.md') && e.name !== 'cookbook.md') {
        const rel = path.relative(GUIDES, p).replace(/\\/g, '/');
        const lines = fs.readFileSync(p, 'utf8').split(/\r?\n/);
        const count = {};
        for (let i = 0; i < lines.length; i++) {
          const m = /^```([a-z]+)/.exec(lines[i]);
          if (!m) continue;
          const body = [];
          for (i += 1; i < lines.length && !/^```\s*$/.test(lines[i]); i++) body.push(lines[i]);
          const code = body.join('\n');
          if (!LANGS.includes(m[1]) || INSTALL.test(code.trim())) continue;
          count[m[1]] = (count[m[1]] || 0) + 1;
          out.push({ lang: m[1], file: rel, n: count[m[1]], code });
        }
      }
    }
  };
  walk(GUIDES);
  return out;
}

const run = (cmd, args, cwd) => {
  const r = spawnSync(cmd, args, { cwd, env, encoding: 'utf8', shell: process.platform === 'win32', maxBuffer: 64 * 1024 * 1024 });
  return { ok: r.status === 0, out: `${r.stdout || ''}${r.stderr || ''}` };
};
const label = (s) => `${s.file} ${s.lang} #${s.n}`;
/** The names a sample takes as known: the ones it uses and does not make itself. */
const given = (code, names, declares) => names.filter((n) => new RegExp(`\\b${n}\\b`).test(code) && !declares(n).test(code));

/* ------------------------------------------------------------------ each language: files, then the compiler */

const CHECKS = {
  typescript(list) {
    const file = path.join(ROOT, 'typescript', 'examples', 'zz-guides.mts');
    const imports = new Set();
    const names = new Set(['PhoenixPerps']);
    const bodies = list.map((s, i) => {
      const lines = s.code.split('\n').filter((l) => {
        const m = /^import \{([^}]+)\} from "@phoenixperps\/sdk";/.exec(l);
        if (m) m[1].split(',').forEach((x) => names.add(x.trim()));
        else if (/^import /.test(l)) imports.add(l);
        return !/^import /.test(l);
      });
      return `// ${label(s)}\nexport async function sample${i}() {\n${lines.join('\n')}\n}`;
    });
    fs.writeFileSync(file, [
      `import { ${[...names].join(', ')} } from "@phoenixperps/sdk";`, ...imports,
      'declare const client: PhoenixPerps;', 'declare const rawBody: string;', 'declare const req: { headers: Record<string, string | string[] | undefined> };',
      ...bodies, '',
    ].join('\n'));
    try {
      return run('npx', ['tsc', '-p', 'examples/tsconfig.json'], path.join(ROOT, 'typescript'));
    } finally {
      fs.rmSync(file, { force: true });
    }
  },

  python(list) {
    const dir = fs.mkdtempSync(path.join(require('node:os').tmpdir(), 'pp-guides-'));
    const file = path.join(dir, 'guides.py');
    const imports = new Set(list.some((s) => /\bos\./.test(s.code)) ? ['import os'] : []);
    const sdkNames = new Set();
    const bodies = list.map((s, i) => {
      const lines = s.code.split('\n').filter((l) => {
        const m = /^from phoenixperps import (.+)$/.exec(l);
        if (m) m[1].split(',').forEach((x) => sdkNames.add(x.trim()));
        else if (/^(from \S+ )?import /.test(l)) imports.add(l);
        return !/^(from \S+ )?import /.test(l);
      });
      const known = given(s.code, ['client', 'raw_body', 'headers'], (n) => new RegExp(`^${n}\\s*=`, 'm'));
      return `# ${label(s)}\nasync def sample${i}(${known.join(', ')}):\n${lines.map((l) => `    ${l}`).join('\n')}\n`;
    });
    if (sdkNames.size) imports.add(`from phoenixperps import ${[...sdkNames].join(', ')}`);
    fs.writeFileSync(file, `${[...imports].join('\n')}\n\n\n${bodies.join('\n\n')}`);
    // the calls on the client: resources and methods of the real SDK
    const calls = [...new Set(list.flatMap((s) => [...s.code.matchAll(/\bclient\.(\w+)(?:\.(\w+))?\(/g)].map((m) => m.slice(1).filter(Boolean).join('.'))))];
    // and every name a sample imports from the SDK
    const names = [...new Set([...imports].flatMap((l) => (/^from phoenixperps import (.+)$/.exec(l) || [, ''])[1].split(',').map((x) => x.trim()).filter(Boolean)))];
    const probe = `import sys\nsys.path.insert(0, ${JSON.stringify(path.join(ROOT, 'python', 'src'))})\nimport phoenixperps\nfrom phoenixperps import PhoenixPerps, AsyncPhoenixPerps, Webhooks\nmissing = [n for n in ${JSON.stringify(names)} if not hasattr(phoenixperps, n)]\nfor name in ${JSON.stringify(calls)}:\n    for c in (PhoenixPerps, AsyncPhoenixPerps):\n        o = c(api_key="ppk_test_" + "A" * 50)\n        for part in name.split("."):\n            o = getattr(o, part, None)\n        if o is None: missing.append(c.__name__ + "." + name)\nassert hasattr(Webhooks, "verify")\nprint("missing:", missing) if missing else None\nsys.exit(1 if missing else 0)\n`;
    fs.writeFileSync(path.join(dir, 'probe.py'), probe);
    try {
      // a sample may leave a result for the reader to use
      const flakes = run('python', ['-I', '-m', 'pyflakes', file], dir).out.split('\n').filter((l) => l.trim() && !/is assigned to but never used/.test(l));
      const b = run('python', ['-I', path.join(dir, 'probe.py')], dir);
      return { ok: !flakes.length && b.ok, out: flakes.join('\n') + b.out + (flakes.length ? `\n${fs.readFileSync(file, 'utf8')}` : '') };
    } finally {
      fs.rmSync(dir, { recursive: true, force: true });
    }
  },

  go(list) {
    const file = path.join(ROOT, 'go', 'zz_guides_test.go');
    const bodies = list.map((s, i) => {
      // the names a sample makes at its own top level, used once more so Go does not refuse them as unused
      const made = [...new Set([...s.code.matchAll(/^(?!for |if |switch )([\w, ]+?)\s*:=/gm)].flatMap((m) => m[1].split(',').map((x) => x.trim())).filter((x) => x && x !== '_'))];
      return `// ${label(s)}\nfunc guideSample${i}(client *phoenixperps.Client, ctx context.Context, rawBody []byte, r *http.Request) {\n${s.code}\n${made.map((x) => `\t_ = ${x}`).join('\n')}\n}`;
    });
    fs.writeFileSync(file, `package phoenixperps_test\n\nimport (\n\t"context"\n\t"errors"\n\t"fmt"\n\t"log"\n\t"net/http"\n\t"os"\n\t"time"\n\n\tphoenixperps "github.com/phoenixperps/sdk/go"\n)\n\nvar _, _, _, _, _, _ = errors.Is, fmt.Sprint, log.Print, os.Getenv, time.Now, http.StatusOK\n\n${bodies.join('\n\n')}\n`);
    try {
      return run('go', ['vet', '.'], path.join(ROOT, 'go'));
    } finally {
      fs.rmSync(file, { force: true });
    }
  },

  csharp(list) {
    const file = path.join(ROOT, 'csharp', 'examples', 'zz-guides.cs');
    const usings = new Set(['using PhoenixPerps;']);
    const bodies = list.map((s, i) => {
      const lines = s.code.split('\n').filter((l) => {
        if (/^using [\w.]+;/.test(l)) usings.add(l);
        return !/^using [\w.]+;/.test(l);
      });
      const params = { client: 'PhoenixPerpsClient client', rawBody: 'string rawBody', headers: 'IReadOnlyDictionary<string, string> headers' };
      const known = given(s.code, Object.keys(params), (n) => new RegExp(`\\b(var|using var|[A-Z]\\w*) ${n}\\s*=`));
      return `    // ${label(s)}\n    public static async Task Sample${i}(${known.map((n) => params[n]).join(', ')})\n    {\n${lines.map((l) => `        ${l}`).join('\n')}\n        await Task.CompletedTask;\n    }`;
    });
    fs.writeFileSync(file, `#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj\n${[...usings].join('\n')}\n\nConsole.WriteLine("guides");\n\npublic static class Guides\n{\n${bodies.join('\n\n')}\n}\n`);
    try {
      return run('dotnet', ['build', 'zz-guides.cs'], path.join(ROOT, 'csharp', 'examples'));
    } finally {
      fs.rmSync(file, { force: true });
    }
  },

  java(list) {
    const file = path.join(ROOT, 'java', 'examples', 'ZzGuides.java');
    const bodies = list.map((s, i) => {
      const params = { client: 'PhoenixPerpsClient client', rawBody: 'String rawBody', headers: 'Map<String, String> headers' };
      const known = given(s.code, Object.keys(params), (n) => new RegExp(`\\b[A-Z]\\w* ${n}\\s*=`));
      return `    // ${label(s)}\n    static void sample${i}(${known.map((n) => params[n]).join(', ')}) throws Exception {\n${s.code.split('\n').map((l) => `        ${l}`).join('\n')}\n    }`;
    });
    fs.writeFileSync(file, `import com.phoenixperpsfunding.sdk.*;\nimport com.phoenixperpsfunding.sdk.gen.*;\nimport java.math.BigDecimal;\nimport java.time.Duration;\nimport java.util.List;\nimport java.util.Map;\n\nfinal class ZzGuides {\n${bodies.join('\n\n')}\n}\n`);
    try {
      return run('gradle', ['-q', 'compileExamplesJava'], path.join(ROOT, 'java'));
    } finally {
      fs.rmSync(file, { force: true });
    }
  },

  rust(list) {
    const file = path.join(ROOT, 'rust', 'examples', 'zz_guides.rs');
    const bodies = list.map((s, i) => `// ${label(s)}\n#[allow(unused)]\nasync fn sample${i}(client: &Client, raw_body: String, headers: Vec<(String, String)>, secret: String) -> Result<(), Box<dyn std::error::Error>> {\n${s.code}\n    Ok(())\n}`);
    fs.writeFileSync(file, `#![allow(unused_imports)]\nuse phoenixperps::*;\n\nfn main() {}\n\n${bodies.join('\n\n')}\n`);
    try {
      return run('cargo', ['check', '--quiet', '--example', 'zz_guides'], path.join(ROOT, 'rust'));
    } finally {
      fs.rmSync(file, { force: true });
    }
  },

  php(list) {
    const dir = fs.mkdtempSync(path.join(require('node:os').tmpdir(), 'pp-guides-'));
    let out = '';
    let ok = true;
    list.forEach((s, i) => {
      const f = path.join(dir, `s${i}.php`);
      fs.writeFileSync(f, `<?php\n${s.code}\n`);
      const r = run('php', ['-l', f], dir);
      if (!r.ok) {
        ok = false;
        out += `${label(s)}: ${r.out}\n`;
      }
    });
    // the calls on the client and the static calls: they exist in the SDK
    const calls = [...new Set(list.flatMap((s) => [...s.code.matchAll(/\$client->(\w+)(?:->(\w+))?\(/g)].map((m) => m.slice(1).filter(Boolean).join('->'))))];
    const statics = [...new Set(list.flatMap((s) => [...s.code.matchAll(/(PhoenixPerps\\\w+)::(\w+)\(/g)].map((m) => `${m[1]}::${m[2]}`)))];
    const classes = [...new Set(list.flatMap((s) => [...s.code.matchAll(/PhoenixPerps(?:\\\w+)+/g)].map((m) => m[0])))];
    const probe = path.join(dir, 'probe.php');
    fs.writeFileSync(probe, `<?php\nrequire ${JSON.stringify(path.join(ROOT, 'php', 'vendor', 'autoload.php').replace(/\\/g, '/'))};\n$c = new PhoenixPerps\\Client('ppk_test_' . str_repeat('A', 50));\n$missing = [];\nforeach (${JSON.stringify(calls)} as $call) {\n  $parts = explode('->', $call);\n  $o = $c;\n  if (count($parts) === 2) { $o = $c->{$parts[0]} ?? null; if (!$o || !method_exists($o, $parts[1])) $missing[] = $call; }\n  elseif (!method_exists($c, $parts[0])) $missing[] = $call;\n}\nforeach (${JSON.stringify(statics)} as $s) { [$cls, $m] = explode('::', $s); if (!method_exists($cls, $m)) $missing[] = $s; }\nforeach (${JSON.stringify(classes)} as $k) { if (!class_exists($k)) $missing[] = $k; }\nif ($missing) { echo 'missing: ', implode(' ', $missing), PHP_EOL; exit(1); }\n`);
    const r = run('php', [probe], path.join(ROOT, 'php'));
    fs.rmSync(dir, { recursive: true, force: true });
    return { ok: ok && r.ok, out: out + r.out };
  },

  json(list) {
    const API = path.join(ROOT, '..', 'api');
    const { validate } = require(path.join(API, 'spec', 'validate.js'));
    const spec = JSON.parse(fs.readFileSync(path.join(API, 'openapi', 'v1.json'), 'utf8'));
    const S = spec.components.schemas;
    const strategyTypes = S.CreateStrategy.properties.type.enum;
    let out = '';
    for (const s of list) {
      let v;
      try {
        v = JSON.parse(s.code);
      } catch (err) {
        out += `${label(s)}: not JSON (${err.message})\n`;
        continue;
      }
      // a request body or an answer of the API, recognised by its fields
      const schema = v.operations ? S.ExitsBatch
        : v.type && strategyTypes.includes(v.type) && v.market ? S.CreateStrategy
          : v.market && v.side && v.type ? S.CreateOrder
            : v.data && v.data.take_profit ? { type: 'object', properties: { data: S.Exits } }
              : null;
      if (!schema) continue;
      const problems = validate(schema, v, S);
      if (problems.length) out += `${label(s)}: ${problems.join('; ')}\n`;
    }
    return { ok: !out, out };
  },
};

const all = samples();
let failed = 0;
for (const lang of LANGS) {
  if (ONLY.length && !ONLY.includes(lang)) continue;
  const list = all.filter((s) => s.lang === lang);
  if (!list.length) continue;
  const r = CHECKS[lang](list);
  process.stdout.write(`${r.ok ? 'ok  ' : 'FAIL'} ${lang}: ${list.length} samples (${[...new Set(list.map((s) => s.file))].join(', ')})\n`);
  if (!r.ok) {
    failed += 1;
    process.stdout.write(`${r.out.trim().split('\n').slice(0, 60).join('\n')}\n`);
  }
}
process.exit(failed ? 1 : 0);
