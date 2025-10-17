package com.dt5gen.rickymortia.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.dt5gen.rickymortia.data.local.CharacterEntity

@Composable
fun CharactersListScreen(vm: RickyMortiaViewModel) {

    val title = vm.message.collectAsState().value

    // Paging -> в LazyPagingItems
    val lazyItems = vm.characters.collectAsLazyPagingItems()

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(all = 8.dp)
        )

        LazyColumn {

            items(lazyItems.itemCount) { index ->
                val character: CharacterEntity? = lazyItems[index]
                if (character != null) {
                    CharacterRow(item = character)
                    HorizontalDivider()
                }
            }
        }
    }
}
