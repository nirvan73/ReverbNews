package com.projects.reverbnews.di

import android.content.Context
import androidx.room.Room
import com.projects.reverbnews.BuildConfig
import com.projects.reverbnews.data.GeminiHelper
import com.projects.reverbnews.data.NetworkNewsRepository
import com.projects.reverbnews.data.NewsRepository
import com.projects.reverbnews.network.GeminiApiService
import com.projects.reverbnews.network.NewsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.projects.reverbnews.data.ArticleDatabase
import com.projects.reverbnews.module.likedArticleDao.LikedArticleDao
import com.projects.reverbnews.module.savedArticlesDao.SavedArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://gnews.io/api/v4/"
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @Named("GNewsRetrofit")
    fun provideRetrofit(json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(@Named("GNewsRetrofit") retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(newsApiService: NewsApiService): NewsRepository {
        return NetworkNewsRepository(newsApiService)
    }

    @Provides
    @Singleton
    @Named("GeminiRetrofit")
    fun provideGeminiRetrofit(json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideGeminiApiService(@Named("GeminiRetrofit") retrofit: Retrofit): GeminiApiService {
        return retrofit.create(GeminiApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGeminiHelper(geminiApiService: GeminiApiService): GeminiHelper {
        return GeminiHelper(geminiApiService, BuildConfig.GEMINI_API_KEY)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            "reverbnews_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSavedArticleDao(appDatabase: ArticleDatabase): SavedArticleDao {
        return appDatabase.savedArticleDao
    }

    @Provides
    @Singleton
    fun provideLikedArticleDao(appDatabase: ArticleDatabase): LikedArticleDao {
        return appDatabase.likedArticleDao
    }


}
