package com.projects.reverbnews.ui.screens.favourites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.projects.reverbnews.module.Article
import com.projects.reverbnews.module.NewsUiState
import com.projects.reverbnews.ui.components.NewsPreviewCard
import com.projects.reverbnews.ui.screens.home.HomeScreenUiState
import com.projects.reverbnews.ui.uiStateFunctions.LoadingScreen
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LikedArticleScreen(
    uiState: StateFlow<HomeScreenUiState>,
    newsUiState: NewsUiState,
    bookmarkArticle: (Article) -> Unit,
    onUnbookmarkArticle: (Article) -> Unit,
    onLikeArticle: (Article) -> Unit,
    onDislikeArticle: (Article) -> Unit,
    onArticleClicked: (Article) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (newsUiState) {
        is NewsUiState.Loading -> LoadingScreen()
        is NewsUiState.Success -> LikedArticlesList(
            uiState = uiState,
            articlesList = newsUiState.article.articles,
            onUnbookmarkArticle = onUnbookmarkArticle,
            onArticleClicked = onArticleClicked,
            contentPadding = contentPadding,
            onLikeArticle = onLikeArticle,
            onDislikeArticle = onDislikeArticle,
            bookmarkArticle = bookmarkArticle,
        )

        is NewsUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Something went wrong while loading liked articles.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }

}

@Composable
fun LikedArticlesList(
    uiState: StateFlow<HomeScreenUiState>,
    articlesList: List<Article>,
    modifier: Modifier = Modifier,
    onArticleClicked: (Article) -> Unit,
    bookmarkArticle: (Article) -> Unit,
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
                text = "No liked articles available",
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
                items(articlesList, key = { it.url  }) { article ->
                    NewsPreviewCard(
                        previewData = article,
                        onArticleClicked = { onArticleClicked(article) },
                        onBookmarkArticle = { bookmarkArticle(article) },
                        onUnbookmarkArticle = { onUnbookmarkArticle(article) },
                        onDislikeArticle = { onDislikeArticle(article) },
                        onLikeArticle = { onLikeArticle(article) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
                }
            }
            AnimatedVisibility(
                visible = uiState.isDisliked,
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
                            text = "Article removed from favourites!",
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