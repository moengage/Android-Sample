package com.moengage.mutliple_instances.push

import com.moengage.geofence.listener.OnGeofenceHitListener
import com.moengage.geofence.model.GeofenceData
import logcat.logcat

/**
 * @author Umang Chamaria
 * Date: 2022/02/15
 */
class GeofenceHitListener: OnGeofenceHitListener {
    override fun geofenceHit(geofenceData: GeofenceData): Boolean {
        logcat { "geofenceHit() Geofence hit callback received. Callback data: $geofenceData" }
        // process the intent.
        // return true if the app does not want the SDK to process the Geo-fence callback else false
        return false
    }
}