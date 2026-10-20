<?php
// 06: a position whose stop loss trails the price by 0.5% and moves to break-even once the price has gone 0.5% your
// way, all on the API's servers. Reads the stop every 5 s for 30 s to watch it move, then closes.
//   php examples/06-trailing-breakeven.php
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\Client;

$client = new Client();
if ($client->environment === 'live' && getenv('PHOENIXPERPS_ALLOW_LIVE') !== 'yes') {
    fwrite(STDERR, "This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).\n");
    exit(1);
}
$account = $client->accounts->list()->data[0]->id;
$btc = $client->markets->get('BTC-USD');

$order = $client->orders->create($account, [
    'market' => 'BTC-USD',
    'side' => 'buy',
    'type' => 'market',
    'size' => $btc->minSize,
    'exits' => [
        'stop_loss' => [
            'percent' => '1',
            'trail' => ['percent' => '0.5'],
            'break_even' => ['trigger_percent' => '0.5', 'offset_ticks' => 2],
        ],
    ],
]);
printf("bought %s BTC (%s); watching the stop for 30 s\n", $order->size, $order->status);

for ($i = 0; $i < 6; $i++) {
    sleep(5);
    foreach ($client->exits->get($account, 'BTC-USD')->stopLoss ?? [] as $sl) {
        printf("%s stop loss at %s (%s)\n", date('H:i:s'), $sl->price ?? '-', $sl->status);
    }
}

$closed = $client->positions->close($account, 'BTC-USD', []);
printf("closed: %s\n", $closed->status);
