package com.moengage.sample.kotlin

import android.content.Intent
import com.moengage.geofence.listener.OnGeofenceHitListener

/**
 * @author Umang Chamaria
 * Date: 2019-07-11
 */
class GeoFenceHitListener : OnGeofenceHitListener {
    override fun geofenceHit(geoFenceHit: Intent): Boolean {
        // process the intent.
        // return true if the app does not want the SDK to process the Geo-fence callback else false
        return false
    }
}