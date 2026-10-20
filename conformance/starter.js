'use strict';
const { spawn } = require('node:child_process');
const assert = require('node:assert/strict');
const path = require('node:path');
const { start } = require('./mock-server');

/*
 * The starter bots' conformance (starters/README.md): one starter, run in its own folder, against the mock playing
 * conformance/starter.json (the REST answers in order, one scripted market stream), must send exactly the expected
 * requests: the same method, path, query and body, in the same order, the entry with its Idempotency-Key.
 *
 *   cd starters/<language> && node ../../conformance/starter.js <command> [args...]
 */
const S = require('./starter.json');
const TIMEOUT_MS = 10 * 60 * 1000; // the first run may compile (cargo, gradle, dotnet)

async function main() {
  const [cmd, ...args] = process.argv.slice(2);
  if (!cmd) throw new Error('usage: node conformance/starter.js <command> [args...]');
  const mock = await start({ port: 0 });
  try {
    const load = await fetch(`${mock.url}/__scenario`, { method: 'POST', headers: { 'content-type': 'application/json' }, body: JSON.stringify({ exchanges: S.exchanges, connections: [S.market] }) });
    assert.ok(load.ok, 'the mock took the scenario');
    const env = { ...process.env, ...S.env, PHOENIXPERPS_API_KEY: S.key, PHOENIXPERPS_BASE_URL: mock.url, PP_STREAM_URL: `${mock.url.replace(/^http/, 'ws')}/v1/market-data` };
    for (const k of ['PHOENIXPERPS_ENVIRONMENT', 'PHOENIXPERPS_ALLOW_LIVE', 'PP_ACCOUNT', 'PP_TRAIL_PERCENT', 'PP_BREAK_EVEN_PERCENT']) delete env[k];
    const code = await new Promise((resolve, reject) => {
      // Windows runs npm, gradle and the like through the shell: one command line, each part quoted
      const child = process.platform === 'win32'
        ? spawn([cmd, ...args].map((a) => (/[\s"]/.test(a) ? `"${a.replace(/"/g, '\\"')}"` : a)).join(' '), { env, stdio: 'inherit', shell: true })
        : spawn(cmd, args, { env, stdio: 'inherit' });
      const timer = setTimeout(() => {
        child.kill();
        reject(new Error(`the starter did not stop within ${TIMEOUT_MS / 1000} s (PP_RUN_CANDLES)`));
      }, TIMEOUT_MS);
      child.on('exit', (c) => {
        clearTimeout(timer);
        resolve(c);
      });
      child.on('error', reject);
    });
    assert.equal(code, 0, `the starter ended with ${code}`);
    const got = (await (await fetch(`${mock.url}/__requests`)).json()).filter((r) => !r.ws);
    const want = S.expect.requests;
    const line = (r) => `${r.method} ${r.path}${r.query && Object.keys(r.query).length ? `?${new URLSearchParams(r.query)}` : ''}${r.body ? ` ${JSON.stringify(r.body)}` : ''}`;
    const report = () => `\n  sent:\n    ${got.map(line).join('\n    ')}\n  expected:\n    ${want.map(line).join('\n    ')}`;
    assert.equal(got.length, want.length, `${got.length} requests, ${want.length} expected${report()}`);
    want.forEach((w, i) => {
      const g = got[i];
      assert.equal(`${g.method} ${g.path}`, `${w.method} ${w.path}`, `request ${i + 1}${report()}`);
      assert.deepEqual(g.query || {}, w.query || {}, `request ${i + 1}: its query${report()}`);
      if (w.body !== undefined) assert.deepEqual(g.body, w.body, `request ${i + 1}: its body${report()}`);
      if (w.idempotency_key) assert.equal(g.headers['idempotency-key'], w.idempotency_key, `request ${i + 1}: its Idempotency-Key`);
      assert.equal(g.headers.authorization, `Bearer ${S.key}`, `request ${i + 1}: the key`);
    });
    process.stdout.write(`starter conformance: ${want.length} requests as expected (${path.basename(process.cwd())})\n`);
  } finally {
    // the verdict is out first; the mock may still hold the starter's closed socket
    await Promise.race([mock.stop(), new Promise((r) => setTimeout(r, 2000))]);
  }
}

main().then(() => process.exit(0), (err) => {
  process.stderr.write(`starter conformance FAILED: ${err.message}\n`);
  process.exit(1);
});
