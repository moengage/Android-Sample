package com.moengage.sampleapp

import android.app.Application
import android.os.StrictMode
import com.moengage.sampleapp.libaries.ExternalLibrariesInitializer
import com.moengage.sampleapp.state.ApplicationState
import com.moengage.sampleapp.state.initializeApplicationState
import com.moengage.sampleapp.viewmodel.AppStateViewModel
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    @Inject
    lateinit var externalLibrariesInitializer: ExternalLibrariesInitializer

    @Inject
    lateinit var appStateViewModel: AppStateViewModel

    override fun onCreate() {
        super.onCreate()

        enableStrictThreadMode()
        initializeApplicationState()
        externalLibrariesInitializer.initialize()
        appStateViewModel.updateAppVersion(ApplicationState.appVersionCode)
    }

    private fun enableStrictThreadMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
    }
}