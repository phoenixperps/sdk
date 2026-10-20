// ESM entry: the same client as the CommonJS one (one implementation).
import cjs from './index.js';

export const {
  PhoenixPerps, Page, PagePromise, AccountStream, MarketStream, MarketRules, Webhooks, WebhookVerificationError,
  PhoenixPerpsError, AuthenticationError, PermissionError, NotFoundError, ConflictError, ValidationError, RuleRejectedError,
  RateLimitError, ServerError, ConnectionError, TimeoutError, StreamClosedError, SDK_VERSION, SPEC_VERSION, ERROR_CODES,
} = cjs;
export default cjs.PhoenixPerps;
