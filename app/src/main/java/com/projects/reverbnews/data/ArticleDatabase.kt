package com.projects.reverbnews.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.projects.reverbnews.module.likedArticleDao.LikedArticleDao
import com.projects.reverbnews.module.likedArticleDao.LikedArticlesEntity
import com.projects.reverbnews.module.savedArticlesDao.SavedArticleDao
import com.projects.reverbnews.module.savedArticlesDao.ArticlesEntity


@Database(
    entities =  [ArticlesEntity::class, LikedArticlesEntity::class],
    version = 2
)
abstract class ArticleDatabase: RoomDatabase() {

    abstract val savedArticleDao: SavedArticleDao

    abstract val likedArticleDao: LikedArticleDao
}