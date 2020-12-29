package com.moengage.sample.kotlin.callbacks.push

import com.moengage.hms.pushkit.listener.PushKitEventListener
import timber.log.Timber

class PushKitListener : PushKitEventListener() {
    override fun onTokenAvailable(token: String) {
        super.onTokenAvailable(token)
        Timber.v("onTokenAvailable(): Token Callback Received. Token: %s", token)
        // push token received, add your processing logic here
    }
}