<?php
// 10: the last 100 closed one-minute candles of a market and the one forming (final false), written to candles.csv.
//   php examples/10-candles-to-csv.php [BTC-USD]
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\Client;

$client = new Client();
$symbol = strtoupper($argv[1] ?? 'BTC-USD');
$market = $client->marketStream();
$market->subscribe('candles', [$symbol], ['interval' => '1m', 'history' => 100]);

$candles = [];
foreach ($market as $f) {
    if ($f['type'] === 'snapshot_end') {
        break;
    }
    if ($f['type'] === 'candles') {
        foreach ($f['data'] as $c) {
            $candles[$c['time']] = $c;
        }
    }
}
$market->close();
ksort($candles);

$csv = fopen('candles.csv', 'w');
fputcsv($csv, ['time', 'open', 'high', 'low', 'close', 'volume', 'final'], ',', '"', '');
foreach ($candles as $t => $c) {
    fputcsv($csv, [gmdate('Y-m-d\TH:i:s\Z', intdiv($t, 1000)), $c['open'], $c['high'], $c['low'], $c['close'], $c['volume'], $c['final'] ? 'true' : 'false'], ',', '"', '');
}
fclose($csv);
printf("%d candles of %s in candles.csv\n", count($candles), $symbol);
