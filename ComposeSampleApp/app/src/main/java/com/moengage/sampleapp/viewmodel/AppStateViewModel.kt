package com.moengage.sampleapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moengage.sampleapp.data.repository.Repository
import com.moengage.sampleapp.libaries.moengage.MoEngageHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppStateViewModel @Inject constructor(
    private val application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    fun updateAppVersion(appVersionCode: Long) {
        viewModelScope.launch {
            repository.getLastSavedAppVersion().collectLatest { lastSavedVersion ->
                if (lastSavedVersion == -1L) {
                    MoEngageHelper.trackInstall(application)
                } else if (appVersionCode != lastSavedVersion) {
                    MoEngageHelper.trackUpdate(application)
                }

                repository.saveCurrentAppVersion(appVersionCode)
            }
        }
    }
}