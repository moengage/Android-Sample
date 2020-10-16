package com.moengage.sample.kotlin.callbacks.inapp

import com.moengage.inapp.listeners.InAppMessageListener
import com.moengage.inapp.model.MoEInAppCampaign
import timber.log.Timber

/**
 * @author Umang Chamaria
 * Date: 2020-02-21
 */
class InAppCallback : InAppMessageListener() {
    override fun onShown(inAppCampaign: MoEInAppCampaign) {
        super.onShown(inAppCampaign)
        Timber.v("InApp Shown: %s", inAppCampaign)
    }

    override fun onNavigation(inAppCampaign: MoEInAppCampaign): Boolean {
        Timber.v("Navigation Action: %s", inAppCampaign)
        // return true if the application is handling the navigation else false.
        return super.onNavigation(inAppCampaign)
    }

    override fun onClosed(inAppCampaign: MoEInAppCampaign) {
        super.onClosed(inAppCampaign)
        Timber.v("InApp Closed: %s", inAppCampaign)
    }

    override fun onCustomAction(inAppCampaign: MoEInAppCampaign) {
        super.onCustomAction(inAppCampaign)
        Timber.v("Custom Action: %s", inAppCampaign)
    }

    override fun onSelfHandledAvailable(inAppCampaign: MoEInAppCampaign) {
        super.onSelfHandledAvailable(inAppCampaign)
        Timber.v("Self handled campaign: %s", inAppCampaign)
    }
}
