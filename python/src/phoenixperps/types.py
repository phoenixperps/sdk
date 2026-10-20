# Generated from the API spec by generator/printers/python.js. Do not edit: run `node generator`.
from __future__ import annotations

from decimal import Decimal
from typing import Any, Dict, List, Optional, TypedDict, Union

__all__ = ["Error", "Platform", "Status", "Limits", "Market", "MarketStats", "Rule", "Account", "Limit", "Warning", "AccountRisk", "Progress", "TradingPolicy", "EquityHistory", "DailyResult", "MarginSetting", "ExitLeg", "Position", "ClosedPosition", "Order", "Fill", "Me", "CreateOrder", "OrderExits", "CreateStrategy", "ModifyOrder", "ClosePosition", "CancelAll", "MarginSettingInput", "MarginSettingResult", "WhatIf", "Event", "WebhookPayload", "ExitsLeg", "Exits", "ExitsBatch", "Strategy", "WebhookEndpoint", "WebhookDelivery", "WebhookEventType", "DeletedEndpoint", "TradingViewHook", "TradingViewAlert", "TradingViewParseInput", "TradingViewParse", "DeletedHook", "CancelAllResult", "FlattenResult", "AccountRiskOrNull", "AccountDetail", "LimitStanding"]


class Error(TypedDict, total=False):
    """Every error has this shape."""
    error: Dict[str, Any]


class Platform(TypedDict, total=False):
    environment: str
    version: str
    server_time: int
    docs_url: str
    spec_url: str
    status: Dict[str, Any]


class Status(TypedDict, total=False):
    environment: str
    api: str
    engine: Dict[str, Any]
    account_data: Dict[str, Any]
    market_data: Dict[str, Any]
    features: Dict[str, Any]
    server_time: int


class Limits(TypedDict, total=False):
    environment: str
    rate: Dict[str, Any]
    keys: Dict[str, Any]
    orders: Dict[str, Any]
    sandbox: Dict[str, Any]
    strategies: Dict[str, Any]
    body_max_bytes: int
    page_limit: Dict[str, Any]


class Market(TypedDict, total=False):
    market: str
    name: str
    status: str
    tick_size: str
    size_step: str
    min_size: str
    min_notional: Optional[str]
    max_leverage: int
    fee_rate_pct: str
    exposure_cap: Optional[str]
    max_slippage_pct: Optional[Dict[str, Any]]


class MarketStats(TypedDict, total=False):
    """A market's 24-hour figures and open interest. A figure not known yet is null."""
    market: str
    last: Optional[str]
    change_24h: Optional[str]
    change_24h_pct: Optional[str]
    high_24h: Optional[str]
    low_24h: Optional[str]
    volume_24h: Optional[str]
    notional_volume_24h: Optional[str]
    open_interest: Optional[str]
    notional_open_interest: Optional[str]


class Rule(TypedDict, total=False):
    """The account's rule, as the trading engine enforces it."""
    start_balance: str
    daily_loss: Optional[Dict[str, Any]]
    max_drawdown: Optional[Dict[str, Any]]
    weekly_loss: Optional[Dict[str, Any]]
    profit_target: Optional[Dict[str, Any]]
    min_trading_days: Optional[int]
    min_calendar_days: Optional[int]
    consistency: Optional[Dict[str, Any]]
    overnight_holding: bool
    weekend_holding: bool
    inactivity_days: Optional[int]


class Account(TypedDict, total=False):
    id: str
    environment: str
    stage: str
    status: str
    product: Optional[str]
    tier: Optional[str]
    start_balance: Optional[str]
    balance: Optional[str]
    rule: "Rule"
    payout_eligible: bool
    parked: bool
    created_at: Optional[int]


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
    lifted: bool


class Warning(TypedDict, total=False):
    code: str
    level: str
    market: str
    value: str


class AccountRisk(TypedDict, total=False):
    as_of: int
    stale: bool
    balance: str
    equity: str
    unrealized_pnl: str
    pnl_today: str
    realized_today: str
    fees_today: str
    trades_today: int
    margin_used: str
    margin_available: Optional[str]
    exposure: Dict[str, Any]
    daily_loss: "Limit"
    max_drawdown: "Limit"
    profit_target: Optional[Dict[str, Any]]
    trading_days: Dict[str, Any]
    nearest_liquidation: Optional[Dict[str, Any]]
    trading_day_ends_at: int
    warnings: List["Warning"]


class Progress(TypedDict, total=False):
    stage: str
    objectives: Dict[str, Any]
    limits: Dict[str, Any]
    payout: Optional[Dict[str, Any]]
    as_of: int


class TradingPolicy(TypedDict, total=False):
    rule: "Rule"
    trading_day: Dict[str, Any]
    slippage_pct: Dict[str, Any]
    margin: Dict[str, Any]
    orders: Dict[str, Any]
    controls: Dict[str, Any]
    budgets: Dict[str, Any]
    as_of: int


class EquityHistory(TypedDict, total=False):
    resolution: str
    points: List[Dict[str, Any]]
    live: Dict[str, Any]


class DailyResult(TypedDict, total=False):
    date: str
    trades: int
    gross_pnl: str
    fees: str
    pnl: str
    withdrawn: str
    balance_end: Optional[str]


class MarginSetting(TypedDict, total=False):
    market: str
    leverage: int
    margin_mode: str
    max_leverage: int
    source: str
    locked: bool


class ExitLeg(TypedDict, total=False):
    order_id: str
    type: str
    price: Optional[str]
    size: Optional[str]


class Position(TypedDict, total=False):
    id: str
    market: str
    side: str
    size: str
    entry_price: Optional[str]
    mark_price: Optional[str]
    notional: Optional[str]
    unrealized_pnl: Optional[str]
    leverage: int
    margin_mode: str
    initial_margin: Optional[str]
    added_margin: Optional[str]
    margin: Optional[str]
    liquidation: Optional[Dict[str, Any]]
    breach_prices: Dict[str, Any]
    exits: Dict[str, Any]
    protected: bool
    opened_at: Optional[int]


class ClosedPosition(TypedDict, total=False):
    market: str
    side: str
    size: str
    entry_price: Optional[str]
    exit_price: Optional[str]
    gross_pnl: str
    fees: str
    pnl: str
    opened_at: int
    closed_at: int
    lots: List[Dict[str, Any]]


class Order(TypedDict, total=False):
    id: str
    client_order_id: Optional[str]
    account_id: str
    market: str
    side: Optional[str]
    type: Optional[str]
    status: str
    size: Optional[str]
    filled_size: Optional[str]
    limit_price: Optional[str]
    stop_price: Optional[str]
    average_fill_price: Optional[str]
    fee: Optional[str]
    reduce_only: Optional[bool]
    time_in_force: str
    expires_at: Optional[int]
    post_only: Optional[bool]
    source: str
    strategy_id: Optional[str]
    exits: Optional[Dict[str, Any]]
    cancel_reason: Optional[str]
    reject: Optional[Dict[str, Any]]
    created_at: Optional[int]
    filled_at: Optional[int]
    canceled_at: Optional[int]
    updated_at: Optional[int]


class Fill(TypedDict, total=False):
    id: str
    order_id: str
    market: str
    side: Optional[str]
    size: str
    price: str
    fee: Optional[str]
    liquidation: bool
    time: int


class Me(TypedDict, total=False):
    credential: Dict[str, Any]
    accounts: List[str]
    account_scope: str
    controls: Dict[str, Any]
    budgets: Dict[str, Any]
    assistant: Optional[Dict[str, Any]]


class CreateOrder(TypedDict, total=False):
    """A new order."""
    market: str
    side: str
    type: str
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
    type: str
    market: str
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
    margin_mode: str
    leverage: int


class MarginSettingResult(TypedDict, total=False):
    market: str
    margin_mode: str
    leverage: int


class WhatIf(TypedDict, total=False):
    result: str
    reject: Optional[Dict[str, Any]]
    market: str
    side: str
    type: str
    size: Optional[str]
    execution: Optional[Dict[str, Any]]
    notional: Optional[str]
    fee: Optional[str]
    margin: Optional[Dict[str, Any]]
    position_after: Optional[Dict[str, Any]]
    exposure: Optional[Dict[str, Any]]


class Event(TypedDict, total=False):
    id: str
    type: str
    account_id: str
    time: int
    source: str
    data: Dict[str, Any]


class WebhookPayload(TypedDict, total=False):
    """What a json webhook endpoint receives: the event as GET /v1/events shows it, with the endpoint's API version and environment."""
    id: str
    type: str
    account_id: str
    time: int
    source: str
    data: Dict[str, Any]
    api_version: str
    environment: str
    test: bool


class ExitsLeg(TypedDict, total=False):
    id: str
    group: str
    type: str
    status: str
    size: str
    price: Optional[str]
    limit_price: Optional[str]
    trail: Optional[Dict[str, Any]]
    break_even: Optional[Dict[str, Any]]


class Exits(TypedDict, total=False):
    account_id: str
    market: str
    strategy_id: Optional[str]
    position: Optional[Dict[str, Any]]
    take_profit: List["ExitsLeg"]
    stop_loss: List["ExitsLeg"]
    resize_with_position: bool


class ExitsBatch(TypedDict, total=False):
    """The take-profit and stop-loss legs of a position, changed together."""
    expected_position: Optional[Dict[str, Any]]
    expected_exits: List[Dict[str, Any]]
    operations: List[Dict[str, Any]]
    resize_with_position: bool


class Strategy(TypedDict, total=False):
    id: str
    client_strategy_id: Optional[str]
    account_id: str
    type: str
    market: str
    side: Optional[str]
    status: str
    end_reason: Optional[str]
    params: Dict[str, Any]
    progress: Dict[str, Any]
    orders: List["Order"]
    created_at: int
    updated_at: int
    ended_at: Optional[int]


class WebhookEndpoint(TypedDict, total=False):
    id: str
    url: Optional[str]
    format: str
    events: List[str]
    status: str
    api_version: str
    environment: str


class WebhookDelivery(TypedDict, total=False):
    id: str
    endpoint_id: str
    event_id: str
    event_type: str
    account_id: Optional[str]
    test: bool
    status: str
    attempts: List[Dict[str, Any]]
    next_attempt_at: Optional[int]
    created_at: int
    delivered_at: Optional[int]


class WebhookEventType(TypedDict, total=False):
    type: str
    group: Optional[str]
    description: str


class DeletedEndpoint(TypedDict, total=False):
    id: str
    deleted: bool


class TradingViewHook(TypedDict, total=False):
    id: str
    name: str
    account_id: str
    environment: str
    status: str
    markets: List[str]
    reduce_only: bool
    max_order_usd: Optional[str]
    max_position_usd: Optional[str]
    daily_loss_usd: Optional[str]
    sizing: Dict[str, Any]
    position_mode: str


class TradingViewAlert(TypedDict, total=False):
    id: str
    hook_id: str
    account_id: str
    status: str
    reason: Optional[str]
    reason_text: Optional[str]
    format: Optional[str]
    market: Optional[str]
    action: Optional[str]
    message: Optional[str]
    sender: Optional[str]
    plan: Optional[Dict[str, Any]]
    results: List[Dict[str, Any]]
    details: Optional[Dict[str, Any]]
    duplicate_of: Optional[str]
    repeat_count: int
    received_at: int
    finished_at: Optional[int]


class TradingViewParseInput(TypedDict, total=False):
    """A message to read, sent nowhere."""
    message: str
    hook_id: str


class TradingViewParse(TypedDict, total=False):
    ok: bool
    code: Optional[str]
    field: Optional[str]
    message: Optional[str]
    signal: Optional[Dict[str, Any]]
    market: Optional[str]
    suggestion: Optional[str]
    plan: Optional[Dict[str, Any]]


class DeletedHook(TypedDict, total=False):
    id: str
    deleted: bool


class CancelAllResult(TypedDict, total=False):
    account_id: str
    market: Optional[str]
    canceled: int


class FlattenResult(TypedDict, total=False):
    account_id: str
    orders_canceled: int
    closes: List["Order"]


class AccountRiskOrNull(TypedDict, total=False):
    """The live risk; null while the account is being opened."""
    as_of: int
    stale: bool
    balance: str
    equity: str
    unrealized_pnl: str
    pnl_today: str
    realized_today: str
    fees_today: str
    trades_today: int
    margin_used: str
    margin_available: Optional[str]
    exposure: Dict[str, Any]
    daily_loss: "Limit"
    max_drawdown: "Limit"
    profit_target: Optional[Dict[str, Any]]
    trading_days: Dict[str, Any]
    nearest_liquidation: Optional[Dict[str, Any]]
    trading_day_ends_at: int
    warnings: List["Warning"]


class AccountDetail(TypedDict, total=False):
    id: str
    environment: str
    stage: str
    status: str
    product: Optional[str]
    tier: Optional[str]
    start_balance: Optional[str]
    balance: Optional[str]
    rule: "Rule"
    payout_eligible: bool
    parked: bool
    created_at: Optional[int]
    risk: "AccountRiskOrNull"


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
    lifted: bool
    status: str
