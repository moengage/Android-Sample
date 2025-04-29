package com.moengage.sampleapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moengage.sampleapp.data.repository.Repository
import com.moengage.sampleapp.model.Article
import com.moengage.sampleapp.util.API_KEY
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val repository: Repository
) : PagingSource<Int, Article>() {

    private var fetchedArticlesSize = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val currentPage = params.key ?: 1
            val articlesResponse = repository.getArticles(
                page = currentPage,
                apiKey = API_KEY
            )
            fetchedArticlesSize += articlesResponse.articles.size
            articlesResponse.articles.forEach {
                if (repository.isArticleBookmarked(it.url)) {
                    it.isBookmarked = true
                }
            }
            LoadResult.Page(
                data = articlesResponse.articles,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (fetchedArticlesSize == articlesResponse.totalResults) null else currentPage + 1
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition
    }

}