package com.moengage.sampleapp.domain

import com.moengage.sampleapp.model.ArticlesResponse

interface RemoteRepository {

    suspend fun getArticles(page: Int, apiKey: String): ArticlesResponse
}