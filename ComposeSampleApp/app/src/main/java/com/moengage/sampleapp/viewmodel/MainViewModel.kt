package com.moengage.sampleapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.moengage.sampleapp.data.repository.Repository
import com.moengage.sampleapp.model.Article
import com.moengage.sampleapp.data.paging.NewsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val articlePager = lazy {
        Pager(PagingConfig(pageSize = 10)) { NewsPagingSource(repository) }.flow.cachedIn(
            viewModelScope
        )
    }.value

    val bookmarkedArticles = lazy {
        repository.getAllBookmarkedArticles()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = emptyList()
            )
    }.value

    fun addToBookmark(articles: List<Article>) {
        viewModelScope.launch {
            repository.saveArticles(articles)
        }
    }
}