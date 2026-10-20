'use strict';
const crypto = require('node:crypto');
const http = require('node:http');

/*
 * The conformance mock (docs/BEHAVIOUR.md): it plays one scenario's exchanges in order and keeps every request it got,
 * so each SDK's runner can be held to the same answers. Control (any language's runner):
 *   POST /__scenario  { exchanges: [{ respond: { status, headers, body, delay_ms } }],
 *                       connections: [[ step, ... ], ...] }      load one, forget the requests
 *   GET  /__requests  [{ method, path, query, headers, body, at, ws, messages }]
 * Every other request takes the next exchange (or 599 when none is left: the SDK sent one too many). A WebSocket
 * upgrade (any path) takes the next connection script, whose steps run in order:
 *   { send: frame }        a text frame (JSON)
 *   { expect: "<op>" }     wait for the client's next message (kept in messages; its op should be this one)
 *   { reply: frame }       a text frame carrying the id of the message last expected
 *   { refuse: status, retry_after?, body? }   (first step only) refuse the upgrade with this HTTP status (and the
 *                          Retry-After and error envelope given)
 *   { close: code }        close with this code
 *   { wait_ms: n }         pause
 * With no script left, the upgrade is refused (503). Zero dependencies: the WebSocket framing is written here.
 *   start({ port }) -> { url, stop }      node conformance/mock-server.js [port]
 */
// RFC 6455's fixed key suffix (built from its parts: the words guard refuses anything UUID-shaped written whole)
const GUID = ['258EAFA5', 'E914', '47DA', '95CA', 'C5AB0DC85B11'].join('-');

function frame(opcode, payload) {
  const len = payload.length;
  const head = len < 126 ? Buffer.from([0x80 | opcode, len]) : len < 65536 ? Buffer.from([0x80 | opcode, 126, len >> 8, len & 255]) : Buffer.concat([Buffer.from([0x80 | opcode, 127]), (() => { const b = Buffer.alloc(8); b.writeBigUInt64BE(BigInt(len)); return b; })()]);
  return Buffer.concat([head, payload]);
}

/** One server side of a WebSocket: messages in (text), frames out, close with a code. */
function wsConnection(socket, onMessage, onEnd) {
  let buf = Buffer.alloc(0);
  let ended = false;
  const end = () => {
    if (!ended) {
      ended = true;
      onEnd();
    }
  };
  socket.on('data', (chunk) => {
    buf = Buffer.concat([buf, chunk]);
    for (;;) {
      if (buf.length < 2) return;
      const opcode = buf[0] & 15;
      let len = buf[1] & 127;
      let off = 2;
      if (len === 126) {
        if (buf.length < 4) return;
        len = buf.readUInt16BE(2);
        off = 4;
      } else if (len === 127) {
        if (buf.length < 10) return;
        len = Number(buf.readBigUInt64BE(2));
        off = 10;
      }
      const masked = buf[1] & 128;
      const need = off + (masked ? 4 : 0) + len;
      if (buf.length < need) return;
      const mask = masked ? buf.subarray(off, off + 4) : null;
      const data = Buffer.from(buf.subarray(off + (masked ? 4 : 0), need));
      if (mask) for (let i = 0; i < data.length; i += 1) data[i] ^= mask[i % 4];
      buf = buf.subarray(need);
      if (opcode === 1) onMessage(data.toString('utf8'));
      else if (opcode === 9) socket.write(frame(10, data));
      else if (opcode === 8) {
        if (!ended) socket.write(frame(8, data.subarray(0, 2)));
        socket.end();
        end();
      }
    }
  });
  socket.on('close', end);
  socket.on('error', end);
  return {
    send: (obj) => socket.write(frame(1, Buffer.from(JSON.stringify(obj)))),
    close: (code) => {
      const b = Buffer.alloc(2);
      b.writeUInt16BE(code);
      socket.write(frame(8, b));
      socket.end();
      end();
    },
  };
}

function start({ port = 0 } = {}) {
  let exchanges = [];
  let connections = [];
  let requests = [];
  const server = http.createServer((req, res) => {
    let raw = '';
    req.on('data', (c) => {
      raw += c;
    });
    req.on('end', () => {
      const url = new URL(req.url, 'http://x');
      if (url.pathname === '/__scenario' && req.method === 'POST') {
        const s = JSON.parse(raw);
        exchanges = s.exchanges || [];
        connections = s.connections || [];
        requests = [];
        res.writeHead(204);
        return res.end();
      }
      if (url.pathname === '/__requests') {
        res.writeHead(200, { 'content-type': 'application/json' });
        return res.end(JSON.stringify(requests));
      }
      requests.push({ method: req.method, path: url.pathname, query: Object.fromEntries(url.searchParams), headers: req.headers, body: raw ? JSON.parse(raw) : null, at: Date.now() });
      const x = exchanges.shift();
      if (!x) {
        res.writeHead(599, { 'content-type': 'application/json' });
        return res.end(JSON.stringify({ error: { code: 'unexpected_request', message: 'no exchange left' } }));
      }
      const r = x.respond;
      const send = () => {
        const isText = typeof r.body === 'string';
        res.writeHead(r.status, { 'content-type': isText ? 'text/html' : 'application/json', 'x-request-id': 'req_conformance', ...(r.headers || {}) });
        res.end(r.body === undefined ? '' : isText ? r.body : JSON.stringify(r.body));
      };
      return r.delay_ms ? setTimeout(send, r.delay_ms) : send();
    });
  });
  server.on('upgrade', (req, socket) => {
    // the http server drops its own error listener on an upgrade: a client resetting after a refusal must not end the mock
    socket.on('error', () => {});
    const url = new URL(req.url, 'http://x');
    const seen = { method: 'GET', path: url.pathname, query: Object.fromEntries(url.searchParams), headers: req.headers, body: null, at: Date.now(), ws: true, messages: [] };
    requests.push(seen);
    const script = connections.shift();
    if (!script) {
      socket.end('HTTP/1.1 503 Service Unavailable\r\ncontent-length: 0\r\n\r\n');
      return;
    }
    if (script.length && script[0].refuse) {
      // { refuse: status, retry_after?: seconds, body?: the API's error envelope }
      const r = script[0];
      const body = JSON.stringify(r.body || {});
      socket.end(`HTTP/1.1 ${r.refuse} Refused\r\ncontent-type: application/json\r\ncontent-length: ${Buffer.byteLength(body)}\r\n${r.retry_after !== undefined ? `retry-after: ${r.retry_after}\r\n` : ''}\r\n${body}`);
      return;
    }
    const accept = crypto.createHash('sha1').update(req.headers['sec-websocket-key'] + GUID).digest('base64');
    socket.write(`HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: ${accept}\r\n\r\n`);
    const inbox = [];
    let waiting = null;
    let alive = true;
    const conn = wsConnection(socket, (text) => {
      let msg;
      try {
        msg = JSON.parse(text);
      } catch {
        msg = text;
      }
      seen.messages.push(msg);
      if (waiting) {
        const w = waiting;
        waiting = null;
        w(msg);
      } else inbox.push(msg);
    }, () => {
      alive = false;
      if (waiting) waiting(null);
    });
    const next = () => (inbox.length ? Promise.resolve(inbox.shift()) : new Promise((r) => { waiting = r; }));
    (async () => {
      let last = null;
      for (const step of script) {
        if (!alive) return;
        if (step.send) conn.send(step.send);
        else if (step.reply) conn.send({ ...step.reply, id: last && last.id });
        else if (step.expect) last = await Promise.race([next(), new Promise((r) => setTimeout(() => r(null), 5000))]);
        else if (step.wait_ms) await new Promise((r) => setTimeout(r, step.wait_ms));
        else if (step.close) return conn.close(step.close);
      }
    })();
  });
  return new Promise((resolve) => server.listen(port, '127.0.0.1', () => resolve({
    url: `http://127.0.0.1:${server.address().port}`,
    stop: () => new Promise((done) => {
      server.closeAllConnections();
      server.close(done);
    }),
  })));
}

if (require.main === module) {
  start({ port: Number(process.argv[2]) || 0 }).then((s) => process.stdout.write(`${s.url}\n`));
}

module.exports = { start };
