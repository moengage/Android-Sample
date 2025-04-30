package com.moengage.sampleapp.domain

import kotlinx.coroutines.flow.Flow

interface DataStorePreference {

    suspend fun saveCurrentAppVersion(appVersionCode: Long)

    fun getLastSavedAppVersion(): Flow<Long>

    suspend fun saveOnBoardingStatus(isOnBoardingCompleted: Boolean)

    fun getOnBoardingStatus(): Flow<Boolean>

    suspend fun saveUserId(userId: String)

    fun getUserId(): Flow<String?>

    suspend fun saveFirstName(firstName: String)

    fun getFirstName(): Flow<String?>

    suspend fun saveLastName(firstName: String)

    fun getLastName(): Flow<String?>

    suspend fun saveEmail(firstName: String)

    fun getEmail(): Flow<String?>

    suspend fun saveGaidStatus(optedIn: Boolean)

    fun getGaidStatus(): Flow<Boolean>

    suspend fun clearUserData()
}