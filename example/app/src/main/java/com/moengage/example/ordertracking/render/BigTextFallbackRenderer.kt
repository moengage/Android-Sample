package com.moengage.example.ordertracking.render

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.moengage.example.R
import com.moengage.example.ordertracking.CHANNEL_ID
import com.moengage.example.ordertracking.NOTIFICATION_ID
import com.moengage.example.ordertracking.model.OrderTrackingPayload

/** API 31–33 fallback: [NotificationCompat.BigTextStyle] with emoji segment indicators in the body. */
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
