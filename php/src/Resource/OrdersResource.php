<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\CancelAll;
use PhoenixPerps\Model\CancelAllResult;
use PhoenixPerps\Model\CreateOrder;
use PhoenixPerps\Model\ModifyOrder;
use PhoenixPerps\Model\Order;
use PhoenixPerps\Model\OrderExits;
use PhoenixPerps\Model\OrdersListParams;
use PhoenixPerps\Model\WhatIf;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The orders calls ($client->orders). */
final class OrdersResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** List orders. */
    /** @return Page<Order>|null null on a 304 Not Modified */
    public function list(string $accountId, OrdersListParams|array|null $params = null, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_ORDERS, [$accountId], $params, [Order::class, 'fromArray'], $options);
    }

    /** Every item of list, page after page (RequestOptions::$maxItems caps it). */
    /** @return \Generator<int, Order> */
    public function listAutoPaging(string $accountId, OrdersListParams|array|null $params = null, ?RequestOptions $options = null): \Generator
    {
        return $this->list($accountId, $params, $options)?->autoPagingIterator($options?->maxItems) ?? (static fn (): \Generator => yield from [])();
    }

    /** Place an order. */
    public function create(string $accountId, CreateOrder|array $body, ?RequestOptions $options = null): Order
    {
        return Order::fromArray($this->c->one(Ops::PLACE_ORDER, [$accountId], null, $body, $options) ?? []);
    }

    /** Get an order. */
    /** @return Order|null null on a 304 Not Modified */
    public function get(string $accountId, string $orderId, ?RequestOptions $options = null): ?Order
    {
        $d = $this->c->one(Ops::GET_ORDER, [$accountId, $orderId], null, null, $options);
        return $d === null ? null : Order::fromArray($d);
    }

    /** Change an order. */
    public function modify(string $accountId, string $orderId, ModifyOrder|array $body, ?RequestOptions $options = null): Order
    {
        return Order::fromArray($this->c->one(Ops::MODIFY_ORDER, [$accountId, $orderId], null, $body, $options) ?? []);
    }

    /** Cancel an order. */
    public function cancel(string $accountId, string $orderId, ?RequestOptions $options = null): Order
    {
        return Order::fromArray($this->c->one(Ops::CANCEL_ORDER, [$accountId, $orderId], null, null, $options) ?? []);
    }

    /** Preview an order. */
    public function preview(string $accountId, CreateOrder|array $body, ?RequestOptions $options = null): WhatIf
    {
        return WhatIf::fromArray($this->c->one(Ops::TEST_ORDER, [$accountId], null, $body, $options) ?? []);
    }

    /** Cancel all orders. */
    public function cancelAll(string $accountId, CancelAll|array $body, ?RequestOptions $options = null): CancelAllResult
    {
        return CancelAllResult::fromArray($this->c->one(Ops::CANCEL_ALL_ORDERS, [$accountId], null, $body, $options) ?? []);
    }

    /** Attach exits to an order. */
    public function setExits(string $accountId, string $orderId, OrderExits|array $body, ?RequestOptions $options = null): Order
    {
        return Order::fromArray($this->c->one(Ops::SET_ORDER_EXITS, [$accountId, $orderId], null, $body, $options) ?? []);
    }
}
