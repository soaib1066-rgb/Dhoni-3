package com.bangla.dhonihobar.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    secondary = GoldAccent,
    background = SlateBackground,
    surface = SlateSurface,
    onPrimary = TextLight,
    onSecondary = SlateBackground,
    onBackground = TextLight,
    onSurface = TextLight
)

@Composable
fun DhoniHobarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
