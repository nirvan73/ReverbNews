package com.projects.reverbnews.data

import com.projects.reverbnews.module.geminiModule.Content
import com.projects.reverbnews.module.geminiModule.GeminiRequest
import com.projects.reverbnews.module.geminiModule.Part
import com.projects.reverbnews.network.GeminiApiService


class GeminiHelper(
    private val apiService: GeminiApiService,
    private val apiKey: String
) {

    suspend fun extractKeywordsFromQuery(query: String): String {
        val prompt = buildPrompt(query)

        val request = GeminiRequest(
            contents = listOf(
                Content(parts = listOf(Part(text = prompt)))
            )
        )

        return try {
            val response = apiService.getSearchKeywords(request,apiKey)


            val keyword = response.candidates.firstOrNull()
                ?.content?.parts?.firstOrNull()
                ?.text


            keyword.takeIf { !it.isNullOrBlank() } ?: query
        } catch (e: Exception) {
            query
        }
    }

    private fun buildPrompt(userQuery: String): String {
        return """
            Convert the following natural language query into a clean, keyword-based search query for a news GNews API.

            - Keep proper nouns and key information (e.g., names, places, events, years).
            - Remove stop words and unnecessary phrases.
            - Do not rephrase, explain, or add extra words.
            - Return only the keywords. No prefix, suffix, or explanation. Just the final string.

            Examples:
            "What is the status of the India budget 2025?"
            → India budget 2025

            "Tell me about Elon Musk’s latest announcement"
            → Elon Musk latest announcement

            Now convert this:
            "$userQuery"
        """.trimIndent()
    }
}
