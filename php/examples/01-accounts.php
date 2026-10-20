<?php
// 01: who you are (the key, its access and limits) and your accounts.
//   PHOENIXPERPS_API_KEY=ppk_test_... php examples/01-accounts.php
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\Client;

$client = new Client(); // the key: PHOENIXPERPS_API_KEY (a sandbox key: the sandbox)

$me = $client->me->get();
$c = $me->credential;
printf("%s \"%s\" on %s: %s access\n", $c->kind, $c->name, $c->environment, $c->access);
printf("limits: %d reads and %d trading calls a minute\n", $me->budgets->readPerMin, $me->budgets->tradePerMin);

foreach ($client->accounts->list()->data as $a) {
    printf("%s  %s  %s  balance %s\n", $a->id, $a->stage, $a->status, $a->balance ?? '-');
}
