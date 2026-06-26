package com.moengage.example.ordertracking.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.moengage.example.ordertracking.EXTRA_ORDER_ID
import com.moengage.example.ordertracking.LOG_TAG
import com.moengage.example.ordertracking.data.orderSessionRepository
import com.moengage.example.ordertracking.live.cancelLiveUpdateWork
import com.moengage.example.ordertracking.live.orderTrackingScope
import kotlinx.coroutines.launch

/**
 * Handles notification swipe-dismiss: records dismiss per order and cancels WorkManager ticks.
 * Uses [goAsync] so session file writes do not block the broadcast main thread.
 */
internal class OrderNotificationDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val orderId = intent.getStringExtra(EXTRA_ORDER_ID) ?: return
        val pendingResult = goAsync()
        orderTrackingScope.launch {
            try {
                orderSessionRepository(context).setDismissed(orderId, true)
                cancelLiveUpdateWork(context, orderId)
                Log.d(LOG_TAG, "User dismissed — orderId=$orderId")
            } finally {
                pendingResult.finish()
            }
        }
    }
}
