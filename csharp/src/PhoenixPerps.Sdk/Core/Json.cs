using System.Globalization;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace PhoenixPerps;

/// <summary>An open enum: a known value, or any value a newer API sends (kept as is, never fatal).</summary>
public interface IOpenEnum<TSelf> where TSelf : struct, IOpenEnum<TSelf>
{
    /// <summary>The value as the API writes it.</summary>
    string Value { get; }

    /// <summary>A value from its wire form.</summary>
    static abstract TSelf From(string value);
}

/// <summary>Reads and writes an open enum as its wire string.</summary>
public sealed class OpenEnumConverter<T> : JsonConverter<T> where T : struct, IOpenEnum<T>
{
    /// <inheritdoc />
    public override T Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options) =>
        T.From(reader.TokenType == JsonTokenType.String ? reader.GetString()! : JsonDocument.ParseValue(ref reader).RootElement.GetRawText());

    /// <inheritdoc />
    public override void Write(Utf8JsonWriter writer, T value, JsonSerializerOptions options) => writer.WriteStringValue(value.Value);
}

/// <summary>A decimal goes out as a string, exactly (never an exponent), and is read from a string or a number.</summary>
public sealed class DecimalStringConverter : JsonConverter<decimal>
{
    /// <inheritdoc />
    public override decimal Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
    {
        if (reader.TokenType == JsonTokenType.Number) return reader.GetDecimal();
        // anything else (null where a decimal must be, a word) is an answer that cannot be read: a JsonException, which
        // the client tells as invalid_response
        if (reader.TokenType == JsonTokenType.String && decimal.TryParse(reader.GetString(), NumberStyles.Float, CultureInfo.InvariantCulture, out var d)) return d;
        throw new JsonException($"A decimal was expected, not {(reader.TokenType == JsonTokenType.String ? $"'{reader.GetString()}'" : reader.TokenType.ToString())}.");
    }

    /// <inheritdoc />
    public override void Write(Utf8JsonWriter writer, decimal value, JsonSerializerOptions options) =>
        writer.WriteStringValue(value.ToString(CultureInfo.InvariantCulture));
}

/// <summary>A decimal? as DecimalStringConverter, null as null.</summary>
public sealed class NullableDecimalStringConverter : JsonConverter<decimal?>
{
    private static readonly DecimalStringConverter Inner = new();

    /// <inheritdoc />
    public override bool HandleNull => true;

    /// <inheritdoc />
    public override decimal? Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options) =>
        reader.TokenType == JsonTokenType.Null ? null : Inner.Read(ref reader, typeof(decimal), options);

    /// <inheritdoc />
    public override void Write(Utf8JsonWriter writer, decimal? value, JsonSerializerOptions options)
    {
        if (value is null) writer.WriteNullValue();
        else Inner.Write(writer, value.Value, options);
    }
}
