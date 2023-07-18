package com.moengage.example

import android.app.Application
import com.moengage.core.MoECoreHelper
import com.moengage.core.MoEngage
import com.moengage.core.config.FcmConfig
import com.moengage.core.config.GeofenceConfig
import com.moengage.core.config.NotificationConfig
import com.moengage.core.config.PushKitConfig
import com.moengage.core.isSdkInitialised
import com.moengage.core.ktx.MoEngageBuilderKtx
import com.moengage.example.callbacks.ApplicationBackgroundListener
import com.moengage.example.callbacks.LogoutCompleteListener
import com.moengage.example.inapp.ClickActionCallback
import com.moengage.example.inapp.InAppLifecycleCallbacks
import com.moengage.example.inapp.SelfHandledCallback
import com.moengage.example.push.CustomPushMessageListener
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.inapp.MoEInAppHelper
import com.moengage.pushbase.MoEPushHelper
import logcat.logcat

class MoEngageHandler {

    fun initialise(application: Application) {
        MoEngage.initialiseDefaultInstance(
            MoEngageBuilderKtx(
                application = application,
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
                geofenceConfig = GeofenceConfig(true)
            ).build()
        )
        // register for application background listener
        MoECoreHelper.addAppBackgroundListener(ApplicationBackgroundListener())
        // register for logout complete listener
        MoECoreHelper.addLogoutCompleteListener(LogoutCompleteListener())
        setupPushCallbacks()
        setupInAppCallbacks()
    }

    fun initialiseIfRequired() {
        if (isSdkInitialised()) return
        initialise(MoEngageDemoApplication.getApplication())
    }

    private fun setupPushCallbacks() {
        // callback for notification events and notification customisation point.
        MoEPushHelper.getInstance().registerMessageListener(CustomPushMessageListener())
        // Callback for Firebase Token
        MoEFireBaseHelper.getInstance().addTokenListener { token ->
            logcat { " fcm token: ${token.pushToken}" }
        }
    }

    private fun setupInAppCallbacks() {
        // callback for in-app campaign click
        MoEInAppHelper.getInstance().setClickActionListener(ClickActionCallback())
        // callback for in-app lifecycle - campaign shown/dismissed.
        MoEInAppHelper.getInstance().addInAppLifeCycleListener(InAppLifecycleCallbacks())
        // callback for self handled campaigns that are triggered based on events.
        MoEInAppHelper.getInstance().setSelfHandledListener(SelfHandledCallback())
    }
}