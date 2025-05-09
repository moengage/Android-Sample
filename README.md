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
* Replace the dummy `google-services.json` file with your actual file.
* Add the `agconnect-services.json` to the project for using HMS Push Kit.

### ComposeSampleApp Usage

* Add your WORKSPACE-ID in the `MoEngageInitializer`
* Add your NEWS_API_KEY_ in the `NetworkConstants`
* Replace singing configuration with your signing key in app level `build.gradle.kts` file
* Add your Web Client Id for google cloud for google signing in `NetworkConstants`
* Replace the dummy `google-services.json` file with your actual file.
