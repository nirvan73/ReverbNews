package com.projects.reverbnews.module


sealed interface NewsUiState {
    data class Success(val article: NewsData) : NewsUiState

    object Loading : NewsUiState

    data class Error(val code: Int?) : NewsUiState
}