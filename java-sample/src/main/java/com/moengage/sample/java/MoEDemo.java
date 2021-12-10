package com.moengage.sample.java;

import android.app.Application;
import android.content.SharedPreferences;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.LogLevel;
import com.moengage.core.MoECallbacks;
import com.moengage.core.MoEngage;
import com.moengage.core.config.GeofenceConfig;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.MiPushConfig;
import com.moengage.core.config.NotificationConfig;
import com.moengage.core.config.PushKitConfig;
import com.moengage.core.model.AppStatus;
import com.moengage.firebase.MoEFireBaseHelper;
import com.moengage.geofence.MoEGeofenceHelper;
import com.moengage.hms.pushkit.MoEPushKitHelper;
import com.moengage.inapp.MoEInAppHelper;
import com.moengage.pushbase.MoEPushHelper;
import com.moengage.sample.java.callbacks.ApplicationBackgroundListener;
import com.moengage.sample.java.callbacks.GeoFenceHitListener;
import com.moengage.sample.java.callbacks.inapp.InAppCallback;
import com.moengage.sample.java.callbacks.push.FcmEventListener;
import com.moengage.sample.java.callbacks.push.PushKitListener;
import timber.log.Timber;

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
            .configureLogs(new LogConfig(LogLevel.VERBOSE, false))
            // production
            .configureNotificationMetaData(
                new NotificationConfig(R.drawable.icon, R.drawable.ic_launcher, R.color.colorAccent,
                    null, true, false, true))
            .configurePushKit(new PushKitConfig(true)) // push kit token registration handled by the
            // SDK
            .configureMiPush(new MiPushConfig("xxxx", "yyyy", true)) // replace xxxx and yyyy
            // with the app-key and app-id from Mi Console.
            .configureGeofence(new GeofenceConfig(true, true))
            .build();
    // initialize MoEngage SDK
    MoEngage.initialise(moEngage);

    // install update differentiation
    trackInstallOrUpdate();

    // FCM event listener
    MoEFireBaseHelper.getInstance().addEventListener(new FcmEventListener());
    // PushKit Event listener
    MoEPushKitHelper.getInstance().addEventListener(new PushKitListener());

    // Setting CustomPushMessageListener for notification customisation
    MoEPushHelper.getInstance().setMessageListener(new CustomPushMessageListener());

    //register for app background listener
    MoECallbacks.getInstance().addAppBackgroundListener(new ApplicationBackgroundListener());

    // register geo-fence hit callback
    MoEGeofenceHelper.getInstance().addListener(new GeoFenceHitListener());
    // register in-app listener
    MoEInAppHelper.getInstance().registerListener(new InAppCallback());

    // logout complete listener
    MoECallbacks.getInstance().addLogoutCompleteListener(() -> {
      Timber.v("logoutComplete(): Logout Complete.");
    });
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
