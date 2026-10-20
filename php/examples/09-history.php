<?php
// 09: an account's history: the equity curve by day, the result of each trading day, closed positions and fills.
//   php examples/09-history.php
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\Client;
use PhoenixPerps\RequestOptions;

$client = new Client();
$account = $client->accounts->list()->data[0]->id;

$equity = $client->history->equity($account, ['resolution' => 'day']);
printf("equity: %d points\n", count($equity->points));

foreach ($client->history->daily($account)->data as $d) {
    printf("%s: %d trades, P&L %s, fees %s\n", $d->date, $d->trades, $d->pnl, $d->fees);
}
foreach ($client->positions->listClosed($account, ['limit' => 10])->data as $p) {
    printf("closed %s %s %s: %s -> %s, P&L %s\n", $p->market, $p->side, $p->size, $p->entryPrice ?? '-', $p->exitPrice ?? '-', $p->pnl);
}
$n = 0;
foreach ($client->fills->listAutoPaging($account, null, new RequestOptions(maxItems: 100)) as $f) { // every page, at most 100
    if (++$n <= 5) {
        printf("fill %s %s %s @ %s\n", $f->market, $f->side, $f->size, $f->price);
    }
}
printf("%d fills read\n", $n);
