package com.moengage.sampleapp

import android.app.Application
import com.moengage.sampleapp.ordertracking.notification.ensureOrderTrackingNotificationChannel
import com.moengage.sampleapp.push.BrewNotifications
import com.moengage.sampleapp.sdkhelper.MoEngageSDKHelper
import timber.log.Timber

/**
 * MoEngage is initialised here, before any Activity exists — this is the "SDK init" moment
 * the splash screen refers to. Everything the SDK needs at startup is registered from this
 * one place via [MoEngageSDKHelper].
 */
class BrewBarApp : Application() {

    override fun onCreate() {
        super.onCreate()
        plantLogger()
        BrewNotifications.ensureChannels(this)
        ensureOrderTrackingNotificationChannel(this)
        MoEngageSDKHelper.initialise(this)
        MoEngageSDKHelper.registerPushCallbacks()
        MoEngageSDKHelper.registerInAppCallbacks(applicationContext)
        MoEngageSDKHelper.registerGeofenceCallbacks()
    }

    /**
     * Debug builds only, so nothing is logged from a release APK. Tags come out as
     * `BrewBar/MoEngageSDKHelper` — the calling class, prefixed — so a single `BrewBar/` logcat
     * filter shows everything the app logs.
     */
    private fun plantLogger() {
        if (!BuildConfig.DEBUG) return
        Timber.plant(
            object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String =
                    "BrewBar/${super.createStackElementTag(element)}"
            },
        )
    }
}
