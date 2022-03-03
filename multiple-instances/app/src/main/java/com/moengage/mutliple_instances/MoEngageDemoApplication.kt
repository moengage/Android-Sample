package com.moengage.mutliple_instances

import android.app.Application
import com.moengage.core.MoECoreHelper
import com.moengage.core.MoEngage
import com.moengage.core.config.*
import com.moengage.core.ktx.MoEngageBuilderKtx
import com.moengage.example.callbacks.ApplicationBackgroundListener
import com.moengage.example.callbacks.LogoutCompleteListener
import com.moengage.micro_app.inapp.ClickActionCallback
import com.moengage.micro_app.inapp.InAppLifecycleCallbacks
import com.moengage.micro_app.inapp.SelfHandledCallback
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.inapp.MoEInAppHelper
import com.moengage.micro_app.MicroAppInitializer
import com.moengage.micro_app.push.CustomPushMessageListener
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
                appId = "",
                notificationConfig = NotificationConfig(
                    smallIcon = R.drawable.small_icon,
                    largeIcon = R.drawable.large_icon,
                    notificationColor = R.color.notification_color,
                    isMultipleNotificationInDrawerEnabled = true,
                    isBuildingBackStackEnabled = NOTIFICATION_CONFIG_DEFAULT_BACK_STACK_STATE,
                    isLargeIconDisplayEnabled = NOTIFICATION_CONFIG_DEFAULT_LARGE_ICON_STATE
                ),
                fcmConfig = FcmConfig(true),
                miConfig = MiPushConfig("", "", true),
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
        MicroAppInitializer.initialise(this)
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