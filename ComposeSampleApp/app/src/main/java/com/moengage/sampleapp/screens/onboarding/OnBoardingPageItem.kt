package com.moengage.sampleapp.screens.onboarding

import android.os.Build
import androidx.annotation.DrawableRes

class OnBoardingPageItem(
    @DrawableRes val icon: Int,
    val title: String,
    val description: String,
    val permission: String,
    val isSpecialPermission: Boolean = false,
    val permissionAction: String? = null,
    val minSdkVersion: Int = Build.VERSION_CODES.M,
    val postPermissionCallback: (isGranted: Boolean) -> Unit = {}
)