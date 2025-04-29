package com.moengage.sampleapp.screens.home

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationBarItem(
    val label: String,
    val icon: ImageVector,
    val badgeCount: Int,
)