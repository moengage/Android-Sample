# Order Tracking — Progress Centric Template (Customer Guide)

Sample reference for **food delivery order tracking** using MoEngage **self-handled** Background Update pushes. The app renders the notification UI; MoEngage delivers the payload.

**Sample code:** `Android-Sample/example/` → package `com.moengage.example.ordertracking`

The sample is organized into sub-packages under that root: `model/` (JSON types), `data/` (session + decode), `render/` (API-level notification UIs), `live/` (WorkManager countdown ticks), and `notification/` (channels, intents, receivers). Shared constants and the push entry point stay at the package root.

---

## 1. Overview

MoEngage sends a silent Background Update push with a JSON payload. Your app receives it in `onSelfHandledNotificationReceived()`, parses the JSON, and shows **one updating notification** per `order_id` (stages 1→6).

This sample shows how self-handled push can power **Progress Centric Template (PCT)** on Android 16+ and **fallback UIs** on older versions — same payload, different rendering per OS.

---

## 2. Prerequisites

| Requirement | Detail |
|-------------|--------|
| **Android SDK** | **14.06.00+** (self-handled callback). |
| **Android BOM** | **1.5.0+**. |
| **Self-handled integration** | Register `PushMessageListener` and implement `onSelfHandledNotificationReceived()`. See [Callbacks & Customisation — Self-handled notification received](https://www.moengage.com/docs/developer-guide/android-sdk/push/advanced/callbacks-and-customisation#self-handled-notification-received-callback). |
| **Background Update template** | [Push Templates — Background Update](https://www.moengage.com/docs/user-guide/campaigns-and-channels/mobile-push/create/push-templates#background-update). |
| **Android 16 full PCT** | `compileSdk 36`, `androidx.core` 1.17+, `POST_PROMOTED_NOTIFICATIONS` for Live Updates chip. |
| **Push permission** | `POST_NOTIFICATIONS` on Android 13+. |

---

## 3. What users see (by Android version)

Same `pct_payload` JSON for every device. The sample app picks the UI by API level.

| Android | API | User experience |
|---------|-----|-----------------|
| **16+** | 36+ | Full progress bar, tracker icon, status-bar chip (Live Update). |
| **14–15** | 34–35 | Big picture notification with coloured progress strip (collapsed thumbnail + expanded image). |
| **12–13** | 31–33 | Big text + emoji segment line + countdown in body. |
| **≤11** | ≤30 | Standard notification — title + short step/chip line. |

---

## 4. Dashboard setup

1. Create a push campaign → select **Background Update** template ([docs](https://www.moengage.com/docs/user-guide/campaigns-and-channels/mobile-push/create/push-templates#background-update)).
2. MoEngage delivers a **self-handled** push — the SDK does **not** show a notification; your app must ([Push display handled by application](https://www.moengage.com/docs/developer-guide/android-sdk/push/advanced/push-display-handled-by-application)).
3. Add one custom key-value pair:

| Key | Value |
|-----|-------|
| `pct_payload` | Stage JSON from [templates/food-delivery-stage-payloads.md](./templates/food-delivery-stage-payloads.md) |

Use the **same `order_id`** in every stage so Android replaces one notification slot.

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
  "chip_text": "string",
  "tracker_position": 0,
  "tracker_position_end": 2750,
  "eta_epoch_ms": 1718707200000,
  "stale_chip_text": "Soon",
  "respect_user_dismiss": false,
  "styled_by_progress": true,
  "terminal": false,
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
| `chip_text` | Yes | Short status / ETA (status-bar chip on API 36; in body on fallbacks). |
| `tracker_position` | Yes | Tracker position on progress scale (food sample total = 3000). |
| `tracker_position_end` | No | Max tracker position until next push (for local motion between stages). |
| `eta_epoch_ms` | No | **Preferred** for local countdown ticks — Unix ms when chip hits zero. |
| `stale_chip_text` | No | Chip text if ETA passes without next push (default `"Soon"`). |
| `respect_user_dismiss` | No | If `true`, do not re-show after user dismisses until terminal stage. Sample default: `false`. |
| `styled_by_progress` | No | Dim segments ahead/behind tracker on API 36 (default `true`). |
| `terminal` | No | `true` on final stage — sample auto-dismisses after ~3s. |
| `segments` | Yes | Coloured bar sections: `color` (hex), `size` (relative length). Send full layout every stage. |
| `points` | Yes | Milestone dots: `color`, `position` on same scale as tracker. |
| `tracker_icon` | Yes | Moving icon key — app maps to drawable (`receipt`, `chef`, `helmet`, `scooter`, `plate`). |
| `start_icon` | Yes | Start of bar icon key (`restaurant`, etc.). |
| `end_icon` | Yes | End of bar icon key (`home`, etc.). |

**Analytics:** Impression is logged by the SDK when `onSelfHandledNotificationReceived()` fires. For notification taps, call `MoEPushHelper.logNotificationClick()` with the **original full MoEngage push `Bundle`** (the sample persists push keys in `{orderId}.session.json` and rebuilds the `Bundle` only on tap).

**Session on disk:** `{orderId}.session.json` holds all string push keys; `{orderId}.meta` holds receive time and dismiss flag. Local ticks read `pct_payload` from the JSON file into `OrderTrackingPayload` — no `Bundle` on the re-render path.

**Local countdown:** Prefer `eta_epoch_ms` on countdown stages. The sample also accepts `chip_text` in `"N min"` form (e.g. `"12 min"`) as a food-delivery fallback; other chip formats (OTP codes, `"Placing"`) do not start background ticks.

---

## 6. Stage payloads

Copy-paste JSON for each stage:

**[templates/food-delivery-stage-payloads.md](./templates/food-delivery-stage-payloads.md)**

Keep `order_id` identical across stages 1–6.

---

## 7. Beyond food delivery

This sample implements **one PCT use case** (food delivery, 6 stages). The same pattern applies to other product verticals (e.g. quick commerce, ride hailing):

- Same **self-handled** Background Update + `pct_payload` (or your own key).
- Define your own `template` id, stages, segments, and icons.
- Reuse the **fallback idea**: full `ProgressStyle` on API 36+, simpler UI on older Android.

See the sample `ordertracking/` package (`model/`, `data/`, `render/`, `live/`, `notification/`) for routing, fallbacks, and local countdown between milestone pushes.

---

## 8. Running the sample app

1. Open `example/` in Android Studio.
2. Set your **MoEngage App ID**, **environment** (TEST / LIVE), and **data center** in `MoEngageDemoApplication`.
3. Add **`app/google-services.json`** for FCM and uncomment `apply(plugin = "com.google.gms.google-services")` in `app/build.gradle.kts` (see comment in that file).
4. Build and install on a test device (notification permission granted on Android 13+).
5. Register the test user in MoEngage (match the unique id your test campaign targets).
6. Send Background Update test pushes with Key `pct_payload` and stage JSON from §6.

**Reference implementation:** `CustomPushMessageListener` → `handleOrderTrackingPush()` in `ordertracking/`.

**Logcat:** filter `tag:OrderTracking`

---

## References

- [Self-handled notification received callback](https://www.moengage.com/docs/developer-guide/android-sdk/push/advanced/callbacks-and-customisation#self-handled-notification-received-callback)
- [Push display handled by application](https://www.moengage.com/docs/developer-guide/android-sdk/push/advanced/push-display-handled-by-application)
- [Background Update template (campaign)](https://www.moengage.com/docs/user-guide/campaigns-and-channels/mobile-push/create/push-templates#background-update)
- [Android Progress-centric notifications (API 36)](https://developer.android.com/about/versions/16/features/progress-centric-notifications)
