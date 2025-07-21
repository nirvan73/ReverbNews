package com.projects.reverbnews.ui.screens.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.projects.reverbnews.R

@Composable
fun SearchBarWithFilter(
    query: String,
    geminiKeyword: String,
    isError: Boolean = false,
    onSearchInput: (String) -> Unit,
    onSearched: (String) -> Unit,
    filterOptions: List<String>,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .animateContentSize()
            .shadow(4.dp, RoundedCornerShape(24.dp))
            .background(backgroundColor, RoundedCornerShape(24.dp)),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = query,
            onValueChange = {
                onSearchInput(it)
            },
            placeholder = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        color = contentColor
                    )
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = contentColor
                )
            },
            trailingIcon = {
                Row {
                    if (query.isNotBlank()) {
                        IconButton(onClick = {
                            onSearchInput("")
                            onSearched("")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = contentColor
                            )
                        }
                    }

                    Box {
                        IconButton(
                            onClick = { expanded = true },
                            modifier = Modifier.background(
                                color = backgroundColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter options",
                                tint = contentColor
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            filterOptions.forEach { filter ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = filter,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        onSearched(filter)
                                    }
                                )
                            }
                        }
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            textStyle = TextStyle(color = contentColor).copy(
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                disabledContainerColor = backgroundColor,
                focusedTextColor = contentColor,
                unfocusedTextColor = contentColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onSearched(query)
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (geminiKeyword.isNotBlank() && !isError) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 10.dp),
            ) {
                Text(
                    text = "Searched results for:- ${geminiKeyword.replaceFirstChar { it.uppercaseChar() }}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }
    }
}
