### MoEngage SDK Multi-Instance Example

## UseCase Based on integrating MoEngage SDK within Another SDK

Let us consider the use case of an e-commerce app.
The e-commerce app has integrated a PaymentGateway SDK for handling payments. The e-commerce app
might have already integrated the MoEngage SDK.
If the PaymentGateway SDK also needs to integrate the MoEngage SDK for engaging users, the
PaymentGateway SDK needs to initialize the MoEngage SDK as a secondary instance.

Supported Features

- Event Tracking
- InApps
- InApp Nudges
- Cards

### Push Notification (Not Recommended)

Push notifications are not covered in this sample app.
Let us consider a use case for push notifications for PaymentGateway SDK.
PaymentGateway needs to send push notifications to all the users across all the apps that have
integrated the payment SDK.
This case for sending push notifications is not straightforward.
Since PaymentGateway is an SDK, it cannot have its own google-service.json . Also, the e-commerce
app might have integrated the Firebase SDK already.
So PaymentGateway SDK needs to initialize the FirebaseApp secondary app with Firebase SDK. Also,
each app integrating the PaymentGateway SDK
should be added to the FCM project of PaymentGateway (This is subject to be verified with Google
Policies). Then, for receiving push, an app cannot have two FCM service classes. So the alternate
way is to use the below Broadcast Receiver and handle showing notification in the receiver / pass
the payload to MoEngage SDK.

> com.google.android.c2dm.intent.RECEIVE

This approach is not recommended if the app integrating payment SDK also use the above Broadcast
receiver.

### MoEngage SDK Initialization Notes

Since MoEngage SDK initialization is recommended in the Application class of the app,
it is also recommended for the Payment SDK to have an initialization API, in which MoEngage SDK
should be initialized. The apps that integrate the Payment SDK should initialize the Payment SDK in
the Application class of the app.

## Using MoEngage SDK APIs

MoEngage SDK supports secondary instances out of the box.

Apps are recommended to use APIs with the default MoEngage instance.
If another SDK is using the MoEngage SDK, it is recommended to use APIs with APP_ID as additional
Params. So all the event tracking, callbacks, campaigns, etc… will be coupled with the <MOE_APP_ID>

### Initializing PaymentSDK

The sample Payment SDK can be initialized in the Application class like below.
This internally initializes MoEngage SDK as a secondary instance.

```
class MoEngageDemoApplication: Application() { 
   override fun onCreate() { 
    super.onCreate() 
    PaymentSDK.initialize(this,<PAYMENT_SDK_APP_ID>) 
   }
}
```

For demo purposes, the payment checkout page can be launched by passing User data and Payment data,
like below.
This data can be used for event tracking with MoEngage SDK.

```
PaymentSDK.checkoutPayment(context, userData = userData, paymentData = paymentData)
```