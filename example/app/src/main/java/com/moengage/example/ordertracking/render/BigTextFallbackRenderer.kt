package com.moengage.example.ordertracking.render

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.moengage.example.R
import com.moengage.example.ordertracking.CHANNEL_ID
import com.moengage.example.ordertracking.model.OrderTrackingPayload

/** API 31–33 fallback: [NotificationCompat.BigTextStyle] with emoji segment indicators in the body. */
@RequiresApi(Build.VERSION_CODES.N)
internal fun buildBigTextFallbackNotification(
    context: Context,
    moeBundle: Bundle,
    payload: OrderTrackingPayload,
    chipText: String,
): Notification {
    val emojiLine = emojiProgressLine(payload.stage, payload.segments.size)
    val body = "$emojiLine\n${fallbackStepSummary(payload, chipText)}"
    return orderNotificationBuilder(
            context,
            CHANNEL_ID,
            R.string.order_tracking_channel_name,
            NotificationManager.IMPORTANCE_DEFAULT,
            payload,
            moeBundle,
        )
        .setContentTitle(payload.title)
        .setContentText(body)
        .setStyle(NotificationCompat.BigTextStyle().bigText(body))
        .build()
}
