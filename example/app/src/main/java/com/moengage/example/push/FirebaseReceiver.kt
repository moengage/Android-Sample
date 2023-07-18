package com.moengage.example.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moengage.core.isSdkInitialised
import com.moengage.example.MoEngageHandler
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper

class FirebaseReceiver : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val pushPayload = remoteMessage.data
        if (MoEPushHelper.getInstance().isFromMoEngagePlatform(pushPayload)) {
            MoEngageHandler().initialiseIfRequired()
            MoEFireBaseHelper.getInstance().passPushPayload(applicationContext, pushPayload)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        MoEngageHandler().initialiseIfRequired()
        MoEFireBaseHelper.getInstance().passPushToken(applicationContext, token)
    }
}