'use strict';
const { Core, Page, PagePromise, SDK_VERSION } = require('./core');
const { resources } = require('./gen/resources');
const { SPEC_VERSION, ERROR_CODES } = require('./gen/meta');
const errors = require('./errors');
const { AccountStream, MarketStream } = require('./streams');
const { MarketRules, Webhooks, WebhookVerificationError } = require('./helpers');

/*
 * The client: new PhoenixPerps({ apiKey, environment, baseUrl, timeout, maxRetries, appInfo, logger, fetch }) or new
 * PhoenixPerps() with PHOENIXPERPS_API_KEY in the environment (without a key: the public calls). Every resource of the
 * API (client.orders.create, ...), any route raw (client.request), and the streams: client.accountStream({ accounts,
 * risk }), client.marketStream().
 */
class PhoenixPerps extends Core {
  constructor(opts = {}) {
    super(opts);
    Object.assign(this, resources(this));
  }
  accountStream(opts = {}) {
    return new AccountStream(this, opts);
  }
  marketStream(opts = {}) {
    return new MarketStream(this, opts);
  }
}

module.exports = {
  PhoenixPerps,
  default: PhoenixPerps,
  Page, PagePromise, AccountStream, MarketStream, MarketRules, Webhooks, WebhookVerificationError,
  ...errors,
  SDK_VERSION, SPEC_VERSION, ERROR_CODES,
};
