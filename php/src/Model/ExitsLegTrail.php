<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** A trailing stop-loss: how it follows the price. */
final class ExitsLegTrail implements \JsonSerializable
{
    private const WIRE = ['distance' => true, 'percent' => true, 'activation_price' => true, 'active' => true, 'best_price' => true];

    public function __construct(
        /** How far behind the best price (or null: percent). */
        public readonly ?string $distance = null,
        /** How far behind, in percent of the best price (or null: distance). */
        public readonly ?string $percent = null,
        /** It starts once the price reaches this; null: from the start. */
        public readonly ?string $activationPrice = null,
        /** It follows the price now (its activation price was reached, or it had none). */
        public readonly ?bool $active = null,
        /** The best price since it started, which the stop follows. */
        public readonly ?string $bestPrice = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            distance: isset($d['distance']) ? (string) $d['distance'] : null,
            percent: isset($d['percent']) ? (string) $d['percent'] : null,
            activationPrice: isset($d['activation_price']) ? (string) $d['activation_price'] : null,
            active: $d['active'] ?? null,
            bestPrice: isset($d['best_price']) ? (string) $d['best_price'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->distance !== null) {
            $out['distance'] = $this->distance;
        }
        if ($this->percent !== null) {
            $out['percent'] = $this->percent;
        }
        if ($this->activationPrice !== null) {
            $out['activation_price'] = $this->activationPrice;
        }
        if ($this->active !== null) {
            $out['active'] = $this->active;
        }
        if ($this->bestPrice !== null) {
            $out['best_price'] = $this->bestPrice;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
