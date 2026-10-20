'use strict';
const fs = require('node:fs');
const path = require('node:path');

/*
 * The SDKs' one version (docs/api/design/14-sdks.md section 9): VERSION, and every place a language states it.
 *   node scripts/release.js --check      every SDK says VERSION (CI)
 *   node scripts/release.js 1.2.0        sets 1.2.0 everywhere (CHANGELOG.md must have its "## 1.2.0" section first)
 * Then: commit, tag v1.2.0 and go/v1.2.0, push the tags: release.yml takes it from there (behind the owner's approval).
 */
const ROOT = path.join(__dirname, '..');
const PLACES = [
  ['typescript/package.json', /("version":\s*")([^"]+)(")/],
  ['python/pyproject.toml', /^(version = ")([^"]+)(")/m],
  ['python/src/phoenixperps/_core.py', /^(SDK_VERSION = ")([^"]+)(")/m],
  ['go/version.go', /(const SDKVersion = ")([^"]+)(")/],
  ['csharp/src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj', /(<Version>)([^<]+)(<\/Version>)/],
  ['csharp/src/PhoenixPerps.Sdk/Core/SdkInfo.cs', /(public const string Version = ")([^"]+)(")/],
  ['java/build.gradle.kts', /^(version = ")([^"]+)(")/m],
  ['java/src/main/java/com/phoenixperpsfunding/sdk/PhoenixPerpsClient.java', /(public static final String SDK_VERSION = ")([^"]+)(")/],
  ['rust/Cargo.toml', /^(version = ")([^"]+)(")/m],
  ['php/src/Client.php', /(public const SDK_VERSION = ')([^']+)(')/],
];
const SEMVER = /^\d+\.\d+\.\d+$/;

const read = (rel) => fs.readFileSync(path.join(ROOT, rel), 'utf8');

function stated() {
  return PLACES.map(([rel, re]) => {
    const m = re.exec(read(rel));
    if (!m) throw new Error(`${rel}: no version found`);
    return { rel, version: m[2] };
  });
}

function check() {
  const want = read('VERSION').trim();
  const off = stated().filter((s) => s.version !== want);
  for (const s of off) process.stderr.write(`${s.rel} says ${s.version}, VERSION says ${want}\n`);
  if (off.length) process.exit(1);
  process.stdout.write(`every SDK says ${want}\n`);
}

const parts = (v) => v.split('.').map(Number);
const newer = (a, b) => {
  const [x, y] = [parts(a), parts(b)];
  for (let i = 0; i < 3; i++) if (x[i] !== y[i]) return x[i] > y[i];
  return false;
};

function bump(next) {
  if (!SEMVER.test(next)) throw new Error(`${next} is not X.Y.Z`);
  const current = read('VERSION').trim();
  if (!newer(next, current)) throw new Error(`${next} is not after ${current}`);
  if (!new RegExp(`^## ${next.replace(/\./g, '\\.')}\\b`, 'm').test(read('CHANGELOG.md'))) throw new Error(`CHANGELOG.md has no "## ${next}" section yet`);
  for (const [rel, re] of PLACES) fs.writeFileSync(path.join(ROOT, rel), read(rel).replace(re, `$1${next}$3`));
  fs.writeFileSync(path.join(ROOT, 'VERSION'), `${next}\n`);
  // the lockfiles keep the package's own version too
  const locks = [
    ['rust/Cargo.lock', /(name = "phoenixperps"\nversion = ")([^"]+)(")/],
    ['typescript/package-lock.json', /("name": "@phoenixperps\/sdk",\s*"version": ")([^"]+)(")/g],
  ];
  for (const [rel, re] of locks) {
    if (fs.existsSync(path.join(ROOT, rel))) fs.writeFileSync(path.join(ROOT, rel), read(rel).replace(re, `$1${next}$3`));
  }
  check();
  process.stdout.write(`next: git commit -am "Release ${next}" && git tag v${next} && git tag go/v${next} && git push --atomic origin main v${next} go/v${next}\n`);
}

const arg = process.argv[2];
try {
  if (arg === '--check') check();
  else if (arg) bump(arg);
  else throw new Error('node scripts/release.js --check | X.Y.Z');
} catch (err) {
  process.stderr.write(`release: ${err.message}\n`);
  process.exit(1);
}
