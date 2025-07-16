package com.projects.reverbnews.module.geminiModule

import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>
)

@Serializable
data class Candidate(
    val content: ResponseContent
)

@Serializable
data class ResponseContent(
    val parts: List<ResponsePart>
)

@Serializable
data class ResponsePart(
    val text: String
)
