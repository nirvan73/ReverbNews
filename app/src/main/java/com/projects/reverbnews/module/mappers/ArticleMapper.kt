package com.projects.reverbnews.module.mappers

import com.projects.reverbnews.module.Article
import com.projects.reverbnews.module.Source
import com.projects.reverbnews.module.savedArticlesDao.ArticleSource
import com.projects.reverbnews.module.savedArticlesDao.ArticlesEntity

fun Article.toSavedEntity(): ArticlesEntity {
    return ArticlesEntity(
        url = this.url,
        title = this.title,
        description = this.description ?: "",
        content = this.content ?: "",
        image = this.image,
        publishedAt = this.publishedAt,
        savedAt = System.currentTimeMillis(),
        articleSource = ArticleSource(
            name = this.source.name,
            url = this.source.url
        )
    )
}

fun ArticlesEntity.toArticleSaved(): Article {
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