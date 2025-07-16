package com.projects.reverbnews.ui.screens.AppScreens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.projects.reverbnews.model.Article
import com.projects.reverbnews.navigation.Screens
import com.projects.reverbnews.ui.screens.HomeScreen
import com.projects.reverbnews.ui.screens.NewsViewModel
import com.projects.reverbnews.ui.screens.components.BottomNavBar
import com.projects.reverbnews.ui.screens.components.SearchBarWithFilter
import com.projects.reverbnews.ui.screens.home.ArticleDetailScreen
import com.projects.reverbnews.ui.theme.ReverbNewsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReverbNewsAppExpandedScreen(
    currentScreen: Screens,
    onNavigate: (Screens) -> Unit,
    windowSize: WindowWidthSizeClass,
) {
    val reverbNewsViewModel: NewsViewModel = hiltViewModel()
    val uiState = reverbNewsViewModel.queryUiState.collectAsState().value
    val selectedArticle = remember { mutableStateOf<Article?>(null) }
    val query = remember { mutableStateOf("") }
    val keyword by reverbNewsViewModel.geminiKeyword.collectAsState()
    Scaffold {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            BottomNavBar(
                windowSize = windowSize,
                currentScreen = currentScreen,
                onNavigate = { screen ->
                    if (screen != currentScreen) {
                        onNavigate(screen)
                    }
                }
            )
            VerticalDivider(
                thickness = 1.dp,
                color = Color.Gray,
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.4f),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        SearchBarWithFilter(
                            query = uiState.query,
                            onSearchInput = { reverbNewsViewModel.queryInput(it) },
                            onSearched = {
                                query.value = it
                                selectedArticle.value = null
                                reverbNewsViewModel.getArticle(it)
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
                    }
                    Spacer(modifier = Modifier.padding(6.dp))
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
                                    newsUiState = reverbNewsViewModel.newsUiState,
                                    onArticleClicked = { article ->
                                        selectedArticle.value = article
                                    },
                                    onRetryAction = reverbNewsViewModel::getArticle
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            VerticalDivider(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(1.dp),
                                color = Color.Gray
                            )
                        }

                    }
                }
            }
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
                            onBackClicked = {},
                            windowSize = windowSize
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


@Preview
@Composable
fun ExpandedScreenPreview() {
    ReverbNewsTheme {
        ReverbNewsAppExpandedScreen(
            currentScreen = Screens.Home,
            onNavigate = {},
            windowSize = WindowWidthSizeClass.Expanded
        )
    }
}