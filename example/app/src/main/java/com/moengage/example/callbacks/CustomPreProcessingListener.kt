package com.moengage.example.callbacks

import com.moengage.core.listeners.IntentPreProcessingListener
import logcat.logcat

class CustomPreProcessingListener: IntentPreProcessingListener {

    override fun onIntentReceived() {
        logcat { "onIntentReceived(): Trying to process intent before SDK Initialization" }

        // Initialize SDK here to process the intent
    }
}