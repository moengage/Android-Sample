package com.moengage.micro_app

import android.app.Application
import com.moengage.core.MoECoreHelper
import com.moengage.core.MoEngage
import com.moengage.core.config.*
import com.moengage.core.ktx.MoEngageBuilderKtx
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.inapp.MoEInAppHelper
import com.moengage.micro_app.callbacks.ApplicationBackgroundListener
import com.moengage.micro_app.callbacks.LogoutCompleteListener
import com.moengage.micro_app.inapp.ClickActionCallback
import com.moengage.micro_app.inapp.InAppLifecycleCallbacks
import com.moengage.micro_app.inapp.SelfHandledCallback
import com.moengage.micro_app.push.CustomPushMessageListener
import com.moengage.pushbase.MoEPushHelper
import logcat.logcat

/**
 * @author Umang Chamaria
 * Date: 2022/02/21
 */
object MicroAppInitializer {

    fun initialise(application: Application) {
        // micro app's initialisation
        initializeMoEngage(application)
    }

    private fun initializeMoEngage(application: Application) {
        MoEngage.initialiseInstance(
            MoEngageBuilderKtx(
                application = application,
                appId = MOENGAGE_APP_ID,
                fcmConfig = FcmConfig(false),
            ).build()
        )
        // register for application background listener
        MoECoreHelper.addAppBackgroundListener(ApplicationBackgroundListener(), MOENGAGE_APP_ID)
        // register for logout complete listener
        MoECoreHelper.addLogoutCompleteListener(LogoutCompleteListener(), MOENGAGE_APP_ID)
        setupInAppCallbacks()
        setupPushCallbacks()
    }

    private fun setupPushCallbacks() {
        // callback for notification events and notification customisation point.
        MoEPushHelper.getInstance().registerMessageListener(CustomPushMessageListener(), MOENGAGE_APP_ID)
        // Callback for Firebase Token
        MoEFireBaseHelper.getInstance().addTokenListener { token ->
            logcat { " fcm token: ${token.pushToken}" }
        }
    }

    private fun setupInAppCallbacks() {
        // callback for in-app campaign click
        MoEInAppHelper.getInstance().setClickActionListener(MOENGAGE_APP_ID, ClickActionCallback())
        // callback for in-app lifecycle - campaign shown/dismissed.
        MoEInAppHelper.getInstance().addInAppLifeCycleListener(MOENGAGE_APP_ID, InAppLifecycleCallbacks())
        // callback for self handled campaigns that are triggered based on events.
        MoEInAppHelper.getInstance().setSelfHandledListener(MOENGAGE_APP_ID, SelfHandledCallback())
    }
}