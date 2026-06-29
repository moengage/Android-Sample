package com.moengage.example.ordertracking.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

/** Creates [channelId] if it does not exist yet. */
@RequiresApi(Build.VERSION_CODES.N)
internal fun ensureNotificationChannel(
    context: Context,
    channelId: String,
    name: String,
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (manager.getNotificationChannel(channelId) != null) return
    manager.createNotificationChannel(NotificationChannel(channelId, name, importance))
}
