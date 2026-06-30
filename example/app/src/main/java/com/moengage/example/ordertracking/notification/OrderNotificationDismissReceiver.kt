package com.moengage.example.ordertracking.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.moengage.example.ordertracking.EXTRA_ORDER_ID
import com.moengage.example.ordertracking.LOG_TAG
import com.moengage.example.ordertracking.live.OrderTrackingLiveUpdater

/** Handles notification swipe-dismiss: records dismiss per order and stops local ticks. */
internal class OrderNotificationDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val orderId = intent.getStringExtra(EXTRA_ORDER_ID) ?: return
        OrderTrackingDismissPrefs.setDismissed(context, orderId, true)
        OrderTrackingLiveUpdater.stop(context, orderId)
        Log.d(LOG_TAG, "User dismissed — orderId=$orderId")
    }
}
