package com.moengage.sampleapp.screens.article

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.moengage.sampleapp.libaries.moengage.MoEngageHelper
import com.moengage.sampleapp.screens.common.ArticleList
import com.moengage.sampleapp.viewmodel.MainViewModel

@Composable
fun ArticleScreen(viewModel: MainViewModel = hiltViewModel()) {

    MoEngageHelper.showInApp(LocalContext.current)

    val articles = viewModel.articlePager.collectAsLazyPagingItems()
    ArticleList(articles = articles, viewModel = viewModel)
}