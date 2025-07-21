package com.projects.reverbnews.ui.screens.home

data class HomeScreenUiState(
    val isSaved: Boolean = false,
    val isRemoved:Boolean = false,
    val isLiked:Boolean = false,
    val isDisliked:Boolean = false,
)
