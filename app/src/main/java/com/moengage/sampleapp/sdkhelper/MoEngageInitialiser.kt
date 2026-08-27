package com.moengage.sampleapp.sdkhelper

import android.app.Application
import com.moengage.core.LogLevel
import com.moengage.core.MoEngage
import com.moengage.core.config.FcmConfig
import com.moengage.core.config.LogConfig
import com.moengage.core.config.NotificationConfig
import com.moengage.core.config.RttConfig
import com.moengage.sampleapp.BuildConfig
import com.moengage.sampleapp.R
import timber.log.Timber

/** SDK construction and configuration. Reached through [MoEngageSDKHelper]. */
internal object MoEngageInitialiser {

    /** True when `local.properties` actually carries an App ID. */
    val isConfigured: Boolean
        get() = BuildConfig.YOUR_MOENGAGE_WORKSPACE_ID.isNotBlank()

    /**
     * Called from `BrewBarApp.onCreate()`. Both the workspace ID and the data centre come from
     * `local.properties` via `BuildConfig`, so nothing is hard-coded here; the data centre string is
     * resolved by [dataCenterFrom].
     */
    fun initialise(application: Application) {
        val config = MoEngage.Builder(
            application,
            BuildConfig.YOUR_MOENGAGE_WORKSPACE_ID,
            dataCenterFrom(BuildConfig.MOENGAGE_DATA_CENTER),
        )
            .configureNotificationMetaData(
                NotificationConfig(
                    smallIcon = R.drawable.ic_notification_small,
                    largeIcon = R.drawable.ic_notification_large,
                    notificationColor = R.color.brand_primary,
                    isMultipleNotificationInDrawerEnabled = true,
                    isBuildingBackStackEnabled = true,
                    isLargeIconDisplayEnabled = true,
                ),
            )
            // MoEngage registers its own FirebaseMessagingService and pulls the token itself.
            .configureFcm(FcmConfig(isRegistrationEnabled = true))
            // Device-triggered (real-time) campaigns. A device trigger is evaluated on the
            // device: the SDK holds the campaign's conditions locally and fires when a tracked
            // event satisfies them, with no server round trip. Background sync keeps those
            // conditions fresh, so a trigger can fire without the app having been opened first —
            // and means the app never has to pull them itself.
            .configureRealTimeTrigger(RttConfig(isBackgroundSyncEnabled = true))
            .configureLogs(LogConfig(LogLevel.VERBOSE, isEnabledForReleaseBuild = false))
            .build()

        MoEngage.initialiseDefaultInstance(config)

        if (!isConfigured) {
            Timber.w(
                "YOUR_MOENGAGE_WORKSPACE_ID is the placeholder — the SDK is running but reports nowhere. " +
                    "Add YOUR_MOENGAGE_WORKSPACE_ID to local.properties. See README.md.",
            )
        }
    }
}
