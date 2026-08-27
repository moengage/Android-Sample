package com.moengage.sampleapp.ordertracking.render

import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.moengage.sampleapp.R
import com.moengage.sampleapp.ordertracking.CHANNEL_ID
import com.moengage.sampleapp.ordertracking.ORDER_STAGE_COUNT
import com.moengage.sampleapp.ordertracking.model.OrderTrackingPayload
import com.moengage.sampleapp.ordertracking.notification.orderNotificationContentIntent
import com.moengage.sampleapp.ordertracking.notification.orderNotificationDeleteIntent

/** Shared [NotificationCompat.Builder] fields for all order-tracking renderers. Channel is created at app startup. */
internal fun orderNotificationBuilder(
    context: Context,
    payload: OrderTrackingPayload,
    moeBundle: Bundle,
): NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID)
    .setSmallIcon(R.drawable.ic_notification_small)
    .setColor(context.getColor(R.color.brand_primary))
    .setOnlyAlertOnce(true)
    .setOngoing(!payload.terminal)
    .setAutoCancel(payload.terminal)
    .setContentIntent(orderNotificationContentIntent(context, payload.orderId, moeBundle))
    .setDeleteIntent(orderNotificationDeleteIntent(context, payload.orderId))

internal fun fallbackStepSummary(
    payload: OrderTrackingPayload,
    chipText: String,
    includeMessage: Boolean = true,
): String = if (includeMessage) {
    "Step ${payload.stage}/$ORDER_STAGE_COUNT · ${payload.message} · $chipText"
} else {
    "Step ${payload.stage}/$ORDER_STAGE_COUNT · $chipText"
}
