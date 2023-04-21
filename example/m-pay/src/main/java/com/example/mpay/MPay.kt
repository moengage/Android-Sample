package com.example.mpay

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.moengage.core.DataCenter
import com.moengage.core.LogLevel
import com.moengage.core.MoEngage
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.config.*
import com.moengage.core.ktx.MoEngageBuilderKtx
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper


/**
 * For example, here MPay is a payment gateway SDK. This for demo Purposes for integrating MoEngage SDK in Another SDK
 * */
object MPay {

    const val MOE_APP_ID = "<MOE_APP_ID>"

    const val MOE_APP_ID_KEY = "moe_app_id"

    /**
     * This is Unique ID for customers who are integrating MPay SDK.
     * */
    var MPAY_APP_ID = ""

    const val TAG = "M_Pay"


    fun initialize(application: Application) {
        MoEngage.initialiseInstance(
            MoEngageBuilderKtx(
                application = application,
                appId = MOE_APP_ID,
                dataCenter = DataCenter.DATA_CENTER_2,
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
                inAppConfig = InAppConfig(setOf()),
                logConfig = LogConfig(level = LogLevel.VERBOSE),
                rttConfig = RttConfig(isBackgroundSyncEnabled = true),
                cardConfig = CardConfig.defaultConfig()
            ).build()
        )
        trackEvent(application)
        setUpPushCallbackListeners()
    }

    private fun trackEvent(context: Context) {
        val properties = Properties()
        properties.addAttribute("mPayAppId", MPAY_APP_ID)
        MoEAnalyticsHelper.trackEvent(context, "mPayInit", properties, appId = MOE_APP_ID)
    }


    fun checkoutMPay(context: Context, userData: UserData, paymentData: PaymentData) {
        val intent = Intent(context, MPayCheckoutActivity::class.java)
        intent.putExtra("userData", userData)
        intent.putExtra("paymentData", paymentData)
        context.startActivity(intent)
    }

    private fun setUpPushCallbackListeners() {
        MoEPushHelper.getInstance().registerMessageListener(MPayPushMessageListener(), MOE_APP_ID)
        MoEFireBaseHelper.getInstance().addTokenListener { token ->
            Log.d(TAG, " fcm token: ${token.pushToken}")
        }
    }
}