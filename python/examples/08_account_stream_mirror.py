"""08: a live copy of your accounts' positions and working orders: the snapshot first, then every event applied once.
After a reconnect the SDK resumes from the last event; when the server cannot cover that (a gap), the events missed are
read by REST, so the copy never misses one. Ctrl+C to stop.

    python examples/08_account_stream_mirror.py
"""
import asyncio
from typing import Any, Dict

from phoenixperps import PhoenixPerps
from phoenixperps.types import Event

FINAL = {"filled", "canceled", "rejected", "expired"}
mirror: Dict[str, Dict[str, Dict[str, Any]]] = {}


def show(why: str) -> None:
    for account_id, m in mirror.items():
        positions = ", ".join(f'{p["market"]} {p["side"]} {p["size"]}' for p in m["positions"].values()) or "none"
        print(f'[{why}] {account_id}: positions {positions}; {len(m["orders"])} working orders')


def apply(event: Event) -> None:
    m = mirror.get(event["account_id"])
    if m is None:
        return
    order, position = event["data"].get("order"), event["data"].get("position")
    if order:
        if order["status"] in FINAL:
            m["orders"].pop(order["id"], None)
        else:
            m["orders"][order["id"]] = order
    if position:
        if event["type"] == "position.closed":
            m["positions"].pop(position["market"], None)
        else:
            m["positions"][position["market"]] = position


async def main() -> None:
    client = PhoenixPerps()
    async with client.account_stream() as stream:
        await stream.ready.wait()
        for account_id, snap in stream.snapshots.items():
            if snap.get("error"):  # not readable now: its orders and positions are unknown, not empty
                print(f'[snapshot] {account_id}: not mirrored ({snap["error"]}); read it again by REST')
                continue
            mirror[account_id] = {
                "positions": {p["market"]: p for p in snap["positions"]},
                "orders": {o["id"]: o for o in snap["orders"]},
            }
        show("snapshot")
        async for event in stream:
            if event["type"] == "notice":  # a gap: the events after event["after"] may be missing here
                if event["after"]:
                    for missed in client.events.list({"after": event["after"]}).auto_paging_iter():
                        apply(missed)
                show(f'gap after {event["after"]}')
                continue
            apply(event)
            show(event["type"])


asyncio.run(main())
