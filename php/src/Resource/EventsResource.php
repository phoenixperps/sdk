<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\Event;
use PhoenixPerps\Model\EventsListParams;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The events calls ($client->events). */
final class EventsResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** List events. */
    /** @return Page<Event>|null null on a 304 Not Modified */
    public function list(EventsListParams|array|null $params = null, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_EVENTS, [], $params, [Event::class, 'fromArray'], $options);
    }

    /** Every item of list, page after page (RequestOptions::$maxItems caps it). */
    /** @return \Generator<int, Event> */
    public function listAutoPaging(EventsListParams|array|null $params = null, ?RequestOptions $options = null): \Generator
    {
        return $this->list($params, $options)?->autoPagingIterator($options?->maxItems) ?? (static fn (): \Generator => yield from [])();
    }

    /** Get an event. */
    /** @return Event|null null on a 304 Not Modified */
    public function get(string $eventId, ?RequestOptions $options = null): ?Event
    {
        $d = $this->c->one(Ops::GET_EVENT, [$eventId], null, null, $options);
        return $d === null ? null : Event::fromArray($d);
    }
}
