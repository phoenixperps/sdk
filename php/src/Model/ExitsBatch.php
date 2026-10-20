<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The take-profit and stop-loss legs of a position, changed together. */
final class ExitsBatch implements \JsonSerializable
{
    private const WIRE = ['expected_position' => true, 'expected_exits' => true, 'operations' => true, 'resize_with_position' => true];

    public function __construct(
        /** The position you saw: { side, size }, or null. */
        public readonly ?ExitsBatchExpectedPosition $expectedPosition = null,
        /** Every leg you saw (an empty list when there were none): if anything differs now, nothing is done and the answer is 409 exits_changed with the state now. */
        /** @var list<ExitsBatchExpectedExit>|null */
        public readonly ?array $expectedExits = null,
        /** Applied in this order: cancels, changes, then new legs (stop-losses first). An engine refusal stops the batch: 422 exit_operation_failed says what each operation did. */
        /** @var list<ExitsBatchOperation>|null */
        public readonly ?array $operations = null,
        /** The legs grow with the position (default true). */
        public readonly ?bool $resizeWithPosition = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            expectedPosition: isset($d['expected_position']) && is_array($d['expected_position']) ? ExitsBatchExpectedPosition::fromArray($d['expected_position']) : null,
            expectedExits: isset($d['expected_exits']) && is_array($d['expected_exits']) ? array_map(fn ($x) => ExitsBatchExpectedExit::fromArray($x), $d['expected_exits']) : null,
            operations: isset($d['operations']) && is_array($d['operations']) ? array_map(fn ($x) => ExitsBatchOperation::fromArray($x), $d['operations']) : null,
            resizeWithPosition: $d['resize_with_position'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->expectedPosition !== null) {
            $out['expected_position'] = $this->expectedPosition->toArray();
        }
        if ($this->expectedExits !== null) {
            $out['expected_exits'] = array_map(fn ($x) => $x->toArray(), $this->expectedExits);
        }
        if ($this->operations !== null) {
            $out['operations'] = array_map(fn ($x) => $x->toArray(), $this->operations);
        }
        if ($this->resizeWithPosition !== null) {
            $out['resize_with_position'] = $this->resizeWithPosition;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
