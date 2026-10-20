// 07: what an order would do now, without placing it: fill or rest, the price, the fee, the margin, the position
// after. Nothing is sent to the market.
//   gradle -q example -Pn=07
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.gen.CreateOrder;
import com.phoenixperpsfunding.sdk.gen.CreateOrderType;
import com.phoenixperpsfunding.sdk.gen.OrderSide;
import com.phoenixperpsfunding.sdk.gen.WhatIf;
import java.math.BigDecimal;

final class Example07 {
    public static void main(String[] args) {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();
        String account = client.accounts().list().data().get(0).id();

        WhatIf w = client.orders().preview(account, new CreateOrder()
                .market("BTC-USD")
                .side(OrderSide.BUY)
                .type(CreateOrderType.MARKET)
                .notionalUsd(new BigDecimal("1000")));
        System.out.printf("result:         %s%s%n", w.result(), w.reject() == null ? "" : " (" + w.reject().code() + ")");
        System.out.printf("size:           %s%n", w.size());
        if (w.execution() != null) System.out.printf("price:          %s (worst %s)%n", w.execution().expectedPrice(), w.execution().worstPrice());
        System.out.printf("notional, fee:  %s, %s%n", w.notional(), w.fee());
        if (w.margin() != null) System.out.printf("margin:         %s needed, %s left after%n", w.margin().required(), w.margin().availableAfter());
        if (w.positionAfter() != null) System.out.printf("position after: %s %s%n", w.positionAfter().side() == null ? "flat" : w.positionAfter().side(), w.positionAfter().size());
    }
}
