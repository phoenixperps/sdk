'use strict';
const fs = require('node:fs');
const path = require('node:path');
const { generate, ROOT } = require('.');

// The drift gate: what the generator writes now is what is committed, byte for byte.
const drift = Object.entries(generate()).filter(([rel, text]) => {
  const file = path.join(ROOT, rel);
  return !fs.existsSync(file) || fs.readFileSync(file, 'utf8') !== text;
}).map(([rel]) => rel);
if (drift.length) {
  process.stderr.write(`generated files out of date (run node generator):\n  ${drift.join('\n  ')}\n`);
  process.exit(1);
}
process.stdout.write('generated files up to date\n');
