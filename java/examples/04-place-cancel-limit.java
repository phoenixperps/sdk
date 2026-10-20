// 04: a limit order far from the market, found again by your own id, then canceled. The Idempotency-Key makes a
// retried request return the first order instead of placing a second one.
//   gradle -q example -Pn=04
import com.phoenixperpsfunding.sdk.MarketRules;
import com.phoenixperpsfunding.sdk.MarketStream;
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.RequestOptions;
import com.phoenixperpsfunding.sdk.gen.CreateOrder;
import com.phoenixperpsfunding.sdk.gen.CreateOrderType;
import com.phoenixperpsfunding.sdk.gen.Order;
import com.phoenixperpsfunding.sdk.gen.OrderSide;
import com.phoenixperpsfunding.sdk.gen.OrdersListParams;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

final class Example04 {
    public static void main(String[] args) {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();
        if ("live".equals(client.environment()) && !"yes".equals(System.getenv("PHOENIXPERPS_ALLOW_LIVE"))) {
            System.err.println("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).");
            System.exit(1);
        }
        String account = client.accounts().list().data().get(0).id();
        MarketRules rules = new MarketRules(client.markets().get("BTC-USD"));

        BigDecimal bid = null;
        try (MarketStream market = client.openMarketStream()) {
            market.subscribe("ticker", List.of("BTC-USD"), Map.of(), Duration.ofSeconds(10));
            for (MarketStream.Frame f : market) {
                if ("ticker".equals(f.type())) {
                    bid = new BigDecimal(f.data().path("bid").asText());
                    break;
                }
            }
        }
        if (bid == null) throw new IllegalStateException("no price");

        String clientOrderId = "example-04-" + System.currentTimeMillis();
        Order order = client.orders().create(account, new CreateOrder()
                .market("BTC-USD")
                .side(OrderSide.BUY)
                .type(CreateOrderType.LIMIT)
                .size(rules.sizeForNotional(new BigDecimal("100"), bid)) // about 100 USD
                .limitPrice(rules.roundPrice(bid.multiply(new BigDecimal("0.9")), MarketRules.Mode.DOWN)) // 10% below: it rests
                .clientOrderId(clientOrderId),
                RequestOptions.none().withIdempotencyKey(UUID.randomUUID().toString()));
        System.out.printf("placed %s: %s, %s at %s%n", order.id(), order.status(), order.size(), order.limitPrice());

        Order found = client.orders().list(account, new OrdersListParams().clientOrderId(clientOrderId)).data().get(0);
        System.out.printf("found by client_order_id: %s%n", found.id());

        Order canceled = client.orders().cancel(account, order.id());
        System.out.printf("canceled: %s (%s)%n", canceled.status(), canceled.cancelReason());
    }
}
