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
            val response = apiService.getSearchKeywords(request, apiKey)


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
          Convert the following natural language query into a clean, keyword-based search query for a news API (like GNews).

           Instructions:
            - Extract and retain only important keywords: proper nouns (names, people, places, events), relevant nouns, years, organizations, and critical context.
            - Remove unnecessary words, stop words, question formats, and conversational fillers.
            - Do NOT rephrase or summarize.
            - Do NOT explain the result.
            - Do NOT add or change meaning.
            - Just return the final keyword string only.
            
            Examples:
            - "What is the status of the India budget 2025?" → India budget 2025  
            - "Tell me about Elon Musk’s latest announcement" → Elon Musk latest announcement  
            - "Any new updates on climate change in Europe?" → climate change Europe  
            - "What are the latest trends in artificial intelligence?" → latest trends artificial intelligence  
            - "Show me headlines from the Ukraine war" → Ukraine war headlines

            Now convert this query:  
            "$userQuery"
        """.trimIndent()
    }
}
