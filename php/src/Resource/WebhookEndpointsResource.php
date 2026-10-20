<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\DeletedEndpoint;
use PhoenixPerps\Model\WebhookDelivery;
use PhoenixPerps\Model\WebhookEndpoint;
use PhoenixPerps\Model\WebhookEndpointsListDeliveriesParams;
use PhoenixPerps\Model\WebhookEventType;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The webhookEndpoints calls ($client->webhookEndpoints). */
final class WebhookEndpointsResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** List webhook endpoints. */
    /** @return Page<WebhookEndpoint>|null null on a 304 Not Modified */
    public function list(?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_WEBHOOK_ENDPOINTS, [], null, [WebhookEndpoint::class, 'fromArray'], $options);
    }

    /** Get a webhook endpoint. */
    /** @return WebhookEndpoint|null null on a 304 Not Modified */
    public function get(string $endpointId, ?RequestOptions $options = null): ?WebhookEndpoint
    {
        $d = $this->c->one(Ops::GET_WEBHOOK_ENDPOINT, [$endpointId], null, null, $options);
        return $d === null ? null : WebhookEndpoint::fromArray($d);
    }

    /** Delete an endpoint. */
    public function delete(string $endpointId, ?RequestOptions $options = null): DeletedEndpoint
    {
        return DeletedEndpoint::fromArray($this->c->one(Ops::DELETE_WEBHOOK_ENDPOINT, [$endpointId], null, null, $options) ?? []);
    }

    /** Send a test. */
    public function test(string $endpointId, ?RequestOptions $options = null): WebhookDelivery
    {
        return WebhookDelivery::fromArray($this->c->one(Ops::TEST_WEBHOOK_ENDPOINT, [$endpointId], null, null, $options) ?? []);
    }

    /** List deliveries. */
    /** @return Page<WebhookDelivery>|null null on a 304 Not Modified */
    public function listDeliveries(string $endpointId, WebhookEndpointsListDeliveriesParams|array|null $params = null, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_WEBHOOK_DELIVERIES, [$endpointId], $params, [WebhookDelivery::class, 'fromArray'], $options);
    }

    /** Every item of listDeliveries, page after page (RequestOptions::$maxItems caps it). */
    /** @return \Generator<int, WebhookDelivery> */
    public function listDeliveriesAutoPaging(string $endpointId, WebhookEndpointsListDeliveriesParams|array|null $params = null, ?RequestOptions $options = null): \Generator
    {
        return $this->listDeliveries($endpointId, $params, $options)?->autoPagingIterator($options?->maxItems) ?? (static fn (): \Generator => yield from [])();
    }

    /** Get a delivery. */
    /** @return WebhookDelivery|null null on a 304 Not Modified */
    public function getDelivery(string $endpointId, string $deliveryId, ?RequestOptions $options = null): ?WebhookDelivery
    {
        $d = $this->c->one(Ops::GET_WEBHOOK_DELIVERY, [$endpointId, $deliveryId], null, null, $options);
        return $d === null ? null : WebhookDelivery::fromArray($d);
    }

    /** Send a delivery again. */
    public function resendDelivery(string $endpointId, string $deliveryId, ?RequestOptions $options = null): WebhookDelivery
    {
        return WebhookDelivery::fromArray($this->c->one(Ops::RESEND_WEBHOOK_DELIVERY, [$endpointId, $deliveryId], null, null, $options) ?? []);
    }

    /** Pause an endpoint. */
    public function disable(string $endpointId, ?RequestOptions $options = null): WebhookEndpoint
    {
        return WebhookEndpoint::fromArray($this->c->one(Ops::DISABLE_WEBHOOK_ENDPOINT, [$endpointId], null, null, $options) ?? []);
    }

    /** Resume an endpoint. */
    public function enable(string $endpointId, ?RequestOptions $options = null): WebhookEndpoint
    {
        return WebhookEndpoint::fromArray($this->c->one(Ops::ENABLE_WEBHOOK_ENDPOINT, [$endpointId], null, null, $options) ?? []);
    }

    /** List event types. */
    /** @return Page<WebhookEventType>|null null on a 304 Not Modified */
    public function eventTypes(?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_WEBHOOK_EVENT_TYPES, [], null, [WebhookEventType::class, 'fromArray'], $options);
    }
}
