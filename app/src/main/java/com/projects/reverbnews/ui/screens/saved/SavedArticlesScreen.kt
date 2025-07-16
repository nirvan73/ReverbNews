package com.projects.reverbnews.ui.screens.saved

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.projects.reverbnews.navigation.Screens
import com.projects.reverbnews.ui.screens.components.BottomNavBar

@Composable
fun SavedArticleScreen(
    currentScreen: Screens,
    onNavigate: (Screens) -> Unit,
    windowSize: WindowWidthSizeClass,
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentScreen = currentScreen,
                onNavigate = { screen ->
                    if (screen != currentScreen) {
                        onNavigate(screen)
                    }
                },
                windowSize = windowSize
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Saved articles are here.....")
        }
    }
}