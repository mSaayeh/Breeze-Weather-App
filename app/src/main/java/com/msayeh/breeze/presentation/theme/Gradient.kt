package com.msayeh.breeze.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Brush

class Gradients(colorScheme: ColorScheme = LightColorScheme) {
    val primaryGradient = Brush.linearGradient(listOf(colorScheme.primary, colorScheme.inversePrimary))
}

val LocalGradients = compositionLocalOf { Gradients() }

val MaterialTheme.gradients: Gradients
    @Composable
    @ReadOnlyComposable
    get() = LocalGradients.current