package com.dt5gen.rickymortia.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dt5gen.rickymortia.ui.splash.SplashScreen
import com.dt5gen.rickymortia.ui.theme.AppTheme

@Composable
fun AppRoot() {
    val vm: RickyMortiaViewModel = hiltViewModel()

    var showSplash by rememberSaveable { mutableStateOf(true) }

    AppTheme {
        if (showSplash) {
            SplashScreen(
                onFinished = { showSplash = false }
            )
        } else {

            CharactersListScreen()
        }
    }
}