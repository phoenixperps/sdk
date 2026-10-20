// 11: receive webhooks: every delivery is checked (its signature and its time) before it is trusted.
//   PHOENIXPERPS_WEBHOOK_SECRET=ppwhsec_... gradle -q example -Pn=11
// Then point a webhook endpoint (dashboard, Developers, Webhooks) at https://<this machine>/webhooks (port 8080 here).
import com.phoenixperpsfunding.sdk.Webhooks;
import com.phoenixperpsfunding.sdk.gen.WebhookPayload;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

final class Example11 {
    public static void main(String[] args) throws IOException {
        String secret = System.getenv("PHOENIXPERPS_WEBHOOK_SECRET");
        if (secret == null || secret.isEmpty()) throw new IllegalStateException("Set PHOENIXPERPS_WEBHOOK_SECRET to the endpoint's signing secret.");

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/webhooks", exchange -> {
            int status;
            if (!"POST".equals(exchange.getRequestMethod())) {
                status = 404;
            } else {
                String raw = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8); // the body exactly as received
                Map<String, String> headers = new HashMap<>();
                exchange.getRequestHeaders().forEach((k, v) -> headers.put(k.toLowerCase(Locale.ROOT), v.get(0)));
                try {
                    WebhookPayload e = Webhooks.verify(raw, headers, secret); // webhook.ping (test true) included
                    System.out.printf("%s%s on %s: %s%n", e.type(), Boolean.TRUE.equals(e.test()) ? " (a test)" : "", e.accountId(), e.data());
                    status = 200;
                } catch (Webhooks.VerificationException err) {
                    System.out.printf("refused a delivery: %s%n", err.code());
                    status = 400;
                }
            }
            exchange.sendResponseHeaders(status, -1);
            exchange.close();
        });
        server.start();
        System.out.println("listening on :8080/webhooks");
    }
}
