<?php
// 07: what an order would do now, without placing it: fill or rest, the price, the fee, the margin, the position
// after. Nothing is sent to the market.
//   php examples/07-what-if.php
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\Client;

$client = new Client();
$account = $client->accounts->list()->data[0]->id;

$w = $client->orders->preview($account, ['market' => 'BTC-USD', 'side' => 'buy', 'type' => 'market', 'notional_usd' => '1000']);
printf("result:         %s%s\n", $w->result, $w->reject ? " ({$w->reject->code})" : '');
printf("size:           %s\n", $w->size ?? '-');
printf("price:          %s (worst %s)\n", $w->execution?->expectedPrice ?? '-', $w->execution?->worstPrice ?? '-');
printf("notional, fee:  %s, %s\n", $w->notional ?? '-', $w->fee ?? '-');
printf("margin:         %s needed, %s left after\n", $w->margin?->required ?? '-', $w->margin?->availableAfter ?? '-');
printf("position after: %s %s\n", $w->positionAfter?->side ?? 'flat', $w->positionAfter?->size ?? '');
