package com.dt5gen.rickymortia.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: GreetingViewModel = hiltViewModel()
            val text by vm.message.collectAsState()   // ← реактивно читаем StateFlow
            AppScreen(text = text)
        }
    }
}

@Composable
private fun AppScreen(text: String) {
    Text(text = text, style = MaterialTheme.typography.bodyLarge)
}

@Preview
@Composable
private fun AppPreview() {
    AppScreen(text = "Preview")
}