package com.moengage.sample.java;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;
import com.moengage.core.ConfigurationProvider;
import com.moengage.pushbase.push.PushMessageListener;

public class CustomPushMessageListener extends PushMessageListener {

  // decide whether notification should be shown or not. If super() returns false this method
  // should return false. In case super() isn't called notification will not be displayed.
  @Override public boolean isNotificationRequired(Context context, Bundle payload) {
    boolean shouldDisplayNotification = super.isNotificationRequired(context, payload);
    // do not show notification if MoEngage SDK returns false.
    if (shouldDisplayNotification){
      // app's logic to decide whether to show notification or not.
      // for illustration purpose reading notification preference from SharedPreferences and
      // deciding whether to show notification or not. Logic can vary from application to
      // application.
      SharedPreferences preferences = context.getSharedPreferences("demoapp", 0);
      return preferences.getBoolean("notification_preference", true);
    }
   return shouldDisplayNotification;
  }

  // customise the notification builder object as required
  @Override public Builder onCreateNotification(Context context, Bundle payload,
      ConfigurationProvider provider) {
    // get the object constructed by MoEngage SDK
    NotificationCompat.Builder builder = super.onCreateNotification(context, payload, provider);
    // customise as required.
    // below customisation is only for illustration purpose. You can chose to have other
    // customisations as required by the application.
    builder.setOngoing(true);
    // return the builder object to the SDK for posting notification.
    return builder;
  }

  @Override public void onNotificationCleared(Context context, Bundle payload) {
    super.onNotificationCleared(context, payload);
    // callback for notification cleared.
  }

  @Override public void onNotificationReceived(Context context, Bundle payload) {
    super.onNotificationReceived(context, payload);
    //callback for push notification received.
  }

  @Override public void onHandleRedirection(Activity activity, Bundle payload) {
    super.onHandleRedirection(activity, payload);
    //callback for notification clicked. if you want to handle redirection then do not call super()
    // and add the redirection logic here.
  }
}