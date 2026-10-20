<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\CreateStrategy;
use PhoenixPerps\Model\StrategiesListParams;
use PhoenixPerps\Model\Strategy;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The strategies calls ($client->strategies). */
final class StrategiesResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** Create a strategy. */
    public function create(string $accountId, CreateStrategy|array $body, ?RequestOptions $options = null): Strategy
    {
        return Strategy::fromArray($this->c->one(Ops::CREATE_STRATEGY, [$accountId], null, $body, $options) ?? []);
    }

    /** List strategies. */
    /** @return Page<Strategy>|null null on a 304 Not Modified */
    public function list(string $accountId, StrategiesListParams|array|null $params = null, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_STRATEGIES, [$accountId], $params, [Strategy::class, 'fromArray'], $options);
    }

    /** Every item of list, page after page (RequestOptions::$maxItems caps it). */
    /** @return \Generator<int, Strategy> */
    public function listAutoPaging(string $accountId, StrategiesListParams|array|null $params = null, ?RequestOptions $options = null): \Generator
    {
        return $this->list($accountId, $params, $options)?->autoPagingIterator($options?->maxItems) ?? (static fn (): \Generator => yield from [])();
    }

    /** Get a strategy. */
    /** @return Strategy|null null on a 304 Not Modified */
    public function get(string $accountId, string $strategyId, ?RequestOptions $options = null): ?Strategy
    {
        $d = $this->c->one(Ops::GET_STRATEGY, [$accountId, $strategyId], null, null, $options);
        return $d === null ? null : Strategy::fromArray($d);
    }

    /** Cancel a strategy. */
    public function cancel(string $accountId, string $strategyId, ?RequestOptions $options = null): Strategy
    {
        return Strategy::fromArray($this->c->one(Ops::CANCEL_STRATEGY, [$accountId, $strategyId], null, null, $options) ?? []);
    }
}
