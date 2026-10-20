'use strict';
const fs = require('node:fs');
const path = require('node:path');

/*
 * snippets/sdk-manifest.json: the seven SDKs as the docs list them (docs/api/design/14-sdks.md section 11: versions are
 * never typed by hand): id, name, package, registry, version (VERSION, lockstep), install, import, minimum runtime,
 * source folder. The docs' developer/sdks.json is this file.
 */
const ROOT = path.join(__dirname, '..', '..');
// Published from GitHub (github.com/phoenixperps/sdk, scripts/publish-github.js): release assets, the Go module from the
// repo itself, and static Maven, NuGet and Composer repositories on its GitHub Pages. No login and no account anywhere.
const REL = 'https://github.com/phoenixperps/sdk/releases/download/v{version}';
const PAGES = 'https://phoenixperps.github.io/sdk';
const SDKS = [
  { id: 'python', name: 'Python', package: 'phoenixperps', registry: 'GitHub', install: `pip install ${REL}/phoenixperps-{version}-py3-none-any.whl`, import: 'from phoenixperps import PhoenixPerps', runtime: 'Python 3.10+', source: 'python' },
  { id: 'typescript', name: 'TypeScript', package: '@phoenixperps/sdk', registry: 'GitHub', install: `npm install ${REL}/phoenixperps-sdk-{version}.tgz`, import: "import { PhoenixPerps } from '@phoenixperps/sdk';", runtime: 'Node 22+', source: 'typescript' },
  { id: 'go', name: 'Go', package: 'github.com/phoenixperps/sdk/go', registry: 'Go modules', install: 'go get github.com/phoenixperps/sdk/go', import: 'import phoenixperps "github.com/phoenixperps/sdk/go"', runtime: 'Go 1.23+', source: 'go' },
  { id: 'csharp', name: 'C#', package: 'PhoenixPerps.Sdk', registry: 'GitHub', install: `dotnet nuget add source ${PAGES}/nuget/index.json --name phoenixperps\ndotnet add package PhoenixPerps.Sdk`, import: 'using PhoenixPerps;', runtime: '.NET 8+', source: 'csharp' },
  { id: 'java', name: 'Java', package: 'com.phoenixperpsfunding:phoenixperps-sdk', registry: 'GitHub', install: `repositories { mavenCentral(); maven("${PAGES}/maven") }\ndependencies { implementation("com.phoenixperpsfunding:phoenixperps-sdk:{version}") }`, import: 'import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;', runtime: 'Java 17+', source: 'java' },
  { id: 'rust', name: 'Rust', package: 'phoenixperps', registry: 'GitHub', install: 'cargo add phoenixperps --git https://github.com/phoenixperps/sdk --tag v{version}', import: 'use phoenixperps::Client;', runtime: 'Rust 1.85+', source: 'rust' },
  { id: 'php', name: 'PHP', package: 'phoenixperps/sdk', registry: 'GitHub', install: `composer config repositories.phoenixperps composer ${PAGES}/composer\ncomposer require phoenixperps/sdk`, import: 'use PhoenixPerps\\Client;', runtime: 'PHP 8.2+', source: 'php' },
];

function print(ir) {
  const version = fs.readFileSync(path.join(ROOT, 'VERSION'), 'utf8').trim();
  const list = SDKS.map((s) => ({ ...s, version, install: s.install.replaceAll('{version}', version), spec: ir.version }));
  return { 'snippets/sdk-manifest.json': `${JSON.stringify(list, null, 2)}\n` };
}

module.exports = { print, SDKS };
