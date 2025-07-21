package com.projects.reverbnews.module.likedArticleDao

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likedArticles")
data class LikedArticlesEntity(
    @PrimaryKey
    val url: String,

    val title: String,
    val description: String,
    val content: String,
    val image: String?,
    val publishedAt: String,
    val savedAt: Long = System.currentTimeMillis(),
    @Embedded
    val articleSource: LikedArticleSource
)


data class LikedArticleSource(
    val name: String,
    @ColumnInfo(name = "source_url") val url: String?
)