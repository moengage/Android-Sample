package com.moengage.sampleapp.domain

import com.moengage.sampleapp.model.Article
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    fun getAllBookmarkedArticles(): Flow<List<Article>>

    suspend fun saveArticles(articles: List<Article>)

    suspend fun isArticleBookmarked(url: String): Boolean
}