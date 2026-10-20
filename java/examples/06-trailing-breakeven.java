// 06: a position whose stop loss trails the price by 0.5% and moves to break-even once the price has gone 0.5% your
// way, all on the API's servers. Follows the account stream for 30 s to watch the stop move, then closes.
//   gradle -q example -Pn=06
import com.phoenixperpsfunding.sdk.AccountStream;
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.gen.ClosePosition;
import com.phoenixperpsfunding.sdk.gen.CreateOrder;
import com.phoenixperpsfunding.sdk.gen.CreateOrderType;
import com.phoenixperpsfunding.sdk.gen.Event;
import com.phoenixperpsfunding.sdk.gen.Market;
import com.phoenixperpsfunding.sdk.gen.Order;
import com.phoenixperpsfunding.sdk.gen.OrderExits;
import com.phoenixperpsfunding.sdk.gen.OrderExitsStopLoss;
import com.phoenixperpsfunding.sdk.gen.OrderExitsStopLossBreakEven;
import com.phoenixperpsfunding.sdk.gen.OrderExitsStopLossTrail;
import com.phoenixperpsfunding.sdk.gen.OrderSide;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

final class Example06 {
    public static void main(String[] args) throws InterruptedException {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();
        if ("live".equals(client.environment()) && !"yes".equals(System.getenv("PHOENIXPERPS_ALLOW_LIVE"))) {
            System.err.println("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).");
            System.exit(1);
        }
        String account = client.accounts().list().data().get(0).id();
        Market btc = client.markets().get("BTC-USD");

        Instant end = Instant.now().plusSeconds(30);
        // the stream first, so every event of the stop is told, from its placing on
        try (AccountStream stream = client.openAccountStream(AccountStream.options().accounts(List.of(account)))) {
            stream.ready().join();
            Order order = client.orders().create(account, new CreateOrder()
                    .market("BTC-USD")
                    .side(OrderSide.BUY)
                    .type(CreateOrderType.MARKET)
                    .size(btc.minSize())
                    .exits(new OrderExits().stopLoss(new OrderExitsStopLoss()
                            .percent(new BigDecimal("1"))
                            .trail(new OrderExitsStopLossTrail().percent(new BigDecimal("0.5")))
                            .breakEven(new OrderExitsStopLossBreakEven().triggerPercent(new BigDecimal("0.5")).offsetTicks(2L)))));
            System.out.printf("bought %s BTC (%s); watching the stop for 30 s%n", order.size(), order.status());
            while (Instant.now().isBefore(end)) {
                Event e = stream.poll(Duration.between(Instant.now(), end));
                if (e != null && e.type().toString().startsWith("strategy.")) {
                    System.out.printf("%s %s %s%n", Instant.ofEpochMilli(e.time()), e.type(), e.data());
                }
            }
        }

        Order closed = client.positions().close(account, "BTC-USD", new ClosePosition());
        System.out.printf("closed: %s%n", closed.status());
    }
}
