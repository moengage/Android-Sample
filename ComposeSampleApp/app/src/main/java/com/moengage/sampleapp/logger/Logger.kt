package com.moengage.sampleapp.logger

import com.moengage.sampleapp.state.ApplicationState
import com.moengage.sampleapp.util.BASE_TAG
import timber.log.Timber

fun log(
    tag: String,
    logLevel: LogLevel = LogLevel.VERBOSE,
    throwable: Throwable? = null,
    message: () -> String
) {
    if (!ApplicationState.isLogEnabled) return
    Timber.tag(tag = "${BASE_TAG}_$tag")
    Timber.log(
        priority = logLevel.value,
        message = message(),
        t = throwable,
    )
}