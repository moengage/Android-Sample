package com.moengage.example.ordertracking.render

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.moengage.example.R
import com.moengage.example.ordertracking.CHANNEL_ID
import com.moengage.example.ordertracking.NOTIFICATION_ID
import com.moengage.example.ordertracking.model.OrderTrackingPayload

/** API 34–35 fallback: [NotificationCompat.BigPictureStyle] with a client-drawn coloured progress strip. */
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
internal fun renderBigPictureFallbackNotification(
    context: Context,
    payload: OrderTrackingPayload,
    chipText: String,
    trackerPosition: Int,
) {
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

    val notification =
        orderNotificationBuilder(
                context,
                CHANNEL_ID,
                R.string.order_tracking_channel_name,
                payload,
            )
            .setContentTitle(payload.title)
            .setContentText(summary)
            .setStyle(style)
            .build()

    NotificationManagerCompat.from(context)
        .notify(payload.orderId, NOTIFICATION_ID, notification)
}
