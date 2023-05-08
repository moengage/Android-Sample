### MoEngage SDK Integration Example

Supported Features in App Module

- Data Tracking
- InApps
- InApp Nudges
- Cards
- Push Notifications with Inbox

The app module should use MoEngage.initialiseDefaultInstance to initialize MoEngage SDK like below
as initialiseDefaultInstance.

```
        MoEngage.initialiseDefaultInstance(
            MoEngageBuilderKtx(
                application = this,
                appId = "YOUR_APP_ID",
                notificationConfig = NotificationConfig(
                    smallIcon = R.drawable.small_icon,
                    largeIcon = R.drawable.large_icon,
                    notificationColor = R.color.notification_color,
                    isMultipleNotificationInDrawerEnabled = true,
                    isBuildingBackStackEnabled = true,
                    isLargeIconDisplayEnabled = true
                ),
                fcmConfig = FcmConfig(true),
                pushKitConfig = PushKitConfig(true),
                cardConfig = CardConfig.defaultConfig()
            ).build()
        )
```

### MoEngage SDK Multi-Instance Use Case

Let us consider the use case of an e-commerce app.
The e-commerce app has integrated a PaymentGateway SDK for handling payments. The e-commerce app
might have already integrated the MoEngage SDK.
If the PaymentGateway SDK also needs to integrate the MoEngage SDK,the
PaymentGateway SDK needs to initialize the MoEngage SDK as a secondary instance.

Supported Features in Library Module

- Event Tracking

Since Payment SDK is a library module , it should use MoEngage.initialiseInstance to initialize
MoEngage SDK as a secondary instance.

```
        MoEngage.initialiseInstance(
            MoEngageBuilderKtx(
                application = application,
                appId = MOE_APP_ID,
                dataCenter = DataCenter.DATA_CENTER_2,
                logConfig = LogConfig(level = LogLevel.VERBOSE),
            ).build()
        )
```

### MoEngage SDK Initialization Notes

Since MoEngage SDK initialization is recommended in the Application class of the app,
it is also recommended for the Payment SDK to have an initialization API, in which MoEngage SDK
should be initialized. The apps that integrate the Payment SDK should initialize the Payment SDK in
the Application class of the app.

### Initializing PaymentSDK

The sample Payment SDK can be initialized in the Application class like below.
This internally initializes MoEngage SDK as a secondary instance.

```
class MoEngageDemoApplication: Application() { 
   override fun onCreate() { 
    super.onCreate()
        
    //Initialize API of Payment SDK - For Demo Purposes.
    PaymentSDK.initialize(this,<PAYMENT_SDK_APP_ID>) 
    
    //App Module's MoEngage Intialization
    MoEngage.initialiseDefaultInstance(
            MoEngageBuilderKtx(
                application = this,
                appId = "YOUR_APP_ID",
                notificationConfig = NotificationConfig(
                    smallIcon = R.drawable.small_icon,
                    largeIcon = R.drawable.large_icon,
                    notificationColor = R.color.notification_color,
                    isMultipleNotificationInDrawerEnabled = true,
                    isBuildingBackStackEnabled = true,
                    isLargeIconDisplayEnabled = true
                ),
                fcmConfig = FcmConfig(true),
                pushKitConfig = PushKitConfig(true),
                cardConfig = CardConfig.defaultConfig()
            ).build()
        )
   }
}
```

For demo purposes, the payment checkout page can be launched by passing User data and Payment data,
like below. This data can be used for event tracking with MoEngage SDK.

```
PaymentSDK.checkoutPayment(context, userData = userData, paymentData = paymentData)
```