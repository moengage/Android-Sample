package com.moengage.example

import android.app.Application
import com.moengage.core.DataCenter
import com.moengage.core.MoECoreHelper
import com.moengage.core.MoEngage
import com.moengage.core.config.*
import com.moengage.core.ktx.MoEngageBuilderKtx
import com.moengage.example.callbacks.ApplicationBackgroundListener
import com.moengage.example.callbacks.LogoutCompleteListener
import com.moengage.example.inapp.ClickActionCallback
import com.moengage.example.inapp.InAppLifecycleCallbacks
import com.moengage.example.inapp.SelfHandledCallback
import com.moengage.example.push.CustomPushMessageListener
import com.moengage.example.push.GeofenceHitListener
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.geofence.MoEGeofenceHelper
import com.moengage.inapp.MoEInAppHelper
import com.moengage.pushbase.MoEPushHelper
import logcat.logcat

/**
 * @author Umang Chamaria
 * Date: 2022/02/03
 */
class MoEngageDemoApplication: Application() {

    private val tag = "MoEngageDemoApplication"

    override fun onCreate() {
        super.onCreate()
        MoEngage.initialiseDefaultInstance(
            MoEngageBuilderKtx(
                application = this,
                appId = "YOUR_APP_ID",
                dataCenter = DataCenter.DATA_CENTER_1,
                notificationConfig = NotificationConfig(
                    smallIcon = R.drawable.small_icon,
                    largeIcon = R.drawable.large_icon,
                    notificationColor = R.color.notification_color,
                    isMultipleNotificationInDrawerEnabled = true,
                    isBuildingBackStackEnabled = true,
                    isLargeIconDisplayEnabled = true
                ),
                fcmConfig = FcmConfig(true),
                pushKitConfig = PushKitConfig(true)
            ).build()
        )
        // register for application background listener
        MoECoreHelper.addAppBackgroundListener(ApplicationBackgroundListener())
        // register for logout complete listener
        MoECoreHelper.addLogoutCompleteListener(LogoutCompleteListener())
        setupPushCallbacks()
        setupInAppCallbacks()

        // Register Geofence Hit Listener
        MoEGeofenceHelper.getInstance().addListener(GeofenceHitListener())

        // Enables geofence monitoring, required Only for Location-Triggered campaigns
        MoEGeofenceHelper.getInstance().startGeofenceMonitoring(this)
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