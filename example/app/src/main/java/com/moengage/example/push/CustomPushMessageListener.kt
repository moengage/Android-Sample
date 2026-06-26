package com.moengage.example.push

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.annotation.RequiresPermission
import com.moengage.example.ordertracking.handleOrderTrackingPush
import com.moengage.pushbase.push.PushMessageListener
import logcat.logcat

/**
 * @author Umang Chamaria
 * Date: 2022/02/10
 */
class CustomPushMessageListener: PushMessageListener() {

    override fun onNotificationReceived(context: Context, payload: Bundle) {
        super.onNotificationReceived(context, payload)
        logcat { " onNotificationReceived() Notification received $payload" }
    }

    /**
     * Called for MoEngage Background Update (self-handled) pushes.
     * Forwards to [handleOrderTrackingPush] when the dashboard sends key `pct_payload`.
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onSelfHandledNotificationReceived(context: Context, payload: Bundle) {
        super.onSelfHandledNotificationReceived(context, payload)
        handleOrderTrackingPush(context, payload)
    }

    override fun onNotificationCleared(context: Context, payload: Bundle) {
        super.onNotificationCleared(context, payload)
        logcat { " onNotificationCleared() Notification Cleared $payload" }
    }

    override fun onNotificationClick(activity: Activity, payload: Bundle): Boolean {
        super.onNotificationClick(activity, payload)
        logcat { " onNotificationClick() Notification clicked $payload" }
        return false
    }

    override fun handleCustomAction(context: Context, payload: String) {
        super.handleCustomAction(context, payload)
        logcat { " handleCustomAction() Callback for custom action." }
    }
}
