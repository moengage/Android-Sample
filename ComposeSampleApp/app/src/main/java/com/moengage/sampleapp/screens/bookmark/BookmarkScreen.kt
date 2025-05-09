package com.moengage.sampleapp.screens.bookmark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.moengage.sampleapp.libaries.moengage.MoEngageHelper
import com.moengage.sampleapp.screens.common.ArticleList
import com.moengage.sampleapp.viewmodel.MainViewModel

@Composable
fun BookmarkScreen(viewModel: MainViewModel = hiltViewModel()) {

    MoEngageHelper.showInApp(LocalContext.current)

    val articles = viewModel.bookmarkedArticles.collectAsState(emptyList())
    ArticleList(articles = articles, viewModel)
}