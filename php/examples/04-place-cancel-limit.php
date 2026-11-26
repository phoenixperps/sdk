<?php
// 04: a limit order far from the market, found again by your own id, then canceled. The Idempotency-Key makes a
// retried request return the first order instead of placing a second one.
//   php examples/04-place-cancel-limit.php
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\Client;
use PhoenixPerps\MarketRules;
use PhoenixPerps\RequestOptions;

$client = new Client();
if ($client->environment === 'live' && getenv('PHOENIXPERPS_ALLOW_LIVE') !== 'yes') {
    fwrite(STDERR, "This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).\n");
    exit(1);
}
$account = $client->accounts->list()->data[0]->id;
$rules = new MarketRules($client->markets->get('BTC-USD'));

$market = $client->marketStream();
$market->subscribe('ticker', ['BTC-USD']);
$bid = null;
foreach ($market as $f) {
    if ($f['type'] === 'ticker') {
        $bid = $f['data']['bid'];
        break;
    }
}
$market->close();

$clientOrderId = 'example-04-' . (int) (microtime(true) * 1000);
$order = $client->orders->create($account, [
    'market' => 'BTC-USD',
    'side' => 'buy',
    'type' => 'limit',
    'size' => $rules->sizeForNotional('100', $bid), // about 100 USD
    'limit_price' => $rules->roundPrice(bcmul($bid, '0.9', 8), MarketRules::DOWN), // 10% below: it rests
    'client_order_id' => $clientOrderId,
], new RequestOptions(idempotencyKey: bin2hex(random_bytes(16))));
printf("placed %s: %s, %s at %s\n", $order->id, $order->status, $order->size, $order->limitPrice);

// found again by your own id; whatever the lookup answers, the cancel goes by the id the order answered
try {
    $found = $client->orders->list($account, ['client_order_id' => $clientOrderId])->data;
    echo $found ? "found by client_order_id: {$found[0]->id}\n" : "not listed yet\n";
} catch (PhoenixPerps\Exception\PhoenixPerpsException $e) {
    printf("the lookup failed (%s): canceling by the order's id\n", $e->getMessage());
}

$canceled = $client->orders->cancel($account, $order->id);
printf("canceled: %s (%s)\n", $canceled->status, $canceled->cancelReason);
