<?php
// 05: a market order with its exits: a take profit 1% away and a stop loss 0.5% away, placed by the API the moment
// it fills and kept on its servers. The position is closed at the end.
//   php examples/05-market-with-brackets.php
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
        'take_profit' => [['percent' => '1']],
        'stop_loss' => ['percent' => '0.5'],
    ],
]);
printf("market order %s: %s at %s\n", $order->id, $order->status, $order->averageFillPrice ?? '-');

$exits = $client->exits->get($account, 'BTC-USD');
foreach ($exits->takeProfit ?? [] as $tp) {
    printf("take profit: %s at %s (%s)\n", $tp->size, $tp->price ?? '-', $tp->status);
}
foreach ($exits->stopLoss ?? [] as $sl) {
    printf("stop loss:   %s at %s (%s)\n", $sl->size, $sl->price ?? '-', $sl->status);
}

$closed = $client->positions->close($account, 'BTC-USD', []);
printf("closed: %s %s (the exits went with the position)\n", $closed->id, $closed->status);
