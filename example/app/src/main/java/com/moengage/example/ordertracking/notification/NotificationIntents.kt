package com.moengage.example.ordertracking.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.moengage.example.ordertracking.EXTRA_ORDER_ID

/** Fired when the user taps the notification body. Carries the full MoEngage push extras for click analytics. */
internal fun orderNotificationContentIntent(
    context: Context,
    orderId: String,
    moeBundle: Bundle,
): PendingIntent =
    orderNotificationBroadcastIntent(
        context,
        orderId,
        OrderNotificationClickReceiver::class.java,
        orderId.hashCode(),
        moeBundle,
    )

/** Fired when the user removes the notification from the shade. */
internal fun orderNotificationDeleteIntent(context: Context, orderId: String): PendingIntent =
    orderNotificationBroadcastIntent(
        context,
        orderId,
        OrderNotificationDismissReceiver::class.java,
        orderId.hashCode() + 1,
        moeBundle = null,
    )

private fun orderNotificationBroadcastIntent(
    context: Context,
    orderId: String,
    receiver: Class<*>,
    requestCode: Int,
    moeBundle: Bundle?,
): PendingIntent {
    val intent =
        Intent(context, receiver).apply {
            putExtra(EXTRA_ORDER_ID, orderId)
            if (moeBundle != null) {
                putExtras(moeBundle)
            }
        }
    return PendingIntent.getBroadcast(context, requestCode, intent, pendingIntentFlags())
}

private fun pendingIntentFlags(): Int =
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
