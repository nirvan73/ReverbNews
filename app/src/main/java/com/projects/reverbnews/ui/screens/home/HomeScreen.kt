package com.projects.reverbnews.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.projects.reverbnews.R
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.projects.reverbnews.model.Article
import com.projects.reverbnews.model.Source
import com.projects.reverbnews.module.NewsUiState
import com.projects.reverbnews.module.error.mapErrorCodeToDetails
import com.projects.reverbnews.ui.theme.ReverbNewsTheme
import java.time.Duration
import java.time.Instant


@Composable
fun HomeScreen(
    newsUiState: NewsUiState,
    onArticleClicked: (Article) -> Unit,
    onRetryAction: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (newsUiState) {
        is NewsUiState.Loading -> LoadingScreen()
        is NewsUiState.Success -> {
            ArticlesList(
                articlesList = newsUiState.article.articles,
                onArticleClicked = onArticleClicked,
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
    articlesList: List<Article>,
    modifier: Modifier = Modifier,
    onArticleClicked: (Article) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
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
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(articlesList, key = { it.url ?: it.title }) { article ->
                NewsPreviewCard(
                    previewData = article,
                    onArticleClicked = { onArticleClicked(article) }
                )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
            }
        }
    }
}


@Composable
fun NewsPreviewCard(
    previewData: Article,
    onArticleClicked: (Article) -> Unit,
) {
    val borderColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.Gray
    val slidingBar = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isLiked = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onArticleClicked(previewData) }
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 4.dp)
                .padding(horizontal = 10.dp),
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        append(previewData.source?.name ?: "Unknown Source ")
                        append(" · ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray
                        )
                    ) {
                        append(getTimeAgo(previewData.publishedAt))
                    }
                },
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = !slidingBar.value,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                ) {
                    Icon(
                        imageVector = if (!isLiked.value) Icons.Outlined.ThumbUp else Icons.Default.ThumbUp,
                        contentDescription = "liked the article",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable { isLiked.value = !isLiked.value }
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                AnimatedVisibility(
                    visible = slidingBar.value,
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .height(30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(22.dp)
                                .clickable {
                                    ShareUrl(
                                        url = previewData.url,
                                        context = context
                                    )
                                }
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(25.dp)
                                .clickable {}
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Icon(
                            imageVector = if (!isLiked.value) Icons.Outlined.ThumbUp else Icons.Default.ThumbUp,
                            contentDescription = "liked the article",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .clickable { isLiked.value = !isLiked.value }
                                .size(25.dp)
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(22.dp)
                                .clickable { slidingBar.value = false }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
                AnimatedVisibility(
                    visible = !slidingBar.value,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable {
                            slidingBar.value = !slidingBar.value
                        }
                    )
                }
            }
        }
        Text(
            text = previewData.title ?: "No title available",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = previewData.description ?: "No description available",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            ),
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(previewData.image ?: "")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                placeholder = painterResource(R.drawable.newsplaceholder),
                error = painterResource(R.drawable.newsplaceholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            20.dp
                        )
                    )
                    .border(
                        2.dp,
                        borderColor,
                        shape = RoundedCornerShape(
                            20.dp
                        )
                    )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lottie/loading_animation.json"))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(200.dp)
        )
    }
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    errorCode: Int?,
    modifier: Modifier = Modifier
) {
    val errorDetails = mapErrorCodeToDetails(errorCode ?: -1)

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("lottie/${errorDetails.lottieAsset}")
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(50.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = errorDetails.message,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = retryAction) {
            Text("Retry")
        }
    }
}


fun getTimeAgo(publishedAt: String): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val publishedTime = Instant.parse(publishedAt)
            val now = Instant.now()
            val diff = Duration.between(publishedTime, now)

            when {
                diff.toMinutes() < 1 -> "Just now"
                diff.toMinutes() < 60 -> "${diff.toMinutes()} min ago"
                diff.toHours() < 24 -> "${diff.toHours()} hr ago"
                diff.toDays() == 1L -> "Yesterday"
                diff.toDays() < 7 -> "${diff.toDays()} days ago"
                else -> "${diff.toDays() / 7} weeks ago"
            }
        } else {
            "Some time ago"
        }
    } catch (e: Exception) {
        "Unknown time"
    }
}


private fun ShareUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
        putExtra(Intent.EXTRA_SUBJECT, "Check this out!")
    }
    val chooser = Intent.createChooser(intent, "Share link via")
    context.startActivity(chooser)

}

@Preview
@Composable
fun Articlepreview() {
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
            onArticleClicked = {}
        )
    }
}