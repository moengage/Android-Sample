package com.moengage.sample.java.callbacks.push;

import androidx.annotation.NonNull;
import timber.log.Timber;

/**
 * @author Umang Chamaria
 * Date: 2020/12/29
 */
public class PushKitListener extends com.moengage.hms.pushkit.listener.PushKitEventListener {

  @Override public void onTokenAvailable(@NonNull String token) {
    super.onTokenAvailable(token);
    Timber.v("onTokenAvailable(): Token Callback Received. Token: %s", token);
    // push token received, add your processing logic here
  }
}
