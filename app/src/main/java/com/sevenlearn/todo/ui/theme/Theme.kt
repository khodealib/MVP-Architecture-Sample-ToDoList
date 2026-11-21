package com.sevenlearn.todo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = White,
    secondary = CyanSecondary,
    onSecondary = DefaultText,
    background = ScreenBackground,
    onBackground = DefaultText,
    surface = White,
    onSurface = DefaultText,
    surfaceVariant = SecondaryButtonBg,
    onSurfaceVariant = SecondaryText,
    outline = LightGray,
)

@Composable
fun TodoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = TodoTypography,
        content = content,
    )
}
