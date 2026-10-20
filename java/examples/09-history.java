// 09: an account's history: the equity curve by day, the result of each trading day, closed positions and fills.
//   gradle -q example -Pn=09
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.RequestOptions;
import com.phoenixperpsfunding.sdk.gen.ClosedPosition;
import com.phoenixperpsfunding.sdk.gen.DailyResult;
import com.phoenixperpsfunding.sdk.gen.EquityHistory;
import com.phoenixperpsfunding.sdk.gen.Fill;
import com.phoenixperpsfunding.sdk.gen.HistoryEquityParams;
import com.phoenixperpsfunding.sdk.gen.PositionsListClosedParams;
import com.phoenixperpsfunding.sdk.gen.Resolution;

final class Example09 {
    public static void main(String[] args) {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();
        String account = client.accounts().list().data().get(0).id();

        EquityHistory equity = client.history().equity(account, new HistoryEquityParams().resolution(Resolution.DAY));
        System.out.printf("equity: %d points%n", equity.points().size());

        for (DailyResult d : client.history().daily(account).data()) {
            System.out.printf("%s: %d trades, P&L %s, fees %s%n", d.date(), d.trades(), d.pnl(), d.fees());
        }
        for (ClosedPosition p : client.positions().listClosed(account, new PositionsListClosedParams().limit(10L)).data()) {
            System.out.printf("closed %s %s %s: %s -> %s, P&L %s%n", p.market(), p.side(), p.size(), p.entryPrice(), p.exitPrice(), p.pnl());
        }
        int n = 0;
        for (Fill f : client.fills().listAutoPaging(account, null, RequestOptions.none().withMaxItems(100))) { // every page, at most 100
            if (++n <= 5) System.out.printf("fill %s %s %s @ %s%n", f.market(), f.side(), f.size(), f.price());
        }
        System.out.printf("%d fills read%n", n);
    }
}
