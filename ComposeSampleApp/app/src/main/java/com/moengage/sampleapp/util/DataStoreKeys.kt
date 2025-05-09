package com.moengage.sampleapp.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

const val DATA_STORE_NAME = "moEngageNewsSampleApp"

val ONBOARDING_STATUS = booleanPreferencesKey("onBoardingStatus")
val APP_VERSION_CODE = longPreferencesKey("appVersionCode")
val USER_ID = stringPreferencesKey("userId")
val FIRST_NAME = stringPreferencesKey("firstName")
val LAST_NAME = stringPreferencesKey("lastName")
val EMAIL = stringPreferencesKey("email")
val GAID_STATUS = booleanPreferencesKey("gaidStatus")