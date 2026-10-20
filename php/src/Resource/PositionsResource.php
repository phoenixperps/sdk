<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\ClosePosition;
use PhoenixPerps\Model\ClosedPosition;
use PhoenixPerps\Model\FlattenResult;
use PhoenixPerps\Model\Order;
use PhoenixPerps\Model\Position;
use PhoenixPerps\Model\PositionsListClosedParams;
use PhoenixPerps\Model\PositionsListParams;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The positions calls ($client->positions). */
final class PositionsResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** List open positions. */
    /** @return Page<Position>|null null on a 304 Not Modified */
    public function list(string $accountId, PositionsListParams|array|null $params = null, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_POSITIONS, [$accountId], $params, [Position::class, 'fromArray'], $options);
    }

    /** Get an open position. */
    /** @return Position|null null on a 304 Not Modified */
    public function get(string $accountId, string $market, ?RequestOptions $options = null): ?Position
    {
        $d = $this->c->one(Ops::GET_POSITION, [$accountId, $market], null, null, $options);
        return $d === null ? null : Position::fromArray($d);
    }

    /** List closed positions. */
    /** @return Page<ClosedPosition>|null null on a 304 Not Modified */
    public function listClosed(string $accountId, PositionsListClosedParams|array|null $params = null, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_CLOSED_POSITIONS, [$accountId], $params, [ClosedPosition::class, 'fromArray'], $options);
    }

    /** Every item of listClosed, page after page (RequestOptions::$maxItems caps it). */
    /** @return \Generator<int, ClosedPosition> */
    public function listClosedAutoPaging(string $accountId, PositionsListClosedParams|array|null $params = null, ?RequestOptions $options = null): \Generator
    {
        return $this->listClosed($accountId, $params, $options)?->autoPagingIterator($options?->maxItems) ?? (static fn (): \Generator => yield from [])();
    }

    /** Close a position. */
    public function close(string $accountId, string $market, ClosePosition|array $body, ?RequestOptions $options = null): Order
    {
        return Order::fromArray($this->c->one(Ops::CLOSE_POSITION, [$accountId, $market], null, $body, $options) ?? []);
    }

    /** Flatten the account. */
    public function flatten(string $accountId, ?RequestOptions $options = null): FlattenResult
    {
        return FlattenResult::fromArray($this->c->one(Ops::FLATTEN, [$accountId], null, null, $options) ?? []);
    }
}
