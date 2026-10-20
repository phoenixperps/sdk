using System.Globalization;
using System.Security.Cryptography;
using System.Text;

namespace PhoenixPerps;

/*
Small helpers (docs/BEHAVIOUR.md "Helpers"), the same names in every SDK, in exact decimals.

MarketRules (from client.Markets.GetAsync): RoundPrice, RoundSize (down to the step), SizeForNotional,
SizeForRisk(riskUsd, stopDistance), WorstPrice(side, price, slippagePct) rounded outward, each from decimals or from the
decimal strings the API writes. The steps and the slippage bounds are the market's own (nothing written in here). A size
rounds down even below the market's MinSize (to 0): check it before ordering. What they refuse (a side other than buy
or sell, a slippage outside the market's bounds, a string that is no decimal) is a ValidationException.

Webhooks.Verify(raw, headers, secrets, tolerance): the WebhookPayload, or a WebhookVerificationException.
X-Phoenix-Signature is sha256=<hex HMAC-SHA256(secret, "<X-Phoenix-Timestamp>.<raw body>")>, any of its comma-separated
values (a rotation sends two), compared as bytes; X-Phoenix-Timestamp within the tolerance (300 s).
*/

/// <summary>How a value meets a step.</summary>
public enum RoundMode
{
    /// <summary>The nearest multiple; a tie goes away from zero.</summary>
    Nearest,

    /// <summary>The multiple at or below.</summary>
    Down,

    /// <summary>The multiple at or above.</summary>
    Up,
}

/// <summary>Rounds prices and sizes the way a market takes them; each answers the decimal an order takes, at the step's decimals.</summary>
public sealed class MarketRules
{
    /// <summary>The rules of a market.</summary>
    public MarketRules(Market market)
    {
        MarketId = market.Value;
        TickSize = market.TickSize;
        SizeStep = market.SizeStep;
        MinSize = market.MinSize;
        Slippage = market.MaxSlippagePct;
    }

    /// <summary>The market.</summary>
    public string MarketId { get; }

    /// <summary>The tick size.</summary>
    public decimal TickSize { get; }

    /// <summary>The size step.</summary>
    public decimal SizeStep { get; }

    /// <summary>The smallest size.</summary>
    public decimal MinSize { get; }

    /// <summary>The slippage bounds of market orders.</summary>
    public MarketMaxSlippagePct? Slippage { get; }

    /// <summary>x as a multiple of step, at the step's decimals ("62000.10" for a 0.05 tick): the decimal an order takes.</summary>
    public static decimal RoundTo(decimal x, decimal step, RoundMode mode = RoundMode.Nearest)
    {
        if (step <= 0) throw new ValidationException("invalid_parameter", "The step is not above zero.");
        var q = x / step;
        var n = mode switch
        {
            RoundMode.Down => decimal.Floor(q),
            RoundMode.Up => decimal.Ceiling(q),
            _ => decimal.Round(q, MidpointRounding.AwayFromZero),
        };
        return decimal.Round(n * step, Places(step), MidpointRounding.AwayFromZero);
    }

    private static int Places(decimal step)
    {
        var s = step.ToString(CultureInfo.InvariantCulture);
        var i = s.IndexOf('.');
        return i < 0 ? 0 : s.TrimEnd('0').Length - i - 1;
    }

    /// <summary>The price as a multiple of the tick size.</summary>
    public decimal RoundPrice(decimal price, RoundMode mode = RoundMode.Nearest) => RoundTo(price, TickSize, mode);

    /// <summary>The size down to the size step.</summary>
    public decimal RoundSize(decimal size) => RoundTo(size, SizeStep, RoundMode.Down);

    /// <summary>The size worth usd at price, down to the step (0 below one step: check it against MinSize).</summary>
    public decimal SizeForNotional(decimal usd, decimal price) =>
        price > 0 ? RoundSize(usd / price) : throw new ValidationException("invalid_parameter", "The price is not above zero.");

    /// <summary>The size that loses riskUsd over stopDistance, down to the step (0 below one step: check it against MinSize).</summary>
    public decimal SizeForRisk(decimal riskUsd, decimal stopDistance) =>
        stopDistance != 0 ? RoundSize(riskUsd / Math.Abs(stopDistance)) : throw new ValidationException("invalid_parameter", "The stop distance is zero.");

    /// <summary>The worst price a market order of side accepts at price, slippagePct away (null: the market's default; else
    /// within the market's bounds), rounded outward.</summary>
    public decimal WorstPrice(OrderSide side, decimal price, decimal? slippagePct = null)
    {
        // any casing; anything but buy or sell is refused, never priced as a sell
        var buy = string.Equals(side.Value, "buy", StringComparison.OrdinalIgnoreCase);
        if (!buy && !string.Equals(side.Value, "sell", StringComparison.OrdinalIgnoreCase)) throw new ValidationException("invalid_parameter", $"The side is buy or sell, not \"{side.Value}\".");
        var pct = slippagePct ?? Slippage?.Default ?? throw new ValidationException("invalid_parameter", "No slippage given and the market has no default.");
        if (pct < Slippage?.Min || pct > Slippage?.Max) throw new ValidationException("invalid_parameter", $"A slippage of {pct} % is outside {MarketId}'s {Slippage?.Min} to {Slippage?.Max} %.");
        return buy ? RoundTo(price * (1 + pct / 100), TickSize, RoundMode.Up) : RoundTo(price * (1 - pct / 100), TickSize, RoundMode.Down);
    }

    /// <summary>WorstPrice with the side as a string ("buy" or "sell").</summary>
    public decimal WorstPrice(string side, decimal price, decimal? slippagePct = null) => WorstPrice(new OrderSide(side), price, slippagePct);

    /// <summary>RoundPrice of a decimal string, as the API and the streams write prices.</summary>
    public decimal RoundPrice(string price, RoundMode mode = RoundMode.Nearest) => RoundPrice(Decimal(price), mode);

    /// <summary>RoundSize of a decimal string.</summary>
    public decimal RoundSize(string size) => RoundSize(Decimal(size));

    /// <summary>SizeForNotional of decimal strings.</summary>
    public decimal SizeForNotional(string usd, string price) => SizeForNotional(Decimal(usd), Decimal(price));

    /// <summary>SizeForRisk of decimal strings.</summary>
    public decimal SizeForRisk(string riskUsd, string stopDistance) => SizeForRisk(Decimal(riskUsd), Decimal(stopDistance));

    /// <summary>WorstPrice of decimal strings.</summary>
    public decimal WorstPrice(string side, string price, string? slippagePct = null) =>
        WorstPrice(side, Decimal(price), slippagePct is null ? null : Decimal(slippagePct));

    // a decimal as the API writes one ("62000.05", "-500"); anything else is refused as the SDK's own error
    private static decimal Decimal(string s) =>
        decimal.TryParse(s, NumberStyles.AllowLeadingSign | NumberStyles.AllowDecimalPoint, CultureInfo.InvariantCulture, out var d)
            ? d : throw new ValidationException("invalid_parameter", $"\"{s}\" is not a decimal.");
}

/// <summary>Why a delivery did not verify: missing_headers, stale, bad_signature or bad_body (a PhoenixPerpsException).</summary>
public sealed class WebhookVerificationException(string code, string message) : PhoenixPerpsException(code, message);

/// <summary>Webhook deliveries.</summary>
public static class Webhooks
{
    /// <summary>The delivery (the event, with its api_version, environment and test) once its signature and time check
    /// out. raw: the body exactly as received.</summary>
    public static WebhookPayload Verify(string raw, IReadOnlyDictionary<string, string> headers, IEnumerable<string> secrets, TimeSpan? tolerance = null, DateTimeOffset? now = null)
    {
        string? Header(string name) => headers.FirstOrDefault(h => string.Equals(h.Key, name, StringComparison.OrdinalIgnoreCase)).Value;
        var ts = Header("X-Phoenix-Timestamp");
        var sig = Header("X-Phoenix-Signature");
        if (string.IsNullOrEmpty(ts) || string.IsNullOrEmpty(sig)) throw new WebhookVerificationException("missing_headers", "X-Phoenix-Timestamp and X-Phoenix-Signature are required.");
        var at = now ?? DateTimeOffset.UtcNow;
        // seconds since the epoch; no number at all, or one beyond any clock, is stale too
        if (!long.TryParse(ts, NumberStyles.None, CultureInfo.InvariantCulture, out var t) || Math.Abs(at.ToUnixTimeMilliseconds() / 1000m - t) > (decimal)(tolerance ?? TimeSpan.FromSeconds(300)).TotalSeconds)
            throw new WebhookVerificationException("stale", "The delivery is too old (or the clock is off).");
        var given = sig.Split(',').Select(s => Encoding.UTF8.GetBytes(s.Trim())).ToList();
        foreach (var secret in secrets)
        {
            var mac = HMACSHA256.HashData(Encoding.UTF8.GetBytes(secret), Encoding.UTF8.GetBytes(ts + "." + raw));
            var want = Encoding.UTF8.GetBytes("sha256=" + Convert.ToHexString(mac).ToLowerInvariant());
            if (!given.Any(g => CryptographicOperations.FixedTimeEquals(g, want))) continue;
            WebhookPayload? e;
            try
            {
                e = System.Text.Json.JsonSerializer.Deserialize(raw, Json.Default.WebhookPayload);
            }
            catch (System.Text.Json.JsonException)
            {
                e = null;
            }
            // signed, but an event has an id and a type at least
            return e is { Id.Length: > 0, Type.Value: not null } ? e : throw new WebhookVerificationException("bad_body", "The body is not an event.");
        }
        throw new WebhookVerificationException("bad_signature", "The signature does not match.");
    }

    /// <summary>Verify with one secret.</summary>
    public static WebhookPayload Verify(string raw, IReadOnlyDictionary<string, string> headers, string secret, TimeSpan? tolerance = null, DateTimeOffset? now = null) =>
        Verify(raw, headers, new[] { secret }, tolerance, now);
}
