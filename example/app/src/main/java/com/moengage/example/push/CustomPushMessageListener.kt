package com.moengage.example.push

import android.app.Activity
import android.content.Context
import android.os.Bundle
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

    override fun onNotificationCleared(context: Context, payload: Bundle) {
        super.onNotificationCleared(context, payload)
        logcat { " onNotificationCleared() Notification Cleared $payload" }
    }

    override fun onNotificationClick(activity: Activity, payload: Bundle) {
        super.onNotificationClick(activity, payload)
        logcat { " onHandleRedirection() Notification clicked $payload" }
    }

    override fun handleCustomAction(context: Context, payload: String) {
        super.handleCustomAction(context, payload)
        logcat { " handleCustomAction() Callback for custom action." }
    }
}