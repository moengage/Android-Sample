package com.moengage.example.ordertracking

/**
 * Shared [NotificationCompat.Builder] fields for all order-tracking renderers (ProgressStyle,
 * BigPicture, BigText, Standard).
 */

import android.content.Context
import androidx.core.app.NotificationCompat
import com.moengage.example.R

internal fun orderNotificationBuilder(
    context: Context,
    channelId: String,
    channelNameResId: Int,
    payload: OrderTrackingPayload,
): NotificationCompat.Builder {
    ensureNotificationChannel(context, channelId, context.getString(channelNameResId))
    return NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.small_icon)
        .setColor(context.getColor(R.color.notification_color))
        .setOnlyAlertOnce(true)
        .setOngoing(!payload.terminal)
        .setAutoCancel(payload.terminal)
        .setContentIntent(orderNotificationContentIntent(context, payload.orderId))
        .setDeleteIntent(orderNotificationDeleteIntent(context, payload.orderId))
}

internal fun fallbackStepSummary(payload: OrderTrackingPayload, chipText: String): String =
    "Step ${payload.stage}/$ORDER_STAGE_COUNT · ${payload.message} · $chipText"
