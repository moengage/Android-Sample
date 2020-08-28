package com.moengage.sample.java;

import android.app.Application;
import android.content.SharedPreferences;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.Logger;
import com.moengage.core.MoEngage;
import com.moengage.core.model.AppStatus;
import com.moengage.firebase.MoEFireBaseHelper;
import com.moengage.geofence.MoEGeofenceHelper;
import com.moengage.inapp.MoEInAppHelper;
import com.moengage.pushbase.MoEPushHelper;
import com.moengage.sample.java.callbacks.ApplicationBackgroundListener;
import com.moengage.sample.java.callbacks.GeoFenceHitListener;
import com.moengage.sample.java.callbacks.inapp.InAppCallback;
import com.moengage.sample.java.callbacks.push.FcmEventListener;

/**
 * @author Umang Chamaria
 * Date: 2019-05-31
 */
public class MoEDemo extends Application {

  @Override public void onCreate() {
    super.onCreate();
    // configure MoEngage initializer
    MoEngage moEngage =
        new MoEngage.Builder(this, "XXXXXXXX")//enter your own app id
            .setLogLevel(Logger.VERBOSE)//enabling Logs for debugging
            .enableLogsForSignedBuild() //Make sure this is removed before apps are pushed to
            // production
            .setNotificationSmallIcon(
                R.drawable.icon)//small icon should be flat, pictured face on, and must be white
            // on a transparent background.
            .setNotificationLargeIcon(R.drawable.ic_launcher)
            .enableLocationServices()//enabled To track location and run geo-fence campaigns
            .enableMultipleNotificationInDrawer()// shows multiple notifications in drawer at one go
            .build();
    // initialize MoEngage SDK
    MoEngage.initialise(moEngage);

    // install update differentiation
    trackInstallOrUpdate();

    // register for token observer
    MoEFireBaseHelper.Companion.getInstance().setEventListener(new FcmEventListener());

    // Setting CustomPushMessageListener for notification customisation
    MoEPushHelper.getInstance().setMessageListener(new CustomPushMessageListener());

    //register for app background listener
    MoEHelper.getInstance(getApplicationContext())
        .registerAppBackgroundListener(new ApplicationBackgroundListener());

    // register geo-fence hit callback
    MoEGeofenceHelper.getInstance().registerGeofenceHitListener(new GeoFenceHitListener());
    // register in-app listener
    MoEInAppHelper.getInstance().registerListener(new InAppCallback());
  }

  /**
   * Tell MoEngage SDK whether the user is a new user of the application or an existing user.
   */
  private void trackInstallOrUpdate() {
    //keys are just sample keys, use suitable keys for the apps
    SharedPreferences preferences = getSharedPreferences("demoapp", 0);
    AppStatus appStatus = AppStatus.INSTALL;
    if (preferences.getBoolean("has_sent_install", false)) {
      if (preferences.getBoolean("existing", false)) {
        appStatus = AppStatus.UPDATE;
      }
      MoEHelper.getInstance(getApplicationContext()).setAppStatus(appStatus);
      preferences.edit().putBoolean("has_sent_install", true).apply();
      preferences.edit().putBoolean("existing", true).apply();
    }
  }
}
