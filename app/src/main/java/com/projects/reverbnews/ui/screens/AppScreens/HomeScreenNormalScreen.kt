package com.projects.reverbnews.ui.screens.AppScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.projects.reverbnews.ui.screens.viewModels.NewsViewModel
import com.projects.reverbnews.module.Article
import com.projects.reverbnews.module.NewsUiState
import com.projects.reverbnews.navigation.Screens
import com.projects.reverbnews.ui.screens.components.BottomNavBar
import com.projects.reverbnews.ui.screens.components.SearchBarWithFilter
import com.projects.reverbnews.ui.screens.HomeScreen
import com.projects.reverbnews.ui.screens.home.ArticleDetailScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenNormalScreen(
    currentScreen: Screens,
    onNavigate: (Screens) -> Unit,
    windowSize: WindowWidthSizeClass,
) {
    val homeScreenViewModel: NewsViewModel = hiltViewModel()
    val uiState = homeScreenViewModel.queryUiState.collectAsState().value
    val keyword by homeScreenViewModel.geminiKeyword.collectAsState()
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
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
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
                            .padding(paddingValues)
                            .padding(top = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SearchBarWithFilter(
                            isError = homeScreenViewModel.newsUiState is NewsUiState.Error,
                            query = uiState.query,
                            onSearchInput = { input ->
                                homeScreenViewModel.queryInput(input)
                            },
                            onSearched = { input ->
                                if (input.isBlank()) {
                                    homeScreenViewModel.resetQuery()
                                }
                                homeScreenViewModel.getArticle(input)
                            },
                            geminiKeyword = keyword,
                            filterOptions = listOf(
                                "Top Headlines", "Politics", "Tech", "Finance",
                                "Business", "Sports", "India", "Bollywood"
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            HomeScreen(
                                contentPadding = PaddingValues(top = 10.dp),
                                uiState = homeScreenViewModel.uiState,
                                newsUiState = homeScreenViewModel.newsUiState,
                                onArticleClicked = {
                                    selectedArticle.value = it
                                    coroutineScope.launch {
                                        delay(100)
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                },
                                onBookmarkArticle = homeScreenViewModel::bookmarkArticle,
                                onUnbookmarkArticle = homeScreenViewModel::unbookmarkArticle,
                                onLikeArticle = homeScreenViewModel::likeArticle,
                                onDislikeArticle = homeScreenViewModel::dislikeArticle,
                                onRetryAction = homeScreenViewModel::getArticle,
                            )
                        }
                    }
                }

                1 -> {
                    if (selectedArticle.value != null) {
                        ArticleDetailScreen(
                            articleData = selectedArticle.value!!,
                            onBackClicked = {
                                coroutineScope.launch {
                                    delay(100)
                                    pagerState.animateScrollToPage(0)
                                }
                            },
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    } else {
                        Box(
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
}
