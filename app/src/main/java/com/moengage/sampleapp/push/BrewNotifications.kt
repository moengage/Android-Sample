package com.moengage.sampleapp.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.moengage.sampleapp.BrewActivity
import com.moengage.sampleapp.R

/**
 * Notification channels and the local test notification behind DemoTools.
 *
 * Real campaign notifications are built and posted by the MoEngage push module; this exists
 * so the "push arrives → tap → Order status" moment can be demonstrated without a live
 * campaign or an FCM token.
 */
object BrewNotifications {

    /** Matches the default channel the MoEngage notification config points at. */
    const val CHANNEL_ORDER_UPDATES = "order_updates"
    const val CHANNEL_OFFERS = "offers_new_menu"

    private const val TEST_NOTIFICATION_ID = 4821

    /**
     * Whether the app can post notifications at all: covers the Android 13+ `POST_NOTIFICATIONS`
     * grant and the user switching notifications off in system settings on any version.
     */
    fun areEnabled(context: Context): Boolean = NotificationManagerCompat.from(context).areNotificationsEnabled()

    fun ensureChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ORDER_UPDATES,
                "Order updates",
                NotificationManager.IMPORTANCE_HIGH,
            ).apply { description = "Your order moving through the bar." },
        )
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_OFFERS,
                "Offers & new menu",
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply { description = "Happy hours, new bakes and star milestones." },
        )
    }

    /**
     * Posts the same copy a real "your drink is ready" campaign would, deep-linking to
     * `brewbar://status/{orderId}`.
     */
    fun postTestNotification(context: Context, orderId: String) {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return

        val intent = Intent(context, BrewActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse("brewbar://status/$orderId")
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            TEST_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ORDER_UPDATES)
            .setSmallIcon(R.drawable.ic_notification_small)
            .setColor(ContextCompat.getColor(context, R.color.brand_primary))
            .setContentTitle("Your flat white is at the bar")
            .setContentText("Order #$orderId · tap to see your order.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        runCatching {
            NotificationManagerCompat.from(context).notify(TEST_NOTIFICATION_ID, notification)
        }
    }
}
