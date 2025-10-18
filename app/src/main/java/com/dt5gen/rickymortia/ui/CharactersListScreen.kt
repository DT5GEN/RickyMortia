package com.dt5gen.rickymortia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dt5gen.rickymortia.data.local.CharacterEntity
import com.dt5gen.rickymortia.ui.theme.CharacterCard

/**
 * Экран списка персонажей (2 колонки) с офлайн-поиском.
 * CHANGE: добавлен SearchBar и корректная работа с Paging Compose.
 */
@Composable
fun CharactersListScreen(
    modifier: Modifier = Modifier,
    viewModel: RickyMortiaViewModel = hiltViewModel()
) {
    // CHANGE: собираем State из StateFlow — теперь 'by' работает корректно
    val query by viewModel.searchQuery.collectAsState()

    val lazyPagingItems: LazyPagingItems<CharacterEntity> =
        viewModel.characters.collectAsLazyPagingItems()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SearchTopBar(
                value = query,
                onChange = viewModel::onSearchChange,
                placeholder = "Search characters"
            )
        }
    ) { inner ->
        // Контент с паддингами из Scaffold
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Stable keys — сначала пробуем id, если item null (плейсхолдер), используем индекс
                items(
                    items = List(lazyPagingItems.itemCount) { it },
                    key = { index -> lazyPagingItems[index]?.id ?: index.toLong() }
                ) { index ->
                    val item = lazyPagingItems[index] ?: return@items
                    CharacterCard(
                        item = item,
                        onLike = viewModel::onLikeClick,
                        onStudied = viewModel::onStudiedClick,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

/* ---------- TopBar с поиском ---------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            TextField(
                value = value,
                onValueChange = onChange,
                singleLine = true,
                placeholder = { Text(placeholder) },
                leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (value.isNotBlank()) {
                        IconButton(onClick = { onChange("") }) {
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp)),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text
                )
            )
        }
    )
}