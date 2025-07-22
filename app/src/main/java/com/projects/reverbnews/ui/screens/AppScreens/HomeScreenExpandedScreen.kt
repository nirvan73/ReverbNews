package com.projects.reverbnews.ui.screens.AppScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.projects.reverbnews.ui.screens.HomeScreen
import com.projects.reverbnews.ui.screens.viewModels.NewsViewModel
import com.projects.reverbnews.module.Article
import com.projects.reverbnews.navigation.Screens
import com.projects.reverbnews.ui.screens.components.BottomNavBar
import com.projects.reverbnews.ui.screens.components.SearchBarWithFilter
import com.projects.reverbnews.ui.screens.home.ArticleDetailScreen
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.text.font.FontWeight
import com.projects.reverbnews.module.NewsUiState
import com.projects.reverbnews.ui.screens.favourites.LikedArticleScreen
import com.projects.reverbnews.ui.screens.saved.SavedArticleScreen
import com.projects.reverbnews.ui.screens.viewModels.LikedArticleViewModel
import com.projects.reverbnews.ui.screens.viewModels.SavedArticleViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandedScreen(
    windowSize: WindowWidthSizeClass,
) {
    val homeScreenViewModel: NewsViewModel = hiltViewModel()
    val savedArticleViewModel: SavedArticleViewModel = hiltViewModel()
    val likedArticleViewModel: LikedArticleViewModel = hiltViewModel()
    val savedArticleNewsUiState = savedArticleViewModel.newsUiState.collectAsState().value
    val likedArticleNewsUiState = likedArticleViewModel.newsUiState.collectAsState().value
    val uiState = homeScreenViewModel.queryUiState.collectAsState().value
    val selectedArticle = remember { mutableStateOf<Article?>(null) }
    val query = remember { mutableStateOf("") }
    val keyword by homeScreenViewModel.geminiKeyword.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf<Screens>(Screens.Home) }
    BackHandler(enabled = pagerState.currentPage == 1) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(0)
        }
    }
    Scaffold {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Spacer(modifier = Modifier.width(3.dp))
            BottomNavBar(
                windowSize = windowSize,
                currentScreen = currentScreen,
                onNavigate = { screen ->
                    if (screen != currentScreen) {
                        currentScreen = screen
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                when (screen) {
                                    Screens.Home -> 0
                                    Screens.Saved -> 1
                                    else -> 2
                                }
                            )
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.width(3.dp))
            VerticalDivider(
                thickness = 1.dp,
                color = Color.Gray,
                modifier = Modifier
            )
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SearchBarWithFilter(
                                isError = homeScreenViewModel.newsUiState is NewsUiState.Error,
                                query = uiState.query,
                                onSearchInput = { homeScreenViewModel.queryInput(it) },
                                onSearched = {
                                    query.value = it
                                    selectedArticle.value = null
                                    homeScreenViewModel.getArticle(it)
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
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        HomeScreen(
                                            contentPadding = PaddingValues(top = 10.dp),
                                            newsUiState = homeScreenViewModel.newsUiState,
                                            uiState = homeScreenViewModel.uiState,
                                            onArticleClicked = { article ->
                                                selectedArticle.value = article
                                            },
                                            onBookmarkArticle = homeScreenViewModel::bookmarkArticle,
                                            onUnbookmarkArticle = homeScreenViewModel::unbookmarkArticle,
                                            onDislikeArticle = homeScreenViewModel::dislikeArticle,
                                            onLikeArticle = homeScreenViewModel::likeArticle,
                                            onRetryAction = homeScreenViewModel::getArticle,
                                        )
                                    }
                                }

                            }
                        }
                    }

                    1 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
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
                                Row(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        SavedArticleScreen(
                                            uiState = savedArticleViewModel.uiState,
                                            newsUiState = savedArticleNewsUiState,
                                            onArticleClicked = { article ->
                                                selectedArticle.value = article
                                            },
                                            onUnbookmarkArticle = savedArticleViewModel::unbookmarkArticle,
                                            onLikeArticle = savedArticleViewModel::likeArticle,
                                            onDislikeArticle = savedArticleViewModel::dislikeArticle,
                                        )
                                    }
                                }

                            }
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.padding(10.dp))
                            Text(
                                text = "Liked Articles",
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
                                Row(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        LikedArticleScreen(
                                            uiState = likedArticleViewModel.uiState,
                                            newsUiState = likedArticleNewsUiState,
                                            onArticleClicked = { article ->
                                                selectedArticle.value = article
                                            },
                                            onUnbookmarkArticle = likedArticleViewModel::unbookmarkArticle,
                                            onLikeArticle = likedArticleViewModel::likeArticle,
                                            onDislikeArticle = likedArticleViewModel::dislikeArticle,
                                            bookmarkArticle = likedArticleViewModel::bookmarkArticle
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.6f),
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (selectedArticle.value != null) {
                        ArticleDetailScreen(
                            articleData = selectedArticle.value!!,
                            onBackClicked = {
                                selectedArticle.value = null
                            },
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