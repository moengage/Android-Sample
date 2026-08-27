package com.moengage.sampleapp.ordertracking.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.moengage.sampleapp.R
import com.moengage.sampleapp.ordertracking.CHANNEL_ID

/**
 * Creates the `order_tracking` channel once at app startup (no-op below API 26).
 *
 * Kept separate from [com.moengage.sampleapp.push.BrewNotifications] so the whole
 * `ordertracking` package can be lifted into another app as one unit.
 */
internal fun ensureOrderTrackingNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (manager.getNotificationChannel(CHANNEL_ID) != null) return
    manager.createNotificationChannel(
        NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.order_tracking_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ),
    )
}
