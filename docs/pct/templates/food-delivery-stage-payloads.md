# Food delivery — stage payloads

Dashboard **Key:** `pct_payload`  
Paste each **Value** below as a single line (remove line breaks if you copy the formatted block).

Use the same `order_id` in every stage.

**Countdown stages (2–5):** Prefer adding `"eta_epoch_ms": <unix_ms>` (time when the chip should reach zero, e.g. now + 32 minutes for stage 2). The sample falls back to `"N min"` in `chip_text` if `eta_epoch_ms` is omitted.

---

## Stage 1 — Order placed

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":1,"title":"Order placed at Punjabi Tadka","message":"Confirming with restaurant…","chip_text":"Placing","tracker_position":0,"styled_by_progress":true,"terminal":false,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"receipt","start_icon":"restaurant","end_icon":"home"}
```

---

## Stage 2 — Restaurant accepted

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":2,"title":"Punjabi Tadka is preparing your order","message":"ETA 7:45 PM · 4 items","chip_text":"32 min","tracker_position":100,"styled_by_progress":true,"terminal":false,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"chef","start_icon":"restaurant","end_icon":"home"}
```

---

## Stage 3 — Rider assigned

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":3,"title":"Anil is heading to pick up your order","message":"Rider 0.8 km from restaurant","chip_text":"18 min","tracker_position":1100,"styled_by_progress":true,"terminal":false,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"helmet","start_icon":"restaurant","end_icon":"home"}
```

---

## Stage 4 — Picked up

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":4,"title":"Anil has picked up your order","message":"On the way · 12 min · Tap to track","chip_text":"12 min","tracker_position":2100,"styled_by_progress":true,"terminal":false,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"scooter","start_icon":"restaurant","end_icon":"home"}
```

---

## Stage 5 — Approaching

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":5,"title":"Anil is 2 minutes away","message":"Order arriving at home — please be ready","chip_text":"2 min","tracker_position":2800,"styled_by_progress":true,"terminal":false,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"scooter","start_icon":"restaurant","end_icon":"home"}
```

---

## Stage 6 — Delivered

```json
{"template":"pct_food_delivery","order_id":"DEMO-FOOD-001","stage":6,"title":"Order delivered. Enjoy your meal!","message":"Tap to rate or reorder","chip_text":"Done ✓","tracker_position":3000,"styled_by_progress":true,"terminal":true,"segments":[{"color":"#f97316","size":100},{"color":"#eab308","size":500},{"color":"#3b82f6","size":1000},{"color":"#1e40af","size":1000},{"color":"#16a34a","size":400}],"points":[{"color":"#94a3b8","position":100},{"color":"#94a3b8","position":600},{"color":"#94a3b8","position":1600},{"color":"#94a3b8","position":2600}],"tracker_icon":"plate","start_icon":"restaurant","end_icon":"home"}
```
