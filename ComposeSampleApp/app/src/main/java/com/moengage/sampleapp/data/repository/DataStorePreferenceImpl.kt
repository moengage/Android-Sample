package com.moengage.sampleapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.moengage.sampleapp.domain.DataStorePreference
import com.moengage.sampleapp.util.APP_VERSION_CODE
import com.moengage.sampleapp.util.DATA_STORE_NAME
import com.moengage.sampleapp.util.EMAIL
import com.moengage.sampleapp.util.FIRST_NAME
import com.moengage.sampleapp.util.GAID_STATUS
import com.moengage.sampleapp.util.LAST_NAME
import com.moengage.sampleapp.util.ONBOARDING_STATUS
import com.moengage.sampleapp.util.USER_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStorePreferenceImpl @Inject constructor(
    @ApplicationContext val context: Context
) : DataStorePreference {

    override suspend fun saveCurrentAppVersion(appVersionCode: Long) {
        context.dataStore.edit { settings ->
            settings[APP_VERSION_CODE] = appVersionCode
        }
    }

    override fun getLastSavedAppVersion(): Flow<Long> {
        return context.dataStore.data.map { preference ->
            preference[APP_VERSION_CODE] ?: -1L
        }
    }

    override suspend fun saveOnBoardingStatus(isOnBoardingCompleted: Boolean) {
        context.dataStore.edit { settings ->
            settings[ONBOARDING_STATUS] = isOnBoardingCompleted
        }
    }

    override fun getOnBoardingStatus(): Flow<Boolean> {
        return context.dataStore.data.map { preference ->
            preference[ONBOARDING_STATUS] == true
        }
    }

    override suspend fun saveUserId(userId: String) {
        context.dataStore.edit { settings ->
            settings[USER_ID] = userId
        }
    }

    override fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preference ->
            preference[USER_ID]
        }
    }

    override suspend fun saveFirstName(firstName: String) {
        context.dataStore.edit { settings ->
            settings[FIRST_NAME] = firstName
        }
    }


    override fun getFirstName(): Flow<String?> {
        return context.dataStore.data.map { preference ->
            preference[FIRST_NAME]
        }
    }

    override suspend fun saveLastName(lastName: String) {
        context.dataStore.edit { settings ->
            settings[LAST_NAME] = lastName
        }
    }

    override fun getLastName(): Flow<String?> {
        return context.dataStore.data.map { preference ->
            preference[LAST_NAME]
        }
    }

    override suspend fun saveEmail(email: String) {
        context.dataStore.edit { settings ->
            settings[EMAIL] = email
        }
    }

    override fun getEmail(): Flow<String?> {
        return context.dataStore.data.map { preference ->
            preference[EMAIL]
        }
    }

    override suspend fun saveGaidStatus(optedIn: Boolean) {
        context.dataStore.edit { settings ->
            settings[GAID_STATUS] = optedIn
        }
    }

    override fun getGaidStatus(): Flow<Boolean> {
        return context.dataStore.data.map { preference ->
            preference[GAID_STATUS] == true
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)