package com.moengage.sample.payment.sdk

import android.app.Activity
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.moengage.pushbase.model.NotificationPayload
import com.moengage.pushbase.push.PushMessageListener

/**
 * Here PaymentSDK can receive callbacks of the push notifications of the Main App if App already integrated MoEngage SDK.
 * This use case will not be valid except few cases. If PaymentSDK needs to send push directly to all the app users, PaymentSDK needs to initialize
 * Firebase Secondary instance
 * */
class PaymentSDKPushMessageListener : PushMessageListener() {
    override fun customizeNotification(
        notification: Notification,
        context: Context,
        payload: Bundle,
    ) {
        super.customizeNotification(notification, context, payload)
    }

    override fun getIntentFlags(payload: Bundle): Int {
        return super.getIntentFlags(payload)
    }

    override fun getRedirectIntent(context: Context): Intent {
        return super.getRedirectIntent(context)
    }

    override fun handleCustomAction(context: Context, payload: String) {
        super.handleCustomAction(context, payload)
    }

    override fun isNotificationRequired(context: Context, payload: Bundle): Boolean {
        return super.isNotificationRequired(context, payload)
    }

    override fun onCreateNotification(
        context: Context,
        notificationPayload: NotificationPayload,
    ): NotificationCompat.Builder {
        return super.onCreateNotification(context, notificationPayload)
    }

    override fun onNonMoEngageMessageReceived(context: Context, payload: Bundle) {
        super.onNonMoEngageMessageReceived(context, payload)
    }

    override fun onNotificationCleared(context: Context, payload: Bundle) {
        super.onNotificationCleared(context, payload)
    }

    override fun onNotificationClick(activity: Activity, payload: Bundle) {
        super.onNotificationClick(activity, payload)
    }

    override fun onNotificationNotRequired(context: Context, payload: Bundle) {
        super.onNotificationNotRequired(context, payload)
    }

    override fun onNotificationReceived(context: Context, payload: Bundle) {
        super.onNotificationReceived(context, payload)
    }

    override fun onPostNotificationReceived(context: Context, payload: Bundle) {
        super.onPostNotificationReceived(context, payload)
    }
}