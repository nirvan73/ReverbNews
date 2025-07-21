package com.projects.reverbnews.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsData(
    @SerialName("totalArticles")
    val totalArticles: Int,

    @SerialName("articles")
    val articles: List<Article> = emptyList()
)

@Serializable
data class Article(
    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("content")
    val content: String? = null,

    @SerialName("url")
    val url: String,

    @SerialName("image")
    val image: String? = null,

    @SerialName("publishedAt")
    val publishedAt: String,

    @SerialName("source")
    val source: Source
)

@Serializable
data class Source(
    @SerialName("name")
    val name: String,

    @SerialName("url")
    val url: String? = null
)
