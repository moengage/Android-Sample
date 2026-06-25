package com.moengage.example.ordertracking

/**
 * API 31–33 fallback: [NotificationCompat.BigTextStyle] with emoji segment indicators in the body.
 */

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.moengage.example.R

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
internal fun renderBigTextFallbackNotification(
    context: Context,
    payload: OrderTrackingPayload,
    chipText: String,
) {
    val emojiLine = emojiProgressLine(payload.stage, payload.segments.size)
    val body = "$emojiLine\n${fallbackStepSummary(payload, chipText)}"
    val notification =
        orderNotificationBuilder(
                context,
                CHANNEL_ID,
                R.string.order_tracking_channel_name,
                payload,
            )
            .setContentTitle(payload.title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .build()

    NotificationManagerCompat.from(context)
        .notify(payload.orderId, NOTIFICATION_ID, notification)
}
