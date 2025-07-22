package com.projects.reverbnews.ui.screens.AppScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.projects.reverbnews.module.Article
import com.projects.reverbnews.navigation.Screens
import com.projects.reverbnews.ui.screens.components.BottomNavBar
import com.projects.reverbnews.ui.screens.home.ArticleDetailScreen
import com.projects.reverbnews.ui.screens.saved.SavedArticleScreen
import com.projects.reverbnews.ui.screens.viewModels.SavedArticleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SavedScreenNormalScreen(
    currentScreen: Screens,
    onNavigate: (Screens) -> Unit,
    windowSize: WindowWidthSizeClass,
) {
    val savedArticleViewModel: SavedArticleViewModel = hiltViewModel()
    val newsUiState = savedArticleViewModel.newsUiState.collectAsState().value
    val selectedArticle = remember { mutableStateOf<Article?>(null) }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = pagerState.currentPage == 1) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(0)
        }
    }
    Scaffold(
        bottomBar = {
            if (pagerState.currentPage == 0) {
                BottomNavBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        if (screen != currentScreen) {
                            onNavigate(screen)
                        }
                    },
                    windowSize = windowSize
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text(
                            text = "Saved Articles",
                            style = MaterialTheme.typography.displaySmall.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SavedArticleScreen(
                                newsUiState = newsUiState,
                                onArticleClicked = {
                                    selectedArticle.value = it
                                    coroutineScope.launch {
                                        delay(100)
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                },
                                uiState = savedArticleViewModel.uiState,onLikeArticle = savedArticleViewModel::likeArticle,
                                onDislikeArticle = savedArticleViewModel::dislikeArticle,
                                onUnbookmarkArticle = savedArticleViewModel::unbookmarkArticle
                            )
                        }
                    }
                }

                1 -> {
                    selectedArticle.value?.let { article ->
                        ArticleDetailScreen(
                            articleData = article,
                            onBackClicked = {
                                coroutineScope.launch {
                                    delay(100)
                                    pagerState.animateScrollToPage(0)
                                }
                            },
                        )
                    } ?: Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Select an article to view details",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }

}