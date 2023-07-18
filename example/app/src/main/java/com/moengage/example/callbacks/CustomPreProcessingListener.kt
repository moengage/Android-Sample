package com.moengage.example.callbacks

import com.moengage.core.listeners.IntentPreProcessingListener
import com.moengage.example.MoEngageHandler
import logcat.logcat

class CustomPreProcessingListener: IntentPreProcessingListener {

    override fun onIntentReceived() {
        logcat { "onIntentReceived(): Trying to process intent before SDK Initialization" }
        MoEngageHandler().initialiseIfRequired()
    }
}