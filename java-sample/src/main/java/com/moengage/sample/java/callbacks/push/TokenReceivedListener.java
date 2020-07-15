package com.moengage.sample.java.callbacks.push;

import com.moengage.push.PushManager;
import timber.log.Timber;

/**
 * @author Umang Chamaria
 * Date: 2019-05-31
 */
public class TokenReceivedListener implements PushManager.OnTokenReceivedListener {
  @Override public void onTokenReceived(String token) {
    Timber.v("onTokenReceived(): Token Callback Received. Token: %s", token);
    // push token received, add your processing logic here
  }
}
