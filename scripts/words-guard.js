'use strict';
const { execFileSync } = require('node:child_process');
const fs = require('node:fs');
const path = require('node:path');

/*
 * The words guard (docs/api/design/14-sdks.md section 8): nothing in this repo, or in a built artefact, says what it
 * must not. It refuses
 *   - an em dash;
 *   - a credential that looks real (ppk_/ppo_/ppr_/pptv_ tokens, ppwhsec_ secrets, private key blocks), except the
 *     fixtures the conformance suite signs with (FAKES);
 *   - every word of the denylist, one regular expression per line, from PP_WORDS_DENYLIST (a CI secret: the public
 *     repo never contains the words themselves).
 *   node scripts/words-guard.js [dir ...]     the tracked files, or every file under the given directories
 * Exits 1 with file:line for each hit.
 */
const ROOT = path.join(__dirname, '..');
const BUILTIN = [
  { name: 'em dash', re: /—/ },
  { name: 'credential', re: /\bpp(?:k|o|r|tv)_(?:live|test)_[0-9A-Za-z]{20,}/ },
  { name: 'webhook secret', re: /\bppwhsec_[0-9A-Za-z]{20,}/ },
  { name: 'private key', re: /-----BEGIN [A-Z ]*PRIVATE KEY-----/ },
];
// the conformance fixtures: a key made of one letter, and the webhook vectors' two secrets
const FAKES = [/\bpp(?:k|o)_(?:live|test)_A{50}\b/, /\bppwhsec_(?:Test|Old)Secret[0-9a-z]+\b/];
const SKIP_DIRS = new Set(['.git', 'node_modules', 'vendor', 'target', 'bin', 'obj', 'build', '.gradle', '__pycache__', '.pytest_cache']);
const TEXT = /\.(js|mjs|cjs|ts|json|md|py|go|mod|sum|cs|csproj|props|java|kts|rs|toml|php|xml|yml|yaml|txt|html|css|sh|ps1|cfg|ini|lock)$|^(README|LICENSE|VERSION|CHANGELOG)/i;

function denylist() {
  return String(process.env.PP_WORDS_DENYLIST || '').split(/\r?\n/).map((l) => l.trim()).filter((l) => l && !l.startsWith('#')).map((l) => ({ name: 'denied word', re: new RegExp(l, 'i') }));
}

function walk(dir, out) {
  for (const e of fs.readdirSync(dir, { withFileTypes: true })) {
    if (e.isDirectory()) {
      if (!SKIP_DIRS.has(e.name)) walk(path.join(dir, e.name), out);
    } else out.push(path.join(dir, e.name));
  }
  return out;
}

function files(dirs) {
  if (dirs.length) return dirs.flatMap((d) => walk(path.resolve(d), []));
  return execFileSync('git', ['ls-files'], { cwd: ROOT, encoding: 'utf8' }).split('\n').filter(Boolean).map((f) => path.join(ROOT, f));
}

function scan(list, rules) {
  const hits = [];
  for (const file of list) {
    if (!TEXT.test(path.basename(file)) || !fs.existsSync(file)) continue;
    if (path.resolve(file) === __filename) continue; // its own patterns
    const lines = fs.readFileSync(file, 'utf8').split('\n');
    lines.forEach((line, i) => {
      const clean = FAKES.reduce((l, f) => l.replace(new RegExp(f.source, 'g'), ''), line);
      for (const r of rules) if (r.re.test(clean)) hits.push(`${path.relative(ROOT, file)}:${i + 1}: ${r.name}`);
    });
  }
  return hits;
}

if (require.main === module) {
  const rules = [...BUILTIN, ...denylist()];
  const hits = scan(files(process.argv.slice(2)), rules);
  if (hits.length) {
    process.stderr.write(`${hits.join('\n')}\n${hits.length} hit(s)\n`);
    process.exit(1);
  }
  process.stdout.write(`words guard: clean (${rules.length} rules)\n`);
}

module.exports = { scan, BUILTIN, FAKES };
