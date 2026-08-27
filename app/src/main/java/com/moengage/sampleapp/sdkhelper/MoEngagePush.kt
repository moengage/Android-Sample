package com.moengage.sampleapp.sdkhelper

import android.content.Context
import com.moengage.pushbase.MoEPushHelper
import com.moengage.sampleapp.push.BrewPushMessageListener
import timber.log.Timber

/** Push permission and push callbacks. Reached through [MoEngageSDKHelper]. */
internal object MoEngagePush {

    /**
     * Screen 3 CTA. On Android 13+ this surfaces the OS `POST_NOTIFICATIONS` dialog; below
     * that the SDK reports the manifest-granted state straight back through the listener.
     */
    fun requestPermission(context: Context) = guarded {
        MoEPushHelper.getInstance().requestPushPermission(context)
    }

    /**
     * Reports the user's answer to the OS prompt back to MoEngage, which is what lets the
     * dashboard segment on notification opt-in. Must be called for both outcomes.
     */
    fun recordPermissionResponse(context: Context, granted: Boolean) = guarded {
        MoEPushHelper.getInstance().pushPermissionResponse(context, granted)
        Timber.d("push permission response: granted=%s", granted)
    }

    /** Profile toggle when the OS has blocked notifications. */
    fun openNotificationSettings(context: Context) = guarded {
        MoEPushHelper.getInstance().navigateToSettings(context)
    }

    /**
     * Hands self-handled campaigns to [BrewPushMessageListener], which is how order-tracking
     * pushes (`pct_payload`) get rendered by the app as a Live Update instead of by the SDK.
     */
    fun registerCallbacks() = guarded {
        MoEPushHelper.getInstance().registerMessageListener(BrewPushMessageListener())
    }
}
