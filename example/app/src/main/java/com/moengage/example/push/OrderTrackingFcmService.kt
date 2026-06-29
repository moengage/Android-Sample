package com.moengage.example.push

import android.Manifest
import android.app.ForegroundServiceStartNotAllowedException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moengage.core.MoECoreHelper
import com.moengage.example.ordertracking.LOG_TAG
import com.moengage.example.ordertracking.data.hasPctPayload
import com.moengage.example.ordertracking.data.moePushBundleFrom
import com.moengage.example.ordertracking.live.OrderTrackingForegroundService
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper

/**
 * Custom FCM entry point for order-tracking (PCT) pushes.
 *
 * Self-handled Background Update pushes with `pct_payload` are handled in [onMessageReceived]
 */
class OrderTrackingFcmService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        if (!MoEPushHelper.getInstance().isFromMoEngagePlatform(data)) {
            return
        }

        if (MoEPushHelper.getInstance().isSelfHandledNotification(data) && hasPctPayload(data)) {
            Log.d(LOG_TAG, "FCM order-tracking push (priority=${remoteMessage.priority})")
            try {
                OrderTrackingForegroundService.startOrUpdate(this, moePushBundleFrom(data))
            } catch (error: ForegroundServiceStartNotAllowedException) {
                Log.w(LOG_TAG, "FGS blocked in background — skip display")
            }
            MoECoreHelper.setupSdkForBackgroundMode(this)
            Log.d(LOG_TAG, "Logging notification received")
            MoEPushHelper.getInstance().logNotificationReceived(applicationContext, data)
            return
        }

        MoECoreHelper.setupSdkForBackgroundMode(this)
        MoEFireBaseHelper.getInstance().passPushPayload(applicationContext, data)
    }

    override fun onNewToken(token: String) {
        MoEFireBaseHelper.getInstance().passPushToken(applicationContext, token)
    }
}
