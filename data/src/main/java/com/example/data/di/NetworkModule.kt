package com.example.data.di

import com.example.data.remote.GeminiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton // Ensures only one instance of HttpClient is created for the whole app
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            // Configure default timeouts for all requests
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 30000
            }
            // Configure JSON serialization
            install(ContentNegotiation) {
                json(
                    kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true // This is useful for parsing API responses
                        isLenient = true
                    }
                )
            }
        }
    }
    @Provides
    @Singleton
    fun provideGeminiService(httpClient: HttpClient): GeminiService {
        return GeminiService(httpClient)
    }
}