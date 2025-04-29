package com.moengage.sampleapp.libaries.moengage

import android.content.Context
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.disableAdIdTracking
import com.moengage.core.enableAdIdTracking
import com.moengage.core.model.AppStatus
import com.moengage.inapp.MoEInAppHelper
import com.moengage.inapp.model.enums.InAppPosition

object MoEngageHelper {

    fun showInApp(context: Context) {
        MoEInAppHelper.getInstance().showNudge(context, InAppPosition.BOTTOM_RIGHT)
    }

    fun trackEvent(context: Context, action: String, properties: Map<String, Any?>) {
        MoEAnalyticsHelper.trackEvent(context, action, Properties().apply {
            properties.forEach { (key, value) ->
                addAttribute(key, value)
            }
        })
    }

    fun setEmail(context: Context, email: String) {
        MoEAnalyticsHelper.setEmailId(context, email)
    }

    fun serIdentifier(context: Context, userId: String) {
        MoEAnalyticsHelper.identifyUser(context, userId)
    }

    fun setFirstName(context: Context, firstName: String) {
        MoEAnalyticsHelper.setFirstName(context, firstName)
    }

    fun setLastName(context: Context, lastName: String) {
        MoEAnalyticsHelper.setLastName(context, lastName)
    }

    fun changeGaidStatus(context: Context, optedIn: Boolean) {
        if (optedIn) {
            enableAdIdTracking(context)
        } else {
            disableAdIdTracking(context)
        }
    }

    fun trackInstall(context: Context) {
        MoEAnalyticsHelper.setAppStatus(context, AppStatus.INSTALL)
    }

    fun trackUpdate(context: Context) {
        MoEAnalyticsHelper.setAppStatus(context, AppStatus.UPDATE)
    }

    fun onScreenChange(screenName: String) {
        MoEInAppHelper.getInstance().setInAppContext(setOf(screenName))
    }
}