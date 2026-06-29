package com.moengage.example.ordertracking.notification

import android.content.Context
import androidx.core.content.edit

/** Persists user dismiss per order for [com.moengage.example.ordertracking.model.OrderTrackingPayload.respectUserDismiss]. */
internal object OrderTrackingDismissPrefs {

    private const val PREFS_NAME = "order_tracking_dismiss"

    fun isDismissed(context: Context, orderId: String): Boolean =
        prefs(context).getBoolean(dismissKey(orderId), false)

    fun setDismissed(context: Context, orderId: String, dismissed: Boolean) {
        prefs(context).edit { putBoolean(dismissKey(orderId), dismissed) }
    }

    fun clearDismissed(context: Context, orderId: String) {
        prefs(context).edit { remove(dismissKey(orderId)) }
    }

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun dismissKey(orderId: String): String = "dismissed_$orderId"
}
