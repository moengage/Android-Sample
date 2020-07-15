package com.moengage.sample.java.callbacks;

import android.content.Intent;
import com.moengage.geofence.listener.OnGeofenceHitListener;

/**
 * @author Umang Chamaria
 * Date: 2019-07-11
 */
public class  GeoFenceHitListener implements OnGeofenceHitListener {
  @Override public boolean geofenceHit(Intent geoFenceHit) {
    // process the intent.
    // return true if the app does not want the SDK to process the Geo-fence callback else false
    return false;
  }
}
