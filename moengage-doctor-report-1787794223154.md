## Report generation details

- **Platform:** android
- **Project path:** `/Users/arshiyakhanum/Documents/dev/Android-Sample`
- **Generated at (UTC):** 2026-08-27T01:30:23.154Z

# MoEngage Android Integration Doctor Report

- **Project path:** `/Users/arshiyakhanum/Documents/dev/Android-Sample`
- **Result:** BLOCKING
- **Findings:** 3 error, 1 warning, 1 info

## Detected

| Label | Value |
| --- | --- |
| moe-android-sdk | 15.02.00 (via BOM 4.3.0) |
| Java language level | Java 17 across 3 declaration(s) |
| MoEngage modules resolved | cards-core, inapp, inbox-core, moe-android-sdk, rich-notification |
| Files scanned | 3 gradle |
| BOM in use | yes — com.moengage:android-bom:4.3.0 |
| Initialisation call sites found | com.moengage.sampleapp.sdkhelper.MoEngageInitialiser.initialise() |
| Activities scanned | 1 — 0 reach showInApp() from their own onStart(), 0 inherit it, 1 unresolved |
| Activities missing showInApp() | com.moengage.sampleapp.MainActivity (no call found) |

## Toolchain — Java / Kotlin language level

| Location | Kind | Declared as | Version |
| --- | --- | --- | --- |
| app/build.gradle.kts:58 | sourceCompatibility | `JavaVersion.VERSION_17` | 17 |
| app/build.gradle.kts:59 | targetCompatibility | `JavaVersion.VERSION_17` | 17 |
| app/build.gradle.kts:74 | jvmTarget | `JvmTarget.JVM_17` | 17 |

## Checks

### Installation

| Status | Title | Detail |
| --- | --- | --- |
| PASS | MoEngage SDK declared | BOM 4.3.0 + 5 module(s) |
| PASS | BOM manages module versions | modules resolve through the BOM — no manual version alignment needed |
| PASS | BOM up to date | 4.3.0 is the latest published release |
| PASS | Module versions compatible | 5 module(s) resolve through BOM 4.3.0 |
| WARN | AndroidX prerequisites | 3 of 3 declared, 2 differ from the version MoEngage compiles against — tracked versions from catalog-v9.2.0 |
| FAIL | Auto-backup excludes MoEngage storage | allowBackup="true" and no fullBackupContent/dataExtractionRules — restored backups can duplicate device IDs |
| PASS | Java 8+ compatibility | Java 17 (3 declaration(s)) |

### Initialisation

| Status | Title | Detail |
| --- | --- | --- |
| WARN | MoEngage SDK Initialised | 1 of 1 initialisation call site(s) are not reachable from, or not properly built at, the Application class's onCreate(). |
| PASS | Notification permission declared and requested | POST_NOTIFICATIONS declared in the manifest and requested at runtime |
| FAIL | google-services.json present | push messaging is in use but google-services.json is missing |
| PASS | MoEFireBaseMessagingService declared | MoEFireBaseMessagingService declared in the manifest |
| SKIP | Push token passed to the SDK | MoEngage's own service receives messages — the app doesn't need to pass the token |
| SKIP | Push payload passed to the SDK | MoEngage's own service receives messages — the app doesn't need to pass the payload |

### In-App

| Status | Title | Detail |
| --- | --- | --- |
| FAIL | showInApp() reached from every activity's onStart() | 1 of 1 activities never reach MoEInAppHelper.getInstance().showInApp() in onStart(): MainActivity |
| SKIP | showInApp() reached from every fragment's onResume() | No fragment classes found in the scanned Kotlin/Java sources. |

### Geofence

| Status | Title | Detail |
| --- | --- | --- |
| SKIP | Location permission requested | com.moengage:geofence not resolved — skipping geofence checks |
| SKIP | Geofence monitoring started | com.moengage:geofence not resolved — skipping geofence checks |

## Dependencies

### MoEngage

| Status | Coordinate | Declared | Expected | Note | Source |
| --- | --- | --- | --- | --- | --- |
| OK | `com.moengage:moe-android-sdk` | 15.02.00 | 15.02.00 | resolves to BOM 4.3.0's version | — |
| OK | `com.moengage:cards-core` | 4.0.1 | 4.0.1 | resolves to BOM 4.3.0's version | — |
| OK | `com.moengage:inapp` | 11.2.1 | 11.2.1 | resolves to BOM 4.3.0's version | — |
| OK | `com.moengage:inbox-core` | 5.0.0 | 5.0.0 | resolves to BOM 4.3.0's version | — |
| OK | `com.moengage:rich-notification` | 7.1.0 | 7.1.0 | resolves to BOM 4.3.0's version | — |

### AndroidX

| Status | Coordinate | Declared | Expected | Note | Source |
| --- | --- | --- | --- | --- | --- |
| VERSION MISMATCH | `androidx.core:core` | 1.17.0 | >= 1.16.0 | differs from the 1.16.0 MoEngage compiles against — review | — |
| OK | `androidx.appcompat:appcompat` | 1.7.1 | >= 1.7.1 | matches the version MoEngage compiles against | — |
| VERSION MISMATCH | `androidx.lifecycle:lifecycle-process` | 2.9.4 | >= 2.10.0 | differs from the 2.10.0 MoEngage compiles against — review | — |

## Issues

### Installation

#### [ERROR] Auto-backup is enabled, but no backup rules defined for excluding MoEngage storage from back-up. Not exlcuding MoEngage storage can result in data corruption.

**Recommendation:** Exclude MoEngage storage via fullBackupContent / dataExtractionRules. See https://www.moengage.com/docs/developer-guide/android-sdk.

**Evidence:**
- `app/build/intermediates/merged_manifests/release/processReleaseManifest/AndroidManifest.xml:31`
  ```
  android:allowBackup="true"
  ```

#### [INFO] 2 of 3 required AndroidX libraries don't match the version MoEngage compiles against — tracked versions from catalog-v9.2.0.

**Recommendation:** Align the listed AndroidX libraries with the version MoEngage compiles against.

### Initialisation

#### [ERROR] Push messaging is in use but google-services.json is missing — FCM cannot deliver push to this app.

**Recommendation:** Download google-services.json from the Firebase console and apply the com.google.gms.google-services plugin. Refer to the Firebase setup documentation for more details.

**Doc:** https://www.moengage.com/docs/developer-guide/android-sdk

#### [WARNING] com.moengage.sampleapp.sdkhelper.MoEngageInitialiser.initialise() runs in onCreate() of the Application class, but the Builder's Workspace ID looks missing or is still a placeholder; configureNotificationMetaData(NotificationConfig(...)) has no argument.

**Recommendation:** Fix the MoEngage.Builder(context, "WORKSPACE_ID", DataCenter.X)...build() construction in the same method — check the language syntax, that it chains into .build(), and that the Workspace ID is real. See https://www.moengage.com/docs/developer-guide/android-sdk.

**Evidence:**
- `app/src/main/java/com/moengage/sampleapp/sdkhelper/MoEngageInitialiser.kt:50`
  ```
  MoEngage.initialiseDefaultInstance(config)
  ```

**Doc:** https://www.moengage.com/docs/developer-guide/android-sdk

### In-App

#### [ERROR] com.moengage.sampleapp.MainActivity never reaches MoEInAppHelper.getInstance().showInApp() in onStart() — it declares no base class inside this project.

**Recommendation:** Call MoEInAppHelper.getInstance().showInApp() from onStart() in MainActivity, or once in a base activity that every screen extends. Without it the SDK is never asked to display a campaign and in-apps stay invisible even when everything else is configured correctly.

**Evidence:**
- `app/src/main/java/com/moengage/sampleapp/MainActivity.kt:27`
  ```
  class MainActivity : ComponentActivity()
  ```

**Doc:** https://www.moengage.com/docs/developer-guide/android-sdk

## Verdict

**BLOCKING** — 3 error(s) must be fixed.

_Static analysis only — confirm push/events/geofence on a real device._
