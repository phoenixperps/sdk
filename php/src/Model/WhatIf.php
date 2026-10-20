<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** WhatIf */
final class WhatIf implements \JsonSerializable
{
    private const WIRE = ['result' => true, 'reject' => true, 'market' => true, 'side' => true, 'type' => true, 'size' => true, 'execution' => true, 'notional' => true, 'fee' => true, 'margin' => true, 'position_after' => true, 'exposure' => true];

    public function __construct(
        /** What the order would do now: fill at once, rest, or be refused (reject says why). */
        public readonly ?string $result = null,
        /** The reject. */
        public readonly ?WhatIfReject $reject = null,
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The order's side. */
        public readonly ?string $side = null,
        /** The order's type. */
        public readonly ?string $type = null,
        /** Its size (after a notional_usd is rounded to the size step). */
        public readonly ?string $size = null,
        /** The execution. */
        public readonly ?WhatIfExecution $execution = null,
        /** Its value in USD. */
        public readonly ?string $notional = null,
        /** The fee its fill would pay. */
        public readonly ?string $fee = null,
        /** The margin. */
        public readonly ?WhatIfMargin $margin = null,
        /** The position after. */
        public readonly ?WhatIfPositionAfter $positionAfter = null,
        /** The exposure. */
        public readonly ?WhatIfExposure $exposure = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            result: $d['result'] ?? null,
            reject: isset($d['reject']) && is_array($d['reject']) ? WhatIfReject::fromArray($d['reject']) : null,
            market: $d['market'] ?? null,
            side: $d['side'] ?? null,
            type: $d['type'] ?? null,
            size: isset($d['size']) ? (string) $d['size'] : null,
            execution: isset($d['execution']) && is_array($d['execution']) ? WhatIfExecution::fromArray($d['execution']) : null,
            notional: isset($d['notional']) ? (string) $d['notional'] : null,
            fee: isset($d['fee']) ? (string) $d['fee'] : null,
            margin: isset($d['margin']) && is_array($d['margin']) ? WhatIfMargin::fromArray($d['margin']) : null,
            positionAfter: isset($d['position_after']) && is_array($d['position_after']) ? WhatIfPositionAfter::fromArray($d['position_after']) : null,
            exposure: isset($d['exposure']) && is_array($d['exposure']) ? WhatIfExposure::fromArray($d['exposure']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->result !== null) {
            $out['result'] = $this->result;
        }
        if ($this->reject !== null) {
            $out['reject'] = $this->reject->toArray();
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->side !== null) {
            $out['side'] = $this->side;
        }
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->size !== null) {
            $out['size'] = $this->size;
        }
        if ($this->execution !== null) {
            $out['execution'] = $this->execution->toArray();
        }
        if ($this->notional !== null) {
            $out['notional'] = $this->notional;
        }
        if ($this->fee !== null) {
            $out['fee'] = $this->fee;
        }
        if ($this->margin !== null) {
            $out['margin'] = $this->margin->toArray();
        }
        if ($this->positionAfter !== null) {
            $out['position_after'] = $this->positionAfter->toArray();
        }
        if ($this->exposure !== null) {
            $out['exposure'] = $this->exposure->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
