# MoEngageSDK MultiInstance Example

## UseCase Based on integrating MoEngage SDK within Another SDK

Let use consider an UseCase of E-Commerce App.

The Ecommerce App have integrated PaymentGateway SDK for handling Payments. Ecommerce App might have
already integrated MoEngage SDK.
Here PaymentGateway SDK also need to integrate MoEngage SDK for engaging users. So PaymentGateway
SDK needs to initialize MoEngage SDK as secondary instance.

Supported Features

- Event Tracking
- InApps
- InApp Nudges
- Cards

### Push Notification(Not Recommended)

Push Notification is not covered in this sample app.
If PaymentGateway needs to send push notification to all the users across all the apps that have
integrated Payment SDK.
This case for Sending Push notification is not straight forward.
Since PaymentGateway is an SDK , it cannot have its own google-service.json . Also ECommerce might
have integrated Firebase SDK already.
So PaymentGateway SDK needs to initialize FirebaseApp secondary app with Firebase SDK. Also each app
integrating the PaymentGateway SDK
should be added in FCM project of PaymentGateway (This is subjected to be verified with Google
Policies). Then for receiving push , single App cannot have two FCM Service class. So the alternate
way is to use
> com.google.android.c2dm.intent.RECEIVE

BroadCast Receiver and handle showing notification in the receiver / pass the payload to MoEngage
SDK.

### MoEngage SDK Initialization Notes

Since MoEngage SDK initialization is recommended in Application Class of the App.
So Payment SDK is also recommended to have an initialize API, in which MoEngage SDK should be
initialize. So the apps integrating MPay Sdk should be initialized in Application class of the App.

## Using MoEngage SDK APIs

MoEngage SDK supports secondary instance out of the box. Apps are recommended to use API with
Default MoEngage Instance.
If another SDK is using MoEngage SDK, it is recommended to use APIs with APP_ID as additional
Params. So all the event tracking, callbacks , campaigns etc.. will be coupled with the <MOE_APP_ID>
.

### Initializing PaymentSDK

The Sample Payment SDK can be initialized in Application class like below.  
This internally initializes MoEngageSDK as secondary instance.

```
class MoEngageDemoApplication: Application() {
    override fun onCreate() {
    super.onCreate()
    PaymentSDK.initialize(this,<PAYMENT_SDK_APP_ID>)
   }
}
```

The checkout page can be launched. User Data and Payment Data should be passed.
This data can be used for event tracking.

```
 PaymentSDK.checkoutPayment(context, userData = userData, paymentData = paymentData)
```