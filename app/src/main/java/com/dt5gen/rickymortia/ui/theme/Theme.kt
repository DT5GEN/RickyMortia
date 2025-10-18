package com.dt5gen.rickymortia.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/* ----------------  Цветосхемы под сплэш  ---------------- */

// Насыщённая «ночная» схема — ближе к сплэшу
private val DarkColors = darkColorScheme(
    primary = AccentGreen,                  // зелёный для акцентов/иконок
    secondary = Lavender,
    surface = PurpleGray,                   // базовая тёмная поверхность
    surfaceVariant = CardFooter,            // нижние плашки карточек
    onSurface = WhiteAlpha90,               // текст на тёмном
    onSurfaceVariant = WhiteAlpha90
)

// Светлая схема — тоже с нужными оттенками, но без перегруза
private val LightColors = lightColorScheme(
    primary = AccentGreen,
    secondary = Lavender,
    surface = Color(0xFFF7F5FB),            // мягкий светлый фон
    surfaceVariant = Color(0xFFEDE8F5),     // светлая «тень» для карточек
    onSurface = Color(0xFF1F1B2D),
    onSurfaceVariant = Color(0xFF1F1B2D)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, //статичная палитра —
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}