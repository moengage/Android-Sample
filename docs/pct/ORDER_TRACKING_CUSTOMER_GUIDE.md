# Order Tracking — Customer Guide

Food delivery **Progress Centric Template (PCT)** sample using MoEngage **self-handled** Background Update pushes.

**Code:** `Android-Sample/example/` → `com.moengage.example.ordertracking`  
**Entry point:** `CustomPushMessageListener.onSelfHandledNotificationReceived()`

Package layout: `model/` (JSON types), `data/` (decode), `render/` (API-level UIs), `live/` (FGS + countdown logic), `notification/` (channel, intents, receivers).

---

## 1. Flow

```
MoEFireBaseMessagingService → SDK (parse + impression) → onSelfHandledNotificationReceived()
  → OrderTrackingForegroundService → notification (UI by API level)
```

- App does **not** handle FCM directly — SDK only.
- Impression: automatic in callback. Clicks: `MoEPushHelper.logNotificationClick()` with original `Bundle`.
- **Foreground required** for first notification (user placing order). No background FGS fallback in this sample.
- **Channel:** `order_tracking` (`IMPORTANCE_DEFAULT`) on all APIs. UI differs by API; Live Update comes from notification style, not a separate channel.
- **State:** In-memory in FGS between stage pushes (no WorkManager, no disk session in this sample).

---

## 2. Prerequisites

| Item | Detail |
|------|--------|
| MoEngage Android SDK | 14.06.00+, BOM 1.5.0+ |
| FCM | `MoEFireBaseMessagingService` in manifest |
| Callback | `onSelfHandledNotificationReceived()` — [docs](https://www.moengage.com/docs/developer-guide/android-sdk/push/advanced/callbacks-and-customisation#self-handled-notification-received-callback) |
| Campaign | [Background Update](https://www.moengage.com/docs/user-guide/campaigns-and-channels/mobile-push/create/push-templates#background-update) template |
| Android 16 PCT | `compileSdk 36`, `androidx.core` 1.17+, `POST_PROMOTED_NOTIFICATIONS` |
| Permission | `POST_NOTIFICATIONS` (Android 13+) |

---

## 3. UI by Android version

Same `pct_payload` everywhere.

| API | Experience |
|-----|------------|
| 36+ | ProgressStyle, status-bar chip, Live Update. Chip countdown from `eta_epoch_ms` via `setWhen` (system rounded minutes; OEM-specific text). |
| 34–35 | BigPicture + progress strip |
| 31–33 | BigText + emoji line + countdown in body |
| ≤30 | Standard title + step line |

FGS updates tracker every ~2 min between stage pushes.

---

## 4. Dashboard setup

1. Background Update campaign → custom key **`pct_payload`** = stage JSON ([templates](./templates/food-delivery-stage-payloads.md)).
2. Same **`order_id`** for all stages (one notification slot).

### ETA & chip fields

| Field | When |
|-------|------|
| **`eta_epoch_ms`** | **Required** for countdown stages (2–5). Set **fresh on every push** from backend (Unix ms delivery instant). |
| **`chip_text`** | Optional on countdown. Use for static labels: `"Placing"` (stage 1), `"Done ✓"` (stage 6). |
| **`stale_chip_text`** | After ETA passes without next push (default `"Soon"`). |

If `eta_epoch_ms` is missing, sample falls back to `"N min"` in `chip_text` (not recommended).

### Status-bar chip (API 36+)

Countdown stages use **`setWhen(eta_epoch_ms)`** — the system shows rounded minutes and decrements automatically.

| Topic | Detail |
|-------|--------|
| **Payload field** | `eta_epoch_ms` — recompute on every push |
| **App code** | `LiveUpdateChip.kt` → `setWhen(etaMs)` for countdown; `setShortCriticalText` for static/stale |
| **Do not use** | `setShowWhen(false)` — can hide chip text (icon-only chip) on some devices |
| **OEM wording** | Same ETA, different labels: Pixel 8a `39m`, Nothing Phone 3 `in 39m` (tested) |
| **FGS role** | 2 min ticks move the tracker only; chip is system-driven on API 36+ |

---

## 5. Payload schema (`pct_food_delivery`)

Dashboard key: **`pct_payload`**. Value: JSON string (Android stringifies all KV values).

```json
{
  "template": "pct_food_delivery",
  "order_id": "DEMO-FOOD-001",
  "stage": 1,
  "title": "string",
  "message": "string",
  "tracker_position": 0,
  "tracker_position_end": 2750,
  "eta_epoch_ms": 1718707200000,
  "chip_text": "Placing",
  "stale_chip_text": "Soon",
  "terminal": false,
  "styled_by_progress": true,
  "respect_user_dismiss": false,
  "segments": [{ "color": "#hex", "size": 100 }],
  "points": [{ "color": "#hex", "position": 100 }],
  "tracker_icon": "scooter",
  "start_icon": "restaurant",
  "end_icon": "home"
}
```

### Field reference

| Field | Required | Meaning |
|-------|----------|---------|
| `template` | Yes | Vertical id (`pct_food_delivery` for this sample). |
| `order_id` | Yes | Notification tag — **same value for all stages** of one order. |
| `stage` | Yes | Journey step (1–6 for food delivery). |
| `title` | Yes | Notification title. |
| `message` | Yes | Body text (API 36 uses this as content text). |
| `chip_text` | Static stages | Short label for non-countdown stages (`"Placing"`, `"Done ✓"`). Optional on countdown when using `eta_epoch_ms`. |
| `tracker_position` | Yes | Tracker position on progress scale (food sample total = 3000). |
| `tracker_position_end` | No | Max tracker position until next push (for FGS motion between stages). |
| `eta_epoch_ms` | Countdown (2–5) | **Recommended** — Unix ms when delivery is expected; drives API 36+ chip via `setWhen` and FGS tracker interpolation. Recompute on every push. |
| `stale_chip_text` | No | Chip text if ETA passes without next push (default `"Soon"`). |
| `respect_user_dismiss` | No | If `true`, do not re-show after user dismisses until terminal stage. Sample default: `false`. |
| `styled_by_progress` | No | Dim segments ahead/behind tracker on API 36 (default `true`). |
| `terminal` | No | `true` on final stage — sample auto-dismisses after ~3s. |
| `segments` | Yes | Coloured bar sections: `color` (hex), `size` (relative length). Send full layout every stage. |
| `points` | Yes | Milestone dots: `color`, `position` on same scale as tracker. |
| `tracker_icon` | Yes | Moving icon key — app maps to drawable (`receipt`, `chef`, `helmet`, `scooter`, `plate`). |
| `start_icon` | Yes | Start of bar icon key (`restaurant`, etc.). |
| `end_icon` | Yes | End of bar icon key (`home`, etc.). |

**Analytics:** Impression is logged by the SDK when `onSelfHandledNotificationReceived()` fires. For notification taps, `OrderNotificationClickReceiver` calls `MoEPushHelper.logNotificationClick()` with the **original full MoEngage push `Bundle`** passed through notification intent extras.

**Local countdown:** Prefer `eta_epoch_ms` on countdown stages. Fallback: `chip_text` in `"N min"` form (e.g. `"18 min"`) derives ETA from push receive time — not recommended for production.

---

## 6. Stage payloads

Copy-paste JSON for each stage:

**[templates/food-delivery-stage-payloads.md](./templates/food-delivery-stage-payloads.md)**

Keep `order_id` identical across stages 1–6. Replace sample `eta_epoch_ms` placeholders with a fresh timestamp each time you send.

---

## 7. Beyond food delivery

This sample implements **one PCT use case** (food delivery, 6 stages). The same pattern applies to other verticals (e.g. quick commerce, ride hailing):

- Same **self-handled** Background Update + `pct_payload` (or your own custom key).
- Define your own `template` id, stages, segments, and icons.
- Reuse the **fallback idea**: full `ProgressStyle` on API 36+, simpler UI on older Android.

See `ordertracking/` (`model/`, `data/`, `render/`, `live/`, `notification/`) for routing, fallbacks, and FGS ticks between milestone pushes.

---

## 8. Running the sample app

1. Open `example/` in Android Studio.
2. Set your **MoEngage App ID** and **data center** in `MoEngageDemoApplication`.
3. Add **`app/google-services.json`** for FCM and uncomment `apply(plugin = "com.google.gms.google-services")` in `app/build.gradle.kts` (see comment in that file).
4. Build and install on a test device (grant notification permission on Android 13+).
5. Register the test user in MoEngage (match the unique id your test campaign targets).
6. With **app in foreground**, send Background Update test pushes with Key `pct_payload` and stage JSON from §6.

**Reference implementation:** `CustomPushMessageListener.onSelfHandledNotificationReceived()` → `OrderTrackingForegroundService`.

**Logcat:** `tag:OrderTracking`

---

## References

- [Self-handled callback](https://www.moengage.com/docs/developer-guide/android-sdk/push/advanced/callbacks-and-customisation#self-handled-notification-received-callback)
- [Push display handled by application](https://www.moengage.com/docs/developer-guide/android-sdk/push/advanced/push-display-handled-by-application)
- [Background Update template](https://www.moengage.com/docs/user-guide/campaigns-and-channels/mobile-push/create/push-templates#background-update)
- [Android Live Updates](https://developer.android.com/develop/ui/views/notifications/live-update)
