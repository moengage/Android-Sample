package com.moengage.sample.java.callbacks;

import android.content.Context;
import androidx.annotation.NonNull;
import com.moengage.core.listeners.AppBackgroundListener;
import timber.log.Timber;

/**
 * @author Umang Chamaria
 * Date: 2019-05-31
 */
public class ApplicationBackgroundListener implements AppBackgroundListener {

  @Override public void onAppBackground(@NonNull Context context) {
    Timber.v(" goingToBackground(): Application going to background callback received.");
    // application going to background, add your logic here.
  }
}
