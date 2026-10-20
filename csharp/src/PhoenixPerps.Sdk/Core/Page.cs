using System.Text.Json;
using System.Text.Json.Serialization;

namespace PhoenixPerps;

/// <summary>One page of a list: its items, whether more follow, and the cursor to them.</summary>
public sealed class Page<T>
{
    /// <summary>The items.</summary>
    [JsonPropertyName("data")]
    public List<T> Data { get; set; } = new();

    /// <summary>More items follow.</summary>
    [JsonPropertyName("has_more")]
    public bool HasMore { get; set; }

    /// <summary>The cursor to the next page.</summary>
    [JsonPropertyName("next_cursor")]
    public string? NextCursor { get; set; }

    /// <summary>When the list was read (ms).</summary>
    [JsonPropertyName("as_of")]
    public long? AsOf { get; set; }

    /// <summary>The list's other fields, kept as they came (history_complete of the orders list, a newer API's).</summary>
    [JsonExtensionData]
    public Dictionary<string, JsonElement>? Extra { get; set; }

    /// <summary>The answer the page came in.</summary>
    [JsonIgnore]
    public RawResponse? Raw { get; internal set; }

    [JsonIgnore]
    internal Func<string, CancellationToken, Task<Page<T>>>? Next { get; set; }

    /// <summary>The next page, or null after the last.</summary>
    public async Task<Page<T>?> GetNextPageAsync(CancellationToken cancellationToken = default)
    {
        if (!HasMore || string.IsNullOrEmpty(NextCursor) || Next is null) return null;
        return await Next(NextCursor!, cancellationToken).ConfigureAwait(false);
    }
}

/// <summary>The answer of a call as it came.</summary>
public sealed class RawResponse
{
    /// <summary>The HTTP status.</summary>
    public int StatusCode { get; init; }

    /// <summary>The headers.</summary>
    public IReadOnlyDictionary<string, string> Headers { get; init; } = new Dictionary<string, string>();

    /// <summary>The body.</summary>
    public byte[] Body { get; init; } = Array.Empty<byte>();

    /// <summary>The request id.</summary>
    public string? RequestId { get; init; }

    /// <summary>The call's Idempotency-Key.</summary>
    public string? IdempotencyKey { get; init; }
}

/// <summary>What the last answer said of the rate-limit window.</summary>
public sealed record RateLimit(int Limit, int Remaining, DateTimeOffset? ResetAt);

/// <summary>The options of one call.</summary>
public sealed record RequestOptions
{
    /// <summary>The call's Idempotency-Key (else a new UUID, the same on every attempt).</summary>
    public string? IdempotencyKey { get; init; }

    /// <summary>The time one attempt may take.</summary>
    public TimeSpan? Timeout { get; init; }

    /// <summary>How often the call is tried again when that is safe.</summary>
    public int? MaxRetries { get; init; }

    /// <summary>Caps an AutoPaging iteration (null or 0: every item).</summary>
    public int? MaxItems { get; init; }

    /// <summary>Headers added to the request.</summary>
    public IReadOnlyDictionary<string, string>? Headers { get; init; }

    /// <summary>Receives the answer of the call as it came.</summary>
    public Action<RawResponse>? OnResponse { get; init; }
}
