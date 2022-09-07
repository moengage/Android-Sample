package com.moengage.push.amp.plus

import android.content.Context
import com.moengage.core.internal.logger.Logger
import com.xiaomi.mipush.sdk.MiPushCommandMessage
import com.xiaomi.mipush.sdk.MiPushMessage
import com.xiaomi.mipush.sdk.PushMessageReceiver

/**
 * Callback receiver for Mi SDK
 *
 * @author Umang Chamaria
 * @since 1.0.0
 */
public class MiPushReceiver : PushMessageReceiver() {

    private val tag = "MiPushReceiver"

    override fun onReceivePassThroughMessage(context: Context?, message: MiPushMessage?) {
        Logger.print { "$tag onReceivePassThroughMessage() : $message" }
        if (message == null || context == null) return
        if (MiPushHelper.isFromMoEngagePlatform(message)) {
            MiPushHelper.passPushPayload(context, message)
        }
    }

    override fun onNotificationMessageClicked(context: Context?, message: MiPushMessage?) {
        Logger.print { "$tag onNotificationMessageClicked() : $message" }
        if (message == null || context == null) return
        MiPushHelper.onNotificationClicked(context, message)
    }

    override fun onReceiveRegisterResult(context: Context?, message: MiPushCommandMessage?) {
        Logger.print { "$tag onReceiveRegisterResult() : $message" }
        if (message == null || context == null) return
        MiPushHelper.passPushToken(context, message)
    }

    override fun onCommandResult(context: Context?, message: MiPushCommandMessage?) {
        Logger.print { "$tag onCommandResult() : $message" }
        if (message == null || context == null) return
        MiPushHelper.passPushToken(context, message)
    }
}