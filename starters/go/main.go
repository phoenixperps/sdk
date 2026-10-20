// The starter bot: an EMA cross on one-minute candles, each entry sized so its stop loses PP_RISK_USD, previewed first,
// placed with its stop loss on the API's servers. What it does, exactly: ../README.md. Run it on the sandbox.
//
//	go mod tidy && go run .
package main

import (
	"bufio"
	"context"
	"encoding/json"
	"fmt"
	"log"
	"math"
	"math/big"
	"os"
	"os/signal"
	"regexp"
	"strconv"
	"strings"
	"time"

	phoenixperps "github.com/phoenixperps/sdk/go"
)

type candle struct {
	Time             int64
	High, Low, Close string
	Final            bool
}

// ema is EMA(n): the mean of the first n values, then (x - ema) * 2 / (n + 1) + ema.
type ema struct {
	n     int
	value *float64
	seed  []float64
}

func (e *ema) add(x float64) {
	if e.value == nil {
		e.seed = append(e.seed, x)
		if len(e.seed) == e.n {
			v := sum(e.seed) / float64(e.n)
			e.value = &v
		}
		return
	}
	v := (x-*e.value)*(2/float64(e.n+1)) + *e.value
	e.value = &v
}

// atr is Wilder's ATR(n): the mean of the first n true ranges, then (atr * (n - 1) + tr) / n.
type atr struct {
	n         int
	value     *float64
	seed      []float64
	prevClose *float64
}

func (a *atr) add(c candle) {
	h, l, cl := num(c.High), num(c.Low), num(c.Close)
	tr := h - l
	if a.prevClose != nil {
		tr = math.Max(h-l, math.Max(math.Abs(h-*a.prevClose), math.Abs(l-*a.prevClose)))
	}
	a.prevClose = &cl
	if a.value == nil {
		a.seed = append(a.seed, tr)
		if len(a.seed) == a.n {
			v := sum(a.seed) / float64(a.n)
			a.value = &v
		}
		return
	}
	v := (*a.value*float64(a.n-1) + tr) / float64(a.n)
	a.value = &v
}

func sum(xs []float64) float64 {
	s := 0.0
	for _, x := range xs {
		s += x
	}
	return s
}

func num(s string) float64 {
	f, _ := strconv.ParseFloat(s, 64)
	return f
}

func env(name, fallback string) string {
	if v := os.Getenv(name); v != "" {
		return v
	}
	return fallback
}

func atoi(name string, fallback int) int {
	n, err := strconv.Atoi(env(name, strconv.Itoa(fallback)))
	if err != nil {
		log.Fatalf("%s is a whole number", name)
	}
	return n
}

// loadDotEnv reads .env beside it: only what the environment does not set already.
func loadDotEnv() {
	f, err := os.Open(".env")
	if err != nil {
		return
	}
	defer f.Close()
	re := regexp.MustCompile(`^\s*([A-Z0-9_]+)\s*=\s*(.*?)\s*$`)
	sc := bufio.NewScanner(f)
	for sc.Scan() {
		line := sc.Text()
		m := re.FindStringSubmatch(line)
		if m == nil || strings.HasPrefix(strings.TrimSpace(line), "#") {
			continue
		}
		if _, set := os.LookupEnv(m[1]); !set {
			_ = os.Setenv(m[1], strings.Trim(m[2], `"'`))
		}
	}
}

func less(a, b phoenixperps.Decimal) bool {
	x, _ := new(big.Rat).SetString(string(a))
	y, _ := new(big.Rat).SetString(string(b))
	return x != nil && y != nil && x.Cmp(y) < 0
}

func main() {
	loadDotEnv()
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt)
	defer stop()
	market := strings.ToUpper(env("PP_MARKET", "BTC-USD"))
	riskUSD := phoenixperps.Decimal(env("PP_RISK_USD", "50"))
	fastN, slowN, atrN := atoi("PP_FAST", 9), atoi("PP_SLOW", 21), atoi("PP_ATR", 14)
	atrMult := num(env("PP_ATR_MULT", "2"))
	trailPercent, breakEvenPercent := env("PP_TRAIL_PERCENT", ""), env("PP_BREAK_EVEN_PERCENT", "")
	maxTrades, runCandles := atoi("PP_MAX_TRADES_PER_DAY", 10), atoi("PP_RUN_CANDLES", 0)

	client, err := phoenixperps.NewClient(phoenixperps.WithStreamURLs("", env("PP_STREAM_URL", "")))
	if err != nil {
		log.Fatal(err)
	}
	live := client.Environment() == "live"
	if live && os.Getenv("PHOENIXPERPS_ALLOW_LIVE") != "yes" {
		log.Fatal("This is a live key: the starter trades real money only with PHOENIXPERPS_ALLOW_LIVE=yes. Use a sandbox key to try it.")
	}
	say := func(format string, args ...any) {
		prefix := ""
		if live {
			prefix = "LIVE "
		}
		fmt.Printf("%s %s%s\n", time.Now().UTC().Format("2006-01-02T15:04:05.000Z"), prefix, fmt.Sprintf(format, args...))
	}

	// the account and the market's rules
	accounts, err := client.Accounts.List(ctx)
	if err != nil {
		log.Fatal(err)
	}
	account := ""
	for _, a := range accounts.Data {
		if want := strings.ToUpper(os.Getenv("PP_ACCOUNT")); (want != "" && a.ID == want) || (want == "" && a.Status == "active") {
			account = a.ID
			break
		}
	}
	if account == "" {
		log.Fatal("No active account (or no PP_ACCOUNT) for this key.")
	}
	m, err := client.Markets.Get(ctx, market)
	if err != nil {
		log.Fatal(err)
	}
	rules := phoenixperps.NewMarketRules(m)
	tick := num(string(m.TickSize))
	say("%s on %s: EMA %d/%d, stop %s x ATR(%d), %s USD a trade", account, market, fastN, slowN, env("PP_ATR_MULT", "2"), atrN, riskUSD)

	fast, slow, vol := &ema{n: fastN}, &ema{n: slowN}, &atr{n: atrN}
	tradesDay, tradesN := "", 0

	// onLive is one live candle: the position first, then at most one order (../README.md step 4).
	onLive := func(c candle, prevFast, prevSlow *float64) error {
		if fast.value == nil || slow.value == nil || vol.value == nil || prevFast == nil || prevSlow == nil {
			return nil
		}
		up := *prevFast <= *prevSlow && *fast.value > *slow.value
		down := *prevFast >= *prevSlow && *fast.value < *slow.value
		positions, err := client.Positions.List(ctx, account, &phoenixperps.PositionsListParams{Market: phoenixperps.Ptr(market)})
		if err != nil {
			return err
		}
		var position *phoenixperps.Position
		for i := range positions.Data {
			if num(string(positions.Data[i].Size)) != 0 {
				position = &positions.Data[i]
				break
			}
		}
		if position != nil && !position.Protected {
			say("the %s position has no stop: closing it", position.Side)
			_, err := client.Positions.Close(ctx, account, market, phoenixperps.ClosePosition{})
			return err
		}
		if !up && !down {
			return nil
		}
		side, cross := phoenixperps.OrderSideBuy, "up"
		if down {
			side, cross = phoenixperps.OrderSideSell, "down"
		}
		if position != nil {
			if (position.Side == "long") != up {
				say("cross %s: closing the %s position", cross, position.Side)
				_, err := client.Positions.Close(ctx, account, market, phoenixperps.ClosePosition{})
				return err
			}
			return nil
		}
		day := time.UnixMilli(c.Time).UTC().Format("2006-01-02")
		if tradesDay != day {
			tradesDay, tradesN = day, 0
		}
		if tradesN >= maxTrades {
			say("cross %s: %d entries today already (PP_MAX_TRADES_PER_DAY)", cross, tradesN)
			return nil
		}
		ticks := math.Floor(*vol.value*atrMult/tick + 0.5)
		if ticks < 1 {
			return nil
		}
		distance, err := rules.RoundPrice(phoenixperps.DecimalOf(ticks*tick), phoenixperps.RoundNearest)
		if err != nil {
			return err
		}
		size, err := rules.SizeForRisk(riskUSD, distance)
		if err != nil {
			return err
		}
		if less(size, m.MinSize) || num(string(size)) == 0 {
			say("cross %s: %s USD over a %s stop is below the market's smallest size", cross, riskUSD, distance)
			return nil
		}
		preview, err := client.Orders.Preview(ctx, account, phoenixperps.CreateOrder{Market: market, Side: side, Type: phoenixperps.CreateOrderTypeMarket, Size: &size})
		if err != nil {
			return err
		}
		if preview.Result == "would_reject" {
			code := ""
			if preview.Reject != nil {
				code = preview.Reject.Code
			}
			say("cross %s: the API would refuse %s %s (%s)", cross, side, size, code)
			return nil
		}
		stopLoss := &phoenixperps.OrderExitsStopLoss{Distance: &distance}
		if trailPercent != "" {
			stopLoss.Trail = &phoenixperps.OrderExitsStopLossTrail{Percent: phoenixperps.Dec(trailPercent)}
		}
		if breakEvenPercent != "" {
			stopLoss.BreakEven = &phoenixperps.OrderExitsStopLossBreakEven{TriggerPercent: phoenixperps.Dec(breakEvenPercent), OffsetTicks: phoenixperps.Ptr[int64](1)}
		}
		order, err := client.Orders.Create(ctx, account, phoenixperps.CreateOrder{
			Market: market, Side: side, Type: phoenixperps.CreateOrderTypeMarket, Size: &size,
			ClientOrderID: phoenixperps.Ptr(fmt.Sprintf("starter-%d", c.Time)), Exits: &phoenixperps.OrderExits{StopLoss: stopLoss},
		}, phoenixperps.WithIdempotencyKey(fmt.Sprintf("starter-%s-%d", market, c.Time)))
		if err != nil {
			return err
		}
		tradesN++
		say("cross %s: %s %s %s (%s), stop %s away", cross, side, size, market, order.Status, distance)
		return nil
	}

	// candles in, one at a time: history warms the indicators up, live ones trade
	stream := client.MarketStream(ctx)
	defer stream.Close()
	history := max(3*slowN, 50)
	if _, err := stream.Subscribe(ctx, "candles", []string{market}, map[string]any{"interval": "1m", "history": history}); err != nil {
		log.Fatal(err)
	}
	var last int64
	warm, liveCandles := false, 0
	for {
		f, err := stream.Next(ctx)
		if err != nil {
			if ctx.Err() != nil {
				return
			}
			log.Fatal(err)
		}
		switch f.Type {
		case "snapshot_end":
			if !warm {
				say("warmed up: trading on the next candles")
			}
			warm = true
			continue
		case "candles":
		default:
			continue
		}
		var cs []candle
		if err := json.Unmarshal(f.Data, &cs); err != nil {
			continue
		}
		for _, c := range cs {
			if !c.Final || c.Time <= last {
				continue
			}
			last = c.Time
			prevFast, prevSlow := fast.value, slow.value
			fast.add(num(c.Close))
			slow.add(num(c.Close))
			vol.add(c)
			if !warm {
				continue
			}
			if err := onLive(c, prevFast, prevSlow); err != nil {
				log.Fatalf("stopped: %v", err)
			}
			liveCandles++
			if runCandles > 0 && liveCandles >= runCandles {
				return
			}
		}
	}
}
