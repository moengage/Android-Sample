package com.moengage.sampleapp.sdkhelper

import timber.log.Timber

/**
 * Every SDK entry point throws `SdkNotInitializedException` when the App ID is missing.
 * A sample app must still be clickable in that state, so failures are logged, not fatal.
 *
 * Shared by every feature object in this package.
 */
internal inline fun guarded(block: () -> Unit) {
    try {
        block()
    } catch (throwable: Throwable) {
        Timber.w(throwable, "MoEngage call skipped")
    }
}
