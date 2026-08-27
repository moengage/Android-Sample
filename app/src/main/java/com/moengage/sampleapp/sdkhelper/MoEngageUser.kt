package com.moengage.sampleapp.sdkhelper

import android.content.Context
import com.moengage.core.MoECoreHelper
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.sampleapp.data.DemoUser

/** Identity and user attributes. Reached through [MoEngageSDKHelper]. */
internal object MoEngageUser {

    /** User-attribute keys. */
    object UserAttrs {
        const val FAVOURITE_DRINK = "favourite_drink"
        const val MILK_PREFERENCE = "milk_preference"
        const val SWEETNESS = "sweetness"
        const val HOME_STORE = "home_store"
        const val PUSH_OPT_IN = "push_opt_in"
        const val OFFERS_OPT_IN = "offers_opt_in"
        const val MARKETING_OPT_IN = "marketing_opt_in"
    }

    /** Screen 2 — after OTP verification (or the Google button). */
    fun onLoginSucceeded(context: Context) = guarded {
        // `setUniqueId` is deprecated on this SDK line; `identifyUser` is its replacement.
        MoEAnalyticsHelper.identifyUser(context, DemoUser.ID)
        MoEAnalyticsHelper.setMobileNumber(context, DemoUser.PHONE_E164)
        MoEAnalyticsHelper.setUserName(context, DemoUser.NAME)
        MoEAnalyticsHelper.setFirstName(context, DemoUser.FIRST_NAME)
        MoEAnalyticsHelper.setLastName(context, DemoUser.LAST_NAME)
        MoEAnalyticsHelper.setEmailId(context, DemoUser.EMAIL)
        MoEAnalyticsHelper.setBirthDate(context, DemoUser.taste.birthdayIso)
    }

    /** Screen 10 — the taste profile is mirrored onto the MoEngage user. */
    fun syncTasteProfile(context: Context) = guarded {
        MoEAnalyticsHelper.setUserAttribute(context, UserAttrs.FAVOURITE_DRINK, DemoUser.taste.favouriteDrink)
        MoEAnalyticsHelper.setUserAttribute(context, UserAttrs.MILK_PREFERENCE, DemoUser.taste.milk)
        MoEAnalyticsHelper.setUserAttribute(context, UserAttrs.SWEETNESS, DemoUser.taste.sweetness)
        MoEAnalyticsHelper.setUserAttribute(context, UserAttrs.HOME_STORE, DemoUser.taste.homeStore)
    }

    /** Screen 10 — the two in-app notification-category toggles. */
    fun setNotificationPreference(context: Context, key: String, enabled: Boolean) = guarded {
        val attribute = when (key) {
            "offers_new_menu" -> UserAttrs.OFFERS_OPT_IN
            "marketing_campaigns" -> UserAttrs.MARKETING_OPT_IN
            else -> UserAttrs.PUSH_OPT_IN
        }
        MoEAnalyticsHelper.setUserAttribute(context, attribute, enabled)
    }

    /** Screen 10 — "Log out". */
    fun logout(context: Context) = guarded {
        MoECoreHelper.logoutUser(context)
    }
}
