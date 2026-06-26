package com.moengage.example.ordertracking.render

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import com.moengage.example.R
import com.moengage.example.ordertracking.CHANNEL_ID
import com.moengage.example.ordertracking.NOTIFICATION_ID
import com.moengage.example.ordertracking.model.OrderTrackingPayload

/**
 * API ≤30 fallback: standard collapsed notification (title + one-line step/chip summary).
 * No expandable style — matches product guidance for legacy devices.
 */
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
internal fun renderStandardFallbackNotification(
    context: Context,
    payload: OrderTrackingPayload,
    chipText: String,
) {
    val collapsed = fallbackStepSummary(payload, chipText, includeMessage = false)
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
