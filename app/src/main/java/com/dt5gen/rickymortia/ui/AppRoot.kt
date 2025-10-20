package com.dt5gen.rickymortia.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dt5gen.rickymortia.ui.screens.CharactersListScreen
import com.dt5gen.rickymortia.ui.screens.FilterScreen
import com.dt5gen.rickymortia.ui.screens.QuizScreen
import com.dt5gen.rickymortia.ui.splash.SplashScreen
import com.dt5gen.rickymortia.ui.theme.AppTheme
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.dt5gen.rickymortia.ui.screens.CharacterDetailsRoute

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
            MainApp(vm = vm)
        }
    }
}

@Composable
private fun MainApp(vm: RickyMortiaViewModel) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                currentRoute = navController.currentBackStackEntryAsState().value?.destination,
                onClick = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { inner ->
        val topInsets = WindowInsets.statusBars

        NavHost(
            navController = navController,
            startDestination = Dest.Home.route,
            modifier = Modifier.padding(inner)
        ) {
            composable(Dest.Home.route) {
                CharactersListScreen(
                    viewModel = vm,
                    onOpenDetails = { id -> navController.navigate("details/$id") } // <<< НОВОЕ
                )
            }
            composable(Dest.Filter.route) { FilterScreen() }
            composable(Dest.Quiz.route)   { QuizScreen() }

            // <<< НОВЫЙ экран (не в нижнем меню)
            composable(
                route = "details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) {
                CharacterDetailsRoute(onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun BottomBar(
    currentRoute: NavDestination?,
    onClick: (String) -> Unit
) {
    val items = listOf(Dest.Filter, Dest.Home, Dest.Quiz)

    NavigationBar(tonalElevation = 0.dp) {
        items.forEach { item ->
            val selected =
                currentRoute?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = { onClick(item.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF4CAF50), // зелёный при выборе
                    selectedTextColor = Color(0xFF4CAF50), // зелёный текст при выборе
                    unselectedIconColor = Color.LightGray,
                    unselectedTextColor = Color.LightGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

sealed class Dest(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Filter : Dest("filter", "Filter", Icons.Outlined.Tune)
    data object Home   : Dest("home",   "Home",   Icons.Outlined.Home)
    data object Quiz   : Dest("quiz",   "Quiz",   Icons.Outlined.Help)
}