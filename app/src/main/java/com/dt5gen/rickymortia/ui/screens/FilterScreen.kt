package com.dt5gen.rickymortia.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FilterScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Filters (soon)",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Здесь будет экран фильтрации.\nUI и логику подключу после .",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}