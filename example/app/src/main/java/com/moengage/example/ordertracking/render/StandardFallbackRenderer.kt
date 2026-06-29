package com.moengage.example.ordertracking.render

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.moengage.example.R
import com.moengage.example.ordertracking.CHANNEL_ID
import com.moengage.example.ordertracking.model.OrderTrackingPayload

/**
 * API ≤30 fallback: standard collapsed notification (title + one-line step/chip summary).
 * No expandable style — matches product guidance for legacy devices.
 */
@RequiresApi(Build.VERSION_CODES.N)
internal fun buildStandardFallbackNotification(
    context: Context,
    moeBundle: Bundle,
    payload: OrderTrackingPayload,
    chipText: String,
): Notification {
    val collapsed = fallbackStepSummary(payload, chipText, includeMessage = false)
    return orderNotificationBuilder(
            context,
            CHANNEL_ID,
            R.string.order_tracking_channel_name,
            NotificationManager.IMPORTANCE_DEFAULT,
            payload,
            moeBundle,
        )
        .setContentTitle(payload.title)
        .setContentText(collapsed)
        .build()
}
