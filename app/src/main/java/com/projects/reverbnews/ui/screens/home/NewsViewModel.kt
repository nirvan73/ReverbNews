package com.projects.reverbnews.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.reverbnews.data.GeminiHelper
import com.projects.reverbnews.data.NewsRepository
import com.projects.reverbnews.module.NewsUiState
import com.projects.reverbnews.module.QueryData
import com.projects.reverbnews.module.error.NewsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val geminiHelper: GeminiHelper
) : ViewModel() {

    var newsUiState: NewsUiState by mutableStateOf(NewsUiState.Loading)
        private set

    private val _queryUiState = MutableStateFlow(QueryData())
    val queryUiState: StateFlow<QueryData> = _queryUiState.asStateFlow()

    private val _geminiKeyword = MutableStateFlow("")
    val geminiKeyword: StateFlow<String> = _geminiKeyword.asStateFlow()

    init {
        getArticle()
    }

    fun queryInput(input: String) {
        _queryUiState.update { it.copy(query = input) }
    }

    fun resetQuery() {
        _queryUiState.value = QueryData()
        _geminiKeyword.value = ""
        newsUiState = NewsUiState.Loading
    }

    fun getArticle(userQuery: String? = "Top News") {
        viewModelScope.launch {
            newsUiState = NewsUiState.Loading
            try {
                val safeQuery = userQuery?.takeIf { it.isNotBlank() }?.trimEnd() ?: "Top News"
                if (safeQuery.equals("Top News", ignoreCase = true)) {
                    _geminiKeyword.value = ""
                    newsUiState = NewsUiState.Success(newsRepository.getNewsData(safeQuery, country = "in"))
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
