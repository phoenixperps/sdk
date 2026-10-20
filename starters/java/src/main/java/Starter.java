// The starter bot: an EMA cross on one-minute candles, each entry sized so its stop loses PP_RISK_USD, previewed first,
// placed with its stop loss on the API's servers. What it does, exactly: ../README.md. Run it on the sandbox.
//   gradle -q run
import com.fasterxml.jackson.databind.JsonNode;
import com.phoenixperpsfunding.sdk.MarketRules;
import com.phoenixperpsfunding.sdk.MarketStream;
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.RequestOptions;
import com.phoenixperpsfunding.sdk.gen.Account;
import com.phoenixperpsfunding.sdk.gen.ClosePosition;
import com.phoenixperpsfunding.sdk.gen.CreateOrder;
import com.phoenixperpsfunding.sdk.gen.CreateOrderType;
import com.phoenixperpsfunding.sdk.gen.Market;
import com.phoenixperpsfunding.sdk.gen.Order;
import com.phoenixperpsfunding.sdk.gen.OrderExits;
import com.phoenixperpsfunding.sdk.gen.OrderExitsStopLoss;
import com.phoenixperpsfunding.sdk.gen.OrderExitsStopLossBreakEven;
import com.phoenixperpsfunding.sdk.gen.OrderExitsStopLossTrail;
import com.phoenixperpsfunding.sdk.gen.OrderSide;
import com.phoenixperpsfunding.sdk.gen.Position;
import com.phoenixperpsfunding.sdk.gen.PositionsListParams;
import com.phoenixperpsfunding.sdk.gen.WhatIf;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Starter {
    private static final Map<String, String> ENV = new HashMap<>(System.getenv());

    /** EMA(n): the mean of the first n values, then (x - ema) * 2 / (n + 1) + ema. */
    static final class Ema {
        final int n;
        final List<Double> seed = new ArrayList<>();
        Double value;

        Ema(int n) {
            this.n = n;
        }

        void add(double x) {
            if (value == null) {
                seed.add(x);
                if (seed.size() == n) value = seed.stream().mapToDouble(Double::doubleValue).sum() / n;
            } else {
                value = (x - value) * (2.0 / (n + 1)) + value;
            }
        }
    }

    /** Wilder's ATR(n): the mean of the first n true ranges, then (atr * (n - 1) + tr) / n. */
    static final class Atr {
        final int n;
        final List<Double> seed = new ArrayList<>();
        Double prevClose;
        Double value;

        Atr(int n) {
            this.n = n;
        }

        void add(double high, double low, double close) {
            double tr = prevClose == null ? high - low : Math.max(high - low, Math.max(Math.abs(high - prevClose), Math.abs(low - prevClose)));
            prevClose = close;
            if (value == null) {
                seed.add(tr);
                if (seed.size() == n) value = seed.stream().mapToDouble(Double::doubleValue).sum() / n;
            } else {
                value = (value * (n - 1) + tr) / n;
            }
        }
    }

    static String env(String name, String fallback) {
        String v = ENV.get(name);
        return v == null || v.isEmpty() ? fallback : v;
    }

    /** .env beside it: only what the environment does not set already. */
    static void loadDotEnv() throws IOException {
        Path file = Path.of(".env");
        if (!Files.exists(file)) return;
        Pattern re = Pattern.compile("^\\s*([A-Z0-9_]+)\\s*=\\s*(.*?)\\s*$");
        for (String line : Files.readAllLines(file)) {
            Matcher m = re.matcher(line);
            if (m.matches() && !line.strip().startsWith("#") && !ENV.containsKey(m.group(1))) {
                ENV.put(m.group(1), m.group(2).replaceAll("^(['\"])(.*)\\1$", "$2"));
            }
        }
    }

    final String market = env("PP_MARKET", "BTC-USD").toUpperCase(Locale.ROOT);
    final BigDecimal riskUsd = new BigDecimal(env("PP_RISK_USD", "50"));
    final int fastN = Integer.parseInt(env("PP_FAST", "9"));
    final int slowN = Integer.parseInt(env("PP_SLOW", "21"));
    final int atrN = Integer.parseInt(env("PP_ATR", "14"));
    final double atrMult = Double.parseDouble(env("PP_ATR_MULT", "2"));
    final String trailPercent = env("PP_TRAIL_PERCENT", "");
    final String breakEvenPercent = env("PP_BREAK_EVEN_PERCENT", "");
    final int maxTrades = Integer.parseInt(env("PP_MAX_TRADES_PER_DAY", "10"));
    final int runCandles = Integer.parseInt(env("PP_RUN_CANDLES", "0"));

    final PhoenixPerpsClient client;
    final boolean live;
    String account;
    Market rulesOf;
    MarketRules rules;
    double tick;
    final Ema fast = new Ema(fastN);
    final Ema slow = new Ema(slowN);
    final Atr atr = new Atr(atrN);
    String tradesDay = "";
    int tradesN;

    Starter() {
        String streamUrl = env("PP_STREAM_URL", "");
        client = PhoenixPerpsClient.builder().apiKey(ENV.get("PHOENIXPERPS_API_KEY")).streamUrls(null, streamUrl.isEmpty() ? null : streamUrl).build();
        live = "live".equals(client.environment());
    }

    void log(String msg) {
        System.out.printf("%s %s%s%n", Instant.now(), live ? "LIVE " : "", msg);
    }

    /** One live candle: the position first, then at most one order (../README.md step 4). */
    void onLive(long time, Double prevFast, Double prevSlow) {
        if (fast.value == null || slow.value == null || atr.value == null || prevFast == null || prevSlow == null) return;
        boolean up = prevFast <= prevSlow && fast.value > slow.value;
        boolean down = prevFast >= prevSlow && fast.value < slow.value;
        Position position = null;
        for (Position p : client.positions().list(account, new PositionsListParams().market(market)).data()) {
            if (p.size().signum() != 0) {
                position = p;
                break;
            }
        }
        if (position != null && !Boolean.TRUE.equals(position.protectedValue())) {
            log("the " + position.side() + " position has no stop: closing it");
            client.positions().close(account, market, new ClosePosition());
            return;
        }
        if (!up && !down) return;
        String cross = up ? "up" : "down";
        if (position != null) {
            if ("long".equals(position.side().toString()) != up) {
                log("cross " + cross + ": closing the " + position.side() + " position");
                client.positions().close(account, market, new ClosePosition());
            }
            return;
        }
        String day = Instant.ofEpochMilli(time).atOffset(ZoneOffset.UTC).toLocalDate().toString();
        if (!tradesDay.equals(day)) {
            tradesDay = day;
            tradesN = 0;
        }
        if (tradesN >= maxTrades) {
            log("cross " + cross + ": " + tradesN + " entries today already (PP_MAX_TRADES_PER_DAY)");
            return;
        }
        double ticks = Math.floor(atr.value * atrMult / tick + 0.5);
        if (ticks < 1) return;
        BigDecimal distance = rules.roundPrice(BigDecimal.valueOf(ticks * tick), MarketRules.Mode.NEAREST);
        BigDecimal size = rules.sizeForRisk(riskUsd, distance);
        if (size.compareTo(rulesOf.minSize()) < 0 || size.signum() == 0) {
            log("cross " + cross + ": " + riskUsd + " USD over a " + distance + " stop is below the market's smallest size");
            return;
        }
        OrderSide side = up ? OrderSide.BUY : OrderSide.SELL;
        WhatIf preview = client.orders().preview(account, new CreateOrder().market(market).side(side).type(CreateOrderType.MARKET).size(size));
        if ("would_reject".equals(preview.result().toString())) {
            log("cross " + cross + ": the API would refuse " + side + " " + size + " (" + (preview.reject() == null ? "" : preview.reject().code()) + ")");
            return;
        }
        OrderExitsStopLoss stopLoss = new OrderExitsStopLoss().distance(distance);
        if (!trailPercent.isEmpty()) stopLoss.trail(new OrderExitsStopLossTrail().percent(new BigDecimal(trailPercent)));
        if (!breakEvenPercent.isEmpty()) stopLoss.breakEven(new OrderExitsStopLossBreakEven().triggerPercent(new BigDecimal(breakEvenPercent)).offsetTicks(1L));
        Order order = client.orders().create(account, new CreateOrder()
                .market(market).side(side).type(CreateOrderType.MARKET).size(size)
                .clientOrderId("starter-" + time).exits(new OrderExits().stopLoss(stopLoss)),
                RequestOptions.none().withIdempotencyKey("starter-" + market + "-" + time));
        tradesN++;
        log("cross " + cross + ": " + side + " " + size.toPlainString() + " " + market + " (" + order.status() + "), stop " + distance.toPlainString() + " away");
    }

    int run() throws InterruptedException {
        if (live && !"yes".equals(env("PHOENIXPERPS_ALLOW_LIVE", ""))) {
            System.err.println("This is a live key: the starter trades real money only with PHOENIXPERPS_ALLOW_LIVE=yes. Use a sandbox key to try it.");
            return 1;
        }
        // the account and the market's rules
        String wanted = env("PP_ACCOUNT", "").toUpperCase(Locale.ROOT);
        for (Account a : client.accounts().list().data()) {
            if (wanted.isEmpty() ? "active".equals(a.status().toString()) : a.id().equals(wanted)) {
                account = a.id();
                break;
            }
        }
        if (account == null) throw new IllegalStateException(wanted.isEmpty() ? "No active account for this key." : "No account " + wanted + " for this key.");
        rulesOf = client.markets().get(market);
        rules = new MarketRules(rulesOf);
        tick = rulesOf.tickSize().doubleValue();
        log(account + " on " + market + ": EMA " + fastN + "/" + slowN + ", stop " + env("PP_ATR_MULT", "2") + " x ATR(" + atrN + "), " + riskUsd + " USD a trade");

        // candles in, one at a time: history warms the indicators up, live ones trade
        try (MarketStream stream = client.openMarketStream()) {
            stream.subscribe("candles", List.of(market), Map.of("interval", "1m", "history", Math.max(3 * slowN, 50)), Duration.ofSeconds(30));
            long last = 0;
            boolean warm = false;
            int liveCandles = 0;
            for (MarketStream.Frame f : stream) {
                if ("snapshot_end".equals(f.type())) {
                    if (!warm) log("warmed up: trading on the next candles");
                    warm = true;
                    continue;
                }
                if (!"candles".equals(f.type())) continue;
                for (JsonNode c : f.data()) {
                    long time = c.path("time").asLong();
                    if (!c.path("final").asBoolean() || time <= last) continue;
                    last = time;
                    Double prevFast = fast.value;
                    Double prevSlow = slow.value;
                    double close = Double.parseDouble(c.path("close").asText());
                    fast.add(close);
                    slow.add(close);
                    atr.add(Double.parseDouble(c.path("high").asText()), Double.parseDouble(c.path("low").asText()), close);
                    if (!warm) continue;
                    onLive(time, prevFast, prevSlow);
                    if (runCandles > 0 && ++liveCandles >= runCandles) return 0;
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        loadDotEnv();
        System.exit(new Starter().run());
    }
}
