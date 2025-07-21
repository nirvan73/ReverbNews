package com.projects.reverbnews.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.projects.reverbnews.navigation.Screens


data class Item(
    val title: String,
    val icon: ImageVector,
    val iconFilled: ImageVector,
    val screen: Screens
)

val destinations = listOf(
    Item("Home", icon = Icons.Outlined.Home, iconFilled = Icons.Default.Home,screen = Screens.Home),
    Item("Saved", icon = Icons.Default.BookmarkBorder, iconFilled = Icons.Default.Bookmark, screen = Screens.Saved),
    Item("Favourites", icon = Icons.Default.FavoriteBorder, iconFilled = Icons.Default.Favorite, screen = Screens.Favourites)
)

@Composable
fun BottomNavBar(
    currentScreen: Screens,
    onNavigate: (Screens) -> Unit,
    windowSize: WindowWidthSizeClass
) {
    var selectedIndex by remember { mutableStateOf(0) }
    destinations.forEachIndexed { index, item ->
        if (currentScreen == item.screen) {
            selectedIndex = index
        }
    }
    if (windowSize == WindowWidthSizeClass.Expanded) {
        NavigationRail {
            destinations.forEachIndexed { index, dest ->
                NavigationRailItem(
                    icon = {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if(selectedIndex == index) dest.iconFilled else dest.icon,
                                contentDescription = dest.title,
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    },
                    selected = selectedIndex == index,
                    onClick = {
                        onNavigate(dest.screen)
                    },
                    alwaysShowLabel = true,
                    label = {
                        Text(
                            text = dest.title,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    colors = NavigationRailItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    ),
                )
            }
        }
    } else {
        NavigationBar(
            modifier = Modifier
                .background(Color.Transparent),
        ) {
            destinations.forEachIndexed { index, dest ->
                NavigationBarItem(
                    icon = {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if(selectedIndex == index) dest.iconFilled else dest.icon,
                                contentDescription = dest.title,
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    },
                    selected = selectedIndex == index,
                    onClick = {
                        onNavigate(dest.screen)
                    },
                    alwaysShowLabel = true,
                    label = {
                        Text(
                            text = dest.title,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    ),
                )
            }
        }
    }
}