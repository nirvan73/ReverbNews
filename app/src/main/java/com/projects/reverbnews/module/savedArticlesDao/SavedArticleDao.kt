package com.projects.reverbnews.module.savedArticlesDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticleDao {

    @Upsert
    suspend fun upsertArticle(article: ArticlesEntity)

    @Delete
    suspend fun deleteArticle(article: ArticlesEntity)


    @Query("SELECT * FROM articles ORDER BY savedAt DESC")
    fun getArticlesOrderedBySavedAt(): Flow<List<ArticlesEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM articles WHERE url = :url)")
    fun isArticleBookmarked(url: String): Flow<Boolean>
}