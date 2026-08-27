package com.moengage.sampleapp.ordertracking.render

import android.app.Notification
import android.content.Context
import android.os.Bundle
import com.moengage.sampleapp.ordertracking.model.OrderTrackingPayload

/**
 * API ≤30 fallback: standard collapsed notification (title + one-line step/chip summary).
 * No expandable style — matches product guidance for legacy devices.
 */
internal fun buildStandardFallbackNotification(
    context: Context,
    moeBundle: Bundle,
    payload: OrderTrackingPayload,
    chipText: String,
): Notification {
    val collapsed = fallbackStepSummary(payload, chipText, includeMessage = false)
    return orderNotificationBuilder(context, payload, moeBundle)
        .setContentTitle(payload.title)
        .setContentText(collapsed)
        .build()
}
