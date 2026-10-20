'use strict';
const crypto = require('node:crypto');
const fs = require('node:fs');
const path = require('node:path');

/*
 * The vendored spec (spec/openapi.json) from the API's own (the api repo beside this one, or a path given), pinned in
 * spec/SPEC_SOURCE with its version and sha256. Then run node generator.
 *   node scripts/sync-spec.js [path/to/openapi.json]
 */
const ROOT = path.join(__dirname, '..');
const from = process.argv[2] || path.join(ROOT, '..', 'api', 'openapi', 'v1.json');
const text = fs.readFileSync(from, 'utf8');
const spec = JSON.parse(text);
fs.writeFileSync(path.join(ROOT, 'spec', 'openapi.json'), text);
const sha = crypto.createHash('sha256').update(text).digest('hex');
fs.writeFileSync(path.join(ROOT, 'spec', 'SPEC_SOURCE'), `version ${spec.info.version}\nsha256 ${sha}\n`);
process.stdout.write(`spec ${spec.info.version} ${sha.slice(0, 12)}\n`);
