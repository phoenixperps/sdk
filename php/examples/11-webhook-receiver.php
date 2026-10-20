<?php
// 11: receive webhooks: every delivery is checked (its signature and its time) before it is trusted.
//   PHOENIXPERPS_WEBHOOK_SECRET=ppwhsec_... php -S 0.0.0.0:8080 examples/11-webhook-receiver.php
// Then point a webhook endpoint (dashboard, Developers, Webhooks) at https://<this machine>/webhooks (port 8080 here).
// Behind your web server, this file is the handler of that address.
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\WebhookVerificationException;
use PhoenixPerps\Webhooks;

$secret = getenv('PHOENIXPERPS_WEBHOOK_SECRET');
if (!$secret) {
    http_response_code(500);
    error_log("Set PHOENIXPERPS_WEBHOOK_SECRET to the endpoint's signing secret.");
    exit;
}
if ($_SERVER['REQUEST_METHOD'] !== 'POST' || parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH) !== '/webhooks') {
    http_response_code(404);
    exit;
}

$raw = file_get_contents('php://input'); // the body exactly as received
$headers = array_change_key_case(getallheaders(), CASE_LOWER);
try {
    $event = Webhooks::verify($raw, $headers, $secret); // the WebhookPayload: the event, its environment, test
    // a webhook.ping (the Test button) names no account
    error_log(sprintf('%s on %s (%s): %s', $event->type, $event->accountId ?? 'no account', $event->environment, json_encode($event->data)));
    http_response_code(200);
} catch (WebhookVerificationException $err) {
    error_log("refused a delivery: {$err->errorCode}"); // missing_headers, stale, bad_signature or bad_body
    http_response_code(400);
}
