# Generated from the API spec by generator/printers/python.js. Do not edit: run `node generator`.
from __future__ import annotations

import sys
from decimal import Decimal
from typing import Any, Dict, List, Optional, TypedDict, Union

if sys.version_info >= (3, 11):
    from typing import Required
else:  # 3.10
    from typing_extensions import Required

__all__ = ["Error", "Platform", "Status", "Limits", "Market", "MarketStats", "Rule", "Account", "Limit", "Warning", "AccountRisk", "Progress", "TradingPolicy", "EquityHistory", "DailyResult", "MarginSetting", "ExitLeg", "Position", "ClosedPosition", "Order", "Fill", "Me", "CreateOrder", "OrderExits", "CreateStrategy", "ModifyOrder", "ClosePosition", "CancelAll", "MarginSettingInput", "MarginSettingResult", "WhatIf", "Event", "WebhookPayload", "ExitsLeg", "Exits", "ExitsBatch", "Strategy", "WebhookEndpoint", "WebhookDelivery", "WebhookEventType", "DeletedEndpoint", "TradingViewHook", "TradingViewAlert", "TradingViewParseInput", "TradingViewParse", "DeletedHook", "PriceAlert", "CreatePriceAlert", "ChangePriceAlert", "DeletedAlert", "RestrictedCountry", "CancelAllResult", "FlattenResult", "AccountRiskOrNull", "AccountDetail", "LimitStanding"]


class Error(TypedDict, total=False):
    """Every error has this shape."""
    error: Required[Dict[str, Any]]


class Platform(TypedDict, total=False):
    environment: Required[str]
    version: Required[str]
    server_time: Required[int]
    docs_url: Required[str]
    spec_url: Required[str]
    status: Required[Dict[str, Any]]


class Status(TypedDict, total=False):
    environment: Required[str]
    api: Required[str]
    engine: Required[Dict[str, Any]]
    account_data: Required[Dict[str, Any]]
    market_data: Required[Dict[str, Any]]
    features: Required[Dict[str, Any]]
    server_time: Required[int]


class Limits(TypedDict, total=False):
    environment: Required[str]
    rate: Required[Dict[str, Any]]
    keys: Required[Dict[str, Any]]
    orders: Required[Dict[str, Any]]
    sandbox: Dict[str, Any]
    strategies: Required[Dict[str, Any]]
    alerts: Required[Dict[str, Any]]
    body_max_bytes: Required[int]
    page_limit: Required[Dict[str, Any]]


class Market(TypedDict, total=False):
    market: Required[str]
    name: Required[str]
    status: Required[str]
    venue: Required[Optional[str]]
    trading_hours: Required[str]
    option_of: Required[Optional[str]]
    options: Required[Optional[List[Dict[str, Any]]]]
    tick_size: Required[str]
    size_step: Required[str]
    min_size: Required[str]
    min_notional: Required[Optional[str]]
    max_leverage: Required[int]
    fee_rate_pct: Required[str]
    exposure_cap: Required[Optional[str]]
    max_slippage_pct: Required[Optional[Dict[str, Any]]]


class MarketStats(TypedDict, total=False):
    """A market's 24-hour figures and open interest. A figure not known yet is null."""
    market: Required[str]
    last: Required[Optional[str]]
    change_24h: Required[Optional[str]]
    change_24h_pct: Required[Optional[str]]
    high_24h: Required[Optional[str]]
    low_24h: Required[Optional[str]]
    volume_24h: Required[Optional[str]]
    notional_volume_24h: Required[Optional[str]]
    open_interest: Required[Optional[str]]
    notional_open_interest: Required[Optional[str]]


class Rule(TypedDict, total=False):
    """The account's rule, as the trading engine enforces it."""
    start_balance: Required[str]
    daily_loss: Required[Optional[Dict[str, Any]]]
    max_drawdown: Required[Optional[Dict[str, Any]]]
    weekly_loss: Required[Optional[Dict[str, Any]]]
    profit_target: Required[Optional[Dict[str, Any]]]
    min_trading_days: Required[Optional[int]]
    min_calendar_days: Required[Optional[int]]
    consistency: Required[Optional[Dict[str, Any]]]
    overnight_holding: Required[bool]
    weekend_holding: Required[bool]
    inactivity_days: Required[Optional[int]]


class Account(TypedDict, total=False):
    id: Required[str]
    environment: Required[str]
    stage: Required[str]
    status: Required[str]
    product: Required[Optional[str]]
    tier: Required[Optional[str]]
    start_balance: Required[Optional[str]]
    balance: Required[Optional[str]]
    rule: Required["Rule"]
    payout_eligible: Required[bool]
    parked: bool
    created_at: Required[Optional[int]]


class Limit(TypedDict, total=False):
    """A loss limit and the room left to it."""
    limit: str
    floor: str
    room: str
    used_pct: Optional[str]
    resets_at: int
    on_breach: str
    estimated: bool
    mode: str
    at_least: bool
    lifted: Required[bool]


class Warning(TypedDict, total=False):
    code: Required[str]
    level: Required[str]
    market: str
    value: str


class AccountRisk(TypedDict, total=False):
    as_of: Required[int]
    stale: Required[bool]
    paused: Required[bool]
    balance: Required[str]
    equity: Required[str]
    unrealized_pnl: Required[str]
    pnl_today: Required[str]
    realized_today: Required[str]
    fees_today: Required[str]
    trades_today: Required[int]
    margin_used: Required[str]
    margin_available: Required[Optional[str]]
    exposure: Required[Dict[str, Any]]
    daily_loss: Required["Limit"]
    max_drawdown: Required["Limit"]
    profit_target: Required[Optional[Dict[str, Any]]]
    trading_days: Required[Dict[str, Any]]
    nearest_liquidation: Required[Optional[Dict[str, Any]]]
    trading_day_ends_at: Required[int]
    warnings: Required[List["Warning"]]


class Progress(TypedDict, total=False):
    stage: Required[str]
    objectives: Required[Dict[str, Any]]
    limits: Required[Dict[str, Any]]
    payout: Required[Optional[Dict[str, Any]]]
    as_of: Required[int]


class TradingPolicy(TypedDict, total=False):
    rule: Required["Rule"]
    trading_day: Required[Dict[str, Any]]
    slippage_pct: Required[Dict[str, Any]]
    margin: Required[Dict[str, Any]]
    orders: Required[Dict[str, Any]]
    controls: Required[Dict[str, Any]]
    budgets: Required[Dict[str, Any]]
    as_of: Required[int]


class EquityHistory(TypedDict, total=False):
    resolution: Required[str]
    points: Required[List[Dict[str, Any]]]
    live: Required[Dict[str, Any]]


class DailyResult(TypedDict, total=False):
    date: Required[str]
    trades: Required[int]
    gross_pnl: Required[str]
    fees: Required[str]
    pnl: Required[str]
    withdrawn: Required[str]
    balance_end: Required[Optional[str]]


class MarginSetting(TypedDict, total=False):
    market: Required[str]
    leverage: Required[int]
    margin_mode: Required[str]
    max_leverage: Required[Optional[int]]
    source: Required[str]
    locked: Required[bool]


class ExitLeg(TypedDict, total=False):
    order_id: Required[str]
    type: Required[str]
    price: Required[Optional[str]]
    size: Required[Optional[str]]


class Position(TypedDict, total=False):
    id: Required[str]
    market: Required[str]
    side: Required[str]
    size: Required[str]
    entry_price: Required[Optional[str]]
    mark_price: Required[Optional[str]]
    notional: Required[Optional[str]]
    unrealized_pnl: Required[Optional[str]]
    leverage: Required[int]
    margin_mode: Required[str]
    initial_margin: Required[Optional[str]]
    added_margin: Required[Optional[str]]
    margin: Required[Optional[str]]
    liquidation: Required[Optional[Dict[str, Any]]]
    breach_prices: Required[Dict[str, Any]]
    exits: Required[Dict[str, Any]]
    protected: Required[bool]
    opened_at: Required[Optional[int]]


class ClosedPosition(TypedDict, total=False):
    market: Required[str]
    side: Required[str]
    size: Required[str]
    entry_price: Required[Optional[str]]
    exit_price: Required[Optional[str]]
    gross_pnl: Required[str]
    fees: Required[str]
    pnl: Required[str]
    opened_at: Required[int]
    closed_at: Required[int]
    lots: Required[List[Dict[str, Any]]]


class Order(TypedDict, total=False):
    id: Required[str]
    client_order_id: Required[Optional[str]]
    account_id: Required[str]
    market: Required[str]
    side: Required[Optional[str]]
    type: Required[Optional[str]]
    status: Required[str]
    size: Required[Optional[str]]
    filled_size: Required[Optional[str]]
    limit_price: Required[Optional[str]]
    stop_price: Required[Optional[str]]
    average_fill_price: Required[Optional[str]]
    fee: Required[Optional[str]]
    reduce_only: Required[Optional[bool]]
    time_in_force: Required[str]
    expires_at: Required[Optional[int]]
    post_only: Required[Optional[bool]]
    source: Required[str]
    strategy_id: Required[Optional[str]]
    exits: Required[Optional[Dict[str, Any]]]
    cancel_reason: Required[Optional[str]]
    reject: Required[Optional[Dict[str, Any]]]
    created_at: Required[Optional[int]]
    filled_at: Required[Optional[int]]
    canceled_at: Required[Optional[int]]
    updated_at: Optional[int]


class Fill(TypedDict, total=False):
    id: Required[str]
    order_id: Required[str]
    market: Required[str]
    side: Required[Optional[str]]
    size: Required[str]
    price: Required[str]
    fee: Required[Optional[str]]
    liquidation: Required[bool]
    time: Required[int]


class Me(TypedDict, total=False):
    credential: Required[Dict[str, Any]]
    accounts: Required[List[str]]
    account_scope: Required[str]
    controls: Required[Dict[str, Any]]
    budgets: Required[Dict[str, Any]]
    assistant: Required[Optional[Dict[str, Any]]]


class CreateOrder(TypedDict, total=False):
    """A new order."""
    market: Required[str]
    side: Required[str]
    type: Required[str]
    size: Union[str, int, float, Decimal]
    notional_usd: Union[str, int, float, Decimal]
    limit_price: Union[str, int, float, Decimal]
    stop_price: Union[str, int, float, Decimal]
    worst_price: Union[str, int, float, Decimal]
    max_slippage_pct: Union[str, int, float, Decimal]
    reduce_only: bool
    post_only: bool
    time_in_force: str
    expires_at: int
    client_order_id: str
    exits: "OrderExits"


class OrderExits(TypedDict, total=False):
    """Exits placed when the order fills: the stop-loss for the filled size first, then the take-profits. From then on they are the position's exits. An empty object removes them."""
    take_profit: List[Dict[str, Any]]
    stop_loss: Optional[Dict[str, Any]]


class CreateStrategy(TypedDict, total=False):
    """A new strategy. trailing_stop and break_even act on the position's exits: the answer is that exits strategy."""
    type: Required[str]
    market: Required[str]
    side: str
    size: Union[str, int, float, Decimal]
    distance: Union[str, int, float, Decimal]
    percent: Union[str, int, float, Decimal]
    activation_price: Union[str, int, float, Decimal]
    initial_stop_price: Union[str, int, float, Decimal]
    replace_stops: bool
    trigger_distance: Union[str, int, float, Decimal]
    trigger_percent: Union[str, int, float, Decimal]
    trigger_price: Union[str, int, float, Decimal]
    trigger_market: str
    trigger_direction: str
    order_type: str
    limit_price: Union[str, int, float, Decimal]
    expires_at: int
    offset_ticks: int
    orders: List[Dict[str, Any]]
    exits: "OrderExits"
    duration_minutes: int
    interval_seconds: int
    randomize: bool
    reduce_only: bool
    max_price: Union[str, int, float, Decimal]
    min_price: Union[str, int, float, Decimal]
    max_slippage_pct: Union[str, int, float, Decimal]
    start_price: Union[str, int, float, Decimal]
    end_price: Union[str, int, float, Decimal]
    rungs: int
    distribution: str
    post_only: bool
    all_or_none: bool
    allow_marketable: bool
    max_minutes: int
    on_expiry: str
    allow_cross: bool
    client_strategy_id: str


class ModifyOrder(TypedDict, total=False):
    """What to change: at least one field. The order keeps its place in the engine (changed in place, never canceled and placed again)."""
    size: Union[str, int, float, Decimal]
    limit_price: Union[str, int, float, Decimal]
    stop_price: Union[str, int, float, Decimal]


class ClosePosition(TypedDict, total=False):
    """What to close."""
    size: Union[str, int, float, Decimal]
    client_order_id: str


class CancelAll(TypedDict, total=False):
    """Optional: only this market."""
    market: str


class MarginSettingInput(TypedDict, total=False):
    """The market's setting. The mode cannot change while a position or a working order is open on the market."""
    margin_mode: Required[str]
    leverage: Required[int]


class MarginSettingResult(TypedDict, total=False):
    market: Required[str]
    margin_mode: Required[str]
    leverage: Required[int]


class WhatIf(TypedDict, total=False):
    result: Required[str]
    reject: Required[Optional[Dict[str, Any]]]
    market: Required[str]
    side: Required[str]
    type: Required[str]
    size: Required[Optional[str]]
    execution: Required[Optional[Dict[str, Any]]]
    notional: Required[Optional[str]]
    fee: Required[Optional[str]]
    margin: Required[Optional[Dict[str, Any]]]
    position_after: Required[Optional[Dict[str, Any]]]
    exposure: Required[Optional[Dict[str, Any]]]


class Event(TypedDict, total=False):
    id: Required[str]
    type: Required[str]
    account_id: Required[str]
    time: Required[int]
    source: Required[str]
    data: Required[Dict[str, Any]]


class WebhookPayload(TypedDict, total=False):
    """What a json webhook endpoint receives: the event as GET /v1/events shows it, with the endpoint's API version and environment."""
    id: Required[str]
    type: Required[str]
    account_id: str
    time: Required[int]
    source: Required[str]
    data: Required[Dict[str, Any]]
    api_version: Required[str]
    environment: Required[str]
    test: bool


class ExitsLeg(TypedDict, total=False):
    id: Required[str]
    group: Required[str]
    type: Required[str]
    status: Required[str]
    size: Required[str]
    price: Required[Optional[str]]
    limit_price: Required[Optional[str]]
    trail: Required[Optional[Dict[str, Any]]]
    break_even: Required[Optional[Dict[str, Any]]]


class Exits(TypedDict, total=False):
    account_id: Required[str]
    market: Required[str]
    strategy_id: Required[Optional[str]]
    position: Required[Optional[Dict[str, Any]]]
    take_profit: Required[List["ExitsLeg"]]
    stop_loss: Required[List["ExitsLeg"]]
    resize_with_position: Required[bool]


class ExitsBatch(TypedDict, total=False):
    """The take-profit and stop-loss legs of a position, changed together."""
    expected_position: Required[Optional[Dict[str, Any]]]
    expected_exits: Required[List[Dict[str, Any]]]
    operations: Required[List[Dict[str, Any]]]
    resize_with_position: bool


class Strategy(TypedDict, total=False):
    id: Required[str]
    client_strategy_id: Required[Optional[str]]
    account_id: Required[str]
    type: Required[str]
    market: Required[str]
    side: Required[Optional[str]]
    status: Required[str]
    end_reason: Required[Optional[str]]
    params: Required[Dict[str, Any]]
    progress: Required[Dict[str, Any]]
    orders: Required[List["Order"]]
    created_at: Required[int]
    updated_at: Required[int]
    ended_at: Required[Optional[int]]


class WebhookEndpoint(TypedDict, total=False):
    id: Required[str]
    url: Required[Optional[str]]
    format: Required[str]
    events: Required[List[str]]
    status: Required[str]
    api_version: Required[str]
    environment: Required[str]


class WebhookDelivery(TypedDict, total=False):
    id: Required[str]
    endpoint_id: Required[str]
    event_id: Required[str]
    event_type: Required[str]
    account_id: Required[Optional[str]]
    test: Required[bool]
    status: Required[str]
    attempts: Required[List[Dict[str, Any]]]
    next_attempt_at: Required[Optional[int]]
    created_at: Required[int]
    delivered_at: Required[Optional[int]]


class WebhookEventType(TypedDict, total=False):
    type: Required[str]
    group: Required[Optional[str]]
    description: Required[str]


class DeletedEndpoint(TypedDict, total=False):
    id: Required[str]
    deleted: Required[bool]


class TradingViewHook(TypedDict, total=False):
    id: Required[str]
    name: Required[str]
    account_id: Required[str]
    environment: Required[str]
    status: Required[str]
    markets: Required[List[str]]
    reduce_only: Required[bool]
    max_order_usd: Required[Optional[str]]
    max_position_usd: Required[Optional[str]]
    daily_loss_usd: Required[Optional[str]]
    sizing: Required[Dict[str, Any]]
    position_mode: Required[str]


class TradingViewAlert(TypedDict, total=False):
    id: Required[str]
    hook_id: Required[str]
    account_id: Required[str]
    status: Required[str]
    reason: Required[Optional[str]]
    reason_text: Required[Optional[str]]
    format: Required[Optional[str]]
    market: Required[Optional[str]]
    action: Required[Optional[str]]
    message: Required[Optional[str]]
    sender: Required[Optional[str]]
    plan: Required[Optional[Dict[str, Any]]]
    results: Required[List[Dict[str, Any]]]
    details: Required[Optional[Dict[str, Any]]]
    duplicate_of: Required[Optional[str]]
    repeat_count: Required[int]
    received_at: Required[int]
    finished_at: Required[Optional[int]]


class TradingViewParseInput(TypedDict, total=False):
    """A message to read, sent nowhere."""
    message: Required[str]
    hook_id: str


class TradingViewParse(TypedDict, total=False):
    ok: Required[bool]
    code: Required[Optional[str]]
    field: Required[Optional[str]]
    message: Required[Optional[str]]
    signal: Required[Optional[Dict[str, Any]]]
    market: Required[Optional[str]]
    suggestion: Required[Optional[str]]
    plan: Required[Optional[Dict[str, Any]]]


class DeletedHook(TypedDict, total=False):
    id: Required[str]
    deleted: Required[bool]


class PriceAlert(TypedDict, total=False):
    """A level on a market, told once when the price reaches it (alert.triggered: the account stream, webhooks, GET /v1/events). It never places or changes an order."""
    id: Required[str]
    account_id: Required[str]
    market: Required[str]
    price: Required[str]
    condition: Required[str]
    note: Required[Optional[str]]
    status: Required[str]
    price_source: Required[str]
    trigger_price: Required[Optional[str]]
    triggered_at: Required[Optional[int]]
    disabled_reason: Required[Optional[str]]
    created_at: Required[int]
    updated_at: Required[int]


class CreatePriceAlert(TypedDict, total=False):
    """A new price alert. Its condition (above or below) is set from the price now."""
    market: Required[str]
    price: Required[Union[str, int, float, Decimal]]
    note: str


class ChangePriceAlert(TypedDict, total=False):
    """What to change on an armed alert: at least one field."""
    price: Union[str, int, float, Decimal]
    note: Optional[str]


class DeletedAlert(TypedDict, total=False):
    id: Required[str]
    deleted: Required[bool]


class RestrictedCountry(TypedDict, total=False):
    code: Required[str]
    name: Required[str]


class CancelAllResult(TypedDict, total=False):
    account_id: Required[str]
    market: Required[Optional[str]]
    canceled: Required[int]


class FlattenResult(TypedDict, total=False):
    account_id: Required[str]
    orders_canceled: Required[int]
    closes: Required[List["Order"]]


class AccountRiskOrNull(TypedDict, total=False):
    """The live risk; null while the account is being opened."""
    as_of: Required[int]
    stale: Required[bool]
    paused: Required[bool]
    balance: Required[str]
    equity: Required[str]
    unrealized_pnl: Required[str]
    pnl_today: Required[str]
    realized_today: Required[str]
    fees_today: Required[str]
    trades_today: Required[int]
    margin_used: Required[str]
    margin_available: Required[Optional[str]]
    exposure: Required[Dict[str, Any]]
    daily_loss: Required["Limit"]
    max_drawdown: Required["Limit"]
    profit_target: Required[Optional[Dict[str, Any]]]
    trading_days: Required[Dict[str, Any]]
    nearest_liquidation: Required[Optional[Dict[str, Any]]]
    trading_day_ends_at: Required[int]
    warnings: Required[List["Warning"]]


class AccountDetail(TypedDict, total=False):
    id: Required[str]
    environment: Required[str]
    stage: Required[str]
    status: Required[str]
    product: Required[Optional[str]]
    tier: Required[Optional[str]]
    start_balance: Required[Optional[str]]
    balance: Required[Optional[str]]
    rule: Required["Rule"]
    payout_eligible: Required[bool]
    parked: bool
    created_at: Required[Optional[int]]
    risk: Required["AccountRiskOrNull"]


class LimitStanding(TypedDict, total=False):
    limit: str
    floor: str
    room: str
    used_pct: Optional[str]
    resets_at: int
    on_breach: str
    estimated: bool
    mode: str
    at_least: bool
    lifted: Required[bool]
    status: Required[str]
