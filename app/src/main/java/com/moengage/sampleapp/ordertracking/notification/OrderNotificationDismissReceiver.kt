package com.moengage.sampleapp.ordertracking.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moengage.sampleapp.ordertracking.EXTRA_ORDER_ID
import com.moengage.sampleapp.ordertracking.live.OrderTrackingForegroundService
import timber.log.Timber

/** Handles notification swipe-dismiss: records dismiss per order and stops the foreground service. */
internal class OrderNotificationDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val orderId = intent.getStringExtra(EXTRA_ORDER_ID) ?: return
        OrderTrackingDismissPrefs.setDismissed(context, orderId, true)
        OrderTrackingForegroundService.stop(context, orderId)
        Timber.d("User dismissed order tracking — orderId=%s", orderId)
    }
}
