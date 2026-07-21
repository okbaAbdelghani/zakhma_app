package com.okbatech.zakhm.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GreenGlow,
    onPrimary = DarkNavy,
    primaryContainer = GreenDark,
    onPrimaryContainer = GreenLight,
    secondary = CyanNeon,
    onSecondary = DarkNavy,
    secondaryContainer = CyanDark,
    onSecondaryContainer = TextPrimary,
    tertiary = XpGold,
    onTertiary = DarkNavy,
    background = DarkNavy,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    surfaceContainer = DarkCard,
    surfaceContainerHigh = DarkElevated,
    error = StreakRed,
    onError = TextPrimary,
    outline = TextDisabled,
    outlineVariant = DarkElevated,
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightSurface,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightSurface,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightTextPrimary,
    tertiary = LightXpGold,
    onTertiary = LightSurface,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCard,
    onSurfaceVariant = LightTextSecondary,
    surfaceContainer = LightCard,
    surfaceContainerHigh = LightElevated,
    error = LightStreakRed,
    onError = LightSurface,
    outline = LightTextDisabled,
    outlineVariant = LightElevated,
)

@Composable
fun ZakhmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
