![Logo](/.github/assets/logo.png)

## MoEngage Android SDK Integration Sample

![MavenBadge](http://maven-badges.herokuapp.com/maven-central/com.moengage/moe-android-sdk/badge.svg)

To get up and running with MoEngage on Android, there a couple of steps we will walk you through.

#### Adding MoEngage Dependency:

Along with the segment dependency add the below dependency in your build.gradle file.

```groovy
 implementation 'com.moengage:moe-android-sdk:$sdkVersion'
```
`sdkVersion` - is the latest version of the MoEngage SDK.

Once you have installed the SDK follow the below documentation to integrate the SDK

* [Initialise the SDK](https://docs.moengage.com/docs/android-sdk-initialization)

* [Install and Update Differentiation](https://docs.moengage.com/docs/android-install-update)

* [User Attribute Tracking](https://docs.moengage.com/docs/identifying-user)

* [Event Tracking](https://docs.moengage.com/docs/android-track-event)

* [Push Configuration](https://docs.moengage.com/docs/push-configuration)

* [Geo-fence Configuration](https://docs.moengage.com/docs/android-geofence)

* [Rich Landing](https://docs.moengage.com/docs/adding-rich-landing)
 
* [In-App messaging](https://docs.moengage.com/docs/android-in-app-nativ)
 
* [Notification Center](https://docs.moengage.com/docs/android-notification-center)
 
* [Advanced Configuration](https://docs.moengage.com/docs/android-advanced-integration)
 
* [API Reference](https://moengage.github.io/android-api-reference/index.html)
 
* [GDPR Compliance](https://docs.moengage.com/docs/android-compliance)
 
 
 **Note:** This sample application uses Timber for logging purposes. MoEngage SDK is not 
 dependent on this library. You need not add this library while integrating the SDK.
 
## Sample App Usage
 
* Add your APP-ID in the Application class
* Replace the dummy `google-services.json` file with your actual file.
* Add the `agconnect-services.json` to the project for using HMS Push Kit.
* Add the `App-id` and `App-Key` from Mi Console to use Xiaomi Push.

 ## Variations

 |       Sample      |                                                                          Description                                                                          |
|:-----------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|       [master](https://github.com/moengage/Android-Sample/tree/master)      | Integration Sample where MoEngage SDK handles push token registration and push display.                                                                       |
| [app_handling_push](https://github.com/moengage/Android-Sample/tree/app_handling_push) | Integration Sample where client app handles the push token registration, push display and passes a callback to the SDK for notification received and clicked. |