// 03: live market data: prices for two markets and the trades of one, until 20 frames came.
//   gradle -q example -Pn=03
import com.fasterxml.jackson.databind.JsonNode;
import com.phoenixperpsfunding.sdk.MarketStream;
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;

final class Example03 {
    public static void main(String[] args) {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();
        try (MarketStream market = client.openMarketStream()) { // subscriptions are made again after a reconnect
            market.subscribe("ticker", List.of("BTC-USD", "ETH-USD"), Map.of("interval_ms", 500), Duration.ofSeconds(10));
            market.subscribe("trades", List.of("BTC-USD"), Map.of("history", 5), Duration.ofSeconds(10));
            int n = 0;
            for (MarketStream.Frame f : market) {
                if ("ticker".equals(f.type())) {
                    System.out.printf("ticker %s: bid %s ask %s%n", f.symbol(), f.data().path("bid").asText(), f.data().path("ask").asText());
                } else if ("trades".equals(f.type())) {
                    for (JsonNode t : f.data()) {
                        System.out.printf("trade  %s: %s %s @ %s%n", f.symbol(), t.path("side").asText(), t.path("size").asText(), t.path("price").asText());
                    }
                } else {
                    continue;
                }
                if (++n >= 20) break;
            }
        }
    }
}
