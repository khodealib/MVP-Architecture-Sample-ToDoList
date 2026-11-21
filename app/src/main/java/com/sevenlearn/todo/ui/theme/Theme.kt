package com.sevenlearn.todo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import java.util.Locale

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
    val configuration = LocalConfiguration.current
    val language = configuration.locales[0]?.language ?: Locale.getDefault().language
    val typography = remember(language) {
        val fontFamily = if (language == "fa") FontFamily.SansSerif else PrimaryFontFamily
        todoTypography(fontFamily)
    }

    MaterialTheme(
        colorScheme = LightColors,
        typography = typography,
        content = content,
    )
}
