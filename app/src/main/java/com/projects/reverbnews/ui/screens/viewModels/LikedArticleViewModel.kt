package com.projects.reverbnews.ui.screens.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.reverbnews.module.Article
import com.projects.reverbnews.module.NewsData
import com.projects.reverbnews.module.NewsUiState
import com.projects.reverbnews.module.likedArticleDao.LikedArticleDao
import com.projects.reverbnews.module.likedArticleDao.LikedArticlesEntity
import com.projects.reverbnews.module.mappers.toArticleLiked
import com.projects.reverbnews.module.mappers.toLikedEntity
import com.projects.reverbnews.module.mappers.toSavedEntity
import com.projects.reverbnews.module.savedArticlesDao.SavedArticleDao
import com.projects.reverbnews.ui.screens.home.HomeScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LikedArticleViewModel @Inject constructor(
    private val dao: SavedArticleDao,
    private val likedArticleDao: LikedArticleDao
) : ViewModel() {

    val newsUiState: StateFlow<NewsUiState> = likedArticleDao.getLikedArticlesOrderedBySavedAt()
        .map<List<LikedArticlesEntity>, NewsUiState> {entity->
            val articles = entity.map { it.toArticleLiked() }
            NewsUiState.Success(
                article = NewsData(
                    totalArticles = articles.size,
                    articles = articles
                )
            )
        }
        .catch { emit(NewsUiState.Error(null)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NewsUiState.Loading
        )

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    fun likeArticle(article: Article) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLiked = true
                )
            }
            likedArticleDao.upsertLikedArticle(article.toLikedEntity())
            delay(2000)
            _uiState.update {
                it.copy(
                    isLiked = false
                )
            }
        }
    }

    fun dislikeArticle(article: Article) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isDisliked = true
                )
            }
            likedArticleDao.deleteLikedArticle(article.toLikedEntity())
            delay(2000)
            _uiState.update {
                it.copy(
                    isDisliked = false
                )
            }
        }
    }

    fun bookmarkArticle(article: Article) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaved = true
                )
            }
            dao.upsertArticle(article.toSavedEntity())
            delay(2000)
            _uiState.update {
                it.copy(
                    isSaved = false
                )
            }
        }
    }

    fun unbookmarkArticle(article: Article) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRemoved = true
                )
            }
            delay(1000)
            dao.deleteArticle(article.toSavedEntity())
            delay(2000)
            _uiState.update {
                it.copy(
                    isRemoved = false
                )
            }
        }
    }

}