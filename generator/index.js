'use strict';
const fs = require('node:fs');
const path = require('node:path');
const { build } = require('./ir');

/*
 * The SDKs' generated surface from the vendored spec (spec/openapi.json): node generator writes every file the
 * printers produce; node generator/check.js does the same into memory and fails on any difference with what is
 * committed (the drift gate). Zero dependencies.
 */
const ROOT = path.join(__dirname, '..');
const PRINTERS = ['typescript', 'python', 'go', 'csharp', 'java', 'rust', 'php', 'postman', 'snippets', 'manifest'].map((n) => require(`./printers/${n}`));

function generate() {
  const spec = JSON.parse(fs.readFileSync(path.join(ROOT, 'spec', 'openapi.json'), 'utf8'));
  const ir = build(spec);
  ir.streams = spec['x-pp-streams'];
  const files = {};
  for (const p of PRINTERS) Object.assign(files, p.print(ir));
  return files;
}

if (require.main === module) {
  const files = generate();
  for (const [rel, text] of Object.entries(files)) {
    const file = path.join(ROOT, rel);
    fs.mkdirSync(path.dirname(file), { recursive: true });
    fs.writeFileSync(file, text);
  }
  process.stdout.write(`wrote ${Object.keys(files).length} files\n`);
}

module.exports = { generate, ROOT };
