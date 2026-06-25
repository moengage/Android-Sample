package com.moengage.example.ordertracking

/**
 * Android 16+ ([PROGRESS_STYLE_MIN_SDK]) notification builder using [NotificationCompat.ProgressStyle]:
 * coloured segments, milestone points, moving tracker icon, status-bar chip, and Live Update promotion.
 */

import android.Manifest
import android.content.Context
import android.graphics.Color
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.moengage.example.R

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
internal fun renderProgressStyleNotification(
    context: Context,
    payload: OrderTrackingPayload,
    chipText: String,
    trackerPosition: Int,
) {
    val segments =
        payload.segments.map { segment ->
            NotificationCompat.ProgressStyle.Segment(segment.size)
                .setColor(Color.parseColor(segment.color))
        }
    val points =
        payload.points.map { point ->
            NotificationCompat.ProgressStyle.Point(point.position)
                .setColor(Color.parseColor(point.color))
        }

    val style =
        NotificationCompat.ProgressStyle()
            .setStyledByProgress(payload.styledByProgress)
            .setProgress(trackerPosition)
            .setProgressSegments(segments)
            .setProgressPoints(points)
            .setProgressTrackerIcon(notificationIcon(context, payload.trackerIcon))
            .setProgressStartIcon(notificationIcon(context, payload.startIcon))
            .setProgressEndIcon(notificationIcon(context, payload.endIcon))

    val notification =
        orderNotificationBuilder(
                context,
                LIVE_CHANNEL_ID,
                R.string.order_tracking_live_channel_name,
                payload,
            )
            .setContentTitle(payload.title)
            .setContentText(payload.message)
            .setShortCriticalText(chipText)
            .setStyle(style)
            .setRequestPromotedOngoing(true)
            .build()

    NotificationManagerCompat.from(context)
        .notify(payload.orderId, NOTIFICATION_ID, notification)
}
