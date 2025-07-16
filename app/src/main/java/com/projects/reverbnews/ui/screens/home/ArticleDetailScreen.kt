package com.projects.reverbnews.ui.screens.home

import android.content.Context
import android.content.Intent
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.projects.reverbnews.R
import com.projects.reverbnews.model.Article
import com.projects.reverbnews.model.Source
import com.projects.reverbnews.ui.theme.ReverbNewsTheme

@Composable
fun ArticleDetailScreen(
    articleData: Article,
    onBackClicked: () -> Unit = {},
    windowSize: WindowWidthSizeClass
) {
    val context = LocalContext.current
    val showWebView = remember { mutableStateOf(false) }
    Scaffold(
        bottomBar = {
            if (windowSize != WindowWidthSizeClass.Expanded) {
                BottomBar(
                    onBackClicked = { onBackClicked() },
                    context = context,
                    url = articleData.url
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        if (showWebView.value) {
            InAppWebView(
                url = articleData.url,
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 10.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = articleData.source.name,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                ),
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = articleData.title,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${articleData.publishedAt ?: "Unknown"}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        if (articleData.image != null) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context = LocalContext.current)
                                        .data(articleData.image)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Article reference Image",
                                    contentScale = ContentScale.FillWidth,
                                    placeholder = painterResource(R.drawable.newsplaceholder),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.padding(10.dp))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(0.95f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(12.dp)
                                    .background(Color.Red),
                            )
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = Color.Red,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(12.dp)
                                    .background(Color.Red),
                            )
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .padding(start = 4.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                            fontWeight = FontWeight.Medium,
                                            fontStyle = FontStyle.Italic,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    ) {
                                        append(articleData.content ?: "")
                                        append("... ")
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Read more....",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E88E5)
                                ),
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .clickable { showWebView.value = true }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun InAppWebView(url: String, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

                loadUrl(url)
            }
        },
        modifier = modifier
    )
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

@Composable
fun BottomBar(
    context: Context,
    url: String,
    onBackClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Red)
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(bottom = 16.dp)
                .height(50.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            IconButton(
                onClick = { onBackClicked() },
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate back",

                    )
            }
            IconButton(
                onClick = { ShareUrl(context = context, url) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",

                    )
            }
        }
    }
}


@Preview
@Composable
fun preview() {
    ReverbNewsTheme {
        ArticleDetailScreen(
            windowSize = WindowWidthSizeClass.Compact,
            onBackClicked = {},
            articleData = Article(
                source = Source(url = "the-verge", name = "The Verge"),
                title = "AI Breakthrough: OpenAI’s Latest Model Sets New Benchmarks",
                description = "The new model achieves state-of-the-art results across multiple tasks, showing remarkable reasoning and conversational abilities.",
                url = "https://www.theverge.com/2025/07/05/openai-latest-model-benchmark",
                image = "https://images.unsplash.com/photo-1498050108023-c5249f4df085",
                publishedAt = "2025-07-05T09:30:00Z",
                content = """
OpenAI has unveiled its latest AI model, codenamed GPT-5X, setting a new standard in the artificial intelligence industry. The model, trained on a vast corpus of multilingual and multi-domain data, has shown substantial improvements in reasoning, memory, and safety alignment.

One of the key highlights of GPT-5X is its ability to follow complex multi-turn instructions with minimal hallucination, addressing one of the most common criticisms of earlier models. During internal benchmarks, GPT-5X scored higher than any previous model across a range of domains including math, legal reasoning, medical diagnostics, and programming.

“We believe GPT-5X represents a major leap forward in helpfulness and trustworthiness,” said Mira P., CTO of OpenAI. “We're particularly excited about its improved ability to say 'I don't know' when uncertain, rather than confidently presenting wrong information.”

In addition to performance, the new model is also more energy-efficient. OpenAI claims that the improvements in architecture and training efficiency reduce the energy footprint per query by almost 30% compared to GPT-4.

The rollout of GPT-5X is expected to begin later this month for select enterprise users before expanding to general availability via the ChatGPT platform and API access.

As generative AI continues to evolve, OpenAI’s latest offering reinforces its position as a leader in the space.
""".trimIndent()
            )
        )
    }
}