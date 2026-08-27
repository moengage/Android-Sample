package com.moengage.sampleapp.push

import android.content.Context
import android.os.Bundle
import com.moengage.pushbase.push.PushMessageListener
import com.moengage.sampleapp.ordertracking.data.hasPctPayload
import com.moengage.sampleapp.ordertracking.live.OrderTrackingForegroundService
import timber.log.Timber

/**
 * Push callbacks for Brew Bar, registered from [com.moengage.sampleapp.sdkhelper.MoEngageSDKHelper].
 *
 * The order-tracking hook is [onSelfHandledNotificationReceived]: a campaign marked self-handled on
 * the dashboard is handed to the app instead of being rendered by the SDK, which is what lets us
 * draw the Live Update / ProgressStyle notification ourselves. Order-tracking campaigns are
 * identified by the `pct_payload` custom key; anything else is ignored and left to the SDK.
 */
internal class BrewPushMessageListener : PushMessageListener() {

    override fun onSelfHandledNotificationReceived(context: Context, payload: Bundle) {
        super.onSelfHandledNotificationReceived(context, payload)
        if (!hasPctPayload(payload)) return
        Timber.d("Order-tracking push received")
        OrderTrackingForegroundService.startOrUpdate(context, payload)
    }

    override fun onNotificationReceived(context: Context, payload: Bundle) {
        super.onNotificationReceived(context, payload)
        Timber.d("Notification received")
    }

    override fun onNotificationCleared(context: Context, payload: Bundle) {
        super.onNotificationCleared(context, payload)
        Timber.d("Notification cleared")
    }
}
