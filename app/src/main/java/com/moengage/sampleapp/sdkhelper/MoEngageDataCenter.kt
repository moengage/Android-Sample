package com.moengage.sampleapp.sdkhelper

import com.moengage.core.DataCenter
import timber.log.Timber

/** Used when `MOENGAGE_DATA_CENTER` is absent, blank or unrecognised. */
private val DEFAULT_DATA_CENTER = DataCenter.DATA_CENTER_1

/**
 * Maps the `MOENGAGE_DATA_CENTER` value from `local.properties` onto the SDK's [DataCenter].
 *
 * The build passes the raw string through `BuildConfig`, so this is the one place that decides what
 * a human may write there. Accepted, case-insensitively and ignoring surrounding whitespace:
 *
 * - `DATA_CENTER_3` — the form the MoEngage dashboard and docs use
 * - `dc3` — the SDK's own underlying value
 * - `3` — just the digits
 *
 * Anything unrecognised falls back to [DEFAULT_DATA_CENTER] with a warning rather than failing the
 * launch: a sample app should still start when its configuration is half-filled.
 */
internal fun dataCenterFrom(raw: String): DataCenter {
    val digits = raw.trim().takeLastWhile { it.isDigit() }
    return when (digits) {
        "1" -> DataCenter.DATA_CENTER_1
        "2" -> DataCenter.DATA_CENTER_2
        "3" -> DataCenter.DATA_CENTER_3
        "4" -> DataCenter.DATA_CENTER_4
        "5" -> DataCenter.DATA_CENTER_5
        "6" -> DataCenter.DATA_CENTER_6
        "101" -> DataCenter.DATA_CENTER_101
        else -> {
            Timber.w(
                "MOENGAGE_DATA_CENTER=\"%s\" is not a data centre this app recognises — " +
                    "falling back to DATA_CENTER_1. Use DATA_CENTER_1…6, DATA_CENTER_101, or the digits.",
                raw,
            )
            DEFAULT_DATA_CENTER
        }
    }
}
