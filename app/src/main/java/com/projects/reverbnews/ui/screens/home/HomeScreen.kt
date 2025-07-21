package com.projects.reverbnews.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.projects.reverbnews.R
import com.projects.reverbnews.ui.theme.ReverbNewsTheme
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.projects.reverbnews.module.Article
import com.projects.reverbnews.module.Source
import com.projects.reverbnews.module.NewsUiState
import com.projects.reverbnews.ui.components.NewsPreviewCard
import com.projects.reverbnews.ui.screens.home.HomeScreenUiState
import com.projects.reverbnews.ui.uiStateFunctions.ErrorScreen
import com.projects.reverbnews.ui.uiStateFunctions.LoadingScreen
import kotlinx.coroutines.flow.StateFlow


@Composable
fun HomeScreen(
    uiState: StateFlow<HomeScreenUiState>,
    newsUiState: NewsUiState,
    onArticleClicked: (Article) -> Unit,
    onBookmarkArticle: (Article) -> Unit,
    onUnbookmarkArticle: (Article) -> Unit,
    onLikeArticle: (Article) -> Unit,
    onDislikeArticle: (Article) -> Unit,
    onRetryAction: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (newsUiState) {
        is NewsUiState.Loading -> LoadingScreen()
        is NewsUiState.Success -> {
            ArticlesList(
                uiState = uiState,
                articlesList = newsUiState.article.articles,
                onArticleClicked = onArticleClicked,
                onLikeArticle = onLikeArticle,
                onDislikeArticle = onDislikeArticle,
                onBookmarkArticle = onBookmarkArticle,
                onUnbookmarkArticle = onUnbookmarkArticle,
                contentPadding = contentPadding
            )
        }

        is NewsUiState.Error -> {
            ErrorScreen(
                errorCode = newsUiState.code,
                retryAction = onRetryAction
            )
        }
    }
}

@Composable
fun ArticlesList(
    uiState: StateFlow<HomeScreenUiState>,
    articlesList: List<Article>,
    modifier: Modifier = Modifier,
    onArticleClicked: (Article) -> Unit,
    onBookmarkArticle: (Article) -> Unit,
    onUnbookmarkArticle: (Article) -> Unit,
    onLikeArticle: (Article) -> Unit,
    onDislikeArticle: (Article) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by uiState.collectAsState()
    if (articlesList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No articles available",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(articlesList, key = { it.url ?: it.title }) { article ->
                    NewsPreviewCard(
                        previewData = article,
                        onArticleClicked = { onArticleClicked(article) },
                        onUnbookmarkArticle = { onUnbookmarkArticle(article) },
                        onBookmarkArticle = { onBookmarkArticle(article) },
                        onDislikeArticle = { onDislikeArticle(article) },
                        onLikeArticle = { onLikeArticle(article) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
                }
            }
            AnimatedVisibility(
                visible = uiState.isSaved,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.95f
                        )
                    ),
                    modifier = Modifier
                        .padding(8.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.bookmarks_pana),
                            contentDescription = "Saved article",
                            modifier = Modifier
                                .size(160.dp)
                                .clip(RoundedCornerShape(24.dp))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Article saved to your list!",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = uiState.isRemoved,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.95f
                        )
                    ),
                    modifier = Modifier
                        .padding(8.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Article removed from your list!",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    ReverbNewsTheme {
        NewsPreviewCard(
            previewData = Article(
                source = Source(url = "bbc-news", name = "BBC News"),
                title = "Global Markets Rally as Tech Stocks Surge",
                description = "Tech companies lead the recovery with impressive Q2 results.",
                url = "https://www.bbc.com/news/business-123456",
                image = "https://ichef.bbci.co.uk/news/image.jpg",
                publishedAt = "2025-07-05T12:45:00Z",
                content = "Full article content goes here..."
            ),
            onArticleClicked = {},
            onBookmarkArticle = {},
            onUnbookmarkArticle = {},
            onLikeArticle = {},
            onDislikeArticle = {}
        )
    }
}