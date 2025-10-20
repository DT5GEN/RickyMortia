package com.dt5gen.rickymortia.helpers


import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.clearFocusOnTap(focusManager: FocusManager) = this.then(
    Modifier.pointerInput(Unit) {
        awaitEachGesture {
            // Ждём ПЕРВОЕ касание, НИЧЕГО не потребляя
            awaitFirstDown(requireUnconsumed = false)
            // Ждём отпускание
            val up = waitForUpOrCancellation()
            if (up != null) focusManager.clearFocus()
            //  не вызываем consume(), чтобы дочерние клики дошли
        }
    }
)