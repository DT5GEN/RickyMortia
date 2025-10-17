package com.dt5gen.rickymortia.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dt5gen.rickymortia.R

@Composable
fun SplashScreen(
    durationMs: Long = 1_700L,
    onFinished: () -> Unit = {},
    barWidthFraction: Float = 0.62f,
    barHeight: Dp = 20.dp,
    barCorner: Dp = 16.dp,
    barXFraction: Float = 0.5f,
    barYFraction: Float = 0.8615f,
    barOffsetX: Dp = 6.dp,
    scale: Float = 1.09f
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(durationMs) {
        progress.animateTo(1f, animationSpec = tween(durationMs.toInt(), easing = LinearEasing))
        onFinished()
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val wPx = constraints.maxWidth.toFloat()
        val hPx = constraints.maxHeight.toFloat()

        // фон
        Image(
            painter = painterResource(R.drawable.splash_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )

        // параметры прогресса
        val barWidthPx = wPx * barWidthFraction
        val barHeightPx = with(LocalDensity.current) { barHeight.toPx() }
        val cornerPx = with(LocalDensity.current) { barCorner.toPx() }
        val offsetXPx = with(LocalDensity.current) { barOffsetX.toPx() }

        val centerX = wPx * barXFraction
        val centerY = hPx * barYFraction
        val left = centerX - barWidthPx / 2f + offsetXPx
        val top = centerY - barHeightPx / 2f

        Canvas(Modifier.fillMaxSize()) {
            // фон дорожки
            drawRoundRect(
                color = Color(0x1A000000),
                topLeft = Offset(left, top),
                size = Size(barWidthPx, barHeightPx),
                cornerRadius = CornerRadius(cornerPx, cornerPx)
            )
            //  прогресс
            drawRoundRect(
                color = Color(0xFF22C55E),
                topLeft = Offset(left, top),
                size = Size(barWidthPx * progress.value, barHeightPx),
                cornerRadius = CornerRadius(cornerPx, cornerPx)
            )
        }
    }
}
