package com.projects.reverbnews.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.projects.reverbnews.model.Article
import com.projects.reverbnews.ui.screens.AppScreens.ReverbNewsAppExpandedScreen
import com.projects.reverbnews.ui.screens.AppScreens.ReverbNewsNormalScreen
import com.projects.reverbnews.ui.screens.home.ArticleDetailScreen
import com.projects.reverbnews.ui.screens.saved.SavedArticleScreen

sealed class Screens {
    object Home : Screens()
    object Saved : Screens()
    data class ArticleContent(val article: Article) : Screens()
}

@Composable
fun AppNavigationScreen(
    windowSize: WindowWidthSizeClass
) {


    if (windowSize != WindowWidthSizeClass.Expanded) {
        val backstack = remember { mutableStateListOf<Screens>(Screens.Home) }
        NavDisplay(
            backStack = backstack,
            onBack = { backstack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Screens.Home> {
                    ReverbNewsNormalScreen(
                        currentScreen = backstack.last(),
                        windowSize = windowSize,
                        onNavigate = { screen ->
                            if (backstack.last() != screen) {
                                backstack.clear()
                                backstack.add(screen)
                            }
                        },
                        onArticleClicked = { article ->
                            backstack.add(Screens.ArticleContent(article))
                        }
                    )
                }

                entry<Screens.Saved> {
                    SavedArticleScreen(
                        currentScreen = backstack.last(),
                        onNavigate = { screen ->
                            if (backstack.last() != screen) {
                                backstack.clear()
                                backstack.add(screen)
                            }
                        },
                        windowSize = windowSize
                    )
                }

                entry<Screens.ArticleContent> { screen ->
                    ArticleDetailScreen(
                        articleData = screen.article,
                        onBackClicked = {
                            backstack.removeLastOrNull()
                        },
                        windowSize = windowSize
                    )
                }
            }
        )
    } else {
        val backstack = remember { mutableStateListOf<Screens>(Screens.Home) }
        NavDisplay(
            backStack = backstack,
            onBack = { backstack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Screens.Home> {
                    ReverbNewsAppExpandedScreen(
                        currentScreen = backstack.last(),
                        onNavigate = { screen ->
                            if (backstack.last() != screen) {
                                backstack.clear()
                                backstack.add(screen)
                            }
                        },
                        windowSize = windowSize
                    )
                }

                entry<Screens.Saved> {
                    SavedArticleScreen(
                        currentScreen = backstack.last(),
                        onNavigate = { screen ->
                            if (backstack.last() != screen) {
                                backstack.clear()
                                backstack.add(screen)
                            }
                        },
                        windowSize = windowSize
                    )
                }
            }
        )
    }
}
