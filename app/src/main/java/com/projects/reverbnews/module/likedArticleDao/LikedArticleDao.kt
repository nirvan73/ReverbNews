package com.projects.reverbnews.module.likedArticleDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedArticleDao {

    @Upsert
    suspend fun upsertLikedArticle(article: LikedArticlesEntity)

    @Delete
    suspend fun deleteLikedArticle(article: LikedArticlesEntity)


    @Query("SELECT * FROM likedArticles ORDER BY savedAt DESC")
    fun getLikedArticlesOrderedBySavedAt(): Flow<List<LikedArticlesEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM likedArticles WHERE url = :url)")
    fun isArticleLiked(url: String): Flow<Boolean>
}