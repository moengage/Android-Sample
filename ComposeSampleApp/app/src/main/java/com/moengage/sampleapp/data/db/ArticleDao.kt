package com.moengage.sampleapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moengage.sampleapp.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article")
    fun getAllBookmarkedArticles(): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticles(vararg article: Article)

    @Query("SELECT EXISTS(SELECT 1 FROM article WHERE url = :url)")
    suspend fun isArticleBookmarked(url: String): Boolean
}