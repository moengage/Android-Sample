package com.moengage.sample.payment.sdk

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
import com.moengage.inapp.MoEInAppHelper
import com.moengage.pushbase.MoEPushHelper


/**
 * For example, This is Payment Gateway SDK. This for demo Purposes for integrating MoEngage SDK in Another SDK
 * */
object PaymentSDK {

    /**
     * MoEngage APP Identifier
     * */
    const val MOE_APP_ID = "<MOE_APP_ID>"


    /**
     * This is Unique ID for customers who are integrating Payment SDK. (For Demo Purposes)
     * */
    var paymentSDKAppId = ""

    const val TAG = "PaymentSDK"

    /**
     * @param application - Application Instance of the App
     * @param paymentSDKAppId - APP Id for Apps integrating Payment SDK(For Demo Purposes)
     * */
    fun initialize(application: Application, paymentSDKAppId: String) {
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
        this.paymentSDKAppId = paymentSDKAppId
        trackEvent(application)
        setUpPushCallbackListeners()
        setInAppListener()
    }

    /**
     * Track Event when SDK is initialized
     * */
    private fun trackEvent(context: Context) {
        val properties = Properties()
        properties.addAttribute("paymentSDKAppId", paymentSDKAppId)
        MoEAnalyticsHelper.trackEvent(context, "paymentSDKInit", properties, appId = MOE_APP_ID)
    }


    fun checkoutPayment(context: Context, userData: UserData, paymentData: PaymentData) {
        val intent = Intent(context, CheckoutActivity::class.java)
        intent.putExtra(INTENT_KEY_USER_DATA, userData)
        intent.putExtra(INTENT_KEY_PAYMENT_DATA, paymentData)
        context.startActivity(intent)
    }

    /**
     * Showing InApp From Payment SDK. This should be handled based on the activity
     * */
    fun showInApp(context: Context) {
        MoEInAppHelper.getInstance().showInApp(context, MOE_APP_ID)
        MoEInAppHelper.getInstance().getSelfHandledInApp(context, MOE_APP_ID) {
            Log.d(TAG, it.toString())
        }
    }


    private fun setUpPushCallbackListeners() {
        MoEPushHelper.getInstance()
            .registerMessageListener(PaymentSDKPushMessageListener(), MOE_APP_ID)
        MoEFireBaseHelper.getInstance().addTokenListener { token ->
            Log.d(TAG, " fcm token: ${token.pushToken}")
        }
    }

    private fun setInAppListener() {
        MoEInAppHelper.getInstance()
            .addInAppLifeCycleListener(MOE_APP_ID, InAppsLifeCycleListener())
        MoEInAppHelper.getInstance().setClickActionListener(MOE_APP_ID) { clickData ->
            Log.d(TAG, clickData.toString())
            false
        }
    }
}