package com.moengage.example.ordertracking.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.moengage.example.R
import com.moengage.example.ordertracking.CHANNEL_ID

/** Creates the `order_tracking` channel once at app startup (no-op below API 26). */
@RequiresApi(Build.VERSION_CODES.N)
internal fun ensureOrderTrackingNotificationChannels(context: Context) {
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
