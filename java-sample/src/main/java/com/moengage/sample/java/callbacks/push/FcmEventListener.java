package com.moengage.sample.java.callbacks.push;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.firebase.listener.FirebaseEventListener;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

/**
 * @author Umang Chamaria
 * Date: 2020/08/28
 */
public class FcmEventListener extends FirebaseEventListener {
  @Override public void onNonMoEngageMessageReceived(@NotNull RemoteMessage remoteMessage) {
    Timber.v("onNonMoEngageMessageReceived(): " + remoteMessage);
    // payload received, add your processing logic here
  }

  @Override public void onTokenAvailable(@NotNull String token) {
    Timber.v("onTokenAvailable(): Token Callback Received. Token: %s", token);
    // push token received, add your processing logic here
  }
}
