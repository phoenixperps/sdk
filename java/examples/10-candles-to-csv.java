// 10: the last 100 closed one-minute candles of a market and the one forming (final false), written to candles.csv.
//   gradle -q example -Pn=10 [-Pargs=BTC-USD]
import com.fasterxml.jackson.databind.JsonNode;
import com.phoenixperpsfunding.sdk.MarketStream;
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

final class Example10 {
    public static void main(String[] args) throws IOException {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();
        String symbol = (args.length > 0 ? args[0] : "BTC-USD").toUpperCase(Locale.ROOT);
        TreeMap<Long, JsonNode> candles = new TreeMap<>();

        try (MarketStream market = client.openMarketStream()) {
            market.subscribe("candles", List.of(symbol), Map.of("interval", "1m", "history", 100), Duration.ofSeconds(10));
            for (MarketStream.Frame f : market) {
                if ("snapshot_end".equals(f.type())) break;
                if (!"candles".equals(f.type())) continue;
                for (JsonNode c : f.data()) candles.put(c.path("time").asLong(), c);
            }
        }

        try (PrintWriter csv = new PrintWriter("candles.csv", StandardCharsets.UTF_8)) {
            csv.println("time,open,high,low,close,volume,final");
            for (Map.Entry<Long, JsonNode> e : candles.entrySet()) {
                JsonNode c = e.getValue();
                csv.println(String.join(",", Instant.ofEpochMilli(e.getKey()).toString(), c.path("open").asText(), c.path("high").asText(),
                        c.path("low").asText(), c.path("close").asText(), c.path("volume").asText(), c.path("final").asText()));
            }
        }
        System.out.printf("%d candles of %s in candles.csv%n", candles.size(), symbol);
    }
}
