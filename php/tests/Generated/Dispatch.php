<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Tests\Generated;

use PhoenixPerps\Client;
use PhoenixPerps\RequestOptions;

/** Makes the call a conformance scenario names (resource + method) through the typed client. */
final class Dispatch
{
    /**
     * @param array<string, mixed> $call
     * @return array{result?: mixed, items?: list<string>}
     */
    public static function run(Client $c, array $call, RequestOptions $options): array
    {
        switch ($call['resource'] . '.' . $call['method']) {
            case 'platform.info':
                return ['result' => $c->platform->info($options)?->toArray()];
            case 'platform.spec':
                return ['result' => $c->platform->spec($options)];
            case 'platform.status':
                return ['result' => $c->platform->status($options)?->toArray()];
            case 'platform.limits':
                return ['result' => $c->platform->limits($options)?->toArray()];
            case 'me.get':
                return ['result' => $c->me->get($options)?->toArray()];
            case 'markets.list':
                $page = $c->markets->list($options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'markets.stats':
                $page = $c->markets->stats($options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'markets.get':
                return ['result' => $c->markets->get($call['path']['market'], $options)?->toArray()];
            case 'accounts.list':
                $page = $c->accounts->list($options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'accounts.createSandbox':
                return ['result' => $c->accounts->createSandbox($options)->toArray()];
            case 'accounts.get':
                return ['result' => $c->accounts->get($call['path']['account_id'], $options)?->toArray()];
            case 'accounts.risk':
                return ['result' => $c->accounts->risk($call['path']['account_id'], $options)?->toArray()];
            case 'accounts.tradingPolicy':
                return ['result' => $c->accounts->tradingPolicy($call['path']['account_id'], $options)?->toArray()];
            case 'accounts.progress':
                return ['result' => $c->accounts->progress($call['path']['account_id'], $options)?->toArray()];
            case 'history.equity':
                return ['result' => $c->history->equity($call['path']['account_id'], $call['query'] ?? null, $options)?->toArray()];
            case 'history.daily':
                $page = $c->history->daily($call['path']['account_id'], $options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'margin.list':
                $page = $c->margin->list($call['path']['account_id'], $options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'positions.list':
                $page = $c->positions->list($call['path']['account_id'], $call['query'] ?? null, $options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'positions.get':
                return ['result' => $c->positions->get($call['path']['account_id'], $call['path']['market'], $options)?->toArray()];
            case 'positions.listClosed':
                if (!empty($call['iterate'])) {
                    return ['items' => self::ids($c->positions->listClosedAutoPaging($call['path']['account_id'], $call['query'] ?? null, $options))];
                }
                $page = $c->positions->listClosed($call['path']['account_id'], $call['query'] ?? null, $options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'orders.list':
                if (!empty($call['iterate'])) {
                    return ['items' => self::ids($c->orders->listAutoPaging($call['path']['account_id'], $call['query'] ?? null, $options))];
                }
                $page = $c->orders->list($call['path']['account_id'], $call['query'] ?? null, $options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'orders.create':
                return ['result' => $c->orders->create($call['path']['account_id'], $call['body'] ?? [], $options)->toArray()];
            case 'orders.get':
                return ['result' => $c->orders->get($call['path']['account_id'], $call['path']['order_id'], $options)?->toArray()];
            case 'orders.modify':
                return ['result' => $c->orders->modify($call['path']['account_id'], $call['path']['order_id'], $call['body'] ?? [], $options)->toArray()];
            case 'orders.cancel':
                return ['result' => $c->orders->cancel($call['path']['account_id'], $call['path']['order_id'], $options)->toArray()];
            case 'orders.preview':
                return ['result' => $c->orders->preview($call['path']['account_id'], $call['body'] ?? [], $options)->toArray()];
            case 'orders.cancelAll':
                return ['result' => $c->orders->cancelAll($call['path']['account_id'], $call['body'] ?? [], $options)->toArray()];
            case 'positions.close':
                return ['result' => $c->positions->close($call['path']['account_id'], $call['path']['market'], $call['body'] ?? [], $options)->toArray()];
            case 'positions.flatten':
                return ['result' => $c->positions->flatten($call['path']['account_id'], $options)->toArray()];
            case 'margin.set':
                return ['result' => $c->margin->set($call['path']['account_id'], $call['path']['market'], $call['body'] ?? [], $options)->toArray()];
            case 'events.list':
                if (!empty($call['iterate'])) {
                    return ['items' => self::ids($c->events->listAutoPaging($call['query'] ?? null, $options))];
                }
                $page = $c->events->list($call['query'] ?? null, $options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'exits.get':
                return ['result' => $c->exits->get($call['path']['account_id'], $call['path']['market'], $options)?->toArray()];
            case 'exits.set':
                return ['result' => $c->exits->set($call['path']['account_id'], $call['path']['market'], $call['body'] ?? [], $options)->toArray()];
            case 'orders.setExits':
                return ['result' => $c->orders->setExits($call['path']['account_id'], $call['path']['order_id'], $call['body'] ?? [], $options)->toArray()];
            case 'strategies.create':
                return ['result' => $c->strategies->create($call['path']['account_id'], $call['body'] ?? [], $options)->toArray()];
            case 'strategies.list':
                if (!empty($call['iterate'])) {
                    return ['items' => self::ids($c->strategies->listAutoPaging($call['path']['account_id'], $call['query'] ?? null, $options))];
                }
                $page = $c->strategies->list($call['path']['account_id'], $call['query'] ?? null, $options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'strategies.get':
                return ['result' => $c->strategies->get($call['path']['account_id'], $call['path']['strategy_id'], $options)?->toArray()];
            case 'strategies.cancel':
                return ['result' => $c->strategies->cancel($call['path']['account_id'], $call['path']['strategy_id'], $options)->toArray()];
            case 'webhookEndpoints.list':
                $page = $c->webhookEndpoints->list($options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'webhookEndpoints.get':
                return ['result' => $c->webhookEndpoints->get($call['path']['endpoint_id'], $options)?->toArray()];
            case 'webhookEndpoints.delete':
                return ['result' => $c->webhookEndpoints->delete($call['path']['endpoint_id'], $options)->toArray()];
            case 'webhookEndpoints.test':
                return ['result' => $c->webhookEndpoints->test($call['path']['endpoint_id'], $options)->toArray()];
            case 'webhookEndpoints.listDeliveries':
                if (!empty($call['iterate'])) {
                    return ['items' => self::ids($c->webhookEndpoints->listDeliveriesAutoPaging($call['path']['endpoint_id'], $call['query'] ?? null, $options))];
                }
                $page = $c->webhookEndpoints->listDeliveries($call['path']['endpoint_id'], $call['query'] ?? null, $options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'webhookEndpoints.getDelivery':
                return ['result' => $c->webhookEndpoints->getDelivery($call['path']['endpoint_id'], $call['path']['delivery_id'], $options)?->toArray()];
            case 'webhookEndpoints.resendDelivery':
                return ['result' => $c->webhookEndpoints->resendDelivery($call['path']['endpoint_id'], $call['path']['delivery_id'], $options)->toArray()];
            case 'webhookEndpoints.disable':
                return ['result' => $c->webhookEndpoints->disable($call['path']['endpoint_id'], $options)->toArray()];
            case 'webhookEndpoints.enable':
                return ['result' => $c->webhookEndpoints->enable($call['path']['endpoint_id'], $options)->toArray()];
            case 'tradingview.listHooks':
                $page = $c->tradingView->listHooks($options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'tradingview.getHook':
                return ['result' => $c->tradingView->getHook($call['path']['hook_id'], $options)?->toArray()];
            case 'tradingview.deleteHook':
                return ['result' => $c->tradingView->deleteHook($call['path']['hook_id'], $options)->toArray()];
            case 'tradingview.listAlerts':
                if (!empty($call['iterate'])) {
                    return ['items' => self::ids($c->tradingView->listAlertsAutoPaging($call['path']['hook_id'], $call['query'] ?? null, $options))];
                }
                $page = $c->tradingView->listAlerts($call['path']['hook_id'], $call['query'] ?? null, $options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'tradingview.getAlert':
                return ['result' => $c->tradingView->getAlert($call['path']['hook_id'], $call['path']['alert_id'], $options)?->toArray()];
            case 'tradingview.disableHook':
                return ['result' => $c->tradingView->disableHook($call['path']['hook_id'], $options)->toArray()];
            case 'tradingview.enableHook':
                return ['result' => $c->tradingView->enableHook($call['path']['hook_id'], $options)->toArray()];
            case 'tradingview.parse':
                return ['result' => $c->tradingView->parse($call['body'] ?? [], $options)->toArray()];
            case 'webhookEndpoints.eventTypes':
                $page = $c->webhookEndpoints->eventTypes($options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
            case 'events.get':
                return ['result' => $c->events->get($call['path']['event_id'], $options)?->toArray()];
            case 'fills.list':
                if (!empty($call['iterate'])) {
                    return ['items' => self::ids($c->fills->listAutoPaging($call['path']['account_id'], $call['query'] ?? null, $options))];
                }
                $page = $c->fills->list($call['path']['account_id'], $call['query'] ?? null, $options);
                return ['result' => $page === null ? null : array_map(fn ($x) => $x->toArray(), $page->data)];
        }
        throw new \InvalidArgumentException('no operation ' . $call['resource'] . '.' . $call['method']);
    }

    /** @return list<string> */
    private static function ids(\Generator $items): array
    {
        $ids = [];
        foreach ($items as $item) {
            $ids[] = (string) ($item->toArray()['id'] ?? '');
        }
        return $ids;
    }
}
