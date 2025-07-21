package com.projects.reverbnews.ui.screens.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.reverbnews.data.GeminiHelper
import com.projects.reverbnews.data.NewsRepository
import com.projects.reverbnews.module.Article
import com.projects.reverbnews.module.NewsUiState
import com.projects.reverbnews.module.QueryData
import com.projects.reverbnews.module.error.NewsException
import com.projects.reverbnews.module.likedArticleDao.LikedArticleDao
import com.projects.reverbnews.module.mappers.toLikedEntity
import com.projects.reverbnews.module.mappers.toSavedEntity
import com.projects.reverbnews.module.savedArticlesDao.SavedArticleDao
import com.projects.reverbnews.ui.screens.home.HomeScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val geminiHelper: GeminiHelper,
    private val savedArticleDao: SavedArticleDao,
    private val likedArticleDao: LikedArticleDao
) : ViewModel() {

    var newsUiState: NewsUiState by mutableStateOf(NewsUiState.Loading)
        private set

    private val _queryUiState = MutableStateFlow(QueryData())
    val queryUiState: StateFlow<QueryData> = _queryUiState.asStateFlow()

    private val _geminiKeyword = MutableStateFlow("")
    val geminiKeyword: StateFlow<String> = _geminiKeyword.asStateFlow()

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    fun isArticleBookmarked(url: String): Flow<Boolean> {
        return savedArticleDao.isArticleBookmarked(url)
    }

    fun isArticleLiked(url: String): Flow<Boolean> {
        return likedArticleDao.isArticleLiked(url)
    }

    init {
        getArticle()
    }

    fun queryInput(input: String) {
        _queryUiState.update { it.copy(query = input) }
    }

    fun bookmarkArticle(article: Article) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaved = true
                )
            }
            savedArticleDao.upsertArticle(article.toSavedEntity())
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
            savedArticleDao.deleteArticle(article.toSavedEntity())
            delay(2000)
            _uiState.update {
                it.copy(
                    isRemoved = false
                )
            }
        }
    }

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

    fun resetQuery() {
        _queryUiState.value = QueryData()
        _geminiKeyword.value = ""
        newsUiState = NewsUiState.Loading
    }

    fun getArticle(userQuery: String? = null) {
        viewModelScope.launch {
            newsUiState = NewsUiState.Loading
            try {
                val safeQuery = userQuery?.takeIf { it.isNotBlank() }?.trimEnd()
                    ?: "technology OR sports OR politics OR entertainment OR health OR business OR science OR world OR travel OR education"
                if (safeQuery.equals(
                        "technology OR sports OR politics OR entertainment OR health OR business OR science OR world OR travel OR education",
                        ignoreCase = true
                    )
                ) {
                    _geminiKeyword.value = ""
                    newsUiState =
                        NewsUiState.Success(newsRepository.getNewsData(safeQuery, country = "in"))
                } else {
                    val keyword = geminiHelper.extractKeywordsFromQuery(safeQuery)
                    _geminiKeyword.value = keyword
                    val result = newsRepository.getNewsData(query = keyword, country = "in")
                    newsUiState = NewsUiState.Success(result)
                }
            } catch (e: NewsException.HttpError) {
                Log.e("GeminiError", "HTTP ${e.code} - ${e.message}")
                newsUiState = NewsUiState.Error(e.code)
            } catch (e: NewsException.Network) {
                newsUiState = NewsUiState.Error(0)
            } catch (e: NewsException.Unknown) {
                newsUiState = NewsUiState.Error(-1)
            }
        }
    }
}