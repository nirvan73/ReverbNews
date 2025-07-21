package com.projects.reverbnews.network

import com.projects.reverbnews.module.NewsData
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApiService {
    @GET("search")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apikey") apiKey: String,
        @Query("lang") language: String = "en",
        @Query("country") country: String = "in"
    ): NewsData
}