package com.projects.reverbnews.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.projects.reverbnews.ui.screens.AppScreens.FavouriteScreenNormalScreen
import com.projects.reverbnews.ui.screens.AppScreens.HomeScreenExpandedScreen
import com.projects.reverbnews.ui.screens.AppScreens.HomeScreenNormalScreen
import com.projects.reverbnews.ui.screens.AppScreens.SavedScreenNormalScreen

sealed class Screens {
    object Home : Screens()
    object Saved : Screens()

    object Favourites : Screens()
}

@Composable
fun AppNavigationScreen(
    windowSize: WindowWidthSizeClass
) {

    if (windowSize != WindowWidthSizeClass.Expanded) {
        val backstack = remember { mutableStateListOf<Screens>(Screens.Home) }
        var previousScreen by remember { mutableStateOf<Screens?>(null) }
        NavDisplay(
            backStack = backstack,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            transitionSpec = {
                val from = previousScreen
                val to = backstack.lastOrNull()
                val forward = when {
                    from == Screens.Saved && to == Screens.Favourites -> true
                    from == Screens.Home && to == Screens.Saved -> true
                    from == Screens.Home && to == Screens.Favourites -> true
                    else -> false
                }
                if (forward) {
                    slideInHorizontally(animationSpec = tween(600)) { it } + fadeIn(
                        animationSpec = tween(durationMillis = 1200)
                    ) togetherWith
                            slideOutHorizontally(animationSpec = tween(600)) { -it } + fadeOut(
                        animationSpec = tween(durationMillis = 1200)
                    )
                } else {
                    slideInHorizontally(animationSpec = tween(600)) { -it } + fadeIn(
                        animationSpec = tween(durationMillis = 1200)
                    ) togetherWith
                            slideOutHorizontally(animationSpec = tween(600)) { it } + fadeOut(
                        animationSpec = tween(durationMillis = 1200)
                    )
                }
            },
            onBack = {
                backstack.removeLastOrNull()
            },
            entryProvider = entryProvider {
                entry<Screens.Home> {
                    HomeScreenNormalScreen(
                        currentScreen = backstack.last(),
                        windowSize = windowSize,
                        onNavigate = { screen ->
                            if (backstack.last() != screen) {
                                previousScreen = backstack.last()
                                backstack.clear()
                                backstack.add(screen)
                            }
                        }
                    )
                }

                entry<Screens.Saved> {
                    SavedScreenNormalScreen(
                        currentScreen = backstack.last(),
                        windowSize = windowSize,
                        onNavigate = { screen ->
                            if (backstack.last() != screen) {
                                previousScreen = backstack.last()
                                backstack.clear()
                                backstack.add(screen)
                            }
                        }
                    )
                }
                entry<Screens.Favourites> {
                    FavouriteScreenNormalScreen(
                        currentScreen = backstack.last(),
                        windowSize = windowSize,
                        onNavigate = { screen ->
                            if (backstack.last() != screen) {
                                previousScreen = backstack.last()
                                backstack.clear()
                                backstack.add(screen)
                            }
                        }
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
                    HomeScreenExpandedScreen(
                        windowSize = windowSize
                    )
                }
            }
        )
    }
}
