package com.projects.reverbnews.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.projects.reverbnews.R
import com.projects.reverbnews.module.Article
import com.projects.reverbnews.ui.publishedTIme.getTimeAgo
import com.projects.reverbnews.ui.screens.viewModels.NewsViewModel
import com.projects.reverbnews.ui.uiStateFunctions.ShareUrl
import kotlinx.coroutines.delay

@Composable
fun NewsPreviewCard(
    previewData: Article,
    onBookmarkArticle: (Article) -> Unit = {},
    onUnbookmarkArticle: (Article) -> Unit = {},
    onLikeArticle:(Article)->Unit,
    onDislikeArticle:(Article)->Unit,
    onArticleClicked: (Article) -> Unit,
    viewModel: NewsViewModel = hiltViewModel(),
) {
    val borderColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.Gray
    val slidingBar = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val showLikeButton = remember { mutableStateOf(true) }
    val showSlidingBar = remember { mutableStateOf(false) }
    val isBookmarked by viewModel.isArticleBookmarked(previewData.url)
        .collectAsState(initial = false)
    val isLiked by viewModel.isArticleLiked(previewData.url)
        .collectAsState(initial = false)
    LaunchedEffect(slidingBar.value) {
        if (slidingBar.value) {
            showLikeButton.value = false
            delay(300)
            showSlidingBar.value = true
        } else {
            showSlidingBar.value = false
            delay(500)
            showLikeButton.value = true
        }
    }
    val triggerLikeAnimation = remember { mutableStateOf(false) }
    LaunchedEffect(triggerLikeAnimation.value) {
        if (triggerLikeAnimation.value) {
            delay(200)
            triggerLikeAnimation.value = false
        }
    }
    val likeScale by animateFloatAsState(
        targetValue = if (triggerLikeAnimation.value) 1.4f else 1f,
        animationSpec = tween(durationMillis = 200),
    )
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
                    visible = showLikeButton.value,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                ) {
                    Icon(
                        imageVector = if (!isLiked) Icons.Outlined.ThumbUp else Icons.Default.ThumbUp,
                        contentDescription = "liked the article",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .scale(likeScale)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                            triggerLikeAnimation.value = true
                            if (isLiked) {
                                onDislikeArticle(previewData)
                            } else {
                                onLikeArticle(previewData)
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                AnimatedVisibility(
                    visible = showSlidingBar.value,
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
                                    slidingBar.value = false
                                }
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(25.dp)
                                .clickable {
                                    if (isBookmarked) {
                                        onUnbookmarkArticle(previewData)
                                    } else {
                                        onBookmarkArticle(previewData)
                                    }
                                    slidingBar.value = false
                                }
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Icon(
                            imageVector = if (!isLiked) Icons.Outlined.ThumbUp else Icons.Default.ThumbUp,
                            contentDescription = "liked the article",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .clickable {
                                    if (isLiked) {
                                        onDislikeArticle(previewData)
                                    } else {
                                        onLikeArticle(previewData)
                                    }
                                    slidingBar.value = false
                                }
                                .size(25.dp)
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