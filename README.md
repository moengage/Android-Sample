![Logo](/.github/assets/logo.png)

# MoEngage Android SDK Integration Sample

This repository contains a sample integration for [MoEngage](https://www.moengage.com).

![MavenBadge](https://img.shields.io/maven-central/v/com.moengage/moe-android-sdk)

---

## Brew Bar

**Brew Bar** is a coffee-shop ordering app whose real job is to demonstrate a complete MoEngage
Android SDK integration end to end: initialisation, identity and user attributes, custom events,
FCM push with the Android 13+ runtime permission, in-app messages and nudges, self-handled
campaigns, the notification inbox, cards, geofence campaigns, real-time (device) triggers, and
progress-centric order-tracking notifications.

Kotlin + Jetpack Compose, single Activity, Navigation-Compose, Material 3. No XML layouts and no
network layer — the menu, cart, orders and coupon data is in-memory, so the only thing crossing
the wire is the SDK traffic you came to look at.

```bash
./gradlew assembleDebug      # or: ./gradlew installDebug
```

**The app builds and runs with no credentials at all.** Without a workspace ID the SDK still
initialises, logs a warning, and every screen stays clickable — the events simply have nowhere to
land. Add credentials when you want them to arrive somewhere.

### Contents

[Quick start](#quick-start) · [What it demonstrates](#what-it-demonstrates) ·
[Firing each SDK moment](#firing-each-sdk-moment) · [Permissions](#permissions) ·
[Event dictionary](#event-dictionary) · [Screens and deep links](#screens-and-deep-links) ·
[Order tracking](#order-tracking-progress-centric-template) · [Project layout](#project-layout) ·
[Decisions worth knowing](#decisions-worth-knowing) · [Test checklist](#test-checklist)

---

## Quick start

### 1. Workspace ID and data centre

Never hard-coded. Add them to `local.properties` (git-ignored):

```properties
YOUR_MOENGAGE_WORKSPACE_ID=XXXXXXXXXXXXXXX
MOENGAGE_DATA_CENTER=DATA_CENTER_1
```

`MOENGAGE_DATA_CENTER` accepts `DATA_CENTER_1` … `DATA_CENTER_6` and `DATA_CENTER_101`, in any of
three forms — `DATA_CENTER_3`, the SDK's own `dc3`, or just `3`. Case and whitespace do not matter,
and anything unrecognised falls back to `DATA_CENTER_1` with a warning rather than failing the
launch (`dataCenterFrom` in `sdkhelper/MoEngageDataCenter.kt`). Both values are read in
`app/build.gradle.kts` and surfaced as `BuildConfig` fields.

Without them the SDK initialises against the placeholder workspace ID and logs a warning at
startup. `MoEngageSDKHelper.isConfigured` reports which mode you are in, and DemoTools says so on
screen.

### 2. FCM (optional — push only)

1. Create an Android app in your Firebase project with package name **`com.moengage.sampleapp`**.
2. Download `google-services.json` into `app/` (git-ignored).
3. Upload the Firebase **service-account JSON** (or server key) to MoEngage →
   Settings → Push → Android.

The Google Services plugin is applied **only if `app/google-services.json` exists**, so a fresh
clone never fails to build. Without the file, push is inert and Gradle prints:

```
BrewBar: app/google-services.json missing — FCM push will be inert. See README.md.
```

### 3. Toolchain

JDK 17+ (JDK 21 recommended — Android Studio's bundled JBR works).

| | |
|---|---|
| AGP | 9.1.1 |
| Kotlin | 2.3.20 |
| Gradle | 9.3.1 |
| Compose BOM | 2026.06.01 |
| MoEngage BOM | 4.3.0 |
| minSdk / targetSdk / compileSdk | 24 / 37 / 37 |

---

## What it demonstrates

Every SDK capability the app uses is listed in one file — `sdkhelper/MoEngageSDKHelper.kt` — as a
one-line delegation, so the whole integration surface can be read at a glance. The implementations
live in `internal` objects next to it, one per feature, and **nothing outside the `sdkhelper`
package imports `com.moengage.*`**.

| Capability | SDK entry point | Facade | Fires from |
|---|---|---|---|
| Initialisation | `MoEngage.Builder` → `initialiseDefaultInstance` | `MoEngageInitialiser` | `BrewBarApp.onCreate()` |
| Identity & user attributes | `MoEAnalyticsHelper`, `MoECoreHelper` | `MoEngageUser` | Login, Profile |
| Custom events | `MoEAnalyticsHelper.trackEvent` | `AppEvents` | every screen |
| Push permission & callbacks | `MoEPushHelper` | `MoEngagePush` | Push opt-in, Profile |
| In-app messages | `MoEInAppHelper.showInApp` | `MoEngageInApp` | Menu home, Item detail |
| Nudges | `MoEInAppHelper.showNudge` | `MoEngageInApp` | Orders |
| Self-handled in-app | `getSelfHandledInApp` + impression/click/dismiss | `MoEngageInApp` | Menu home promo card |
| Notification inbox | `MoEInboxHelper` | `MoEngageInbox` | Inbox |
| Cards (self-handled) | section loaded/unloaded, fetch, impression, click | `MoEngageCards` | Self-handled cards |
| Geofence campaigns | `MoEGeofenceHelper` | `MoEngageGeofence` | app open, Profile location card |
| Real-time triggers | `RttConfig(isBackgroundSyncEnabled = true)` | `MoEngageInitialiser` | evaluated on-device |
| Rich push / order tracking | `pct_payload` on a MoEngage push | `ordertracking/` | Order status |

Every SDK call goes through a shared `guarded { }` wrapper (`sdkhelper/MoEngageGuard.kt`), so an
unconfigured workspace ID leaves the app fully clickable instead of crashing on
`SdkNotInitializedException`.

---

## Firing each SDK moment

Every moment can be triggered two ways: by walking the flow, or from **DemoTools** — a hidden
bottom sheet reached by **long-pressing the app-bar title** (or the "Priya Sharma" greeting on
Menu home, or tapping the avatar on Profile). DemoTools exists so the app demos on any device
with no live campaign configured.

| Moment | Walk the flow | DemoTools |
|---|---|---|
| **SDK init** | Cold start — happens in `BrewBarApp.onCreate()`, before any Activity. The splash footnote says so. | — |
| **Identity** | Login → "Verify & continue" or "Continue with Google" | — |
| **Push permission** | Push opt-in screen → "Enable notifications" | *Request push permission* |
| **Native in-app** | First arrival on Menu home (once per session, ~900 ms in); also on every Item detail | *Show native in-app* |
| **Self-handled in-app** | Menu home fetches on arrival; the dark promo card renders the payload | *Render self-handled promo* |
| **Push notification** | Place an order with push granted — stage pushes drive Order status | — |
| **Nudge** | Orders (order history), on arrival | — |
| **Notification inbox** | Bell icon on Menu home → Inbox | — |
| **Cards** | Self-handled cards (bottom nav → *Cards*) | — |
| **Geofence** | Granted on app open (or from Profile → Location) starts monitoring | *Start geofence monitoring* |
| **Logout** | Profile → "Log out" | — |

### Notes on each

**Native in-app.** `MoEInAppHelper.showInApp(context)` is called from Menu home and Item
detail. In-app *context* is scoped on every navigation
(`MoEInAppHelper.setInAppContext(setOf(screen))`) so a Menu campaign cannot fire on Cart —
contexts are `splash, login, permission, menu, category, item, cart, payment, order_status,
profile, inbox, rewards, orders`.

**Nudges.** `MoEInAppHelper.showNudge(context, InAppPosition.ANY)` is called from Orders. A nudge
is anchored to a position rather than shown as a modal, and the SDK matches it against nudge
campaigns only — `ANY` lets the campaign's own configured position win.

**Self-handled in-app.** Menu home calls `getSelfHandledInApp`. The payload is expected to be:

```json
{
  "title": "Happy hour · 20% off cold brew",
  "subtitle": "2–5 pm today. Code CHILL20.",
  "code": "CHILL20",
  "deeplink": "brewbar://category/coffee"
}
```

Any missing field falls back to the copy above; `subtitle` is derived from `code` when only the
code is present. The app reports `selfHandledShown` on render, `selfHandledClicked` on tap and
`selfHandledDismissed` on ✕, so campaign stats stay correct. **If the campaign is paused the
card is not composed at all** — the Menu layout is unaffected, no empty slot.

**Notification inbox.** Screen 11 is backed by `MoEInboxHelper.fetchAllMessagesAsync`. Rows are
grouped Today / Earlier from the message's `sentTime`, unread rows get the 3px accent stripe,
read rows drop to 72% opacity, and a tap calls `trackMessageClicked` then routes via the
message's `NavigationAction`. Until a campaign has actually landed on the device the screen
shows a seeded list from `InboxRepository` so a demo is never staring at an empty screen.

**Push permission and geofence** are the two that need real care — see
[Permissions](#permissions).

---

## Permissions

Permissions are the part of an integration most likely to bite, so the app models both flows
honestly rather than hiding them behind a single switch.

### Push — `POST_NOTIFICATIONS`

On API 33+ this is a real runtime request via `ActivityResultContracts.RequestPermission`; the
result goes to `MoEPushHelper.pushPermissionResponse(context, granted)` and to the `push_opt_in`
user attribute. On API 24–32 there is no OS prompt, so the design's stand-in dialog is shown and
its answer is mirrored through the same path. Deny once and Profile switches the "Order updates"
row to **Blocked at OS level**, with a link that calls `MoEPushHelper.navigateToSettings`.

### Location — three separate grants

Profile's **Location** card breaks location out per grant, because they are separate permissions
with separate prompts, not levels of one:

| Row | Permission | How it can be granted |
|---|---|---|
| Approximate location | `ACCESS_COARSE_LOCATION` | OS prompt |
| Precise location | `ACCESS_FINE_LOCATION` | OS prompt |
| Allow all the time | `ACCESS_BACKGROUND_LOCATION` | Android 11+: **settings page only** |

Each row shows *Granted* / *Denied* / *Not requested*, plus the action that can still change it.
A fourth row reports whether geofence monitoring is actually **Active**.

The flow, in `SessionViewModel` and `BrewBarNavGraph`:

1. **Every app open** reads all three grants and requests the foreground pair if precise is missing.
2. A precise grant **chains** a separate background request where the OS still honours one.
3. **`ON_START`** re-reads the grants, so a grant made on the settings page takes effect without a
   restart.
4. Monitoring starts **the moment the grants qualify** — from a prompt, from settings, or already
   granted at launch.

> **The precondition that matters.** `MoEGeofenceHelper.startGeofenceMonitoring` requires
> `ACCESS_BACKGROUND_LOCATION` on Android 10+ (`ACCESS_FINE_LOCATION` below it) and throws
> `PermissionMissingError` **from its own worker thread** when it is missing — an uncatchable crash,
> not a `false` return, and not something `guarded { }` can intercept. `satisfiesGeofence()`
> mirrors that check so the call is never made unqualified. A foreground-only grant is not enough
> on a modern device.

---

## Event dictionary

Every event name and attribute key lives in `sdkhelper/AppEvents.kt` (`AppEvents.Events`,
`AppEvents.Attrs`) and `sdkhelper/MoEngageUser.kt` (`MoEngageUser.UserAttrs`).

| Event                 | Attributes                                                  | Fired from                                           |
|-----------------------|-------------------------------------------------------------|------------------------------------------------------|
| `Menu_Viewed`         | `store`, `category`                                         | Menu home, on arrival                                |
| `Category_Browsed`    | `category`                                                  | tab switch and Category list                         |
| `Item_Viewed`         | `item`, `price`, `category`                                 | Item detail, on arrival                              |
| `Add_To_Cart`         | `item`, `size`, `milk`, `addons`, `amount`                  | Item detail → "Add"                                  |
| `Cart_Viewed`         | `items_count`, `amount`                                     | Cart, on arrival                                     |
| `Checkout_Started`    | `amount`, `fulfilment`, `coupon`                            | Payment, on arrival                                  |
| `Order_Placed`        | `order_id`, `amount`, `mode`, `items_count`, `stars_earned` | Payment → "Pay"                                      |
| `Order_Picked_Up`     | `order_id`                                                  | Order status, on the final step                      |
| `Reorder_Tapped`      | `item`, `order_id`                                          | Orders → "Reorder"; Menu → "your usual"              |
| `Reward_Redeemed`     | `reward`, `stars_balance`                                   | Self-handled cards → "Redeem" / "Use"                |
| `Notification_Opened` | `campaign_id`, `deeplink`                                   | push tap and inbox tap (the SDK also tracks its own) |
| `InApp_Cta_Clicked`   | `campaign_id`, `cta`                                        | native in-app CTA and the self-handled promo card    |

Attribute value conventions: `category` is the tab label (`Coffee` / `Herbal teas` / `Food`);
`fulfilment` and `mode` are `Pickup` / `Delivery`; `addons` is a comma-joined list or `none`;
`coupon` is the code or `none`; amounts are integer rupees.

### User attributes

| Key                | Type    | Set from                                 |
|--------------------|---------|------------------------------------------|
| `favourite_drink`  | String  | Profile taste card, synced at login      |
| `milk_preference`  | String  | "                                        |
| `sweetness`        | String  | "                                        |
| `home_store`       | String  | "                                        |
| `loyalty_stars`    | Int     | login and after each order               |
| `push_opt_in`      | Boolean | permission result and the Profile toggle |
| `offers_opt_in`    | Boolean | Profile → "Offers & new menu"            |
| `marketing_opt_in` | Boolean | Profile → "Marketing campaigns"          |

### Standard identity calls

Fired together on successful login (`MoEngageSDKHelper.onLoginSucceeded`):

```kotlin
MoEAnalyticsHelper.identifyUser(context, userId)     // replaces the deprecated setUniqueId
MoEAnalyticsHelper.setMobileNumber(context, "+919845012345")
MoEAnalyticsHelper.setUserName(context, "Priya Sharma")
MoEAnalyticsHelper.setFirstName(...) / setLastName(...) / setEmailId(...)
MoEAnalyticsHelper.setBirthDate(context, "1994-03-14T00:00:00.000Z")
```

Log out calls `MoECoreHelper.logoutUser(context)` and resets the session (push state, in-app
state, cart) before returning to Login.

---

## Screens and deep links

Thirteen destinations in one `NavHost` (`ui/nav/Routes.kt`), with the bottom nav on Menu and
Profile:

`splash` → `login` → `permission` → `menu` → `category/{id}` → `item/{id}` → `cart` → `payment` →
`status/{orderId}`, plus `profile`, `inbox`, `rewards` (Self-handled cards), `orders`.

Every screen has an `@Preview` with seeded fake state — Profile has two (allowed / blocked at OS
level). No hex literal appears outside `ui/theme/Color.kt`.

Campaign key/value pairs and `brewbar://` links both resolve through `Routes.fromDeeplink`:

| Link                                                                        | Route                   |
|-----------------------------------------------------------------------------|-------------------------|
| `brewbar://order_status/BB-4821` (or `status/…`)                            | `status/{orderId}`      |
| `brewbar://category/coffee` \| `teas` \| `food`                             | `category/{categoryId}` |
| `brewbar://item/{itemId}`                                                   | `item/{itemId}`         |
| `brewbar://rewards` \| `orders` \| `inbox` \| `profile` \| `cart` \| `menu` | that screen             |

Test one without a campaign:

```bash
adb shell am start -a android.intent.action.VIEW -d "brewbar://status/BB-4821" com.moengage.sampleapp
```

> **`rewards` is a retained wire name.** The Self-handled cards screen was called Rewards, and its
> route token, deep link, in-app context and `Reward_Redeemed` event still say so — those are what
> campaigns are configured against in the dashboard. Only the app-facing name changed.

---

## Order tracking (Progress Centric Template)

Stage updates arrive as MoEngage pushes carrying a `pct_payload` (`BrewPushMessageListener`), and
`ordertracking/` renders them — Android 16 Live Updates via `ProgressStyle` where available, with
big-picture, big-text and standard fallbacks below it, plus a `specialUse` foreground service that
keeps the notification current between stage pushes.

Reference documents for the template and its payloads:

* [docs/pct/ORDER_TRACKING_CUSTOMER_GUIDE.md](docs/pct/ORDER_TRACKING_CUSTOMER_GUIDE.md)
* [docs/pct/templates/food-delivery-stage-payloads.md](docs/pct/templates/food-delivery-stage-payloads.md)

---

## Project layout

```
com.moengage.sampleapp
├── BrewBarApp.kt         Application — SDK init, push/in-app/geofence callbacks, channels
├── BrewActivity.kt       single Activity, NavHost host, notification-tap → route
├── sdkhelper/            the ONLY package that touches the MoEngage SDK
│   ├── MoEngageSDKHelper.kt   the index: every SDK call the app makes, one line each
│   ├── MoEngageInitialiser · MoEngageUser · AppEvents · MoEngagePush
│   ├── MoEngageInApp · MoEngageInbox · MoEngageCards · MoEngageGeofence
│   └── MoEngageGuard.kt       the "never crash the sample app" wrapper
├── ordertracking/        PCT payloads, renderers, Live Update service, channel
├── push/                 notification channels and the push message listener
├── domain/model/         MenuItem, CartLine, Order, LocationState, InboxMessageUi, …
├── data/                 in-memory catalogue and repositories
└── ui/
    ├── theme/            Color · Type · Shape · Dimens, from the design tokens
    ├── components/       buttons, chips, cards, bottom nav, progress, toggle, rows
    ├── nav/              Routes, SessionViewModel, BrewBarNavGraph
    ├── splash/ login/ permission/ menu/ order/ orders/ selfhandledcards/ inbox/ profile/
    └── inapp/            SelfHandledPromoCard, DemoToolsSheet
```

### Notification channels

`BrewNotifications.ensureChannels` creates **Order updates** (`order_updates`, high importance)
and **Offers & new menu** (`offers_new_menu`, default importance) before the SDK initialises.
Point your campaigns at those channel ids.

### Formatting

ktlint via Spotless:

```bash
./gradlew spotlessCheck      # verify
./gradlew spotlessApply      # fix
```

---

## Decisions worth knowing

- **Dark mode is deferred.** The handoff flags it as pending design-owner sign-off, so the app
  commits to the single light palette rather than inventing dark tokens.
- **Placeholder art.** `res/drawable-nodpi/cs_*.png` are the prototype's generated images.
  Replace with real photography before any external demo, keeping the 3:2 crop for cards and
  16:9-ish for heroes. The bundle's `cs-detail-hero.png` and `cs-inapp.png` are not shipped:
  Item detail uses the item's own art (so the hero matches the card you tapped), and the
  native in-app modal is drawn by the SDK from the campaign, not by the app.

---

## Test checklist

- [ ] Fresh install → splash → login → permission → allow → menu; native in-app requested once.
- [ ] Deny permission → Profile shows "Blocked at OS level"; the link opens system settings.
- [ ] Allow location on app open → Profile's Location rows read Granted; monitoring goes Active.
- [ ] Choose "Approximate" only → precise row reads Denied and monitoring stays Inactive.
- [ ] Android 11+: grant "Allow all the time" in settings, return to the app → monitoring goes
      Active without a restart.
- [ ] Place an order → push arrives on Order status → tap → deep-links back to the order.
- [ ] Self-handled campaign paused → promo card absent, Menu layout unaffected.
- [ ] Bell badge → Inbox → tap a row → routes and the row goes read.
- [ ] All four bottom-nav destinations reachable from Menu and Profile.
- [ ] Log out → Login, with push and in-app state reset.

---

## Repository layout

| Path        | Description                                                    |
|-------------|----------------------------------------------------------------|
| `app/`      | Brew Bar — the Compose sample app                              |
| `docs/pct/` | Order Tracking (Progress Centric Template) reference documents |
