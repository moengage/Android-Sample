package com.moengage.example.ordertracking

/**
 * Handles notification tap: logs the MoEngage click event from the saved push bundle.
 */

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moengage.pushbase.MoEPushHelper
import kotlinx.coroutines.launch

internal class OrderNotificationClickReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val orderId = intent.getStringExtra(EXTRA_ORDER_ID) ?: return
        val pendingResult = goAsync()
        orderTrackingScope.launch {
            try {
                val moeBundle = OrderSessionRepository(context).getMoeBundle(orderId)
                if (moeBundle != null) {
                    MoEPushHelper.getInstance().logNotificationClick(context, moeBundle)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
