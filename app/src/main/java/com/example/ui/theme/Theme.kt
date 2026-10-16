package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrismIndigo,
    onPrimary = Color.White,
    primaryContainer = PrismViolet,
    onPrimaryContainer = Color.White,
    secondary = PrismCyan,
    onSecondary = Color.Black,
    secondaryContainer = StudioSurfaceElevated,
    onSecondaryContainer = PrismCyan,
    tertiary = PrismRose,
    onTertiary = Color.White,
    background = StudioBackground,
    onBackground = TextPrimary,
    surface = StudioSurface,
    onSurface = TextPrimary,
    surfaceVariant = StudioSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = StudioSurfaceBorder,
    outlineVariant = StudioSurfaceBorder
)

private val LightColorScheme = DarkColorScheme // Prism is a pro photo editing studio, best experienced in studio dark

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our signature Prism studio palette
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
