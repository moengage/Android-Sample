package com.moengage.sample.payment.sdk

import android.app.Application
import android.content.Context
import android.content.Intent
import com.moengage.core.DataCenter
import com.moengage.core.LogLevel
import com.moengage.core.MoEngage
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.config.LogConfig
import com.moengage.core.ktx.MoEngageBuilderKtx
import com.moengage.sample.payment.sdk.internal.CheckoutActivity
import com.moengage.sample.payment.sdk.internal.INTENT_KEY_PAYMENT_DATA
import com.moengage.sample.payment.sdk.internal.INTENT_KEY_USER_DATA
import com.moengage.sample.payment.sdk.model.PaymentData
import com.moengage.sample.payment.sdk.model.UserData


/**
 * For example, This is Payment Gateway SDK. This for demo Purposes for integrating MoEngage SDK in Another SDK
 * */
object PaymentSDK {

    /**
     * MoEngage APP Identifier
     * */
    const val MOE_APP_ID = "<MOE_APP_ID>"

    /**
     * @param application - Application Instance of the App
     * */
    fun initialize(application: Application) {
        MoEngage.initialiseInstance(
            MoEngageBuilderKtx(
                application = application,
                appId = MOE_APP_ID,
                dataCenter = DataCenter.DATA_CENTER_2,
                logConfig = LogConfig(level = LogLevel.VERBOSE),
            ).build()
        )
        trackEvent(application)
    }

    /**
     * Track Event when SDK is initialized
     * */
    private fun trackEvent(context: Context) {
        val properties = Properties()
        properties.addAttribute("paymentSDKAppId", "")
        MoEAnalyticsHelper.trackEvent(context, "paymentSDKInit", properties, appId = MOE_APP_ID)
    }

    fun checkoutPayment(context: Context, userData: UserData, paymentData: PaymentData) {
        val intent = Intent(context, CheckoutActivity::class.java)
        intent.putExtra(INTENT_KEY_USER_DATA, userData)
        intent.putExtra(INTENT_KEY_PAYMENT_DATA, paymentData)
        context.startActivity(intent)
    }
}