package com.moengage.sampleapp.di

import com.moengage.sampleapp.util.API_TIMEOUT_TIME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HttpClientModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            engine {
                connectTimeout = API_TIMEOUT_TIME
                socketTimeout = API_TIMEOUT_TIME
            }

            install(JsonFeature) {
                KotlinxSerializer(
                    Json {
                        prettyPrint = true
                        coerceInputValues = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }

            install(Logging) {
                level = LogLevel.ALL
                filter { request ->
                    request.url.host.contains("ktor.io")
                }
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            com.moengage.sampleapp.logger.log("HttpClient") { message }
                        }
                    }
            }
        }
    }
}