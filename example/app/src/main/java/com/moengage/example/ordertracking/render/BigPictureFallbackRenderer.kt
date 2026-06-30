package com.moengage.example.ordertracking.render

import android.app.Notification
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.moengage.example.ordertracking.model.OrderTrackingPayload

/** API 34–35 fallback: [NotificationCompat.BigPictureStyle] with a client-drawn coloured progress strip. */
@RequiresApi(Build.VERSION_CODES.N)
internal fun buildBigPictureFallbackNotification(
    context: Context,
    moeBundle: Bundle,
    payload: OrderTrackingPayload,
    chipText: String,
    trackerPosition: Int,
): Notification {
    val summary = fallbackStepSummary(payload, chipText)
    val progressStrip = createProgressStripBitmap(payload, trackerPosition)
    val style =
        NotificationCompat.BigPictureStyle()
            .bigPicture(progressStrip)
            .setBigContentTitle(payload.title)
            .setSummaryText(summary)
            .also { bigPictureStyle ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    bigPictureStyle.showBigPictureWhenCollapsed(true)
                }
            }

    return orderNotificationBuilder(context, payload, moeBundle)
        .setContentTitle(payload.title)
        .setContentText(summary)
        .setStyle(style)
        .build()
}
