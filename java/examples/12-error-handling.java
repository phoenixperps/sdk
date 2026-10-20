// 12: what failures look like: each has a class by family, the API's code, the request id to quote to support, and
// details. A write whose outcome is unknown (a timeout) is repeated with the same Idempotency-Key, never a new one.
//   gradle -q example -Pn=12
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.PhoenixPerpsException;
import com.phoenixperpsfunding.sdk.RequestOptions;
import com.phoenixperpsfunding.sdk.gen.CreateOrder;
import com.phoenixperpsfunding.sdk.gen.CreateOrderType;
import com.phoenixperpsfunding.sdk.gen.Order;
import com.phoenixperpsfunding.sdk.gen.OrderSide;
import java.math.BigDecimal;
import java.util.UUID;

final class Example12 {
    public static void main(String[] args) {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();
        String account = client.accounts().list().data().get(0).id();

        try {
            client.orders().get(account, "ord_does_not_exist");
        } catch (PhoenixPerpsException.NotFoundException err) {
            show(err);
        }

        try {
            client.orders().preview(account, new CreateOrder().market("BTC-USD").side(OrderSide.BUY).type(CreateOrderType.MARKET).size(new BigDecimal("0.0000001")));
        } catch (PhoenixPerpsException.ValidationException | PhoenixPerpsException.RuleRejectedException err) {
            show(err);
        }
    }

    static void show(PhoenixPerpsException err) {
        System.out.printf("%s: %s (HTTP %s, %s) %s%n", err.getClass().getSimpleName(), err.code(), err.status(), err.requestId(), err.getMessage());
        if (err.details() != null) System.out.printf("  details: %s%n", err.details());
        System.out.printf("  retryable: %s%s%n", err.retryable(), err.docUrl() == null ? "" : ", see " + err.docUrl());
    }

    /** A write that may or may not have happened: the same key again is safe, a new key could place it twice. */
    static Order placeOnce(PhoenixPerpsClient client, String account, CreateOrder body) throws InterruptedException {
        RequestOptions options = RequestOptions.none().withIdempotencyKey(UUID.randomUUID().toString());
        for (int attempt = 1; ; attempt++) {
            try {
                return client.orders().create(account, body, options);
            } catch (PhoenixPerpsException.ApiTimeoutException err) {
                if (attempt >= 3) throw err; // still unknown after the SDK's own retries and ours: read the orders first
            } catch (PhoenixPerpsException.RateLimitException err) {
                if (attempt >= 3 || err.retryAfter() == null) throw err;
                Thread.sleep(err.retryAfter().toMillis());
            }
        }
    }
}
