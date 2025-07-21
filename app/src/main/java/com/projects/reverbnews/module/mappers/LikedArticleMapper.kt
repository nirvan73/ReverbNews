package com.projects.reverbnews.module.mappers

import com.projects.reverbnews.module.Article
import com.projects.reverbnews.module.Source
import com.projects.reverbnews.module.likedArticleDao.LikedArticleSource
import com.projects.reverbnews.module.likedArticleDao.LikedArticlesEntity


fun Article.toLikedEntity(): LikedArticlesEntity {
    return LikedArticlesEntity(
        url = this.url,
        title = this.title,
        description = this.description ?: "",
        content = this.content ?: "",
        image = this.image,
        publishedAt = this.publishedAt,
        savedAt = System.currentTimeMillis(),
        articleSource = LikedArticleSource(
            name = this.source.name,
            url = this.source.url
        )
    )
}

fun LikedArticlesEntity.toArticleLiked(): Article {
    return Article(
        title = title,
        description = description,
        content = content,
        url = url,
        image = image,
        publishedAt = publishedAt,
        source = Source(
            url = articleSource.url,
            name = articleSource.name
        )
    )
}