package com.moengage.example.ordertracking

/**
 * API ≤30 fallback: standard collapsed notification (title + one-line step/chip summary).
 * No expandable style — matches product guidance for legacy devices.
 */

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import com.moengage.example.R

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
internal fun renderStandardFallbackNotification(
    context: Context,
    payload: OrderTrackingPayload,
    chipText: String,
) {
    val collapsed = "Step ${payload.stage}/$ORDER_STAGE_COUNT · $chipText"
    val notification =
        orderNotificationBuilder(
                context,
                CHANNEL_ID,
                R.string.order_tracking_channel_name,
                payload,
            )
            .setContentTitle(payload.title)
            .setContentText(collapsed)
            .build()

    NotificationManagerCompat.from(context)
        .notify(payload.orderId, NOTIFICATION_ID, notification)
}
