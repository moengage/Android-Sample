package com.moengage.example.ordertracking

/**
 * Pending intents wired into notifications: tap → [OrderNotificationClickReceiver],
 * swipe dismiss → [OrderNotificationDismissReceiver].
 */

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

/** Fired when the user taps the notification body. */
internal fun orderNotificationContentIntent(context: Context, orderId: String): PendingIntent =
    orderNotificationBroadcastIntent(
        context,
        orderId,
        OrderNotificationClickReceiver::class.java,
        orderId.hashCode(),
    )

/** Fired when the user removes the notification from the shade. */
internal fun orderNotificationDeleteIntent(context: Context, orderId: String): PendingIntent =
    orderNotificationBroadcastIntent(
        context,
        orderId,
        OrderNotificationDismissReceiver::class.java,
        orderId.hashCode() + 1,
    )

private fun orderNotificationBroadcastIntent(
    context: Context,
    orderId: String,
    receiver: Class<*>,
    requestCode: Int,
): PendingIntent {
    val intent =
        Intent(context, receiver).apply {
            putExtra(EXTRA_ORDER_ID, orderId)
        }
    return PendingIntent.getBroadcast(context, requestCode, intent, pendingIntentFlags())
}

private fun pendingIntentFlags(): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
