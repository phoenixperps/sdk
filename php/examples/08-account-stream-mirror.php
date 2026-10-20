<?php
// 08: a live copy of your accounts' positions and working orders: the snapshot first, then every event applied once.
// After a reconnect the SDK resumes from the last event; a gap it cannot cover is told (onGap), and the copy is built
// again from the snapshot that follows. An account the server could not read just now comes with its error and is left
// out, never shown flat. Ctrl+C to stop.
//   php examples/08-account-stream-mirror.php
require __DIR__ . '/../vendor/autoload.php';

use PhoenixPerps\Client;

const FINAL_STATES = ['filled', 'canceled', 'rejected', 'expired'];

$client = new Client();
$mirror = []; // account => ['positions' => [market => position], 'orders' => [id => order]]
$unread = []; // account => why its snapshot is empty
$stream = $client->accountStream(onGap: function (?string $after) use (&$mirror, &$unread): void {
    printf("[gap] events after %s may be missing: the copy starts again from the next snapshot\n", $after ?? 'the start');
    [$mirror, $unread] = [[], []];
});

function show(array $mirror, string $why): void
{
    foreach ($mirror as $id => $m) {
        $positions = array_map(fn ($p) => "{$p['market']} {$p['side']} {$p['size']}", $m['positions']);
        printf("[%s] %s: positions %s; %d working orders\n", $why, $id, $positions ? implode(', ', $positions) : 'none', count($m['orders']));
    }
}

foreach ($stream as $e) { // blocks; each event once, after the accounts' snapshots
    if (isset($unread[$e->accountId])) {
        continue;
    }
    if (!isset($mirror[$e->accountId])) {
        $snap = $stream->snapshot($e->accountId) ?? ['positions' => [], 'orders' => []];
        if (isset($snap['error'])) { // its orders and positions are unknown, not empty
            $unread[$e->accountId] = $snap['error'];
            printf("[snapshot] %s: not readable now (%s): read it by REST\n", $e->accountId, $snap['error']);
            continue;
        }
        $mirror[$e->accountId] = [
            'positions' => array_column($snap['positions'], null, 'market'),
            'orders' => array_column($snap['orders'], null, 'id'),
        ];
    }
    $m = &$mirror[$e->accountId];
    if (isset($e->data['order'])) {
        $order = $e->data['order'];
        if (in_array($order['status'], FINAL_STATES, true)) {
            unset($m['orders'][$order['id']]);
        } else {
            $m['orders'][$order['id']] = $order;
        }
    }
    if (isset($e->data['position'])) {
        $position = $e->data['position'];
        if ($e->type === 'position.closed') {
            unset($m['positions'][$position['market']]);
        } else {
            $m['positions'][$position['market']] = $position;
        }
    }
    unset($m);
    show($mirror, $e->type);
}
