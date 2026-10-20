<?php
// 12: what failures look like: each has a class by family, the API's code, the request id to quote to support, and
// details. A write whose outcome is unknown (a timeout) is repeated with the same Idempotency-Key, never a new one.
//   php examples/12-error-handling.php
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\Client;
use PhoenixPerps\Exception\ApiTimeoutException;
use PhoenixPerps\Exception\NotFoundException;
use PhoenixPerps\Exception\PhoenixPerpsException;
use PhoenixPerps\Exception\RateLimitException;
use PhoenixPerps\Exception\RuleRejectedException;
use PhoenixPerps\Exception\ValidationException;
use PhoenixPerps\Model\Order;
use PhoenixPerps\RequestOptions;

$client = new Client();
$account = $client->accounts->list()->data[0]->id;

function show(PhoenixPerpsException $err): void
{
    printf("%s: %s (HTTP %s, %s) %s\n", (new ReflectionClass($err))->getShortName(), $err->errorCode, $err->status, $err->requestId, $err->getMessage());
    if ($err->details !== null) {
        printf("  details: %s\n", json_encode($err->details));
    }
}

try {
    $client->orders->get($account, 'ord_does_not_exist');
} catch (NotFoundException $err) {
    show($err);
}

try {
    $client->orders->preview($account, ['market' => 'BTC-USD', 'side' => 'buy', 'type' => 'market', 'size' => '0.0000001']);
} catch (ValidationException | RuleRejectedException $err) {
    show($err);
}

/** A write that may or may not have happened: the same key again is safe, a new key could place it twice. */
function placeOnce(Client $client, string $account, array $body): Order
{
    $options = new RequestOptions(idempotencyKey: bin2hex(random_bytes(16)));
    for ($attempt = 1; ; $attempt++) {
        try {
            return $client->orders->create($account, $body, $options);
        } catch (ApiTimeoutException $err) {
            if ($attempt >= 3) {
                throw $err; // still unknown after the SDK's own retries and ours: read the orders first
            }
        } catch (RateLimitException $err) {
            if ($attempt >= 3 || $err->retryAfter === null) {
                throw $err;
            }
            usleep((int) ($err->retryAfter * 1_000_000));
        }
    }
}
