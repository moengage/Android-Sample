package com.moengage.sampleapp.data.repository

import com.moengage.sampleapp.data.db.ArticleDao
import com.moengage.sampleapp.domain.LocalRepository
import com.moengage.sampleapp.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val articleDao: ArticleDao
) : LocalRepository {

    override fun getAllBookmarkedArticles(): Flow<List<Article>> {
        return articleDao.getAllBookmarkedArticles()
    }

    override suspend fun saveArticles(articles: List<Article>) {
        articleDao.saveArticles(*articles.toTypedArray())
    }

    override suspend fun isArticleBookmarked(url: String): Boolean {
        return articleDao.isArticleBookmarked(url)
    }
}