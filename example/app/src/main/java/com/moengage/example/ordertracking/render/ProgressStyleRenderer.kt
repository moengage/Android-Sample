package com.moengage.example.ordertracking.render

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.toColorInt
import com.moengage.example.R
import com.moengage.example.ordertracking.LIVE_CHANNEL_ID
import com.moengage.example.ordertracking.PROGRESS_STYLE_MIN_SDK
import com.moengage.example.ordertracking.model.OrderTrackingPayload

/**
 * Android 16+ ([PROGRESS_STYLE_MIN_SDK]) notification using [NotificationCompat.ProgressStyle]:
 * coloured segments, milestone points, moving tracker icon, status-bar chip, and Live Update promotion.
 */
@RequiresApi(Build.VERSION_CODES.N)
internal fun buildProgressStyleNotification(
    context: Context,
    moeBundle: Bundle,
    payload: OrderTrackingPayload,
    chipText: String,
    trackerPosition: Int,
): Notification {
    val segments =
        payload.segments.map { segment ->
            NotificationCompat.ProgressStyle.Segment(segment.size)
                .setColor(segment.color.toColorInt())
        }
    val points =
        payload.points.map { point ->
            NotificationCompat.ProgressStyle.Point(point.position)
                .setColor(point.color.toColorInt())
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

    return orderNotificationBuilder(
            context,
            LIVE_CHANNEL_ID,
            R.string.order_tracking_live_channel_name,
            NotificationManager.IMPORTANCE_HIGH,
            payload,
            moeBundle,
        )
        .setContentTitle(payload.title)
        .setContentText(payload.message)
        .setShortCriticalText(chipText)
        .setStyle(style)
        .setRequestPromotedOngoing(true)
        .build()
}
