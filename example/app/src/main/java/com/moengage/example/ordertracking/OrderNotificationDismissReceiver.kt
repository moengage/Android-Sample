package com.moengage.example.ordertracking

/**
 * Handles notification swipe-dismiss: records dismiss per order, cancels WorkManager ticks, and
 * uses [goAsync] so session file writes do not block the broadcast main thread.
 */

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.launch

internal class OrderNotificationDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val orderId = intent.getStringExtra(EXTRA_ORDER_ID) ?: return
        val pendingResult = goAsync()
        orderTrackingScope.launch {
            try {
                OrderSessionRepository(context).setDismissed(orderId, true)
                cancelLiveUpdateWork(context, orderId)
                Log.d(LOG_TAG, "User dismissed — orderId=$orderId")
            } finally {
                pendingResult.finish()
            }
        }
    }
}
