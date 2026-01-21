package com.example.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    val candidates: List<GeminiCandidates> = emptyList()
)

@Serializable
data class GeminiCandidates(
    val content: GeminiContent
)