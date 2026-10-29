// Generated from the API spec by generator/printers/typescript.js. Do not edit: run `node generator`.
import type { RequestOptions, ListOptions, PagePromise } from '../core';

/** Every error has this shape. */
export interface Error {
  error: {
    /** The family of the error; it decides the HTTP status. */ type: "invalid_request" | "authentication" | "permission" | "not_found" | "method" | "conflict" | "payload" | "rule_rejection" | "key_limit" | "rate_limit" | "server" | "engine" | (string & {});
    /** The error code: match on this. */ code: string;
    /** A sentence for people. It may change; never match on it. */ message: string;
    /** The id of the request; give it to support. */ request_id: string;
    /** Whether the same request may succeed later. */ retryable: boolean;
    /** Where this code is explained. */ doc_url: string;
    /** More about the problem: the fields at fault, a limit, where to go. */ details?: Record<string, unknown> | Array<unknown>;
    /** The order the error is about (an order the engine refused is kept, with this id). */ order_id?: string;
  };
}

export interface Platform {
  /** The environment of the host you called. */ environment: "live" | "sandbox" | (string & {});
  /** The version of this API. */ version: string;
  /** The time on the server. */ server_time: number;
  /** The documentation. */ docs_url: string;
  /** This API as OpenAPI 3.1. */ spec_url: string;
  status: {
    /** Whether the trading engine is reachable. */ engine: "up" | "down" | (string & {});
    /** When the trading engine last started. */ engine_started_at: number | null;
    /** Whether the market-data stream is live, with every price feed. */ market_data: "up" | "down" | (string & {});
    /** Whether outbound webhooks are being delivered. */ webhooks: "up" | "down" | (string & {});
  };
}

export interface Status {
  /** The environment of the host you called. */ environment: "live" | "sandbox" | (string & {});
  /** Whether this environment takes requests. */ api: "operational" | "disabled" | (string & {});
  engine: {
    /** Whether the API is connected to the trading engine. */ connected: boolean;
    /** When the trading engine last started. */ started_at: number | null;
  };
  account_data: {
    /** When your keys and accounts were last confirmed with the website. */ synced_at: number | null;
  };
  market_data: {
    /** Whether the market-data stream is live, with every price feed. */ live: boolean;
    /** When that was last checked. */ checked_at: number | null;
  };
  features: {
    /** The account stream. */ stream: boolean;
    /** Market data. */ market_data: boolean;
    /** Webhooks. */ webhooks: boolean;
    /** TradingView alerts. */ tradingview: boolean;
    /** AI assistants (MCP). */ mcp: boolean;
  };
  /** The time on the server. */ server_time: number;
}

export interface Limits {
  /** The environment of the host you called. */ environment: "live" | "sandbox" | (string & {});
  /** The request budgets. A request takes from every budget it belongs to. */ rate: {
    public_per_ip: {
    /** Requests a minute. */ per_min: number;
    /** Requests at once. */ burst: number;
  };
    per_ip: {
    /** Requests a minute. */ per_min: number;
    /** Requests at once. */ burst: number;
  };
    per_key_read: {
    /** Requests a minute. */ per_min: number;
    /** Requests at once. */ burst: number;
  };
    per_key_trade: {
    /** Requests a minute. */ per_min: number;
    /** Requests at once. */ burst: number;
  };
    per_account_trade: {
    /** Requests a minute. */ per_min: number;
    /** Requests at once. */ burst: number;
  };
    per_customer: {
    /** Requests a minute. */ per_min: number;
    /** Requests at once. */ burst: number;
  };
    bulk: {
    /** Requests a minute. */ per_min: number;
    /** Requests at once. */ burst: number;
  };
    what_if: {
    /** Requests a minute. */ per_min: number;
    /** Requests at once. */ burst: number;
  };
  };
  keys: {
    /** Keys you may hold in each environment. */ per_environment: number;
    /** Addresses in a key's allowlist. */ ip_allowlist_max: number;
    /** The longest a key may live, in days. */ expiry_max_days: number;
  };
  orders: {
    /** Working orders an account may hold. */ max_working_per_account: number;
    /** Working orders across your accounts. */ max_working_per_customer: number;
    /** New orders a minute on one account. */ new_per_min_account: number;
    /** New orders a day on one account. */ new_per_day_account: number;
    /** How far from the market price a priced order may be, in percent. */ price_band_pct: string;
    /** The oldest price an order is checked against, in seconds. */ quote_max_age_s: number;
    /** The oldest unchanged price of a quiet market an order is checked against, while the price feed lives (other markets move), in seconds. */ quote_max_age_slow_s: number;
    /** The nearest a gtd order may expire, in seconds from now. */ gtd_min_seconds: number;
    /** The farthest, in days from now. */ gtd_max_days: number;
  };
  sandbox?: {
    /** Sandbox accounts you may open, ever. */ accounts_for_life: number;
    /** The balance a sandbox account starts with. */ start_balance: string;
    /** Hours without use before a sandbox account is parked. */ parked_after_hours: number;
  };
  strategies: {
    /** Running strategies an account may hold. */ per_account: number;
    /** Of those, programs (oco, twap, vwap, scaled, chase). */ programs_per_account: number;
    /** Working orders of strategies on one account. */ legs_per_account: number;
    /** Running strategies across your accounts. */ per_customer: number;
    /** Take-profit legs on one position. */ take_profits_per_position: number;
    /** Stop-loss legs on one position. */ stop_losses_per_position: number;
    /** The shortest twap or vwap, in minutes. */ twap_min_minutes: number;
    /** The longest twap or vwap, in minutes. */ twap_max_minutes: number;
    /** The shortest time between slices, in seconds. */ slice_min_seconds: number;
    /** The most limits in a scaled ladder. */ scaled_max_rungs: number;
    /** Whether chase strategies may start. */ chase_enabled: boolean;
    /** The longest a chase may run, in minutes. */ chase_max_minutes: number;
  };
  /** The largest request body. */ body_max_bytes: number;
  page_limit: {
    /** Items a page holds by default. */ default: number;
    /** The most a page may hold. */ max: number;
  };
}

export interface Market {
  /** A market id: the asset and USD. */ market: string;
  /** The market's name; for a currency, the pair its price is quoted in (JPY-USD: USD/JPY, yen per dollar). */ name: string;
  /** Whether the market trades now. */ status: "open" | "halted" | "closed" | (string & {});
  /** The venue it trades on: its prices and fills are that venue's. Null for a currency on FX hours. */ venue: string | null;
  /** When it trades: around the clock, its underlying market's sessions (Standard Market Hours, closed on weekends), or FX hours. */ trading_hours: "24/7" | "standard" | "fx" | (string & {});
  /** For an option (the same asset on another venue, or on Standard Market Hours): its market. Null for a market. */ option_of: string | null;
  /** The ways this asset can be traded, the market first, then its options: an account holds one of them at a time (market_option_held). Null when there is only the market. */ options: Array<{
    /** A market id: the asset and USD. */ market: string;
    /** Its venue. */ venue: string | null;
    /** When it trades. */ trading_hours: "24/7" | "standard" | "fx" | (string & {});
  }> | null;
  /** Prices are multiples of this. */ tick_size: string;
  /** Sizes are multiples of this, in the base asset. */ size_step: string;
  /** The smallest order size (the size step). An order must also be worth at least min_notional. */ min_size: string;
  /** The smallest order value in USD: an order worth less is refused order_too_small. */ min_notional: string | null;
  /** The highest leverage on this market. */ max_leverage: number;
  /** The fee on each fill, in percent of its value. */ fee_rate_pct: string;
  /** The most you may hold on this market across your accounts on the same rule, in USD. */ exposure_cap: string | null;
  /** How far from the price a market order may fill, in percent. */ max_slippage_pct: {
    /** The slippage bound a market order gets when it names none. */ default: string | null;
    /** The tightest bound an order may ask. */ min: string | null;
    /** The widest bound an order may ask. */ max: string | null;
  } | null;
}

/** A market's 24-hour figures and open interest. A figure not known yet is null. */
export interface MarketStats {
  /** A market id: the asset and USD. */ market: string;
  /** The last trade. */ last: string | null;
  /** The change over 24 hours. */ change_24h: string | null;
  /** That change in percent. */ change_24h_pct: string | null;
  /** The highest in 24 hours. */ high_24h: string | null;
  /** The lowest in 24 hours. */ low_24h: string | null;
  /** The size traded in 24 hours, in the base asset. */ volume_24h: string | null;
  /** The value traded in 24 hours, in USD. */ notional_volume_24h: string | null;
  /** The market's open interest: the size held open in positions, in the base asset. */ open_interest: string | null;
  /** That open interest in USD, at the last price. */ notional_open_interest: string | null;
}

export type Rule = {
    /** The balance the account started with. */ start_balance: string;
    daily_loss: {
    /** The most the account may lose in a trading day, in USD. */ amount: string | null;
    /** The same limit in percent of its basis. */ pct: string | null;
    /** What the day's loss is measured from. */ basis: "peak_balance" | "start_balance" | "peak_equity" | "day_start_balance" | "day_start_equity" | (string & {});
    /** What happens when it is reached. */ on_breach: "fail_account" | "flatten" | "flatten_and_pause" | (string & {});
  } | null;
    max_drawdown: {
    /** The most the account may fall from its reference, in USD. */ amount: string | null;
    /** The same limit in percent. */ pct: string | null;
    /** How the reference moves: end_of_day and end_of_trade follow the highest balance, static stays at the start, intraday_equity follows the highest equity. */ mode: "end_of_day" | "end_of_trade" | "static" | "intraday_equity" | (string & {});
  } | null;
    weekly_loss: {
    /** The most the account may lose in a week, in USD. */ amount: string | null;
    /** The same limit in percent. */ pct: string | null;
  } | null;
    profit_target: {
    /** The profit to reach, in USD. */ amount: string | null;
    /** The same target in percent. */ pct: string | null;
    /** Positions must be closed when it is reached. */ requires_flat: boolean;
  } | null;
    /** Days with a trade the account needs. */ min_trading_days: number | null;
    /** Calendar days the account needs. */ min_calendar_days: number | null;
    consistency: {
    /** The most one day may bring, in USD. */ amount: string | null;
    /** The most one day may bring, in percent of the total. */ pct: string | null;
  } | null;
    /** Positions may stay open overnight. */ overnight_holding: boolean;
    /** Positions may stay open over the weekend. */ weekend_holding: boolean;
    /** Days without a trade after which the account fails. */ inactivity_days: number | null;
  } | null;

export interface Account {
  /** The account number. */ id: string;
  /** The environment the account lives in. */ environment: "live" | "sandbox" | (string & {});
  /** Where the account is in the programme. */ stage: "evaluation" | "funded" | "sandbox" | (string & {});
  /** pending: being opened; active: trading. */ status: "pending" | "active" | "passed" | "failed" | "closed" | "suspended" | (string & {});
  /** The product it was bought as. */ product: string | null;
  /** Its size as sold. */ tier: string | null;
  /** The balance it started with. */ start_balance: string | null;
  /** The balance: closed trades and fees only. */ balance: string | null;
  rule: Rule;
  /** A payout may be requested now. */ payout_eligible: boolean;
  /** Sandbox only: parked after a while unused; it wakes the moment you use it. */ parked?: boolean;
  /** When the account was made. */ created_at: number | null;
}

export type Limit = {
    /** The limit, in USD. */ limit?: string;
    /** The equity at which it is breached. */ floor?: string;
    /** How much more the account may lose before it. */ room?: string;
    /** How much of it is used, in percent. */ used_pct?: string | null;
    /** Daily loss only: when it resets (midnight New York). */ resets_at?: number;
    /** Daily loss only: what happens when it is reached. */ on_breach?: "fail_account" | "flatten" | "flatten_and_pause" | (string & {});
    /** Daily loss only: the day's start is estimated (the engine does not publish it). */ estimated?: boolean;
    /** Max drawdown only: how the reference moves. */ mode?: "end_of_day" | "end_of_trade" | "static" | "intraday_equity" | (string & {});
    /** Max drawdown only: the engine may hold a higher floor than shown (it follows equity highs). */ at_least?: boolean;
    /** The rule no longer applies this limit (its cut-off was reached). */ lifted: boolean;
  } | null;

export interface Warning {
  /** What it is about. */ code: "daily_loss_used" | "max_drawdown_used" | "liquidation_near" | "no_stop_loss" | "margin_used" | "order_without_position" | (string & {});
  /** How serious. */ level: "info" | "warning" | "critical" | (string & {});
  /** The market it is about. */ market?: string;
  /** The figure behind it, in percent. */ value?: string;
}

export interface AccountRisk {
  /** When the oldest figure here was read. */ as_of: number;
  /** Some figures are older than usual (the engine was busy). */ stale: boolean;
  /** The balance. */ balance: string;
  /** The balance plus the open P&L at the market price. */ equity: string;
  /** The open P&L. */ unrealized_pnl: string;
  /** The equity change since midnight New York, open positions included: what the daily loss rule counts. */ pnl_today: string;
  /** The P&L closed today. */ realized_today: string;
  /** The fees paid today. */ fees_today: string;
  /** Trades closed today. */ trades_today: number;
  /** The margin your positions hold. */ margin_used: string;
  /** What is left of the balance for new positions. */ margin_available: string | null;
  exposure: {
    /** The value of every open position. */ gross: string;
    /** The value of the long ones. */ long: string;
    /** The value of the short ones. */ short: string;
    /** Gross exposure over equity. */ effective_leverage: string | null;
    /** Per market, largest first. */ markets: Array<{
    /** A market id: the asset and USD. */ market: string;
    /** The value held on it. */ notional: string;
    /** That value over equity. */ leverage: string | null;
    /** The exposure cap of the market. */ cap: string | null;
    /** How much of the cap is used, counting your other accounts on the same rule, in percent. */ cap_used_pct: string | null;
  }>;
  };
  daily_loss: Limit;
  max_drawdown: Limit;
  profit_target: {
    /** The balance that reaches it. */ goal_balance: string;
    /** Still to make. */ remaining: string;
    /** How far, in percent. */ progress_pct: string | null;
    /** Positions must be closed when it is reached. */ requires_flat: boolean;
  } | null;
  trading_days: {
    /** Days with a trade so far. */ count: number | null;
    /** Days the rule needs. */ required: number | null;
  };
  nearest_liquidation: {
    /** A market id: the asset and USD. */ market: string;
    /** Its liquidation price. */ price: string;
    /** How far the price is from it, in percent. */ distance_pct: string;
  } | null;
  /** When the trading day ends (midnight New York). */ trading_day_ends_at: number;
  /** What needs your attention, most serious first. */ warnings: Array<Warning>;
}

export interface Progress {
  /** Where the account is in the programme. */ stage: "evaluation" | "funded" | "sandbox" | (string & {});
  objectives: {
    profit_target: {
    /** The balance that reaches it. */ goal_balance: string;
    /** Still to make. */ remaining: string;
    /** How far, in percent. */ progress_pct: string | null;
    /** Positions must be closed when it is reached. */ requires_flat: boolean;
    /** Whether it is reached. */ status: "open" | "met" | (string & {});
  } | null;
    min_trading_days: {
    /** Days the rule needs. */ required: number;
    /** Days with a trade so far. */ count: number | null;
    /** Whether it is reached. */ status: "open" | "met" | (string & {});
  } | null;
    min_calendar_days: {
    /** Calendar days the rule needs. */ required: number;
    /** Calendar days so far. */ count: number;
    /** Whether it is reached. */ status: "open" | "met" | (string & {});
  } | null;
  };
  limits: {
    daily_loss: LimitStanding;
    max_drawdown: LimitStanding;
  };
  payout: {
    /** A payout may be requested now. */ eligible: boolean;
    /** What may be paid out. */ available: string;
    /** The smallest payout. */ min_payout: string | null;
    /** Paid out so far. */ paid_out: string;
  } | null;
  /** When the oldest figure here was read. */ as_of: number;
}

export interface TradingPolicy {
  rule: Rule;
  trading_day: {
    /** The trading day runs midnight to midnight in this zone. */ timezone: string;
    /** When this one ends. */ ends_at: number;
  };
  slippage_pct: {
    /** The bound a market order gets when it names none, in percent. */ default: string | null;
    /** The tightest it may ask. */ min: string | null;
    /** The widest it may ask. */ max: string | null;
  };
  margin: {
    /** The margin mode of a market you never set. */ default_mode: "isolated" | "cross" | (string & {});
    /** The share of a position's margin kept as maintenance, in percent. */ maintenance_margin_pct: string | null;
  };
  orders: {
    /** Working orders the account may hold. */ max_working_per_account: number;
    /** New orders a minute. */ new_per_min_account: number;
    /** How far from the market price a priced order may be, in percent. */ price_band_pct: string;
    /** The oldest price an order is checked against, in seconds. */ quote_max_age_s: number;
    /** The oldest unchanged price of a quiet market an order is checked against, while the price feed lives (other markets move), in seconds. */ quote_max_age_slow_s: number;
  };
  /** The limits you set on the key. */ controls: {
    /** The markets the key may trade; empty: every market. */ markets: Array<string>;
    /** The key may only reduce positions. */ reduce_only: boolean;
    /** The largest order the key may place, in USD. */ max_order_usd: string | null;
    /** The largest position the key may hold, in USD. */ max_position_usd: string | null;
    /** The loss in a trading day at which the key stops trading the account. */ daily_loss_usd: string | null;
  };
  budgets: {
    /** Reads a minute for this key. */ read_per_min: number;
    /** Trading calls a minute for this key. */ trade_per_min: number;
  };
  /** When the oldest figure here was read. */ as_of: number;
}

export interface EquityHistory {
  /** One point per closed trade, or per trading day. */ resolution: "trade" | "day" | (string & {});
  /** Oldest first; the first point is the opening balance. */ points: Array<{
    /** When. */ time: number;
    /** The trading day (New York), with resolution day. */ date?: string;
    /** The balance then. */ balance: string;
  }>;
  live: {
    /** When the live figures were read. */ time: number | null;
    /** The balance now. */ balance: string;
    /** The equity now. */ equity: string;
  };
}

export interface DailyResult {
  /** The trading day (midnight to midnight New York). */ date: string;
  /** Trades closed that day. */ trades: number;
  /** Their P&L before fees. */ gross_pnl: string;
  /** Their fees. */ fees: string;
  /** Their P&L after fees. */ pnl: string;
  /** Paid out that day. */ withdrawn: string;
  /** The balance at the day's end. */ balance_end: string | null;
}

export interface MarginSetting {
  /** A market id: the asset and USD. */ market: string;
  /** The leverage new positions on this market get. */ leverage: number;
  /** Isolated: each position holds its own margin. Cross: positions share the balance. */ margin_mode: "isolated" | "cross" | (string & {});
  /** The highest this account may use on the market (its trading rule's). null: the market is not available on this account. */ max_leverage: number | null;
  /** Where it comes from: the open position, your saved setting, or the market default. */ source: "position" | "saved" | "default" | (string & {});
  /** A position or a working order on the market: the mode cannot change now. */ locked: boolean;
}

export interface ExitLeg {
  /** The working order. */ order_id: string;
  /** The order's type. */ type: "market" | "limit" | "stop_market" | "stop_limit" | "take_market" | "take_limit" | (string & {});
  /** Its trigger or limit price. */ price: string | null;
  /** Its size. */ size: string | null;
}

export interface Position {
  /** This position, while it stays open on the same side. */ id: string;
  /** A market id: the asset and USD. */ market: string;
  /** Long or short. */ side: "long" | "short" | (string & {});
  /** The size, in the base asset. */ size: string;
  /** The average entry price. */ entry_price: string | null;
  /** The market price it is valued at. */ mark_price: string | null;
  /** Its value at that price. */ notional: string | null;
  /** Its open P&L. */ unrealized_pnl: string | null;
  /** Its leverage. */ leverage: number;
  /** Its margin mode. */ margin_mode: "isolated" | "cross" | (string & {});
  /** The margin it took at entry. */ initial_margin: string | null;
  /** Margin you added to it. */ added_margin: string | null;
  /** What it holds of the balance. */ margin: string | null;
  liquidation: {
    /** The price at which it is liquidated; null when there is none or it depends on several positions. */ price: string | null;
    /** How far the market price is from it, in percent. */ distance_pct: string | null;
    /** Cross margin on several markets: no single price, every one of them moves it. */ account_wide: boolean;
  } | null;
  breach_prices: {
    /** The price at which this position alone would breach the daily loss limit. */ daily_loss: string | null;
    /** The price at which it alone would breach the max drawdown. */ max_drawdown: string | null;
  };
  exits: {
    /** Limit orders on the closing side. */ take_profit: Array<ExitLeg>;
    /** Stop orders on the closing side. */ stop_loss: Array<ExitLeg>;
  };
  /** At least one stop loss rests on it. */ protected: boolean;
  /** When it opened. */ opened_at: number | null;
}

export interface ClosedPosition {
  /** A market id: the asset and USD. */ market: string;
  /** Long or short. */ side: "long" | "short" | (string & {});
  /** The size closed. */ size: string;
  /** The average entry price. */ entry_price: string | null;
  /** The average exit price. */ exit_price: string | null;
  /** The P&L before fees. */ gross_pnl: string;
  /** The fees. */ fees: string;
  /** The P&L after fees. */ pnl: string;
  /** When it opened. */ opened_at: number;
  /** When it was fully closed. */ closed_at: number;
  /** The pieces it was closed in. */ lots: Array<{
    /** The size. */ size: string;
    /** Entry. */ entry_price: string | null;
    /** Exit. */ exit_price: string | null;
    /** P&L after fees. */ pnl: string;
    /** Fees. */ fees: string;
    /** Opened. */ opened_at: number | null;
    /** Closed. */ closed_at: number;
  }>;
}

export interface Order {
  /** The order. */ id: string;
  /** Your own id for it, when it was placed through the API. */ client_order_id: string | null;
  /** The account number. */ account_id: string;
  /** A market id: the asset and USD. */ market: string;
  /** The order's side; null for an order placed elsewhere before the API saw it. */ side: "buy" | "sell" | (string & {}) | null;
  /** The order's type. */ type: "market" | "limit" | "stop_market" | "stop_limit" | "take_market" | "take_limit" | (string & {}) | null;
  /** unconfirmed: the engine has not confirmed its last state yet. */ status: "pending" | "working" | "filled" | "canceled" | "rejected" | "expired" | "unconfirmed" | (string & {});
  /** The size, in the base asset. */ size: string | null;
  /** How much filled. */ filled_size: string | null;
  /** Its limit price. */ limit_price: string | null;
  /** Its trigger price. */ stop_price: string | null;
  /** The average fill price. */ average_fill_price: string | null;
  /** The fee its fill paid. */ fee: string | null;
  /** It may only reduce a position; null when not known (placed elsewhere). */ reduce_only: boolean | null;
  /** How long it rests. */ time_in_force: "gtc" | "gtd" | "ioc" | "fok" | (string & {});
  /** A gtd order: when it expires (it ends expired then); null otherwise. */ expires_at: number | null;
  /** It may only rest, never fill at once; null when not known (placed elsewhere). */ post_only: boolean | null;
  /** Where it was placed: this API, an AI assistant (the MCP server), a trading platform, a strategy, a TradingView alert, or the system (a liquidation). */ source: "api" | "mcp" | "platform" | "strategy" | "tradingview" | "system" | (string & {});
  /** The strategy that placed it (source strategy). */ strategy_id: string | null;
  /** Exits attached to it (orders placed through the API). */ exits: Record<string, unknown> | null;
  /** Why it was canceled. */ cancel_reason: "user" | "cancel_all" | "flatten" | "close_all" | "position_closed" | "position_reversed" | "close_assist" | "oco" | "replaced" | "strategy" | "key_loss_stop" | "risk_rule" | "system_restart" | "external" | (string & {}) | null;
  reject: {
    /** The error code. */ code: string;
    /** What happened. */ message: string | null;
  } | null;
  /** When it was placed. */ created_at: number | null;
  /** When it filled. */ filled_at: number | null;
  /** When it was canceled. */ canceled_at: number | null;
  /** Its latest change. */ updated_at?: number | null;
}

export interface Fill {
  /** The fill. */ id: string;
  /** Its order. */ order_id: string;
  /** A market id: the asset and USD. */ market: string;
  /** The side; null for an order placed elsewhere before the API saw it. */ side: "buy" | "sell" | (string & {}) | null;
  /** The size filled. */ size: string;
  /** The fill price. */ price: string;
  /** Its fee. */ fee: string | null;
  /** A liquidation filled it. */ liquidation: boolean;
  /** When it filled. */ time: number;
}

export interface Me {
  credential: {
    /** What you called with: an API key, or an app you signed in to. */ kind: "key" | "app" | (string & {});
    /** The key's id (the part after ppk_test_ or ppk_live_), or the app access's id. */ id: string;
    /** Its name (an app: the app's name). */ name: string;
    /** Its environment. */ environment: "live" | "sandbox" | (string & {});
    /** read: look only. trade: also place and cancel. */ access: "read" | "trade" | (string & {});
    /** When it expires; null: never. */ expires_at: number | null;
    /** When it was made. */ created_at: number | null;
  };
  /** The accounts it reaches. */ accounts: Array<string>;
  /** all: every account of the environment, new ones too. selected: only those listed. */ account_scope: "all" | "selected" | (string & {});
  /** The limits you set on the key. */ controls: {
    /** The markets the key may trade; empty: every market. */ markets: Array<string>;
    /** The key may only reduce positions. */ reduce_only: boolean;
    /** The largest order the key may place, in USD. */ max_order_usd: string | null;
    /** The largest position the key may hold, in USD. */ max_position_usd: string | null;
    /** The loss in a trading day at which the key stops trading the account. */ daily_loss_usd: string | null;
  };
  budgets: {
    /** Reads a minute for this key. */ read_per_min: number;
    /** Trading calls a minute for this key. */ trade_per_min: number;
  };
  /** Through the MCP server (an AI assistant): how its trades are confirmed and its budget. Null for any other call. */ assistant: {
    /** Each trade is shown first and placed only when repeated with its confirmation (preview, then confirm). */ confirm_trades: boolean;
    /** How long a preview's confirmation holds, in seconds. */ confirm_seconds: number;
    /** The AI assistant's own budget, on top of the others. */ budget: {
    /** Reads a minute. */ reads_per_min: number;
    /** Trading calls a minute. */ writes_per_min: number;
    /** Trading calls an hour. */ writes_per_hour: number;
  };
  } | null;
}

/** A new order. */
export interface CreateOrder {
  /** A market id: the asset and USD. */ market: string;
  /** The order's side. */ side: "buy" | "sell" | (string & {});
  /** market: now, at the best price within your worst price. limit: at your price or better. stop_market: a market order once the last price reaches the stop. stop_limit: a limit once the last price reaches the stop. */ type: "market" | "limit" | "stop_market" | "stop_limit" | (string & {});
  /** The size, in the base asset: a multiple of the market's size step. Give size or notional_usd. */ size?: string | number;
  /** The size as a value in USD, rounded down to the size step. */ notional_usd?: string | number;
  /** The limit price (limit and stop_limit): a multiple of the tick size. */ limit_price?: string | number;
  /** The trigger price (stop_market and stop_limit). */ stop_price?: string | number;
  /** Market orders: the worst price you accept. Give worst_price or max_slippage_pct. */ worst_price?: string | number;
  /** Market orders: how far from the best price you accept, in percent (the default and bounds are in GET /v1/markets). */ max_slippage_pct?: string | number;
  /** It may only reduce or close your position, never open or add to one. */ reduce_only?: boolean;
  /** Limit orders: refused if it would fill at once. */ post_only?: boolean;
  /** gtc: until canceled. gtd: until expires_at. ioc and fok: fill now or end (a fill is always the whole order). Default: ioc for market orders, gtc otherwise. */ time_in_force?: "gtc" | "gtd" | "ioc" | "fok" | (string & {});
  /** With gtd: when it ends, from orders.gtd_min_seconds to orders.gtd_max_days ahead (GET /v1/limits). */ expires_at?: number;
  /** Your own id for the order: unique in this environment, 1 to 64 letters, digits and . _ : - */ client_order_id?: string;
  exits?: OrderExits;
}

/** Exits placed when the order fills: the stop-loss for the filled size first, then the take-profits. From then on they are the position's exits. An empty object removes them. */
export interface OrderExits {
  /** Limit orders on the profit side, each at price, or at distance or percent from the fill. */ take_profit?: Array<{
    /** An exact price. */ price?: string | number;
    /** A price difference from the fill price. */ distance?: string | number;
    /** A percent of the fill price. */ percent?: string | number;
    /** Its share of the filled size, in percent (absent: the take-profits split it evenly). */ size_pct?: string | number;
  }>;
  /** A stop on the loss side, at price, or at distance or percent from the fill; it may trail and move to break-even (as in a position's exits). */ stop_loss?: {
    /** An exact price. */ price?: string | number;
    /** A price difference from the fill price. */ distance?: string | number;
    /** A percent of the fill price. */ percent?: string | number;
    /** The stop's type (default stop_market). */ type?: "stop_market" | "stop_limit" | (string & {});
    /** stop_limit: how many ticks past the stop its limit sits. */ limit_offset_ticks?: number;
    /** As a position's exits take it: { distance } or { percent }, with activation_price to start only once the price reaches it. */ trail?: {
    /** How far behind the best price. */ distance?: string | number;
    /** How far behind, in percent of the best price. */ percent?: string | number;
    /** Start once the price reaches this. */ activation_price?: string | number;
  };
    /** As a position's exits take it: one of trigger_distance, trigger_percent, trigger_price, and offset_ticks. */ break_even?: {
    /** How far from the entry. */ trigger_distance?: string | number;
    /** How far from the entry, in percent. */ trigger_percent?: string | number;
    /** Or this price. */ trigger_price?: string | number;
    /** Ticks beyond the entry, on the profit side. */ offset_ticks?: number;
  };
  } | null;
}

/** A new strategy. trailing_stop and break_even act on the position's exits: the answer is that exits strategy. */
export interface CreateStrategy {
  /** trailing_stop: a stop-loss that follows the price. break_even: the stop-losses move to the entry once the price has gone your way far enough. oco: two orders, the first fill cancels the other. twap, vwap: an order in market slices over time (vwap sized by yesterday's volume over the same hours). scaled: a ladder of limits. chase: a limit that follows the bid or ask. */ type: "trailing_stop" | "break_even" | "oco" | "twap" | "vwap" | "scaled" | "chase" | (string & {});
  /** A market id: the asset and USD. */ market: string;
  /** The order's side. */ side?: "buy" | "sell" | (string & {});
  /** trailing_stop: its size (absent: what the stop-losses leave of the position). twap, vwap, scaled, chase: the whole size. */ size?: string | number;
  /** trailing_stop: how far behind the best price (or percent). */ distance?: string | number;
  /** trailing_stop: how far behind, in percent of the best price. */ percent?: string | number;
  /** trailing_stop: start once the price reaches this (until then, initial_stop_price rests, or nothing). */ activation_price?: string | number;
  /** trailing_stop: where the stop rests until the trail moves it (absent: from the price now). */ initial_stop_price?: string | number;
  /** trailing_stop: cancel the position's other stop-losses first. */ replace_stops?: boolean;
  /** break_even: how far from the entry the price must go. */ trigger_distance?: string | number;
  /** break_even: the same, in percent of the entry. */ trigger_percent?: string | number;
  /** break_even: or once the price reaches this. */ trigger_price?: string | number;
  /** break_even: ticks beyond the entry, on the profit side. chase: ticks behind the bid (buy) or ask (sell). */ offset_ticks?: number;
  /** oco: the two orders. */ orders?: Array<{
    /** The order's side. */ side: "buy" | "sell" | (string & {});
    /** Two orders that rest. */ type: "limit" | "stop_market" | "stop_limit" | (string & {});
    /** Its size. */ size: string | number;
    /** Its limit price. */ limit_price?: string | number;
    /** Its stop price. */ stop_price?: string | number;
    /** Refused if it would fill at once. */ post_only?: boolean;
    /** It may only reduce your position. */ reduce_only?: boolean;
  }>;
  exits?: OrderExits;
  /** twap, vwap: how long the schedule runs (bounds in the runner settings, see GET /v1/limits). */ duration_minutes?: number;
  /** twap, vwap: time between slices; at most half the duration. Default: 60 (within those bounds). */ interval_seconds?: number;
  /** twap, vwap: vary each slice's time and size a little (the sizes still add up to size). */ randomize?: boolean;
  /** twap, vwap, scaled, chase: only reduce your position (a twap ends when the position is closed). */ reduce_only?: boolean;
  /** twap, vwap (buy): slices wait while the price is above this. chase (buy): its cap. */ max_price?: string | number;
  /** twap, vwap (sell): slices wait while the price is below this. chase (sell): its floor. */ min_price?: string | number;
  /** twap, vwap: each slice's slippage budget, in percent. */ max_slippage_pct?: string | number;
  /** scaled: the first rung. */ start_price?: string | number;
  /** scaled: the last rung. */ end_price?: string | number;
  /** scaled: how many limits (at most the runner setting). */ rungs?: number;
  /** scaled: equal sizes, growing toward end_price, or shrinking. */ distribution?: "flat" | "ascending" | "descending" | (string & {});
  /** scaled: each rung refused if it would fill at once. */ post_only?: boolean;
  /** scaled: one rung refused cancels the others. */ all_or_none?: boolean;
  /** scaled: rungs through the price may fill at once (refused otherwise, rung_would_fill). */ allow_marketable?: boolean;
  /** chase: how long it follows the price. */ max_minutes?: number;
  /** chase: at max_minutes, cancel it, or send what is left at the market. */ on_expiry?: "cancel" | "market" | (string & {});
  /** chase: it may rest through the last price (it would then fill at once). */ allow_cross?: boolean;
  /** Your own id for the strategy (oco, twap, vwap, scaled, chase): unique in this environment. */ client_strategy_id?: string;
}

/** What to change: at least one field. The order keeps its place in the engine (changed in place, never canceled and placed again). */
export interface ModifyOrder {
  /** The new size. */ size?: string | number;
  /** The new limit price. */ limit_price?: string | number;
  /** The new trigger price. */ stop_price?: string | number;
}

/** What to close. */
export interface ClosePosition {
  /** How much to close; absent: the whole position. */ size?: string | number;
  /** Your own id for the close order (a partial close). */ client_order_id?: string;
}

/** Optional: only this market. */
export interface CancelAll {
  /** A market id: the asset and USD. */ market?: string;
}

/** The market's setting. The mode cannot change while a position or a working order is open on the market. */
export interface MarginSettingInput {
  /** isolated: the position has its own margin. cross: positions share the account's balance. */ margin_mode: "isolated" | "cross" | (string & {});
  /** A whole number from 1 to the market's max_leverage. */ leverage: number;
}

export interface MarginSettingResult {
  /** A market id: the asset and USD. */ market: string;
  /** The margin mode. */ margin_mode: "isolated" | "cross" | (string & {});
  /** The leverage. */ leverage: number;
}

export interface WhatIf {
  /** What the order would do now: fill at once, rest, or be refused (reject says why). */ result: "would_fill" | "would_rest" | "would_reject" | (string & {});
  reject: {
    /** The error code it would get. */ code: string;
    /** What it means. */ message: string | null;
    /** More about it. */ details?: Record<string, unknown> | Array<unknown>;
  } | null;
  /** A market id: the asset and USD. */ market: string;
  /** The order's side. */ side: "buy" | "sell" | (string & {});
  /** The order's type. */ type: "market" | "limit" | "stop_market" | "stop_limit" | (string & {});
  /** Its size (after a notional_usd is rounded to the size step). */ size: string | null;
  execution: {
    /** It would fill at once. */ marketable: boolean;
    /** The price it would fill at (at once) or rest at. */ expected_price: string;
    /** A market order's worst price. */ worst_price: string | null;
  } | null;
  /** Its value in USD. */ notional: string | null;
  /** The fee its fill would pay. */ fee: string | null;
  margin: {
    /** The margin mode the position would use. */ mode: "isolated" | "cross" | (string & {});
    /** The leverage it would use. */ leverage: number;
    /** The margin the order needs. */ required: string;
    /** The margin available now. */ available_before: string | null;
    /** What would remain available. */ available_after: string | null;
  } | null;
  position_after: {
    /** The position's size after the fill. */ size: string;
    /** Its side; null when flat. */ side: "long" | "short" | (string & {}) | null;
    /** Its average price. */ average_price: string | null;
  } | null;
  exposure: {
    /** The market's exposure cap across your accounts of this product. */ cap: string;
    /** Your open value on the market now. */ open_before: string;
    /** What would remain under the cap. */ room_after: string;
  } | null;
}

export interface Event {
  /** The event; pass it as after (GET /v1/events) or resume (the stream) to continue from it. */ id: string;
  /** What happened (GET /v1/webhook-event-types says what each type means). */ type: "order.created" | "order.edited" | "order.filled" | "order.canceled" | "order.rejected" | "order.expired" | "fill.created" | "position.opened" | "position.changed" | "position.closed" | "strategy.created" | "strategy.leg_placed" | "strategy.leg_modified" | "strategy.leg_filled" | "strategy.leg_canceled" | "strategy.leg_rejected" | "strategy.orphan_repaired" | "strategy.slice" | "strategy.ended" | "tradingview.alert_executed" | "tradingview.alert_refused" | "account.updated" | "account.passed" | "account.failed" | "account.payout_eligible" | "account.disabled" | "account.removed" | "key.loss_stop" | "key.expiring" | "system.restarted" | "risk.warning" | "risk.cleared" | (string & {});
  /** The account number. */ account_id: string;
  /** When it happened. */ time: number;
  /** Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert. */ source: "api" | "mcp" | "platform" | "engine" | "strategy" | "tradingview" | (string & {});
  /** order or fill (the objects REST answers), with changes or close_reason when they apply. A position.* event carries position { market, side, size, average_price } as the engine reported it, and previous_size: read GET .../positions/{market} for its margin, P&L and liquidation. */ data: Record<string, unknown>;
}

/** What a json webhook endpoint receives: the event as GET /v1/events shows it, with the endpoint's API version and environment. */
export interface WebhookPayload {
  /** The event; pass it as after (GET /v1/events) or resume (the stream) to continue from it. */ id: string;
  /** What happened; webhook.ping is a test. */ type: "order.created" | "order.edited" | "order.filled" | "order.canceled" | "order.rejected" | "order.expired" | "fill.created" | "position.opened" | "position.changed" | "position.closed" | "strategy.created" | "strategy.leg_placed" | "strategy.leg_modified" | "strategy.leg_filled" | "strategy.leg_canceled" | "strategy.leg_rejected" | "strategy.orphan_repaired" | "strategy.slice" | "strategy.ended" | "tradingview.alert_executed" | "tradingview.alert_refused" | "account.updated" | "account.passed" | "account.failed" | "account.payout_eligible" | "account.disabled" | "account.removed" | "key.loss_stop" | "key.expiring" | "system.restarted" | "risk.warning" | "risk.cleared" | "webhook.ping" | (string & {});
  /** The account number (none on webhook.ping). */ account_id?: string;
  /** When it happened. */ time: number;
  /** Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert. */ source: "api" | "mcp" | "platform" | "engine" | "strategy" | "tradingview" | (string & {});
  /** order or fill (the objects REST answers), with changes or close_reason when they apply. A position.* event carries position { market, side, size, average_price } as the engine reported it, and previous_size: read GET .../positions/{market} for its margin, P&L and liquidation. */ data: Record<string, unknown>;
  /** The API version the endpoint receives. */ api_version: string;
  /** The environment of the endpoint and the account. */ environment: "live" | "sandbox" | (string & {});
  /** A test (webhook.ping) or a delivery resent as one. */ test?: boolean;
}

export interface ExitsLeg {
  /** The leg: an order of this API, also listed under the account's orders. */ id: string;
  /** take_profit: a limit on the profit side. stop_loss: a stop on the loss side. */ group: "take_profit" | "stop_loss" | (string & {});
  /** take_profit legs are limits; stop_loss legs are stop_market or stop_limit. */ type: "limit" | "stop_market" | "stop_limit" | (string & {});
  /** The order's status; waiting: a stop-loss not placed yet (it waits for its activation price or its break-even). */ status: "pending" | "working" | "filled" | "canceled" | "rejected" | "expired" | "unconfirmed" | "waiting" | (string & {});
  /** Its size. */ size: string;
  /** The take-profit price, or the stop price. */ price: string | null;
  /** The limit of a stop_limit leg. */ limit_price: string | null;
  /** A trailing stop-loss: how it follows the price. */ trail: {
    /** How far behind the best price (or null: percent). */ distance: string | null;
    /** How far behind, in percent of the best price (or null: distance). */ percent: string | null;
    /** It starts once the price reaches this; null: from the start. */ activation_price: string | null;
    /** It follows the price now (its activation price was reached, or it had none). */ active: boolean;
    /** The best price since it started, which the stop follows. */ best_price: string | null;
  } | null;
  /** A stop-loss moved to the entry price once the price has gone your way far enough. */ break_even: {
    /** How far from the entry the price must go (or null: another trigger). */ trigger_distance: string | null;
    /** The same, in percent of the entry. */ trigger_percent: string | null;
    /** Or once the price reaches this. */ trigger_price: string | null;
    /** Ticks beyond the entry, on the profit side. */ offset_ticks: number;
    /** The stop-loss was moved to the entry already. */ done: boolean;
  } | null;
}

export interface Exits {
  /** The account number. */ account_id: string;
  /** A market id: the asset and USD. */ market: string;
  /** The strategy that keeps these exits (type exits); null when there are none. */ strategy_id: string | null;
  /** The open position; null when flat. */ position: {
    /** Long or short. */ side: "long" | "short" | (string & {});
    /** Its size. */ size: string;
  } | null;
  /** The take-profit legs: together at most the position. */ take_profit: Array<ExitsLeg>;
  /** The stop-loss legs: together at most the position. */ stop_loss: Array<ExitsLeg>;
  /** The legs grow with the position, keeping their share of it (rounded down to the size step). */ resize_with_position: boolean;
}

/** The take-profit and stop-loss legs of a position, changed together. */
export interface ExitsBatch {
  /** The position you saw: { side, size }, or null. */ expected_position: {
    /** Long or short. */ side: "long" | "short" | (string & {});
    /** Its size. */ size: string | number;
  } | null;
  /** Every leg you saw (an empty list when there were none): if anything differs now, nothing is done and the answer is 409 exits_changed with the state now. */ expected_exits: Array<{
    /** A leg. */ id: string;
    /** Its size. */ size: string | number;
    /** Its price; null for a stop-loss that waits (as GET exits shows it). */ price: string | number | null;
  }>;
  /** Applied in this order: cancels, changes, then new legs (stop-losses first). An engine refusal stops the batch: 422 exit_operation_failed says what each operation did. */ operations: Array<{
    /** What to do. */ op: "place" | "modify" | "cancel" | (string & {});
    /** modify, cancel: the leg. */ id?: string;
    /** place: the group of the new leg. */ group?: "take_profit" | "stop_loss" | (string & {});
    /** place: limit for a take-profit (the default); stop_market (the default) or stop_limit for a stop-loss. */ type?: "limit" | "stop_market" | "stop_limit" | (string & {});
    /** The take-profit price, or the stop price. */ price?: string | number;
    /** The limit of a stop_limit leg. */ limit_price?: string | number;
    /** The size; absent on a place: what the group leaves of the position. */ size?: string | number;
    /** A stop-loss that follows the price: { distance } (a price difference) or { percent }, with activation_price to start only once the price reaches it. Without a price and an activation price it starts from the price now. null removes it. */ trail?: {
    /** How far behind the best price. */ distance?: string | number;
    /** How far behind, in percent of the best price. */ percent?: string | number;
    /** Start once the price reaches this. */ activation_price?: string | number;
  } | null;
    /** Move the stop-loss to the entry price (plus offset_ticks) once the price has gone your way by trigger_distance, trigger_percent or to trigger_price. Without a price, the stop is placed only then. null removes it. */ break_even?: {
    /** How far from the entry. */ trigger_distance?: string | number;
    /** How far from the entry, in percent. */ trigger_percent?: string | number;
    /** Or this price. */ trigger_price?: string | number;
    /** Ticks beyond the entry, on the profit side. */ offset_ticks?: number;
  } | null;
    /** modify: the settings to remove from a stop-loss (the same as sending them null). */ clear?: Array<"trail" | "break_even" | (string & {})>;
  }>;
  /** The legs grow with the position (default true). */ resize_with_position?: boolean;
}

export interface Strategy {
  /** The strategy. */ id: string;
  /** Your own id for it. */ client_strategy_id: string | null;
  /** The account number. */ account_id: string;
  /** exits: a position's take-profit and stop-loss legs. bracket: an order with exits once it fills. oco: two orders, the first fill cancels the other. twap, vwap: an order in slices over time. scaled: a ladder of limits. chase: a limit that follows the price. */ type: "exits" | "bracket" | "oco" | "twap" | "vwap" | "scaled" | "chase" | (string & {});
  /** A market id: the asset and USD. */ market: string;
  /** The side of its orders (exits: the closing side). */ side: "buy" | "sell" | (string & {}) | null;
  /** waiting: armed, nothing placed yet. working: running. completed: done as asked. canceled: you ended it. ended: it stopped for end_reason. */ status: "waiting" | "working" | "completed" | "canceled" | "ended" | (string & {});
  /** Why it stopped. */ end_reason: "take_profit" | "stop_loss" | "filled" | "position_closed" | "position_reversed" | "canceled_by_user" | "external" | "key_revoked" | "account_closed" | "engine_restart" | "child_rejected" | "price_bound" | "expired" | (string & {}) | null;
  /** What it was created with. */ params: Record<string, unknown>;
  /** How far it is (by type). */ progress: Record<string, unknown>;
  /** Every order it placed, oldest first. */ orders: Array<Order>;
  /** When it was created. */ created_at: number;
  /** Its latest change. */ updated_at: number;
  /** When it stopped. */ ended_at: number | null;
}

export interface WebhookEndpoint {
  /** The endpoint. */ id: string;
  /** Its address: the host and the start of the path (never whole: chat addresses carry tokens). */ url: string | null;
  /** json: the signed event. discord, slack, telegram: a chat message. */ format: "json" | "discord" | "slack" | "telegram" | (string & {});
  /** The groups it receives (see GET /v1/webhook-event-types). */ events: Array<"orders" | "positions" | "account" | "risk" | (string & {})>;
  /** disabled: we stopped after failures in a row; turn it on again from your dashboard. */ status: "active" | "paused" | "disabled" | (string & {});
  /** The version its events are written in. */ api_version: string;
  /** Its environment: it receives that environment's events only. */ environment: "live" | "sandbox" | (string & {});
}

export interface WebhookDelivery {
  /** The delivery. */ id: string;
  /** Its endpoint. */ endpoint_id: string;
  /** The event sent (the same id when it is sent again). */ event_id: string;
  /** The event's type. */ event_type: string;
  /** Its account. */ account_id: string | null;
  /** A test. */ test: boolean;
  /** pending: waiting for its next try. failed: every try failed. canceled: the endpoint was disabled or deleted. */ status: "pending" | "delivering" | "delivered" | "failed" | "canceled" | (string & {});
  /** Every try, oldest first. */ attempts: Array<{
    /** Which try. */ attempt: number;
    /** When. */ at: number;
    /** What your endpoint answered; null when it could not be reached. */ http_status: number | null;
    /** Why it failed, when it did (timeout, redirect, private_address, http_500, ...). */ error: string | null;
    /** How long it took. */ duration_ms: number | null;
    /** The start of the answer (up to 512 bytes). */ response: string | null;
  }>;
  /** When it is tried again. */ next_attempt_at: number | null;
  /** When it was queued. */ created_at: number;
  /** When your endpoint took it. */ delivered_at: number | null;
}

export interface WebhookEventType {
  /** The type. */ type: string;
  /** The group that receives it; null: every endpoint (the test). */ group: "orders" | "positions" | "account" | "risk" | (string & {}) | null;
  /** What it means. */ description: string;
}

export interface DeletedEndpoint {
  /** The endpoint. */ id: string;
  /** It was deleted. */ deleted: boolean;
}

export interface TradingViewHook {
  /** The link. */ id: string;
  /** Its name. */ name: string;
  /** The account number. */ account_id: string;
  /** Its environment. */ environment: "live" | "sandbox" | (string & {});
  /** paused: alerts are refused (link_paused). */ status: "active" | "paused" | (string & {});
  /** The markets it may trade; empty: every market. */ markets: Array<string>;
  /** It may only reduce positions. */ reduce_only: boolean;
  /** Its largest order, in USD. */ max_order_usd: string | null;
  /** Its largest position, in USD. */ max_position_usd: string | null;
  /** Its daily loss stop: no new position once today's loss reaches it. */ daily_loss_usd: string | null;
  sizing: {
    /** from_alert: the size the alert gives. qty, usd, pct_balance: the link's own. */ mode: "from_alert" | "qty" | "usd" | "pct_balance" | (string & {});
    /** The link's size, in its mode's unit. */ value: string | null;
  };
  /** signal: an opposite alert reverses, a repeat is skipped. add: a repeat adds. target: the strategy's position size is followed. */ position_mode: "signal" | "add" | "target" | (string & {});
}

export interface TradingViewAlert {
  /** The alert. */ id: string;
  /** Its link. */ hook_id: string;
  /** The account number. */ account_id: string;
  /** What it did. */ status: "processing" | "placed" | "partial" | "dry_run" | "duplicate" | "skipped" | "refused" | "failed" | "rate_limited" | (string & {});
  /** Why it placed nothing (or not all): a reason code. */ reason: string | null;
  /** That reason in words. */ reason_text: string | null;
  /** How the message was read: universal, pickmytrade, traderspost, pineconnector, text. */ format: string | null;
  /** The market it named. */ market: string | null;
  /** Its action. */ action: string | null;
  /** The message as kept (credentials masked). */ message: string | null;
  /** The sender's address, its end masked. */ sender: string | null;
  /** What it was to do: { legs } or { skip } or { refuse }. */ plan: Record<string, unknown> | null;
  /** What each leg did. */ results: Array<Record<string, unknown>>;
  /** More about a refusal. */ details: Record<string, unknown> | null;
  /** The alert this one repeats. */ duplicate_of: string | null;
  /** Refusals folded into this row (rate limits). */ repeat_count: number;
  /** When it came. */ received_at: number;
  /** When it was done. */ finished_at: number | null;
}

/** A message to read, sent nowhere. */
export interface TradingViewParseInput {
  /** The alert's message, as TradingView would send it. */ message: string;
  /** A link: the plan is then worked out on its account and settings, as of now. */ hook_id?: string;
}

export interface TradingViewParse {
  /** It reads, and names a market we offer. */ ok: boolean;
  /** Why not, when it does not. */ code: string | null;
  /** The field at fault. */ field: string | null;
  /** What went wrong. */ message: string | null;
  /** The message as read: action, side, sizes, prices, exits. */ signal: Record<string, unknown> | null;
  /** The market it names (ALL for every market). */ market: string | null;
  /** The market meant, when the ticker names one we offer only scaled. */ suggestion: string | null;
  /** With hook_id: what the alert would do now ({ legs } / { skip } / { refuse }). */ plan: Record<string, unknown> | null;
}

export interface DeletedHook {
  /** The link. */ id: string;
  /** It was revoked: its address answers 404 from now on. */ deleted: boolean;
}

export interface CancelAllResult {
  /** The account number. */ account_id: string;
  /** The market, or null for every market. */ market: string | null;
  /** How many working orders were canceled. */ canceled: number;
}

export interface FlattenResult {
  /** The account number. */ account_id: string;
  /** How many working orders were canceled. */ orders_canceled: number;
  /** The orders that closed the positions. */ closes: Array<Order>;
}

export type AccountRiskOrNull = {
    /** When the oldest figure here was read. */ as_of: number;
    /** Some figures are older than usual (the engine was busy). */ stale: boolean;
    /** The balance. */ balance: string;
    /** The balance plus the open P&L at the market price. */ equity: string;
    /** The open P&L. */ unrealized_pnl: string;
    /** The equity change since midnight New York, open positions included: what the daily loss rule counts. */ pnl_today: string;
    /** The P&L closed today. */ realized_today: string;
    /** The fees paid today. */ fees_today: string;
    /** Trades closed today. */ trades_today: number;
    /** The margin your positions hold. */ margin_used: string;
    /** What is left of the balance for new positions. */ margin_available: string | null;
    exposure: {
    /** The value of every open position. */ gross: string;
    /** The value of the long ones. */ long: string;
    /** The value of the short ones. */ short: string;
    /** Gross exposure over equity. */ effective_leverage: string | null;
    /** Per market, largest first. */ markets: Array<{
    /** A market id: the asset and USD. */ market: string;
    /** The value held on it. */ notional: string;
    /** That value over equity. */ leverage: string | null;
    /** The exposure cap of the market. */ cap: string | null;
    /** How much of the cap is used, counting your other accounts on the same rule, in percent. */ cap_used_pct: string | null;
  }>;
  };
    daily_loss: Limit;
    max_drawdown: Limit;
    profit_target: {
    /** The balance that reaches it. */ goal_balance: string;
    /** Still to make. */ remaining: string;
    /** How far, in percent. */ progress_pct: string | null;
    /** Positions must be closed when it is reached. */ requires_flat: boolean;
  } | null;
    trading_days: {
    /** Days with a trade so far. */ count: number | null;
    /** Days the rule needs. */ required: number | null;
  };
    nearest_liquidation: {
    /** A market id: the asset and USD. */ market: string;
    /** Its liquidation price. */ price: string;
    /** How far the price is from it, in percent. */ distance_pct: string;
  } | null;
    /** When the trading day ends (midnight New York). */ trading_day_ends_at: number;
    /** What needs your attention, most serious first. */ warnings: Array<Warning>;
  } | null;

export interface AccountDetail {
  /** The account number. */ id: string;
  /** The environment the account lives in. */ environment: "live" | "sandbox" | (string & {});
  /** Where the account is in the programme. */ stage: "evaluation" | "funded" | "sandbox" | (string & {});
  /** pending: being opened; active: trading. */ status: "pending" | "active" | "passed" | "failed" | "closed" | "suspended" | (string & {});
  /** The product it was bought as. */ product: string | null;
  /** Its size as sold. */ tier: string | null;
  /** The balance it started with. */ start_balance: string | null;
  /** The balance: closed trades and fees only. */ balance: string | null;
  rule: Rule;
  /** A payout may be requested now. */ payout_eligible: boolean;
  /** Sandbox only: parked after a while unused; it wakes the moment you use it. */ parked?: boolean;
  /** When the account was made. */ created_at: number | null;
  risk: AccountRiskOrNull;
}

export type LimitStanding = {
    /** The limit, in USD. */ limit?: string;
    /** The equity at which it is breached. */ floor?: string;
    /** How much more the account may lose before it. */ room?: string;
    /** How much of it is used, in percent. */ used_pct?: string | null;
    /** Daily loss only: when it resets (midnight New York). */ resets_at?: number;
    /** Daily loss only: what happens when it is reached. */ on_breach?: "fail_account" | "flatten" | "flatten_and_pause" | (string & {});
    /** Daily loss only: the day's start is estimated (the engine does not publish it). */ estimated?: boolean;
    /** Max drawdown only: how the reference moves. */ mode?: "end_of_day" | "end_of_trade" | "static" | "intraday_equity" | (string & {});
    /** Max drawdown only: the engine may hold a higher floor than shown (it follows equity highs). */ at_least?: boolean;
    /** The rule no longer applies this limit (its cut-off was reached). */ lifted: boolean;
    /** Where it stands. */ status: "ok" | "warning" | "critical" | "breached" | (string & {});
  } | null;

export interface GetEquityHistoryQuery {
  /** One point per closed trade, or per trading day. */ resolution?: "trade" | "day" | (string & {});
}

export interface ListPositionsQuery {
  /** Only this market. */ market?: string;
}

export interface ListClosedPositionsQuery {
  /** Only this market. */ market?: string;
  /** Page size. */ limit?: number;
  /** next_cursor from the previous page, with the same filters. */ cursor?: string;
}

export interface ListOrdersQuery {
  /** working: the complete set of working orders. history: past orders. all: both. */ status?: "all" | "working" | "history" | (string & {});
  /** Only this market. */ market?: string;
  /** The order you placed through the API with this id. */ client_order_id?: string;
  /** Page size. */ limit?: number;
  /** next_cursor from the previous page, with the same filters. */ cursor?: string;
}

export interface ListEventsQuery {
  /** Only this account. */ account_id?: string;
  /** The id of the last event you have: the ones after it, oldest first. Absent: from the oldest kept. */ after?: string;
  /** Page size. */ limit?: number;
}

export interface ListStrategiesQuery {
  /** active: waiting or working. ended: completed, canceled or ended. all: both. */ status?: "all" | "active" | "ended" | (string & {});
  /** Only this market. */ market?: string;
  /** The strategy you created with this id. */ client_strategy_id?: string;
  /** Page size. */ limit?: number;
  /** next_cursor from the previous page, with the same filters. */ cursor?: string;
}

export interface ListWebhookDeliveriesQuery {
  /** Page size. */ limit?: number;
  /** next_cursor from the previous page, with the same filters. */ cursor?: string;
}

export interface ListTradingViewAlertsQuery {
  /** Only alerts with this status. */ status?: string;
  /** Page size. */ limit?: number;
  /** next_cursor from the previous page, with the same filters. */ cursor?: string;
}

export interface ListFillsQuery {
  /** Only this market. */ market?: string;
  /** Page size. */ limit?: number;
  /** next_cursor from the previous page, with the same filters. */ cursor?: string;
}

export interface PlatformResource {
  /** Get the API */
  info(opts?: RequestOptions): Promise<Platform>;
  /** Get the OpenAPI document */
  spec(opts?: RequestOptions): Promise<unknown>;
  /** Get the status */
  status(opts?: RequestOptions): Promise<Status>;
  /** Get the limits */
  limits(opts?: RequestOptions): Promise<Limits>;
}

export interface MeResource {
  /** Get your key */
  get(opts?: RequestOptions): Promise<Me>;
}

export interface MarketsResource {
  /** List markets */
  list(opts?: ListOptions): PagePromise<Market>;
  /** List market stats */
  stats(opts?: ListOptions): PagePromise<MarketStats>;
  /** Get a market */
  get(market: string, opts?: RequestOptions): Promise<Market>;
}

export interface AccountsResource {
  /** List accounts */
  list(opts?: ListOptions): PagePromise<Account>;
  /** Open a sandbox account */
  createSandbox(opts?: RequestOptions): Promise<Account>;
  /** Get an account */
  get(accountId: string, opts?: RequestOptions): Promise<AccountDetail>;
  /** Get live risk */
  risk(accountId: string, opts?: RequestOptions): Promise<AccountRisk>;
  /** Get the trading policy */
  tradingPolicy(accountId: string, opts?: RequestOptions): Promise<TradingPolicy>;
  /** Get progress */
  progress(accountId: string, opts?: RequestOptions): Promise<Progress>;
}

export interface HistoryResource {
  /** Get the equity curve */
  equity(accountId: string, query?: GetEquityHistoryQuery, opts?: RequestOptions): Promise<EquityHistory>;
  /** List daily results */
  daily(accountId: string, opts?: ListOptions): PagePromise<DailyResult>;
}

export interface MarginResource {
  /** List margin settings */
  list(accountId: string, opts?: ListOptions): PagePromise<MarginSetting>;
  /** Set leverage and margin mode */
  set(accountId: string, market: string, body: MarginSettingInput, opts?: RequestOptions): Promise<MarginSettingResult>;
}

export interface PositionsResource {
  /** List open positions */
  list(accountId: string, query?: ListPositionsQuery, opts?: ListOptions): PagePromise<Position>;
  /** Get an open position */
  get(accountId: string, market: string, opts?: RequestOptions): Promise<Position>;
  /** List closed positions */
  listClosed(accountId: string, query?: ListClosedPositionsQuery, opts?: ListOptions): PagePromise<ClosedPosition>;
  /** Close a position */
  close(accountId: string, market: string, body: ClosePosition, opts?: RequestOptions): Promise<Order>;
  /** Flatten the account */
  flatten(accountId: string, opts?: RequestOptions): Promise<FlattenResult>;
}

export interface OrdersResource {
  /** List orders */
  list(accountId: string, query?: ListOrdersQuery, opts?: ListOptions): PagePromise<Order>;
  /** Place an order */
  create(accountId: string, body: CreateOrder, opts?: RequestOptions): Promise<Order>;
  /** Get an order */
  get(accountId: string, orderId: string, opts?: RequestOptions): Promise<Order>;
  /** Change an order */
  modify(accountId: string, orderId: string, body: ModifyOrder, opts?: RequestOptions): Promise<Order>;
  /** Cancel an order */
  cancel(accountId: string, orderId: string, opts?: RequestOptions): Promise<Order>;
  /** Preview an order */
  preview(accountId: string, body: CreateOrder, opts?: RequestOptions): Promise<WhatIf>;
  /** Cancel all orders */
  cancelAll(accountId: string, body: CancelAll, opts?: RequestOptions): Promise<CancelAllResult>;
  /** Attach exits to an order */
  setExits(accountId: string, orderId: string, body: OrderExits, opts?: RequestOptions): Promise<Order>;
}

export interface EventsResource {
  /** List events */
  list(query?: ListEventsQuery, opts?: ListOptions): PagePromise<Event>;
  /** Get an event */
  get(eventId: string, opts?: RequestOptions): Promise<Event>;
}

export interface ExitsResource {
  /** Get a position's exits */
  get(accountId: string, market: string, opts?: RequestOptions): Promise<Exits>;
  /** Change a position's exits */
  set(accountId: string, market: string, body: ExitsBatch, opts?: RequestOptions): Promise<Exits>;
}

export interface StrategiesResource {
  /** Create a strategy */
  create(accountId: string, body: CreateStrategy, opts?: RequestOptions): Promise<Strategy>;
  /** List strategies */
  list(accountId: string, query?: ListStrategiesQuery, opts?: ListOptions): PagePromise<Strategy>;
  /** Get a strategy */
  get(accountId: string, strategyId: string, opts?: RequestOptions): Promise<Strategy>;
  /** Cancel a strategy */
  cancel(accountId: string, strategyId: string, opts?: RequestOptions): Promise<Strategy>;
}

export interface WebhookEndpointsResource {
  /** List webhook endpoints */
  list(opts?: ListOptions): PagePromise<WebhookEndpoint>;
  /** Get a webhook endpoint */
  get(endpointId: string, opts?: RequestOptions): Promise<WebhookEndpoint>;
  /** Delete an endpoint */
  delete(endpointId: string, opts?: RequestOptions): Promise<DeletedEndpoint>;
  /** Send a test */
  test(endpointId: string, opts?: RequestOptions): Promise<WebhookDelivery>;
  /** List deliveries */
  listDeliveries(endpointId: string, query?: ListWebhookDeliveriesQuery, opts?: ListOptions): PagePromise<WebhookDelivery>;
  /** Get a delivery */
  getDelivery(endpointId: string, deliveryId: string, opts?: RequestOptions): Promise<WebhookDelivery>;
  /** Send a delivery again */
  resendDelivery(endpointId: string, deliveryId: string, opts?: RequestOptions): Promise<WebhookDelivery>;
  /** Pause an endpoint */
  disable(endpointId: string, opts?: RequestOptions): Promise<WebhookEndpoint>;
  /** Resume an endpoint */
  enable(endpointId: string, opts?: RequestOptions): Promise<WebhookEndpoint>;
  /** List event types */
  eventTypes(opts?: ListOptions): PagePromise<WebhookEventType>;
}

export interface TradingviewResource {
  /** List TradingView links */
  listHooks(opts?: ListOptions): PagePromise<TradingViewHook>;
  /** Get a TradingView link */
  getHook(hookId: string, opts?: RequestOptions): Promise<TradingViewHook>;
  /** Revoke a TradingView link */
  deleteHook(hookId: string, opts?: RequestOptions): Promise<DeletedHook>;
  /** List a link's alerts */
  listAlerts(hookId: string, query?: ListTradingViewAlertsQuery, opts?: ListOptions): PagePromise<TradingViewAlert>;
  /** Get an alert */
  getAlert(hookId: string, alertId: string, opts?: RequestOptions): Promise<TradingViewAlert>;
  /** Pause a TradingView link */
  disableHook(hookId: string, opts?: RequestOptions): Promise<TradingViewHook>;
  /** Resume a TradingView link */
  enableHook(hookId: string, opts?: RequestOptions): Promise<TradingViewHook>;
  /** Read an alert message */
  parse(body: TradingViewParseInput, opts?: RequestOptions): Promise<TradingViewParse>;
}

export interface FillsResource {
  /** List fills */
  list(accountId: string, query?: ListFillsQuery, opts?: ListOptions): PagePromise<Fill>;
}

export interface Resources {
  platform: PlatformResource;
  me: MeResource;
  markets: MarketsResource;
  accounts: AccountsResource;
  history: HistoryResource;
  margin: MarginResource;
  positions: PositionsResource;
  orders: OrdersResource;
  events: EventsResource;
  exits: ExitsResource;
  strategies: StrategiesResource;
  webhookEndpoints: WebhookEndpointsResource;
  tradingview: TradingviewResource;
  fills: FillsResource;
}
