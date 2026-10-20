// 08: a live copy of your accounts' positions and working orders: the snapshot first, then every event applied once.
// After a reconnect the SDK resumes from the last event; a resume the server cannot cover is a gap, told with the last
// event id to read GET /v1/events?after= from. An account the server could not read just now comes with its error and
// is left out, never shown flat. Ctrl+C to stop.
//   gradle -q example -Pn=08
import com.fasterxml.jackson.databind.JsonNode;
import com.phoenixperpsfunding.sdk.AccountStream;
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.gen.Account;
import com.phoenixperpsfunding.sdk.gen.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class Example08 {
    private static final Set<String> FINAL = Set.of("filled", "canceled", "rejected", "expired");
    private static final Map<String, Map<String, Object>> POSITIONS = new HashMap<>(); // account -> market -> position
    private static final Map<String, Map<String, Object>> ORDERS = new HashMap<>(); // account -> id -> order

    public static void main(String[] args) throws Exception {
        PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv();
        List<Account> accounts = client.accounts().list().data();
        AccountStream.Options options = AccountStream.options()
                .onGap(after -> System.out.printf("[gap] events after %s may be missing: read GET /v1/events?after=%s%n", after, after));
        try (AccountStream stream = client.openAccountStream(options)) {
            stream.ready().get(); // throws what ended the stream before it was ready (a refused key, too many accounts)
            for (Account a : accounts) {
                JsonNode snap = stream.snapshot(a.id());
                if (snap == null) continue;
                if (snap.hasNonNull("error")) { // its orders and positions are empty because they could not be read
                    System.out.printf("[snapshot] %s: not readable now (%s): read it by REST%n", a.id(), snap.get("error").asText());
                    continue;
                }
                Map<String, Object> positions = new HashMap<>();
                Map<String, Object> orders = new HashMap<>();
                for (JsonNode p : snap.path("positions")) positions.put(p.path("market").asText(), p);
                for (JsonNode o : snap.path("orders")) orders.put(o.path("id").asText(), o);
                POSITIONS.put(a.id(), positions);
                ORDERS.put(a.id(), orders);
            }
            show("snapshot");
            for (Event e : stream) {
                Map<String, Object> positions = POSITIONS.get(e.accountId());
                Map<String, Object> orders = ORDERS.get(e.accountId());
                if (positions == null || orders == null) continue;
                if (e.data().get("order") instanceof Map<?, ?> order) {
                    String id = String.valueOf(order.get("id"));
                    if (FINAL.contains(String.valueOf(order.get("status")))) orders.remove(id);
                    else orders.put(id, order);
                }
                if (e.data().get("position") instanceof Map<?, ?> position) {
                    String market = String.valueOf(position.get("market"));
                    if ("position.closed".equals(e.type().toString())) positions.remove(market);
                    else positions.put(market, position);
                }
                show(e.type().toString());
            }
        }
    }

    private static void show(String why) {
        for (String id : POSITIONS.keySet()) {
            List<String> pos = new ArrayList<>();
            for (Object p : POSITIONS.get(id).values()) pos.add(describe(p));
            System.out.printf("[%s] %s: positions %s; %d working orders%n", why, id, pos.isEmpty() ? "none" : String.join(", ", pos), ORDERS.get(id).size());
        }
    }

    private static String describe(Object p) {
        if (p instanceof JsonNode n) return n.path("market").asText() + " " + n.path("side").asText() + " " + n.path("size").asText();
        if (p instanceof Map<?, ?> m) return m.get("market") + " " + m.get("side") + " " + m.get("size");
        return String.valueOf(p);
    }
}
