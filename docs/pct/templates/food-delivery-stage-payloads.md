# Food delivery — stage payloads

Dashboard **Key:** `pct_payload`  
Paste each **Value** below as a single line (remove line breaks if you copy the formatted block).

Use the same `order_id` in every stage.

---

## ETA and chip text

### `eta_epoch_ms` (recommended — countdown stages 2–5)

- **Required for countdown stages** in production: Unix ms when delivery is expected (chip reaches zero).
- **Must be computed fresh on every stage push** — do not reuse sample values or a previous stage’s timestamp.
- Set from your order backend at send time, e.g. `deliveryTime.toEpochMilli()` or `now + remainingMinutes × 60_000`.
- On **API 36+**, passed to `NotificationCompat.Builder.setWhen(eta_epoch_ms)` — the system drives the status-bar chip countdown (rounded minutes, updates ~every minute).
- Chip **wording is OEM-specific** (same payload): e.g. Pixel 8a `39m`, Nothing Phone 3 `in 39m`. Do **not** call `setShowWhen(false)` — it can suppress chip text on some devices.
- On **API ≤35**, FGS uses this for tracker motion and derives `"N min"` in the notification body.

### `chip_text` (optional)

| Stage type | `chip_text` | Notes |
|------------|-------------|--------|
| **Countdown (2–5)** | **Optional** | Primary source is `eta_epoch_ms`. Omit `chip_text` when using `eta_epoch_ms`. |
| **Static (1, 6)** | **Use for fixed labels** | e.g. `"Placing"`, `"Done ✓"` — shown via `setShortCriticalText` on API 36+ |
| **Stale (ETA passed)** | **Use `stale_chip_text`** | Default `"Soon"` — not `chip_text` |

**Fallback only:** If `eta_epoch_ms` is missing on a countdown stage, the sample parses `chip_text` in `"N min"` form (e.g. `"18 min"`) and derives ETA from push receive time. This is **not** the recommended integration path.

**Sample `eta_epoch_ms` values below are placeholders** — replace with a real delivery timestamp each time you send.

---

## Stage 1 — Order placed

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":1,"title":"Order placed at Punjabi Tadka","message":"Confirming with restaurant…","chip_text":"Placing","tracker_position":0,"styled_by_progress":true,"terminal":false,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"receipt","start_icon":"restaurant","end_icon":"home"}
```

---

## Stage 2 — Restaurant accepted

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":2,"title":"Punjabi Tadka is preparing your order","message":"ETA 7:45 PM · 4 items","eta_epoch_ms":1739900700000,"tracker_position":100,"styled_by_progress":true,"terminal":false,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"chef","start_icon":"restaurant","end_icon":"home"}
```

---

## Stage 3 — Rider assigned

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":3,"title":"Anil is heading to pick up your order","message":"Rider 0.8 km from restaurant","eta_epoch_ms":1739900340000,"tracker_position":1100,"styled_by_progress":true,"terminal":false,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"helmet","start_icon":"restaurant","end_icon":"home"}
```

---

## Stage 4 — Picked up

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":4,"title":"Anil has picked up your order","message":"On the way · 12 min · Tap to track","eta_epoch_ms":1739900280000,"tracker_position":2100,"styled_by_progress":true,"terminal":false,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"scooter","start_icon":"restaurant","end_icon":"home"}
```

---

## Stage 5 — Approaching

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":5,"title":"Anil is 2 minutes away","message":"Order arriving at home — please be ready","eta_epoch_ms":1739900160000,"tracker_position":2800,"styled_by_progress":true,"terminal":false,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"scooter","start_icon":"restaurant","end_icon":"home"}
```

---

## Stage 6 — Delivered

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":6,"title":"Order delivered. Enjoy your meal!","message":"Tap to rate or reorder","chip_text":"Done ✓","tracker_position":3000,"styled_by_progress":true,"terminal":true,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"plate","start_icon":"restaurant","end_icon":"home"}
```
