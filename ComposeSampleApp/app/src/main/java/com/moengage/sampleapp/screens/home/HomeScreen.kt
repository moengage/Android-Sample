package com.moengage.sampleapp.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moengage.sampleapp.libaries.moengage.MoEngageHelper
import com.moengage.sampleapp.screens.article.ArticleScreen
import com.moengage.sampleapp.screens.bookmark.BookmarkScreen
import com.moengage.sampleapp.screens.common.Toolbar
import com.moengage.sampleapp.screens.setting.SettingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navItemList = getNavigationBarItems()
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Toolbar()
                    }
                )
                HorizontalDivider(thickness = 2.dp)
            }
        },
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "Icon")
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            ContentScreen(selectedIndex = selectedIndex)
        }
    }
}

@Composable
fun ContentScreen(selectedIndex: Int) {
    when (selectedIndex) {
        0 -> {
            MoEngageHelper.onScreenChange("ArticleScreen")
            ArticleScreen()
        }

        1 -> {
            MoEngageHelper.onScreenChange("BookmarkScreen")
            BookmarkScreen()
        }

        2 -> {
            MoEngageHelper.onScreenChange("SettingScreen")
            SettingScreen()
        }
    }
}