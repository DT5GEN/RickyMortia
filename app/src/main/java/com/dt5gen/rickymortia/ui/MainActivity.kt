package com.dt5gen.rickymortia.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
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
                            HorizontalDivider()

                        }
                    }
                }
            }
        }
    }
}

