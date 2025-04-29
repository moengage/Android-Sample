package com.moengage.sampleapp.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.net.toUri

fun ComponentActivity.openPermissionPage(permissionAction: String? = null) {
    if (permissionAction != null) {
        Intent(permissionAction).also(::startActivity)
    } else {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        ).also(::startActivity)
    }
}

fun Context.openDeeplink(link: String) {
    Intent(Intent.ACTION_VIEW, link.toUri()).also(::startActivity)
}