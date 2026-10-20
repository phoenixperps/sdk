<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** What to close. */
final class ClosePosition implements \JsonSerializable
{
    private const WIRE = ['size' => true, 'client_order_id' => true];

    public function __construct(
        /** How much to close; absent: the whole position. */
        public readonly string|int|float|null $size = null,
        /** Your own id for the close order (a partial close). */
        public readonly ?string $clientOrderId = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            size: $d['size'] ?? null,
            clientOrderId: $d['client_order_id'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->size !== null) {
            $out['size'] = Decimal::of($this->size);
        }
        if ($this->clientOrderId !== null) {
            $out['client_order_id'] = $this->clientOrderId;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
