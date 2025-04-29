package com.moengage.sampleapp.libaries.timber

import com.moengage.sampleapp.logger.log
import timber.log.Timber
import javax.inject.Inject

class TimberHelper @Inject constructor() {

    private val tag = "TimberHelper"

    fun initialize(isLogEnabled: Boolean) {
        log(tag) { "initialize(): " }
        if (isLogEnabled) {
            Timber.plant(Timber.DebugTree())
        }
    }
}