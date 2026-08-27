package com.moengage.sampleapp.ordertracking.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.moengage.pushbase.MoEPushHelper
import com.moengage.sampleapp.BrewActivity
import com.moengage.sampleapp.ordertracking.EXTRA_ORDER_ID
import timber.log.Timber

/**
 * Logs the MoEngage click event from the notification's intent extras (the full push bundle), then
 * opens Order status for that order — the same `brewbar://status/{orderId}` deep link a real
 * campaign would carry.
 */
internal class OrderNotificationClickReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras ?: return
        MoEPushHelper.getInstance().logNotificationClick(context.applicationContext, extras)

        val orderId = extras.getString(EXTRA_ORDER_ID) ?: return
        Timber.d("Order tracking notification tapped — orderId=%s", orderId)
        context.startActivity(
            Intent(context, BrewActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                data = "brewbar://status/$orderId".toUri()
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
