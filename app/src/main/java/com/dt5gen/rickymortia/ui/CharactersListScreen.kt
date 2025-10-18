package com.dt5gen.rickymortia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.dt5gen.rickymortia.ui.theme.CharacterCard

/**
 * Экран списка персонажей (2 колонки) на Paging.
 */
@Composable
fun CharactersListScreen(
    modifier: Modifier = Modifier,
    viewModel: RickyMortiaViewModel = hiltViewModel(),
) {

    val items = viewModel.characters.collectAsLazyPagingItems()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(
                count = items.itemCount,
                key = items.itemKey { it.id }
            ) { index ->
                val character = items[index] ?: return@items
                CharacterCard(
                    item = character,
                    onLike = { viewModel.onLikeClick(character) },
                    onStudied = { viewModel.onStudiedClick(character) }
                )
            }
        }
    }
}