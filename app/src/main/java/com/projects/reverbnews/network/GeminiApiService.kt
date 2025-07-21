package com.projects.reverbnews.network

import com.projects.reverbnews.module.geminiModule.GeminiRequest
import com.projects.reverbnews.module.geminiModule.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GeminiApiService {
    @POST("models/gemini-2.0-flash:generateContent")
    suspend fun getSearchKeywords(
        @Body body: GeminiRequest,
        @retrofit2.http.Header("X-goog-api-key") apiKey: String
    ): GeminiResponse

}
