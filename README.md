![Logo](/.github/assets/logo.png)

# MoEngage Android SDK Integration Sample

This repository contains the sample integrating for [MoEngage](https://www.moengage.com)

![MavenBadge](https://img.shields.io/maven-central/v/com.moengage/moe-android-sdk)

## Repository Description

| Folder             | Description                          |
|--------------------|--------------------------------------|
| example            | Sample Project with xml based layout |
| ComposeSampleApp   | Sample Project with Compose          |

### Example App Usage

* Add your APP-ID in the Application class
* For FCM push testing: add `example/app/google-services.json` and uncomment the Google Services plugin in `example/app/build.gradle.kts` (see comment at bottom of file)
* Add the `agconnect-services.json` to the project for using HMS Push Kit.

### Order Tracking (PCT) sample

Food-delivery **Progress Centric Template** demo using MoEngage **self-handled** Background Update pushes.

* **Customer guide:** [docs/pct/ORDER_TRACKING_CUSTOMER_GUIDE.md](docs/pct/ORDER_TRACKING_CUSTOMER_GUIDE.md)
* **Stage payloads:** [docs/pct/templates/food-delivery-stage-payloads.md](docs/pct/templates/food-delivery-stage-payloads.md)
* **Code:** `example/app/.../ordertracking/`
* Configure your MoEngage App ID, environment, data center, and `google-services.json` (plus uncomment the plugin in `build.gradle.kts`) before push testing.

### ComposeSampleApp Usage

* Add your WORKSPACE-ID in the `MoEngageInitializer`
* Add your NEWS_API_KEY_ in the `NetworkConstants`
* Replace singing configuration with your signing key in app level `build.gradle.kts` file
* Add your Web Client Id for google cloud for google signing in `NetworkConstants`
* Replace the dummy `google-services.json` file with your actual file.
