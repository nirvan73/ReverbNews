package com.projects.reverbnews.module

import com.projects.reverbnews.model.NewsData

sealed interface NewsUiState {
    data class Success(val article: NewsData) : NewsUiState

    object Loading : NewsUiState

    data class Error(val code: Int?) : NewsUiState
}