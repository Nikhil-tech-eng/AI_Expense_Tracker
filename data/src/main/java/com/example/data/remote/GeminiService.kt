package com.example.data.remote

import com.example.data.BuildConfig
import com.example.data.remote.model.ExpenseExtractionResult
import com.example.data.remote.model.GeminiRequest
import com.example.data.remote.model.GeminiResponse
import com.example.data.remote.model.PromptType
import com.example.data.utils.GeminiApi
import com.example.data.utils.getContentBasedOnPrompts
import com.example.data.utils.parseGeminiExtractExpenseJson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class GeminiService @Inject constructor(
    private val httpClient: HttpClient
) {

    suspend fun generateContent(title: String, promptType: PromptType): String {
        return try {
            val response = httpClient.post(
                GeminiApi.ENDPOINT_GENERATE_CONTENT
            ) {
                contentType(ContentType.Application.Json)
                parameter(GeminiApi.KEY, BuildConfig.GEMINI_API_KEY)
                setBody(
                    GeminiRequest(
                        contents = getContentBasedOnPrompts(title, promptType)
                    )
                )
            }
            if (response.status.value != 200) {
                val error = response.bodyAsText()
                println("Gemini api error $error")
            }

            val geminiResponse = response.body<GeminiResponse>()
            println("Gemini response$geminiResponse")
            geminiResponse.candidates
                .firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text
                ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    suspend fun extractExpenseDetails(receiptText: String): ExpenseExtractionResult {
        return generateContent(
            receiptText,
            PromptType.EXTRACT_EXPENSE_DETAILS
        ).parseGeminiExtractExpenseJson()
    }
}