package com.moengage.sample.java.callbacks;

import com.moengage.core.listeners.OnAppBackgroundListener;
import timber.log.Timber;

/**
 * @author Umang Chamaria
 * Date: 2019-05-31
 */
public class ApplicationBackgroundListener implements OnAppBackgroundListener {

  @Override public void goingToBackground() {
    Timber.v(" goingToBackground(): Application going to background callback received.");
    // application going to background, add your logic here.
  }
}
