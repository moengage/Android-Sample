package com.moengage.sampleapp.screens.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.moengage.sampleapp.model.Article
import com.moengage.sampleapp.viewmodel.MainViewModel

@Composable
fun ArticleList(
    articles: State<List<Article>>,
    mainViewModel: MainViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(articles.value.size) {
            ArticleItem(articles.value[it], mainViewModel)
        }
    }
}

@Composable
fun ArticleList(
    articles: LazyPagingItems<Article>,
    viewModel: MainViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(articles.itemCount) {
            articles[it]?.let {
                ArticleItem(it, viewModel)
            }
        }
    }
}


@Composable
fun ArticleItem(article: Article, mainViewModel: MainViewModel) {
    ArticleTile(article = article, mainViewModel = mainViewModel)

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
    )

    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp
    )

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
    )
}