<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\Fill;
use PhoenixPerps\Model\FillsListParams;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The fills calls ($client->fills). */
final class FillsResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** List fills. */
    /** @return Page<Fill>|null null on a 304 Not Modified */
    public function list(string $accountId, FillsListParams|array|null $params = null, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_FILLS, [$accountId], $params, [Fill::class, 'fromArray'], $options);
    }

    /** Every item of list, page after page (RequestOptions::$maxItems caps it). */
    /** @return \Generator<int, Fill> */
    public function listAutoPaging(string $accountId, FillsListParams|array|null $params = null, ?RequestOptions $options = null): \Generator
    {
        return $this->list($accountId, $params, $options)?->autoPagingIterator($options?->maxItems) ?? (static fn (): \Generator => yield from [])();
    }
}
