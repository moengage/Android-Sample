package com.moengage.example.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moengage.core.isSdkInitialised
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper

class FirebaseReceiver : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val pushPayload = remoteMessage.data
        if (MoEPushHelper.getInstance().isFromMoEngagePlatform(pushPayload)) {
            if (isSdkInitialised()) {
                MoEFireBaseHelper.getInstance().passPushPayload(applicationContext, pushPayload)
            } else {
                // Pass the payload after SDK is initialised
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if (isSdkInitialised()) {
            MoEFireBaseHelper.getInstance().passPushToken(applicationContext, token)
        } else {
            // Pass the token after SDK is initialised
        }
    }
}