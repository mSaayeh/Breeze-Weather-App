package com.msayeh.breeze.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.msayeh.breeze.data.utils.AppLanguage

internal val DarkColorScheme = darkColorScheme(
    primary = DarkColors.Primary,
    inversePrimary = DarkColors.PrimaryVariant,
    primaryContainer = DarkColors.Primary,
    onPrimaryContainer = DarkColors.OnPrimary,
    background = DarkColors.Background,
    surface = DarkColors.Surface,
    onPrimary = DarkColors.OnPrimary,
    onSurface = DarkColors.OnSurface,
    onBackground = DarkColors.OnBackground,
    tertiary = DarkColors.Tertiary,
    error = DarkColors.Error,
    onError = DarkColors.OnError,
)

internal val LightColorScheme = lightColorScheme(
    primary = LightColors.Primary,
    inversePrimary = LightColors.PrimaryVariant,
    primaryContainer = LightColors.Primary,
    onPrimaryContainer = LightColors.OnPrimary,
    background = LightColors.Background,
    surface = LightColors.Surface,
    onPrimary = LightColors.OnPrimary,
    onSurface = LightColors.OnSurface,
    onBackground = LightColors.OnBackground,
    tertiary = LightColors.Tertiary,
    error = LightColors.Error,
    onError = LightColors.OnError,
)

@Composable
fun BreezeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    appLanguage: AppLanguage? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val language = appLanguage ?: AppLanguage.fromCode(LocalConfiguration.current.locales.get(0).language ?: "en")

    CompositionLocalProvider(LocalGradients provides Gradients(colorScheme)) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = appTypography(language == AppLanguage.ARABIC),
            content = content
        )
    }
}