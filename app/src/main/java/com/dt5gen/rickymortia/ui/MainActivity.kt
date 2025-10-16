package com.dt5gen.rickymortia.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.dt5gen.rickymortia.data.local.CharacterEntity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: GreetingViewModel = hiltViewModel()
            val title by vm.message.collectAsState()
            val lazyItems = vm.characters.collectAsLazyPagingItems()

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(8.dp)
                )
                LazyColumn {

                    items(lazyItems.itemCount) { index ->
                        val character = lazyItems[index]
                        if (character != null) {
                            CharacterRow(item = character)
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterRow(item: CharacterEntity) {
    Text(
        text = item.name,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    )
}