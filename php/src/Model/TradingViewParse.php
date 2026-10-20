<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** TradingViewParse */
final class TradingViewParse implements \JsonSerializable
{
    private const WIRE = ['ok' => true, 'code' => true, 'field' => true, 'message' => true, 'signal' => true, 'market' => true, 'suggestion' => true, 'plan' => true];

    public function __construct(
        /** It reads, and names a market we offer. */
        public readonly ?bool $ok = null,
        /** Why not, when it does not. */
        public readonly ?string $code = null,
        /** The field at fault. */
        public readonly ?string $field = null,
        /** What went wrong. */
        public readonly ?string $message = null,
        /** The message as read: action, side, sizes, prices, exits. */
        /** @var array<string, mixed>|null */
        public readonly ?array $signal = null,
        /** The market it names (ALL for every market). */
        public readonly ?string $market = null,
        /** The market meant, when the ticker names one we offer only scaled. */
        public readonly ?string $suggestion = null,
        /** With hook_id: what the alert would do now ({ legs } / { skip } / { refuse }). */
        /** @var array<string, mixed>|null */
        public readonly ?array $plan = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            ok: $d['ok'] ?? null,
            code: $d['code'] ?? null,
            field: $d['field'] ?? null,
            message: $d['message'] ?? null,
            signal: $d['signal'] ?? null,
            market: $d['market'] ?? null,
            suggestion: $d['suggestion'] ?? null,
            plan: $d['plan'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->ok !== null) {
            $out['ok'] = $this->ok;
        }
        if ($this->code !== null) {
            $out['code'] = $this->code;
        }
        if ($this->field !== null) {
            $out['field'] = $this->field;
        }
        if ($this->message !== null) {
            $out['message'] = $this->message;
        }
        if ($this->signal !== null) {
            $out['signal'] = $this->signal;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->suggestion !== null) {
            $out['suggestion'] = $this->suggestion;
        }
        if ($this->plan !== null) {
            $out['plan'] = $this->plan;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
