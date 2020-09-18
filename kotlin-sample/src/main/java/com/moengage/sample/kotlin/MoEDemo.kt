package com.moengage.sample.kotlin

import android.app.Application
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Logger
import com.moengage.core.MoECallbacks
import com.moengage.core.MoEngage
import com.moengage.core.model.AppStatus
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.geofence.MoEGeofenceHelper
import com.moengage.inapp.MoEInAppHelper
import com.moengage.pushbase.MoEPushHelper
import com.moengage.sample.kotlin.callbacks.ApplicationBackgroundListener
import com.moengage.sample.kotlin.callbacks.inapp.InAppCallback
import com.moengage.sample.kotlin.callbacks.push.FcmEventListener
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * @author Umang Chamaria
 */
class MoEDemo : Application() {

  override fun onCreate() {
    super.onCreate()
    // initialising logger not required for SDK.
    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }
    // configure MoEngage initializer
    val moEngage = MoEngage.Builder(this, "XXXXXXXX")//enter your own app id
        .setLogLevel(Logger.VERBOSE)//enabling Logs for debugging
        .enableLogsForSignedBuild() //Make sure this is removed before apps are pushed to
        // production
        .setNotificationSmallIcon(
            R.drawable.icon)//small icon should be flat, pictured face on, and must be white
        // on a transparent background.
        .setNotificationLargeIcon(R.drawable.ic_launcher)
        .enableLocationServices()//enabled To track location and run geo-fence campaigns
        .enableMultipleNotificationInDrawer()// shows multiple notifications in drawer at one go
        .build()
    // initialize MoEngage SDK
    MoEngage.initialise(moEngage)

    // install update differentiation
    trackInstallOrUpdate()

    // Setting CustomPushMessageListener for notification customisation
    MoEPushHelper.getInstance().messageListener = CustomPushMessageListener()

    //FCM Event Listener.
    MoEFireBaseHelper.getInstance().setEventListener(FcmEventListener())

    //register for app background listener
    MoECallbacks.getInstance().addAppBackgroundListener(ApplicationBackgroundListener())

    // register geo-fence hit callback
    MoEGeofenceHelper.getInstance().registerGeofenceHitListener(GeoFenceHitListener())
    // register in-app listener
    MoEInAppHelper.getInstance().registerListener(InAppCallback())
  }

  /**
   * Tell MoEngage SDK whether the user is a new user of the application or an existing user.
   */
  private fun trackInstallOrUpdate() {
    //keys are just sample keys, use suitable keys for the apps
    val preferences = getSharedPreferences("demoapp", 0)
    var appStatus = AppStatus.INSTALL
    if (preferences.getBoolean("has_sent_install", false)) {
      if (preferences.getBoolean("existing", false)) {
        appStatus = AppStatus.UPDATE
      }
      // passing install/update to MoEngage SDK
      MoEHelper.getInstance(applicationContext).setAppStatus(appStatus)
      preferences.edit().putBoolean("has_sent_install", true).apply()
      preferences.edit().putBoolean("existing", true).apply()
    }
  }
}
