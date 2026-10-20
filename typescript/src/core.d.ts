/** Options of one call. */
export interface RequestOptions {
  /** The Idempotency-Key of a call that takes one (made for you otherwise, the same on every retry). */
  idempotencyKey?: string;
  /** Per attempt, in ms (default: the client's). */
  timeout?: number;
  maxRetries?: number;
  signal?: AbortSignal;
  /** With If-None-Match, a 304 (nothing changed) answers null, a list call too (the return types do not say so). */
  headers?: Record<string, string>;
  /** Told every answer's meta (status, headers, request id, Idempotency-Key), a 304's included. */
  onMeta?: (meta: ResponseMeta) => void;
}

export interface ListOptions extends RequestOptions {
  /** When iterating (for await), stop after this many items. */
  maxItems?: number;
}

export interface ResponseMeta {
  status: number;
  headers: Headers;
  requestId: string | null;
  idempotencyKey?: string | null;
}

/** The raw request's answer, as it came: the body is the JSON (null for none, a 304). */
export interface RawResponse<T = unknown> {
  status: number;
  headers: Headers;
  body: T | null;
  requestId: string | null;
  idempotencyKey: string | null;
}

export declare class Page<T> {
  data: T[];
  hasMore: boolean;
  nextCursor: string | null;
  asOf: number | null;
  /** The orders list: false when the history reaches further back than this list covers. Any other field of the list is
   * kept too, named the same way (snake_case to camelCase). */
  historyComplete?: boolean;
  readonly meta: ResponseMeta;
  /** The next page, or null on the last (asked with the first page's options). */
  next(): Promise<Page<T> | null>;
}

/** Awaited: the first page. Iterated with for await: every item, page after page. */
export interface PagePromise<T> extends PromiseLike<Page<T>>, AsyncIterable<T> {
  catch<R = never>(onrejected?: (reason: unknown) => R | PromiseLike<R>): Promise<Page<T> | R>;
  finally(onfinally?: () => void): Promise<Page<T>>;
}

export interface RateLimit {
  limit: number;
  remaining: number;
  /** When the window resets (ms since the epoch), or null. */
  resetAt: number | null;
}

export interface ClientOptions {
  /** Default: PHOENIXPERPS_API_KEY. Without a key, the public calls (markets, status) work; any other answers missing_api_key. */
  apiKey?: string;
  /** live or sandbox, in any case (default: the key's own, else PHOENIXPERPS_ENVIRONMENT). */
  environment?: 'live' | 'sandbox' | (string & {});
  /** The REST host (a trailing /v1 is dropped); default: PHOENIXPERPS_BASE_URL, else the environment's. The streams take their own. */
  baseUrl?: string;
  /** Per attempt, in ms (default 30000). */
  timeout?: number;
  /** Retries of a call that may be sent twice (default 2). */
  maxRetries?: number;
  /** Added to the User-Agent, e.g. "my-bot/1.2". */
  appInfo?: string;
  logger?: { debug?(msg: string, data?: unknown): void; warn?(msg: string, data?: unknown): void };
  fetch?: typeof fetch;
  accountStreamUrl?: string;
  marketStreamUrl?: string;
}
