"""11: receive webhooks: every delivery is checked (its signature and its time) before it is trusted.

    PHOENIXPERPS_WEBHOOK_SECRET=ppwhsec_... python examples/11_webhook_receiver.py

Then point a webhook endpoint (dashboard, Developers, Webhooks) at https://<this machine>/webhooks (port 8080 here).
"""
import json
import os
from http.server import BaseHTTPRequestHandler, HTTPServer

from phoenixperps import WebhookVerificationError, Webhooks

SECRET = os.environ.get("PHOENIXPERPS_WEBHOOK_SECRET") or ""
if not SECRET:
    raise SystemExit("Set PHOENIXPERPS_WEBHOOK_SECRET to the endpoint's signing secret.")


class Receiver(BaseHTTPRequestHandler):
    def do_POST(self) -> None:
        if self.path != "/webhooks":
            self.send_response(404)
            self.end_headers()
            return
        raw = self.rfile.read(int(self.headers.get("Content-Length", 0)))
        try:
            event = Webhooks.verify(raw, dict(self.headers), SECRET)  # the body exactly as received
        except WebhookVerificationError as err:
            print(f"refused a delivery: {err.code}")
            self.send_response(400)
            self.end_headers()
            return
        test = " (a test)" if event.get("test") else ""  # webhook.ping (no account), or a delivery resent as a test
        print(f'{event["type"]}{test} on {event.get("account_id") or "-"}: {json.dumps(event["data"])}')
        self.send_response(200)
        self.end_headers()


print("listening on :8080/webhooks")
HTTPServer(("", 8080), Receiver).serve_forever()
