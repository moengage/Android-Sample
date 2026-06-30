package com.moengage.example.ordertracking.render

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.moengage.example.R
import com.moengage.example.ordertracking.CHANNEL_ID
import com.moengage.example.ordertracking.ORDER_STAGE_COUNT
import com.moengage.example.ordertracking.model.OrderTrackingPayload
import com.moengage.example.ordertracking.notification.orderNotificationContentIntent
import com.moengage.example.ordertracking.notification.orderNotificationDeleteIntent

/** Shared [NotificationCompat.Builder] fields for all order-tracking renderers. Channel is created at app startup. */
@RequiresApi(Build.VERSION_CODES.N)
internal fun orderNotificationBuilder(
    context: Context,
    payload: OrderTrackingPayload,
    moeBundle: Bundle,
): NotificationCompat.Builder =
    NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.small_icon)
        .setColor(context.getColor(R.color.notification_color))
        .setOnlyAlertOnce(true)
        .setOngoing(!payload.terminal)
        .setAutoCancel(payload.terminal)
        .setContentIntent(orderNotificationContentIntent(context, payload.orderId, moeBundle))
        .setDeleteIntent(orderNotificationDeleteIntent(context, payload.orderId))

internal fun fallbackStepSummary(
    payload: OrderTrackingPayload,
    chipText: String,
    includeMessage: Boolean = true,
): String =
    if (includeMessage) {
        "Step ${payload.stage}/$ORDER_STAGE_COUNT · ${payload.message} · $chipText"
    } else {
        "Step ${payload.stage}/$ORDER_STAGE_COUNT · $chipText"
    }
