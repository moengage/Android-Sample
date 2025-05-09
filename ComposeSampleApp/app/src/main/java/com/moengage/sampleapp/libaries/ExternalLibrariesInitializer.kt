package com.moengage.sampleapp.libaries

import com.moengage.sampleapp.libaries.moengage.MoEngageInitializer
import com.moengage.sampleapp.libaries.timber.TimberHelper
import com.moengage.sampleapp.logger.log
import com.moengage.sampleapp.state.ApplicationState
import javax.inject.Inject

class ExternalLibrariesInitializer @Inject constructor() {

    private val tag = "ExternalLibrariesInitializer"

    @Inject
    lateinit var timberHelper: TimberHelper

    @Inject
    lateinit var moEngageInitializer: MoEngageInitializer

    fun initialize() {
        log(tag) { "initialize(): " }
        timberHelper.initialize(ApplicationState.isLogEnabled)
        moEngageInitializer.initialize()
    }
}