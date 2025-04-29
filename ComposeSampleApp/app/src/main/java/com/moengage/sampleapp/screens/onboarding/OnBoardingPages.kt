package com.moengage.sampleapp.screens.onboarding

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import com.moengage.geofence.MoEGeofenceHelper
import com.moengage.pushbase.MoEPushHelper
import com.moengage.sampleapp.R

@SuppressLint("InlinedApi")
fun getOnBoardingPages(context: Context): List<OnBoardingPageItem> {
    return listOf(
        OnBoardingPageItem(
            icon = R.drawable.icon_app,
            title = "Notification",
            description = "We need the permission to display the notification",
            permission = Manifest.permission.POST_NOTIFICATIONS,
            minSdkVersion = Build.VERSION_CODES.TIRAMISU,
            postPermissionCallback = { hasPermission ->
                MoEPushHelper.getInstance().pushPermissionResponse(context, hasPermission)
            }
        ),

        OnBoardingPageItem(
            icon = R.drawable.icon_app,
            title = "Geofence",
            description = "We need the coarse location permission to display the location based notification",
            permission = Manifest.permission.ACCESS_COARSE_LOCATION,
        ),

        OnBoardingPageItem(
            icon = R.drawable.icon_app,
            title = "Geofence",
            description = "We need the fine location permission to display the location based notification",
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
        ),

        OnBoardingPageItem(
            icon = R.drawable.icon_app,
            title = "Geofence",
            description = "We need the background location permission to display the location based notification",
            permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            postPermissionCallback = { hasPermission ->
                if (hasPermission) {
                    MoEGeofenceHelper.getInstance().startGeofenceMonitoring(context)
                }
            }
        ),

        OnBoardingPageItem(
            icon = R.drawable.icon_app,
            title = "Exact Alarm",
            description = "We need the exact alarm permission to display the timer with progress bar template",
            isSpecialPermission = true,
            permissionAction = ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
            permission = Manifest.permission.SCHEDULE_EXACT_ALARM,
        )
    )
}