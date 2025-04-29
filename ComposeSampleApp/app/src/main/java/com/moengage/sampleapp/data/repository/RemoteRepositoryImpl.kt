package com.moengage.sampleapp.data.repository

import com.moengage.sampleapp.domain.RemoteRepository
import com.moengage.sampleapp.model.ArticlesResponse
import com.moengage.sampleapp.util.API_ARTICLE_PER_PAGE_QUERY_PARAM
import com.moengage.sampleapp.util.API_ARTICLE_PER_PAGE_QUERY_PARAM_VALUE
import com.moengage.sampleapp.util.API_BASE_URL
import com.moengage.sampleapp.util.API_ENDPOINT_EVERYTHING
import com.moengage.sampleapp.util.API_KEY_QUERY_PARAM
import com.moengage.sampleapp.util.API_PAGE_NUMBER_QUERY_PARAM
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(val httpClient: HttpClient) : RemoteRepository {

    override suspend fun getArticles(page: Int, apiKey: String): ArticlesResponse {
        return httpClient.get<ArticlesResponse>("${API_BASE_URL}/${API_ENDPOINT_EVERYTHING}") {
            url {
                parameters.append(API_KEY_QUERY_PARAM, apiKey)
                parameters.append(
                    API_ARTICLE_PER_PAGE_QUERY_PARAM,
                    API_ARTICLE_PER_PAGE_QUERY_PARAM_VALUE,
                )
                parameters.append(API_PAGE_NUMBER_QUERY_PARAM, page.toString())

                // Hardcoding for now as we are only fetching technology news (we can pass it as param after taking input from view layer)
                parameters.append("q", "technology")
            }
        }
    }
}