package com.projects.reverbnews.module.savedArticlesDao

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticlesEntity(
    @PrimaryKey
    val url: String,

    val title: String,
    val description: String,
    val content: String,
    val image: String?,
    val publishedAt: String,
    val savedAt: Long = System.currentTimeMillis(),
    @Embedded
    val articleSource: ArticleSource
)


data class ArticleSource(
    val name: String,
    @ColumnInfo(name = "source_url") val url: String?
)