<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\AlertsListParams;
use PhoenixPerps\Model\ChangePriceAlert;
use PhoenixPerps\Model\CreatePriceAlert;
use PhoenixPerps\Model\DeletedAlert;
use PhoenixPerps\Model\PriceAlert;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The alerts calls ($client->alerts). */
final class AlertsResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** Create a price alert. */
    public function create(string $accountId, CreatePriceAlert|array $body, ?RequestOptions $options = null): PriceAlert
    {
        return PriceAlert::fromArray($this->c->one(Ops::CREATE_ALERT, [$accountId], null, $body, $options) ?? []);
    }

    /** List price alerts. */
    /** @return Page<PriceAlert>|null null on a 304 Not Modified */
    public function list(string $accountId, AlertsListParams|array|null $params = null, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_ALERTS, [$accountId], $params, [PriceAlert::class, 'fromArray'], $options);
    }

    /** Every item of list, page after page (RequestOptions::$maxItems caps it). */
    /** @return \Generator<int, PriceAlert> */
    public function listAutoPaging(string $accountId, AlertsListParams|array|null $params = null, ?RequestOptions $options = null): \Generator
    {
        return $this->list($accountId, $params, $options)?->autoPagingIterator($options?->maxItems) ?? (static fn (): \Generator => yield from [])();
    }

    /** Get a price alert. */
    /** @return PriceAlert|null null on a 304 Not Modified */
    public function get(string $accountId, string $alertId, ?RequestOptions $options = null): ?PriceAlert
    {
        $d = $this->c->one(Ops::GET_ALERT, [$accountId, $alertId], null, null, $options);
        return $d === null ? null : PriceAlert::fromArray($d);
    }

    /** Move a price alert. */
    public function modify(string $accountId, string $alertId, ChangePriceAlert|array $body = [], ?RequestOptions $options = null): PriceAlert
    {
        return PriceAlert::fromArray($this->c->one(Ops::CHANGE_ALERT, [$accountId, $alertId], null, $body, $options) ?? []);
    }

    /** Delete a price alert. */
    public function delete(string $accountId, string $alertId, ?RequestOptions $options = null): DeletedAlert
    {
        return DeletedAlert::fromArray($this->c->one(Ops::DELETE_ALERT, [$accountId, $alertId], null, null, $options) ?? []);
    }

    /** Arm a price alert again. */
    public function rearm(string $accountId, string $alertId, ?RequestOptions $options = null): PriceAlert
    {
        return PriceAlert::fromArray($this->c->one(Ops::REARM_ALERT, [$accountId, $alertId], null, null, $options) ?? []);
    }
}
