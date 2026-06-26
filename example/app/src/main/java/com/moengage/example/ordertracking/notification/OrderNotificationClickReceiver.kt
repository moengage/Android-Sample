package com.moengage.example.ordertracking.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moengage.example.ordertracking.EXTRA_ORDER_ID
import com.moengage.example.ordertracking.data.orderSessionRepository
import com.moengage.example.ordertracking.live.orderTrackingScope
import com.moengage.pushbase.MoEPushHelper
import kotlinx.coroutines.launch

/** Logs the MoEngage click event from the saved push bundle when the user taps the notification. */
internal class OrderNotificationClickReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val orderId = intent.getStringExtra(EXTRA_ORDER_ID) ?: return
        val pendingResult = goAsync()
        orderTrackingScope.launch {
            try {
                val moeBundle = orderSessionRepository(context).getMoeBundle(orderId)
                if (moeBundle != null) {
                    MoEPushHelper.getInstance().logNotificationClick(context.applicationContext, moeBundle)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
