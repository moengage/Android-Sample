package com.moengage.example.ordertracking.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.moengage.pushbase.MoEPushHelper

/** Logs the MoEngage click event from notification intent extras (full push bundle). */
internal class OrderNotificationClickReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras ?: return
        MoEPushHelper.getInstance().logNotificationClick(context.applicationContext, extras)
    }
}
