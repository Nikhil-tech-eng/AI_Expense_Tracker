package com.example.data.utils

object GeminiApi {
    const val BASE_URL = "https://generativelanguage.googleapis.com/v1/models"
    const val MODEL = "gemini-2.5-flash"
    const val ENDPOINT_GENERATE_CONTENT = "$BASE_URL/$MODEL:generateContent"
    const val KEY = "key"
}

