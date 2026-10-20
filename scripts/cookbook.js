'use strict';
const fs = require('node:fs');
const path = require('node:path');

/*
 * The docs' cookbook from the examples (each compiled in CI in all seven languages): one page per example, its
 * languages as tabs, written into the site's guides (app/seeds/devdocs/cookbook/*.md) with an overview page.
 *   node scripts/cookbook.js [path to app/seeds/devdocs] [--check]
 * The text of a recipe is the header comment of its TypeScript example; each file is shown without its header, with
 * what points into this repository (the C# project reference, the PHP autoloader) as a user's project has it.
 * --check writes nothing and fails when a page differs from what the examples make.
 */
const ROOT = path.join(__dirname, '..');
const OUT = path.resolve(process.argv.find((a, i) => i > 1 && !a.startsWith('--')) || path.join(ROOT, '..', 'app', 'seeds', 'devdocs'));
const VERSION = fs.readFileSync(path.join(ROOT, 'VERSION'), 'utf8').trim();

const RECIPES = [
  ['01', 'accounts', 'Your key and accounts'],
  ['02', 'markets', 'Markets and a quote'],
  ['03', 'market-stream', 'Live market data'],
  ['04', 'place-cancel-limit', 'Place and cancel a limit order'],
  ['05', 'market-with-brackets', 'A market order with exits'],
  ['06', 'trailing-breakeven', 'Trailing stop and break-even'],
  ['07', 'what-if', 'What an order would do'],
  ['08', 'account-stream-mirror', 'A live copy of your accounts'],
  ['09', 'history', 'Account history'],
  ['10', 'candles-to-csv', 'Candles to a CSV file'],
  ['11', 'webhook-receiver', 'A webhook receiver'],
  ['12', 'error-handling', 'Handling errors'],
];

// each language: its example file, the fence, and the header to drop
const LANGS = [
  { lang: 'typescript', file: (n, s) => `typescript/examples/${n}-${s}.mts`, header: /^(\/\/.*\n)+/ },
  { lang: 'python', file: (n, s) => `python/examples/${n}_${s.replace(/-/g, '_')}.py`, header: /^"""[\s\S]*?"""\n/ },
  { lang: 'go', file: (n, s) => `go/examples/${n}-${s}/main.go`, header: /^(\/\/.*\n)+/ },
  { lang: 'csharp', file: (n, s) => `csharp/examples/${n}-${s}.cs`, header: /^(\/\/.*\n)+/, fix: (c) => c.replace(/^#:project .*$/m, `#:package PhoenixPerps.Sdk@${VERSION}`) },
  { lang: 'java', file: (n, s) => `java/examples/${n}-${s}.java`, header: /^(\/\/.*\n)+/ },
  { lang: 'rust', file: (n, s) => `rust/examples/${n}-${s}.rs`, header: /^(\/\/!.*\n)+/ },
  { lang: 'php', file: (n, s) => `php/examples/${n}-${s}.php`, header: /^(\/\/.*\n)+/, after: '<?php\n', fix: (c) => c.replace("require __DIR__ . '/../vendor/autoload.php';", "require 'vendor/autoload.php';") },
];

/** The recipe's words and what it needs to run, from the TypeScript header (its number and run lines dropped). */
function words(n, s) {
  const head = fs.readFileSync(path.join(ROOT, LANGS[0].file(n, s)), 'utf8').match(LANGS[0].header)[0];
  const lines = head.split('\n').map((l) => l.replace(/^\/\/ ?/, ''));
  const text = lines.filter((l) => l && !/^\s{2}\S/.test(l)).join(' ').replace(/^\d\d: /, '').replace(/\s+/g, ' ').trim();
  const run = lines.find((l) => /^\s{2}\S/.test(l)) || '';
  const needs = /PHOENIXPERPS_WEBHOOK_SECRET=/.test(run)
    ? 'Set `PHOENIXPERPS_WEBHOOK_SECRET` to your endpoint\'s signing secret (`ppwhsec_...`) and run it.'
    : 'Set `PHOENIXPERPS_API_KEY` to a sandbox key (`ppk_test_...`) and run it.';
  const full = text[0].toUpperCase() + text.slice(1);
  return { text: full, summary: full.split(/(?<=\.) /)[0], needs };
}

function code(l, n, s) {
  let c = fs.readFileSync(path.join(ROOT, l.file(n, s)), 'utf8').replace(/\r\n/g, '\n');
  if (l.after) c = l.after + c.slice(l.after.length).replace(/^\n?/, '').replace(l.header, '');
  else c = c.replace(l.header, '');
  if (l.fix) c = l.fix(c);
  return c.replace(/^\n+/, '').trimEnd();
}

const pages = new Map();
const rows = [];
RECIPES.forEach(([n, s, title], i) => {
  const w = words(n, s);
  rows.push(`| [${title}](/cookbook/${s}) | ${w.summary} |`);
  const fences = LANGS.map((l) => `\`\`\`${l.lang}\n${code(l, n, s)}\n\`\`\``).join('\n\n');
  // the first sentence is the page's lead; the rest, if any, follows how to run it
  const rest = w.text.slice(w.summary.length).trim();
  pages.set(path.join(OUT, 'cookbook', `${s}.md`), `---\ntitle: ${title}.\nnav_title: ${title}\ndescription: ${w.summary}\nsection: Cookbook\nsort: ${310 + i}\nversion: 1\n---\n\nThe whole program, in each language. ${w.needs}${rest ? ` ${rest}` : ''}\n\n${fences}\n`);
});
// what a program needs beyond its own file, per language (the Rust recipes use these crates besides the SDK)
// (the SDKs are published from github.com/phoenixperps/sdk: its release assets, its Go module, and the Maven, NuGet and
// Composer repositories on its GitHub Pages)
const REL = `https://github.com/phoenixperps/sdk/releases/download/v${VERSION}`;
const PAGES = 'https://phoenixperps.github.io/sdk';
const RUNNING = [
  `| TypeScript | Node 22 or later: \`npm install ${REL}/phoenixperps-sdk-${VERSION}.tgz\`, then \`node accounts.mts\` |`,
  `| Python | Python 3.10 or later: \`pip install ${REL}/phoenixperps-${VERSION}-py3-none-any.whl\`, then \`python accounts.py\` |`,
  '| Go | Go 1.23 or later: `go mod init bot && go get github.com/phoenixperps/sdk/go`, the program as `main.go`, then `go run .` |',
  `| C# | .NET 10: once, \`dotnet nuget add source ${PAGES}/nuget/index.json --name phoenixperps\`; each program is a file-based app whose \`#:package\` line brings the SDK: \`dotnet run accounts.cs\` |`,
  `| Java | Java 17 or later, with \`maven("${PAGES}/maven")\` in your repositories and \`com.phoenixperpsfunding:phoenixperps-sdk:${VERSION}\` in your dependencies |`,
  `| Rust | Rust 1.85 or later: \`cargo add phoenixperps --git https://github.com/phoenixperps/sdk --tag v${VERSION}\`, \`cargo add futures-util serde_json\`, \`cargo add uuid --features v4\`, \`cargo add tokio --features macros,rt-multi-thread,time\`, the program as \`src/main.rs\`, then \`cargo run\` |`,
  `| PHP | PHP 8.2 or later: \`composer config repositories.phoenixperps composer ${PAGES}/composer\`, \`composer require phoenixperps/sdk\`, then \`php accounts.php\` |`,
];
pages.set(path.join(OUT, 'cookbook.md'), `---\ntitle: Cookbook.\nnav_title: Overview\ndescription: Twelve complete programs in all seven languages, from listing your accounts to a webhook receiver: copy one and run it.\nsection: Cookbook\nsort: 300\nversion: 1\n---\n\nTwelve complete programs, each in all seven languages, every one compiled against its SDK before a release. Each runs as it is against the sandbox.\n\n| Recipe | What it does |\n|---|---|\n${rows.join('\n')}\n\n## Running one\n\n| Language | What it needs |\n|---|---|\n${RUNNING.join('\n')}\n`);

let stale = 0;
for (const [file, text] of pages) {
  const now = fs.existsSync(file) ? fs.readFileSync(file, 'utf8') : null;
  if (now === text) continue;
  if (process.argv.includes('--check')) {
    process.stderr.write(`${path.relative(OUT, file)} is not what the examples make: run node scripts/cookbook.js\n`);
    stale += 1;
  } else {
    fs.mkdirSync(path.dirname(file), { recursive: true });
    fs.writeFileSync(file, text);
    process.stdout.write(`wrote ${path.relative(OUT, file)}\n`);
  }
}
process.exit(stale ? 1 : 0);
