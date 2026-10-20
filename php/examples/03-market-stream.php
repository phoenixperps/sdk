<?php
// 03: live market data: prices for two markets and the trades of one, until 20 frames came.
//   php examples/03-market-stream.php
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\Client;

$client = new Client();
$market = $client->marketStream(); // subscriptions are made again after a reconnect
$market->subscribe('ticker', ['BTC-USD', 'ETH-USD'], ['interval_ms' => 500]);
$market->subscribe('trades', ['BTC-USD'], ['history' => 5]);

$n = 0;
foreach ($market as $f) {
    if ($f['type'] === 'ticker') {
        printf("ticker %s: bid %s ask %s\n", $f['symbol'], $f['data']['bid'], $f['data']['ask']);
    } elseif ($f['type'] === 'trades') {
        foreach ($f['data'] as $t) {
            printf("trade  %s: %s %s @ %s\n", $f['symbol'], $t['side'], $t['size'], $t['price']);
        }
    } else {
        continue;
    }
    if (++$n >= 20) {
        break;
    }
}
$market->close();
