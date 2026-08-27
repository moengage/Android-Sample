package com.moengage.sampleapp.ordertracking

import android.content.Context
import com.moengage.sampleapp.ordertracking.live.OrderTrackingForegroundService
import com.moengage.sampleapp.ordertracking.notification.OrderTrackingDismissPrefs
import timber.log.Timber

// Entry points for the order-tracking flow.
//
// Stages themselves arrive as MoEngage pushes carrying `pct_payload` (see BrewPushMessageListener) —
// nothing here posts a notification. These calls only manage the state a new order needs before its
// first stage push lands.

/**
 * Called when an order is placed. Clears any dismiss recorded against this order id so the first
 * stage push always shows.
 *
 * This matters in the demo specifically: order ids are reused across runs (`BB-4821`), so without
 * the reset a swipe-away in an earlier run would keep suppressing every later payload that sets
 * `respect_user_dismiss`.
 */
fun primeOrderTracking(context: Context, orderId: String) {
    OrderTrackingDismissPrefs.clearDismissed(context, orderId)
    Timber.d("Order tracking primed — orderId=%s, awaiting stage pushes", orderId)
}

/** Called when the journey ends outside the notification (e.g. the user collects the order in-app). */
fun stopOrderTracking(context: Context, orderId: String) {
    OrderTrackingForegroundService.stop(context, orderId)
}
