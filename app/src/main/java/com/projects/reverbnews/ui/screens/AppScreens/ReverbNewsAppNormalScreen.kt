package com.projects.reverbnews.ui.screens.AppScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.projects.reverbnews.model.Article
import com.projects.reverbnews.navigation.Screens
import com.projects.reverbnews.ui.screens.HomeScreen
import com.projects.reverbnews.ui.screens.NewsViewModel
import com.projects.reverbnews.ui.screens.components.BottomNavBar
import com.projects.reverbnews.ui.screens.components.SearchBarWithFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReverbNewsNormalScreen(
    currentScreen: Screens,
    onNavigate: (Screens) -> Unit,
    windowSize: WindowWidthSizeClass,
    onArticleClicked: (Article) -> Unit
) {
    val reverbNewsViewModel: NewsViewModel = hiltViewModel()
    val uiState = reverbNewsViewModel.queryUiState.collectAsState().value
    val keyword by reverbNewsViewModel.geminiKeyword.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentScreen = currentScreen,
                onNavigate = { screen ->
                    if (screen != currentScreen) {
                        onNavigate(screen)
                    }
                },
                windowSize = windowSize
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBarWithFilter(
                query = uiState.query,
                onSearchInput = { input ->
                    reverbNewsViewModel.queryInput(input)
                },
                onSearched = { input ->
                    if (input.isBlank()) {
                        reverbNewsViewModel.resetQuery()
                    }
                    reverbNewsViewModel.getArticle(input)
                },
                geminiKeyword = keyword,
                filterOptions = listOf(
                    "Top Headlines",
                    "Politics",
                    "Tech",
                    "Finance",
                    "Business",
                    "Sports",
                    "India",
                    "Bollywood"
                )
            )

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                HomeScreen(
                    contentPadding = PaddingValues(top = 10.dp),
                    newsUiState = reverbNewsViewModel.newsUiState,
                    onArticleClicked = onArticleClicked,
                    onRetryAction = reverbNewsViewModel::getArticle
                )
            }
        }
    }
}
