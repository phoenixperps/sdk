'use strict';
const fs = require('node:fs');
const path = require('node:path');

/*
 * The docs' SDK pages (docs design 13, Tools: /sdks and /sdks/<id>), written from this repo so they never drift from it:
 * sdks.md, the seven at a glance, and sdks/<id>.md, each SDK's README (its own words: the client, calls, lists, errors,
 * streams, helpers, examples) under a head that says where it comes from and how to install it.
 *   node scripts/sdk-pages.js [<the docs' seeds/devdocs folder>]      (default ../app/seeds/devdocs)
 * The README's H1 goes (the page has its own), its first paragraph becomes the page's description, and the docs' own
 * addresses become links inside the docs.
 */
const ROOT = path.join(__dirname, '..');
const OUT = path.resolve(process.argv[2] || path.join(ROOT, '..', 'app', 'seeds', 'devdocs'));
const VERSION = fs.readFileSync(path.join(ROOT, 'VERSION'), 'utf8').trim();
const SDKS = JSON.parse(fs.readFileSync(path.join(ROOT, 'snippets', 'sdk-manifest.json'), 'utf8'));
const REPO = 'https://github.com/phoenixperps/sdk';
const DOCS = 'https://docs.phoenixperpsfunding.com';
const SORT = 185;

const front = (meta) => `---\n${Object.entries(meta).map(([k, v]) => `${k}: ${v}`).join('\n')}\n---\n\n`;
// the docs' own addresses as links inside the docs: "cookbook (https://docs.../cookbook)" links the word, a bare one
// links its path
const DOCS_RE = DOCS.replace(/\./g, '\\.');
const local = (text) => text
  .replace(new RegExp(`([\\w'-]+) \\(${DOCS_RE}(/[a-z0-9/-]*)?\\)`, 'g'), (m, word, p = '/') => `[${word}](${p})`)
  .replace(new RegExp(`${DOCS_RE}(/[a-z0-9/-]*)?`, 'g'), (m, p = '') => `[${(p || '/').replace(/^\//, '') || 'the docs'}](${p || '/'})`);

function page(s, i) {
  const readme = fs.readFileSync(path.join(ROOT, s.source, 'README.md'), 'utf8').replace(/\r\n/g, '\n');
  if (/\{\{/.test(readme.replace(/```[\s\S]*?```|`[^`\n]+`/g, ''))) throw new Error(`${s.source}/README.md: {{ outside code would be read as a token`);
  const body = readme.replace(/^# .*\n+/, '');
  const [first, ...rest] = body.split(/\n\n/);
  // the description: the first paragraph, one line, without its pointer back to the docs
  const description = first.replace(/\s+/g, ' ').replace(/\s*Docs: \S+$/, '').trim();
  const head = [
    `Source: [${s.source}/ on GitHub](${REPO}/tree/main/${s.source}), MIT. Version ${VERSION}, the same for all seven SDKs: [releases](${REPO}/releases).`,
    `${s.runtime}. Package \`${s.package}\`.`,
  ].join(' ');
  // the install fence and the first sample follow each other in a README: the docs would make them one tabbed group,
  // the install line behind a tab; a line between keeps them two blocks
  const text = local(rest.join('\n\n')).trim().replace(/^(```[^\n]*\n[\s\S]*?\n```)\n\n(```)/, '$1\n\nA first call:\n\n$2');
  return front({ title: `${s.name} SDK.`, nav_title: s.name, description, section: 'SDKs', sort: SORT + 1 + i, version: 1 }) + `${head}\n\n${text}\n`;
}

function overview() {
  const rows = SDKS.map((s) => `| [${s.name}](/sdks/${s.id}) | \`${s.package}\` | ${s.runtime} |`).join('\n');
  return front({ title: 'SDKs.', nav_title: 'Overview', description: `Seven official SDKs, open source on GitHub: ${SDKS.map((s) => s.name).join(', ')}. One version for all seven, ${VERSION}.`, section: 'SDKs', sort: SORT, version: 1 })
    + `Every SDK makes every call of the API, follows the account stream with resume, reads market data, retries only when it is safe, sends an Idempotency-Key with each write, pages through lists and raises typed errors. They are generated from the same OpenAPI document and pass the same conformance suite, so a call has the same name, arguments and answer in each language ([what every SDK does](${REPO}/blob/main/docs/BEHAVIOUR.md)).

| Language | Package | Needs |
|---|---|---|
${rows}

Each page has its install line, the client's options, the calls, lists, errors, streams and helpers. Every SDK is published from [github.com/phoenixperps/sdk](${REPO}): nothing to sign up for. The [Quickstart](/quickstart) installs yours, and the [cookbook](/cookbook) has twelve complete programs in every language.
`;
}

fs.mkdirSync(path.join(OUT, 'sdks'), { recursive: true });
fs.writeFileSync(path.join(OUT, 'sdks.md'), overview());
SDKS.forEach((s, i) => fs.writeFileSync(path.join(OUT, 'sdks', `${s.id}.md`), page(s, i)));
process.stdout.write(`wrote sdks.md and ${SDKS.length} SDK pages in ${OUT}\n`);
