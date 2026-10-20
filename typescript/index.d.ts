import { EventEmitter } from 'node:events';
import type { ClientOptions, RateLimit, Page, PagePromise, RequestOptions, ListOptions, ResponseMeta, RawResponse } from './src/core';
import type { Resources, Market, Event, WebhookPayload } from './src/gen/types';

export * from './src/gen/types';
export type { ClientOptions, RateLimit, Page, PagePromise, RequestOptions, ListOptions, ResponseMeta, RawResponse };

export declare class PhoenixPerps implements Resources {
  constructor(opts?: ClientOptions);
  readonly environment: 'live' | 'sandbox';
  readonly baseUrl: string;
  /** The last rate-limit headers seen. */
  rateLimit: RateLimit | null;
  /** The key, masked (ppk_test_****abcd); null for a client without a key (the public calls only). */
  readonly maskedKey: string | null;
  /**
   * Any route through the same transport (the key, User-Agent, retries, errors): the answer as it came. A route of the
   * spec keeps its retry-safety and Idempotency-Key; any other is tried again only as a GET or with an idempotencyKey.
   * `client.request('GET', '/v1/events', { after: 'evt_...' })`
   */
  request<T = unknown>(method: string, path: string, query?: Record<string, string | number | boolean | null | undefined> | null, body?: unknown, opts?: RequestOptions): Promise<RawResponse<T>>;
  accountStream(opts?: { accounts?: string[]; risk?: boolean; resume?: string; WebSocket?: unknown }): AccountStream;
  marketStream(opts?: { WebSocket?: unknown }): MarketStream;

  platform: Resources['platform'];
  me: Resources['me'];
  markets: Resources['markets'];
  accounts: Resources['accounts'];
  history: Resources['history'];
  margin: Resources['margin'];
  positions: Resources['positions'];
  orders: Resources['orders'];
  events: Resources['events'];
  exits: Resources['exits'];
  strategies: Resources['strategies'];
  webhookEndpoints: Resources['webhookEndpoints'];
  tradingview: Resources['tradingview'];
  fills: Resources['fills'];
}
export default PhoenixPerps;

/** An account-stream event: the API's Event, with its frame's seq (its place in this connection) and replayed (a resume sent it again). */
export type StreamEvent = Event & { seq: number | null; replayed: boolean };

/** Events the account stream could not tell ('gap'): read them with GET /v1/events?after=<after> (the last event told). */
export interface StreamGap {
  /** resume_gap: the resume reached past what the server keeps; too_far_behind: 4008, reconnected without resume. */
  code: 'resume_gap' | 'too_far_behind';
  after: string | null;
}

/**
 * The account stream: snapshots, then events, resumed after a reconnect; also an async iterable over its events.
 * Events: hello, snapshot, ready, event, <event type>, risk (with risk: true), gap (StreamGap), notice,
 * reconnect ({ in_ms, resume, cause, error? }: error is the refusal it waits out), error (only what ends it), close.
 */
export declare class AccountStream extends EventEmitter implements AsyncIterable<StreamEvent> {
  /** The accounts followed (null: every account of the key), less those unsubscribed. */
  accounts: string[] | null;
  risk: boolean;
  lastEventId: string | null;
  start(): this;
  close(): void;
  /** The server's pong (asked again on the next connection when this one ends first). */
  ping(): Promise<{ type: 'pong'; time: number }>;
  /** Stops following these accounts: they stay out after a reconnect. */
  unsubscribe(accounts: string[]): Promise<{ type: 'unsubscribed'; accounts: string[] }>;
  [Symbol.asyncIterator](): AsyncIterator<StreamEvent>;
}

/** The market stream's limits per connection, from its hello. */
export interface MarketLimits {
  subscriptions: number;
  symbols_per_subscribe: number;
  messages_per_s: number;
  [k: string]: unknown;
}

/** What a subscribe or an unsubscribe answers: the markets taken (or ended), and the others with their code. */
export interface MarketSubscription {
  id: number;
  channel: string;
  symbols: string[];
  rejected: Array<{ symbol: string; code: string }>;
  interval?: string;
}

/**
 * The market-data stream: subscriptions made again after a reconnect (their history too: a trade's id and a candle's
 * time tell apart what comes again). Events: <channel> ({ symbol, data }), snapshot_end, hello, reconnect, error, close.
 */
export declare class MarketStream extends EventEmitter {
  /** The hello's limits, null before the first hello. */
  limits: MarketLimits | null;
  start(): this;
  close(): void;
  subscribe(channel: 'ticker' | 'book' | 'trades' | 'candles' | 'stats' | 'status', symbols?: string[], params?: Record<string, unknown>): Promise<MarketSubscription>;
  /** Ends these markets on a channel (none named: every one); on candles params.interval, else every interval. */
  unsubscribe(channel: string, symbols?: string[], params?: { interval?: string; [k: string]: unknown }): Promise<MarketSubscription>;
  /** The subscriptions the server holds for this connection. */
  list(): Promise<{ id: number; type: 'list'; subscriptions: Array<{ channel: string; symbol: string; interval?: string }> }>;
  ping(): Promise<{ id: number; type: 'pong'; time: number }>;
}

export declare class MarketRules {
  constructor(market: Market);
  roundPrice(price: string | number, mode?: 'nearest' | 'down' | 'up'): string;
  roundSize(size: string | number): string;
  /** Rounded down to the step, 0 included: check it against min_size before you order. */
  sizeForNotional(usd: string | number, price: string | number): string;
  /** Rounded down to the step, 0 included: check it against min_size before you order. */
  sizeForRisk(riskUsd: string | number, stopDistance: string | number): string;
  /** side: buy or sell, in any case; slippagePct within the market's max_slippage_pct (a ValidationError otherwise). */
  worstPrice(side: 'buy' | 'sell' | (string & {}), price: string | number, slippagePct?: string | number): string;
}

export declare const Webhooks: {
  /** The WebhookPayload of a delivery, once its signature and time check out (raw: the body exactly as received). */
  verify(raw: string | Buffer, headers: Record<string, string | string[] | undefined> | Headers, secrets: string | string[], toleranceSeconds?: number): WebhookPayload;
};
/** A delivery refused; a PhoenixPerpsError. */
export declare class WebhookVerificationError extends PhoenixPerpsError {
  constructor(code: 'missing_headers' | 'stale' | 'bad_signature' | 'bad_body', message: string);
  code: 'missing_headers' | 'stale' | 'bad_signature' | 'bad_body';
}

export declare class PhoenixPerpsError extends Error {
  status: number | null;
  code: string;
  requestId: string | null;
  details: unknown;
  /** ms to wait before trying again, when the API said so. */
  retryAfter: number | null;
  /** Whether the same request may succeed later: the API's flag (without its envelope, by the status); true for a timeout or a network failure. */
  retryable: boolean;
  /** Where the code is explained. */
  docUrl: string | null;
  idempotencyKey: string | null;
  orderId: string | null;
  /** The account an account-stream error frame names (account_not_found). */
  accountId: string | null;
  type: string | null;
  /** The answer's headers (Idempotent-Replayed, ...), null when no answer came. Not enumerable. */
  readonly headers: Headers | null;
}
export declare class AuthenticationError extends PhoenixPerpsError {}
export declare class PermissionError extends PhoenixPerpsError {}
export declare class NotFoundError extends PhoenixPerpsError {}
export declare class ConflictError extends PhoenixPerpsError {}
export declare class ValidationError extends PhoenixPerpsError {}
export declare class RuleRejectedError extends PhoenixPerpsError {}
export declare class RateLimitError extends PhoenixPerpsError {}
export declare class ServerError extends PhoenixPerpsError {}
export declare class ConnectionError extends PhoenixPerpsError {}
export declare class TimeoutError extends PhoenixPerpsError {}
export declare class StreamClosedError extends PhoenixPerpsError {}

export declare const SDK_VERSION: string;
export declare const SPEC_VERSION: string;
export declare const ERROR_CODES: Record<string, { type: string; status: number; retryable: boolean }>;
