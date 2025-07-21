package com.projects.reverbnews.data

import com.projects.reverbnews.BuildConfig
import com.projects.reverbnews.module.NewsData
import com.projects.reverbnews.module.error.NewsException
import com.projects.reverbnews.network.NewsApiService

interface NewsRepository {
    suspend fun getNewsData(query: String, country: String): NewsData
}

class NetworkNewsRepository(
    private val newsApiService: NewsApiService
) : NewsRepository {
    override suspend fun getNewsData(query: String, country: String): NewsData {
        try {
            val response = newsApiService.searchNews(
                query = query,
                apiKey = BuildConfig.GNEWS_API_KEY,
                country = "in"
            )
            return response
        } catch (e: retrofit2.HttpException) {
            val errorCode = e.code()
            throw NewsException.HttpError(errorCode)
        } catch (e: java.io.IOException) {
            throw NewsException.Network
        } catch (e: Exception) {
            throw NewsException.Unknown
        }
    }
}

