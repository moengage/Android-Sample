package com.moengage.sampleapp.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star

fun getNavigationBarItems(): List<BottomNavigationBarItem> {
    return listOf(
        BottomNavigationBarItem("News", Icons.Default.Home, 0),
        BottomNavigationBarItem("Bookmark", Icons.Default.Star, 0),
        BottomNavigationBarItem("Setting", Icons.Default.Settings, 0),
    )
}