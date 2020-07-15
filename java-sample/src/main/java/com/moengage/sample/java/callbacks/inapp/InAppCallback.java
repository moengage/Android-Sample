package com.moengage.sample.java.callbacks.inapp;

import androidx.annotation.NonNull;
import com.moengage.inapp.listeners.InAppMessageListener;
import com.moengage.inapp.model.MoEInAppCampaign;
import timber.log.Timber;

/**
 * @author Umang Chamaria
 * Date: 2020-02-21
 */
public class InAppCallback extends InAppMessageListener {

  @Override public void onShown(@NonNull MoEInAppCampaign inAppCampaign) {
    super.onShown(inAppCampaign);
    Timber.v("InApp Shown: %s", inAppCampaign);
  }

  @Override public boolean onNavigation(@NonNull MoEInAppCampaign inAppCampaign) {
    Timber.v("Navigation Action: %s", inAppCampaign);
    // return true if the application is handling the navigation else false.
    return super.onNavigation(inAppCampaign);
  }

  @Override public void onClosed(@NonNull MoEInAppCampaign inAppCampaign) {
    super.onClosed(inAppCampaign);
    Timber.v("InApp Closed: %s", inAppCampaign);
  }

  @Override public void onCustomAction(@NonNull MoEInAppCampaign inAppCampaign) {
    super.onCustomAction(inAppCampaign);
    Timber.v("Custom Action: %s", inAppCampaign);
  }

  @Override public void onSelfHandledAvailable(@NonNull MoEInAppCampaign inAppCampaign) {
    super.onSelfHandledAvailable(inAppCampaign);
    Timber.v("Self handled campaign: %s", inAppCampaign);
  }
}
