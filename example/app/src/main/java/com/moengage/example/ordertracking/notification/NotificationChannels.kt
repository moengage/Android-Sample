package com.moengage.example.ordertracking.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/** Creates [channelId] if it does not exist yet. */
internal fun ensureNotificationChannel(context: Context, channelId: String, name: String) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (manager.getNotificationChannel(channelId) != null) return
    manager.createNotificationChannel(
        NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT),
    )
}
