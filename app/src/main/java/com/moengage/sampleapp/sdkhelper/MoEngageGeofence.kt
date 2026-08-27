package com.moengage.sampleapp.sdkhelper

import android.content.Context
import com.moengage.geofence.MoEGeofenceHelper
import timber.log.Timber

/**
 * Location-triggered campaigns. Reached through [MoEngageSDKHelper].
 *
 * Monitoring is **not** started at init: it needs location permission, and starting it before the
 * user has granted any would just fail silently. The app grants first, then calls
 * [startMonitoring] — see `SessionViewModel.onLocationPermissionResult`.
 */
internal object MoEngageGeofence {

    /**
     * Registered once at startup. Returning `false` leaves the SDK to render the campaign itself;
     * an app that wanted to draw its own UI for a geofence hit would return `true` instead.
     */
    fun registerHitListener() = guarded {
        MoEGeofenceHelper.getInstance().addListener { geofenceData ->
            Timber.d("geofence hit: %s", geofenceData.intent)
            false
        }
    }

    /** Starts fetching and monitoring the workspace's geofences. Requires location permission. */
    fun startMonitoring(context: Context) = guarded {
        MoEGeofenceHelper.getInstance().startGeofenceMonitoring(context)
        Timber.d("geofence monitoring started")
    }

    /** Stops monitoring; the counterpart to [startMonitoring] when the user opts out. */
    fun stopMonitoring(context: Context) = guarded {
        MoEGeofenceHelper.getInstance().stopGeofenceMonitoring(context)
        Timber.d("geofence monitoring stopped")
    }
}
