// 02: the markets and their rules, then a quote (bid, ask, last) from the market stream.
//   gradle -q example -Pn=02 [-Pargs=BTC-USD]
import com.fasterxml.jackson.databind.JsonNode;
import com.phoenixperpsfunding.sdk.MarketStream;
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.gen.Market;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class Example02 {
    public static void main(String[] args) {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();
        String symbol = (args.length > 0 ? args[0] : "BTC-USD").toUpperCase(Locale.ROOT);

        for (Market m : client.markets().list().data()) {
            System.out.printf("%-10s tick %s  step %s  min %s  up to %dx  %s%n", m.market(), m.tickSize().toPlainString(), m.sizeStep().toPlainString(), m.minSize().toPlainString(), m.maxLeverage(), m.status());
        }

        try (MarketStream market = client.openMarketStream()) {
            JsonNode reply = market.subscribe("ticker", List.of(symbol), Map.of("interval_ms", 250), Duration.ofSeconds(10));
            if (!reply.path("rejected").isEmpty()) throw new IllegalStateException(symbol + ": " + reply.path("rejected").get(0).path("code").asText());
            for (MarketStream.Frame f : market) {
                if (!"ticker".equals(f.type())) continue;
                System.out.printf("%s: bid %s  ask %s  last %s%n", symbol, f.data().path("bid").asText(), f.data().path("ask").asText(), f.data().path("last").asText());
                break;
            }
        }
    }
}
