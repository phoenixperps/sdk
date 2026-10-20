<?php

declare(strict_types=1);

namespace PhoenixPerps;

/** Decimals as the API takes them: strings, exactly as given; a number in plain form, never an exponent. */
final class Decimal
{
    public static function of(string|int|float $x): string
    {
        if (is_string($x)) {
            return $x;
        }
        if (is_int($x)) {
            return (string) $x;
        }
        $s = var_export($x, true); // the shortest form that reads back the same
        if (!str_contains($s, 'E') && !str_contains($s, 'e')) {
            return str_ends_with($s, '.0') ? substr($s, 0, -2) : $s;
        }
        // that form's digits with the point moved by the exponent: 1.5E+20 is 150000000000000000000, 1.0E-9 0.000000001
        [$mantissa, $exponent] = preg_split('/[eE]/', $s);
        $sign = str_starts_with($mantissa, '-') ? '-' : '';
        $parts = explode('.', ltrim($mantissa, '-'));
        $digits = $parts[0] . ($parts[1] ?? '');
        $point = strlen($parts[0]) + (int) $exponent;
        if ($point < 1) {
            [$digits, $point] = [str_repeat('0', 1 - $point) . $digits, 1];
        }
        $int = ltrim(substr(str_pad($digits, $point, '0'), 0, $point), '0');
        $frac = rtrim((string) substr($digits, $point), '0');
        return $sign . ($int === '' ? '0' : $int) . ($frac === '' ? '' : '.' . $frac);
    }
}
