// 05: a market order with its exits: a take profit 1% away and a stop loss 0.5% away, placed by the API the moment
// it fills and kept on its servers. The position is closed at the end.
//   gradle -q example -Pn=05
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.gen.ClosePosition;
import com.phoenixperpsfunding.sdk.gen.CreateOrder;
import com.phoenixperpsfunding.sdk.gen.CreateOrderType;
import com.phoenixperpsfunding.sdk.gen.Exits;
import com.phoenixperpsfunding.sdk.gen.ExitsLeg;
import com.phoenixperpsfunding.sdk.gen.Market;
import com.phoenixperpsfunding.sdk.gen.Order;
import com.phoenixperpsfunding.sdk.gen.OrderExits;
import com.phoenixperpsfunding.sdk.gen.OrderExitsStopLoss;
import com.phoenixperpsfunding.sdk.gen.OrderExitsTakeProfit;
import com.phoenixperpsfunding.sdk.gen.OrderSide;
import java.math.BigDecimal;
import java.util.List;

final class Example05 {
    public static void main(String[] args) {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();
        if ("live".equals(client.environment()) && !"yes".equals(System.getenv("PHOENIXPERPS_ALLOW_LIVE"))) {
            System.err.println("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).");
            System.exit(1);
        }
        String account = client.accounts().list().data().get(0).id();
        Market btc = client.markets().get("BTC-USD");

        Order order = client.orders().create(account, new CreateOrder()
                .market("BTC-USD")
                .side(OrderSide.BUY)
                .type(CreateOrderType.MARKET)
                .size(btc.minSize())
                .exits(new OrderExits()
                        .takeProfit(List.of(new OrderExitsTakeProfit().percent(new BigDecimal("1"))))
                        .stopLoss(new OrderExitsStopLoss().percent(new BigDecimal("0.5")))));
        System.out.printf("market order %s: %s at %s%n", order.id(), order.status(), order.averageFillPrice());

        Exits exits = client.exits().get(account, "BTC-USD");
        for (ExitsLeg tp : exits.takeProfit()) System.out.printf("take profit: %s at %s (%s)%n", tp.size(), tp.price(), tp.status());
        for (ExitsLeg sl : exits.stopLoss()) System.out.printf("stop loss:   %s at %s (%s)%n", sl.size(), sl.price(), sl.status());

        Order closed = client.positions().close(account, "BTC-USD", new ClosePosition());
        System.out.printf("closed: %s %s (the exits went with the position)%n", closed.id(), closed.status());
    }
}
