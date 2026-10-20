<?php
// 02: the markets and their rules, then a quote (bid, ask, last) from the market stream.
//   php examples/02-markets.php [BTC-USD]
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\Client;

$client = new Client();
$symbol = strtoupper($argv[1] ?? 'BTC-USD');

foreach ($client->markets->list()->data as $m) {
    printf("%-10s tick %s  step %s  min %s  up to %dx  %s\n", $m->market, $m->tickSize, $m->sizeStep, $m->minSize, $m->maxLeverage, $m->status);
}

$market = $client->marketStream();
$reply = $market->subscribe('ticker', [$symbol], ['interval_ms' => 250]);
if (!empty($reply['rejected'])) {
    fwrite(STDERR, "{$symbol}: {$reply['rejected'][0]['code']}\n");
    exit(1);
}
foreach ($market as $frame) {
    if ($frame['type'] !== 'ticker') {
        continue;
    }
    $q = $frame['data'];
    printf("%s: bid %s  ask %s  last %s\n", $symbol, $q['bid'], $q['ask'], $q['last']);
    break;
}
$market->close();
