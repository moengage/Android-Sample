package com.moengage.sampleapp.state

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Build
import com.moengage.sampleapp.logger.log

private const val tag = "ApplicationState"

internal object ApplicationState {
    var isDebugBuild = false

    var isLogEnabled: Boolean = false

    var osVersion: Int = Build.VERSION_CODES.M

    var appVersionName: String = ""

    var appVersionCode: Long = 0
}

@Suppress("DEPRECATION")
fun Application.initializeApplicationState() {
    log(tag) { "initializeApplicationState(): " }
    ApplicationState.isDebugBuild = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    ApplicationState.osVersion = Build.VERSION.SDK_INT
    ApplicationState.isLogEnabled = ApplicationState.isDebugBuild

    with(packageManager.getPackageInfo(packageName, 0)) {
        ApplicationState.appVersionName = versionName ?: ""
        ApplicationState.appVersionCode =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                longVersionCode
            } else {
                versionCode.toLong()
            }
    }
}